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
 * Manage the Folder Protocol icons.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class FolderProtocolIcons
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(FolderProtocolIcons.class);

	/** Large icon. */
	static private Icon sLargeIcon;

	/** Small icon. */
	static private Icon sSmallIcon;

	static
	{
		BufferedImage image;

		// load large icon
		try (InputStream ins = FolderProtocolIcons.class.getResourceAsStream("folder_32.gif"))
		{
			image = ImageIO.read(ins);
			sLargeIcon = new ImageIcon(image, "Folder");
		}
		catch (IOException exc)
		{
			LOG.error("Failed to load image", exc);
			sLargeIcon = null;
		}

		// load small icon
		try (InputStream ins = FolderProtocolIcons.class.getResourceAsStream("folder_16.gif"))
		{
			image = ImageIO.read(ins);
			sSmallIcon = new ImageIcon(image, "Folder");
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
