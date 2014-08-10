import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.TitledBorder;
import java.applet.AudioClip;

public class FileSplitter extends JPanel
{

	public File fileToSplit=null;

	private JProgressBar progress;

	private String outputPath="";

	private long splitSize;

	boolean process=false;

	private DetailsPane detailsPane;

	private JScrollPane pane;

	String fileName="";

	int no_of_files=1;

	int percentage_process=0;

	boolean sound=true;

	public boolean DONE=false;

	public FileSplitter()
	{
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		detailsPane=new DetailsPane();
		pane=new JScrollPane(detailsPane);
		pane.setPreferredSize(new Dimension(400,90));
		pane.setBorder(new TitledBorder("Details"));
		progress=new JProgressBar(JProgressBar.HORIZONTAL,0,100);
		progress.setStringPainted(true);
		setBorder(new javax.swing.border.TitledBorder("Progress Monitor"));
		detailsPane.setText("");
		add(progress);
		add(pane);
	}
	public synchronized int getValue()
	{
		if(!process)
		{
			try
			{
				wait();
			}
			catch(InterruptedException exc)
			{
				System.out.println(exc);
			}
		}
			System.out.println("Process Completed :"+percentage_process);
			process=false;
			notify();
			return percentage_process;
	}

	public synchronized void setProgress(int value,String name)
	{
		if(process)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
				System.out.println(e);
			}
		}
			this.percentage_process=value;
			process=true;
			if(percentage_process!=0)
			{
				progress.setValue(percentage_process);
				progress.setString("Completed "+percentage_process+"%");
			}
			else
			{
				progress.setValue(percentage_process);
				progress.setString("DONE");
			}
			detailsPane.append("Created :"+name+"\n");
			System.out.println("Got %= "+percentage_process);
			if(percentage_process>99&&SplitterResource.BEEP)
			{
				getToolkit().beep();
				DONE=true;
			}
			notify();
	}
	public void splitFile(File f,File outputWay,long splitSize)throws IOException,InterruptedException
	{

		progress.setValue(0);

		detailsPane.setText("");

		this.fileToSplit=f;

		this.outputPath=outputWay+"\\";

		this.splitSize=splitSize;

		fileName=f.getName();

		no_of_files=(int)(f.length()/splitSize);

		if(f.length()%splitSize!=0)
		no_of_files++;

		this.sound=sound;

		new Splitter(this);

		new Progresser(this);

	}

	public String getOutputPath()
	{
		return outputPath;
	}

	public String getName()
	{
		return fileToSplit.getName();
	}

	public long getBatchSize()
	{
		return splitSize;
	}

	public int filesToSplit()
	{
		return no_of_files;
	}
	public String getAbsolutePath()
	{
		return fileToSplit.getAbsolutePath();
	}
}

class Splitter implements Runnable
{

		FileSplitter thread;

		public String fullFilePath="";

		public String batchFile="";

		private String outputPath="";

		private long splitSize;

		int percentage_process=0;

		private String splitFile="";

		String fileName="";

		static String batchCommand="copy /b ";

		static String deleteCommand="del ";

		String batchName="";

		String batchPath=batchCommand;

		String delPath=" ";

		String absolute="";

		String extension="batch";

		OutputStreamWriter batFile;

		int i=1;

		int no_of_files=1;

		private FileInputStream fin;

		private StringBuffer buff;

		public Splitter(FileSplitter red)throws IOException
		{
			this.thread=red;
			this.outputPath=thread.getOutputPath();
			this.fileName=thread.getName();
			this.splitSize=thread.getBatchSize();
			this.no_of_files=thread.filesToSplit();
			this.absolute=thread.getAbsolutePath();
			buff=new StringBuffer();
			fin=new FileInputStream(absolute);
			batchName=outputPath+fileName+"_merge.bat";
			batFile=new OutputStreamWriter(new FileOutputStream(batchName),"8859_1");
			new Thread(this).start();
		}

		public void run()
		{

			try
			{

				while(fin.available()>0&&fin.available()>=splitSize)
				{
					if(i==1)
					{
						batchPath="copy /b "+"\""+fileName+"."+extension+i+"\""+" "+"\""+fileName+"\""+"\n";
					}
					else
					{
						batchPath="copy /b "+"\""+fileName+"\""+"+"+"\""+fileName+"."+extension+i+"\""+" "+"\""+fileName+"\""+"\n";
					}
					delPath=deleteCommand+"\""+fileName+"."+extension+i+"\""+"\n";
					//byte buff[]=new byte[(int)splitSize];
					batchFile+=fileName+"."+i+"\n";
					FileOutputStream stream=new FileOutputStream(outputPath+fileName+"."+extension+i,true);
					splitFile=fileName+"."+extension+i;
					delPath=deleteCommand+"\""+fileName+"."+extension+i+"\""+"\n";
					//fin.read(buff);
					//stream.write(buff);
					createBatch(stream,splitSize);
					//stream.close();
					buff.append(batchPath);
					buff.append(delPath);
					percentage_process=i*100/no_of_files;
					thread.setProgress(percentage_process,splitFile);
					System.out.println("progress  "+percentage_process);
					i++;
				}

				if(fin.available()<splitSize)
				{

					batchPath="copy /b "+"\""+fileName+"\""+"+"+"\""+fileName+"."+extension+i+"\""+" "+"\""+fileName+"\""+"\n";
					delPath=deleteCommand+"\""+fileName+"."+extension+i+"\""+"\n";
					//byte buff[]=new byte[(int)fin.available()];
					FileOutputStream stream=new FileOutputStream(outputPath+fileName+"."+extension+i);
					//fin.read(buff);
					//stream.write(buff);
					//stream.close();
					createBatch(stream,fin.available());
					buff.append(batchPath);
			     	buff.append(delPath);
					percentage_process=i*100/no_of_files;
					splitFile=fileName+"."+extension+i;
					batchFile+=fileName+"."+extension+i+"\n";
					thread.setProgress(percentage_process,splitFile);
				}
				batchPath+=" "+"\""+fileName+"\"";
				String delBatch="del "+"\""+fileName+"_merge.bat"+"\"";
				buff.append(delBatch);
				batFile.write(buff.toString(),0,buff.toString().length());
				batFile.close();
				fin.close();
				batchFile+=fileName+"_merge.bat";
				splitFile=fileName+"_merge.bat";
				thread.setProgress(0,splitFile);
			}

			catch(IOException e)
			{
				System.out.println(e);
			}
		}
		public void createBatch(FileOutputStream ostream,long size)throws IOException
		{
			long i=size;
			System.out.println(size);
			while(i>=8192)
			{
				byte b[]=new byte[8192];
				fin.read(b);
				ostream.write(b);
				i-=8192L;
				System.out.println(fin.available());
			}
			if(i>0)
			{
				System.out.println(i);
				byte b[]=new byte[(int)i];
				fin.read(b);
				ostream.write(b);
			}
			ostream.close();
		}
}
	class Progresser implements Runnable
	{
		FileSplitter thread;
		public Progresser(FileSplitter red)
		{
			this.thread=red;
			new Thread(this).start();
		}
		public void run()
		{
			while(true)
			{
				thread.getValue();
			}
		}
}
