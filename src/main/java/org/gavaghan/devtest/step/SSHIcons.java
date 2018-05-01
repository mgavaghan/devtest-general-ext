package org.gavaghan.devtest.step;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Icons for SSH steps.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class SSHIcons
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(SSHIcons.class);

	/** Large icon. */
	static private Icon sLargeIcon;

	/** Small icon. */
	static private Icon sSmallIcon;

	static
	{
		BufferedImage image;

		// load large icon
		try (InputStream ins = SaveToFileIcons.class.getResourceAsStream("SSHExecute_32.gif"))
		{
			image = ImageIO.read(ins);
			sLargeIcon = new ImageIcon(image, "SSHExecute");
		}
		catch (IOException exc)
		{
			LOG.error("Failed to load image", exc);
			sLargeIcon = null;
		}

		// load small icon
		try (InputStream ins = SaveToFileIcons.class.getResourceAsStream("SSHExecute_16.gif"))
		{
			image = ImageIO.read(ins);
			sSmallIcon = new ImageIcon(image, "SSHExecute");
		}
		catch (IOException exc)
		{
			LOG.error("Failed to load image", exc);
			sSmallIcon = null;
		}
	}

	/**
	 * Get the large icon.
	 * 
	 * @return
	 */
	static public Icon getLargeIcon()
	{
		return sLargeIcon;
	}

	/**
	 * Get the small icon.
	 * 
	 * @return
	 */
	static public Icon getSmallIcon()
	{
		return sSmallIcon;
	}

}
