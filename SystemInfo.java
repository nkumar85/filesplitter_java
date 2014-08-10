import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import javax.swing.*;

public class SystemInfo extends JPanel
{
	private OperatingSystemMXBean bean;


	public SystemInfo()
	{
		bean=(OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	}
	public final long getPhysicalMemory()
	{
		return bean.getTotalPhysicalMemorySize();
	}
	public final long getFreeMemory()
	{
		return bean.getFreePhysicalMemorySize();
	}
	public final String getOSName()
	{
		return bean.getName()+" version "+bean.getVersion()+" "+System.getProperty("sun.os.patch.level");
	}
	public final String getArch()
	{
		return bean.getArch();
	}
	public final String getCPU()
	{
		return System.getProperty("sun.cpu.isalist");
	}
}