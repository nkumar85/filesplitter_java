import java.applet.*;
import java.net.URL;
import java.io.File;

public class Audio
{
	private Audio()
	{

	}
	public static AudioClip playAudio(String path)
	{
		try
		{
			File file=new File(path);
			if(file.exists())
			{
				AudioClip clip=Applet.newAudioClip(file.toURL());
				return clip;
			}
			else
			{
				return null;
			}
		}
		catch(java.io.IOException exc)
		{

		}
		return null;
	}
}