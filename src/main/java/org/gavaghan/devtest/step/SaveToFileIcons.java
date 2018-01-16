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
*
* @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
*/
public class SaveToFileIcons
{
	/** Logger. */
	static private final Logger LOG = LoggerFactory.getLogger(SaveToFileIcons.class);

	/** Large icon. */
	static private Icon sLargeIcon;

	/** Small icon. */
	static private Icon sSmallIcon;

	static
	{
		BufferedImage image;

		// load large icon
		try (InputStream ins = SaveToFileIcons.class.getResourceAsStream("saveToFile_32.gif"))
		{
			image = ImageIO.read(ins);
			sLargeIcon = new ImageIcon(image, "SaveToFile");
		}
		catch (IOException exc)
		{
			LOG.error("Failed to load image", exc);
			sLargeIcon = null;
		}

		// load small icon
		try (InputStream ins = SaveToFileIcons.class.getResourceAsStream("saveToFile_16.gif"))
		{
			image = ImageIO.read(ins);
			sSmallIcon = new ImageIcon(image, "SaveToFile");
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
