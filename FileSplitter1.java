import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class FileSplitter1 implements Runnable
{

	public File fileToSplit=null;

	public String fullFilePath="";

	private String batchFile="";

	private Progress progress;

	private String outputPath="";

	private long splitSize;

	private FileInputStream fin;

	int percentage_process=0;

	private String splitFile="";

	boolean process=false;

	Thread tr=null;
	public FileSplitter1(File f,File outputPath,long splitSize)throws IOException
	{

		fin=new FileInputStream(f);
		this.fileToSplit=f;
		this.outputPath=outputPath+"/";
		this.splitSize=splitSize;

		tr=new Thread(this);
		tr.start();
	}

	public synchronized void run()
	{

		try{
		String fileName=fileToSplit.getName();

		String batchCommand="copy /b ";

		String deleteCommand="\ndel ";

		String batchName=outputPath+fileName+"_merge.bat";

		String batchPath=batchCommand,delPath=" ";

		FileWriter batFile=new FileWriter(batchName);

		int i=1;

		process=true;

		int no_of_files=(int)(new File(outputPath+fileName).length()/splitSize);

		System.out.println(outputPath+fileName+" "+splitSize);

		while(fin.available()>0&&fin.available()>=splitSize)
		{
			percentage_process=i*100/no_of_files;
			byte buff[]=new byte[(int)splitSize];
			batchFile+=fileToSplit.getName()+"."+i+"\n";
			FileOutputStream stream=new FileOutputStream(outputPath+fileName+"."+i);
			splitFile=fileName+"."+i;
			batchPath+="\""+fileName+"."+i+"\""+" + "+" ";
			delPath+=deleteCommand+fileName+"."+i+"\n";
			fin.read(buff);
			stream.write(buff);
			stream.close();
			i++;
		}

		if(fin.available()<splitSize)
		{
			batchPath+="\""+fileName+"."+i+"\""+" ";;
			delPath+=deleteCommand+fileName+"."+i+"\n";
			byte buff[]=new byte[(int)fin.available()];
			FileOutputStream stream=new FileOutputStream(outputPath+fileName+"."+i);
			fin.read(buff);
			stream.write(buff);
			stream.close();
			splitFile=fileName+"."+i;
			batchFile+=fileName+"."+i+"\n";
		}
		batchPath+=" "+"\""+fileName+"\"";
		String delBatch="del "+fileName+"_merge.bat";
		batFile.write(batchPath,0,batchPath.length());
		batFile.write(delPath,0,delPath.length());
		batFile.write(delBatch,0,delBatch.length());
		batFile.close();
		batchFile+=fileName+"_merge.bat";
		splitFile=fileName+"_merge.bat";
		process=false;
		}
		catch(IOException ioe)
		{
			process=false;
			System.out.println(ioe);
		}
	}
	public synchronized boolean isProcessRunning()
	{
		return this.process;
	}
	public synchronized int getPercentageProcess()
	{
		return percentage_process;
	}
	public synchronized String getSplitFile()
	{
		return splitFile;
	}
	public static void main(String args[])throws IOException
	{
		File test=new File("F:/java/RunsGUI.java");
		FileSplitter1 spt=new FileSplitter1(test,new File(test.getParent()),1000);
		while(spt.isProcessRunning())
		{
			System.out.println(spt.getSplitFile()+"\n");
			System.out.println(spt.getPercentageProcess()+"\n");
		}
	}
}