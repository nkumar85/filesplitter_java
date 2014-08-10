import java.security.*;
import java.util.zip.*;
import java.io.*;

public class CheckFile
{
	private CheckFile()
	{
	}
	public static final String getCRC(String filename)throws IOException
	{
		CRC32 crc = new CRC32();
		FileInputStream f = new FileInputStream(filename);
		CheckedInputStream cis = new CheckedInputStream(f,crc);
		long i=0;
		i=f.available();
		while(i>=8192)
		{
			byte b1[]=new byte[8192];
			cis.read(b1);
			i=i-8192;
		}
		if(i>0)
		{
			byte b2[]=new byte[(int)i];
			cis.read(b2);
		}
		Checksum cs = cis.getChecksum();
		cis.close();
		return (Long.toHexString(cs.getValue()));
	}

	public static final String getMD(String filename,String alg)throws IOException
	{
		String s="";

		long i=0;

		MessageDigest md5=null;

		FileInputStream fin=new FileInputStream(filename);

		i=fin.available();

		try
		{
			md5=MessageDigest.getInstance(alg);
		}
		catch(NoSuchAlgorithmException nsx)
		{
			System.out.println(nsx);
		}

		DigestInputStream stream=new DigestInputStream(fin,md5);

		while(i>=8192)
		{
			byte b1[]=new byte[8192];
			stream.read(b1);
			i=i-8192;
		}
		if(i>0)
		{
			byte b2[]=new byte[(int)i];
			stream.read(b2);
		}

		byte b[]=md5.digest();

		for(int j=0;j<b.length;j++)
		{
			int i1=new Byte(b[j]).intValue();
			i1=i1&0x000000ff;
			if(i1>=0&&i1<15)
			s+="0"+Integer.toHexString(i1)+"";
			else
			s+=Integer.toHexString(i1)+"";
		}

		stream.close();

		return s;
	}

	public static final String getSHA(String filename)throws IOException
	{
		String s="";
		long i=0;
		MessageDigest sha=null;
		FileInputStream fin=new FileInputStream(filename);
		i=fin.available();
		try
		{
			sha=MessageDigest.getInstance("SHA");
		}
		catch(NoSuchAlgorithmException nsx)
		{
			System.out.println(nsx);
		}
		DigestInputStream stream=new DigestInputStream(fin,sha);
		while(i>=8192)
		{
			byte b1[]=new byte[8192];
			stream.read(b1);
			i=i-8192;
		}
		if(i>0)
		{
			byte b2[]=new byte[(int)i];
			stream.read(b2);
		}
		byte b[]=sha.digest();
		for(int j=0;j<b.length;j++)
		{
			int i1=new Byte(b[j]).intValue();
			i1=i1&0x000000ff;
			if(i1>=0&&i1<15)
			s+="0"+Integer.toHexString(i1)+"";
			else
			s+=Integer.toHexString(i1)+"";
		}
		stream.close();
		return s;
	}
}