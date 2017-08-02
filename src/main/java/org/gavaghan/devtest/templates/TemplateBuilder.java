package org.gavaghan.devtest.templates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.gavaghan.json.JSONNull;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONString;
import org.gavaghan.json.JSONValue;

/**
 * Base implementation to a class that creates a DevTest template
 * implementation.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class TemplateBuilder
{
	/** The UTF-8 character set. */
	static private final Charset UTF8 = Charset.forName("UTF-8");

	/** Singleton null. */
	static private final JSONNull JSON_NULL = new JSONNull();

	/** Template name. */
	private String mName;

	/** Package name. */
	private String mPackageName;

	/** Package path. */
	private String mPackagePath;

	/** Author. */
	private String mAuthor;

	/** Email. */
	private String mAuthorEmail;

	/** Zip stream */
	private ZipOutputStream mZOS;

	/**
	 * Get the list of file builders.
	 * 
	 * @return
	 */
	protected abstract List<FileBuilder> getFileBuilders();

	/**
	 * Null safe value get of a string.
	 * 
	 * @param json
	 * @param name
	 * @return 'null' if value does not exist or is not a JSONString.
	 */
	protected String getString(JSONObject json, String name)
	{
		JSONValue value = json.getOrDefault(name, JSON_NULL);
		if (value instanceof JSONString)
		{
			return (String) value.getValue();
		}

		return null;
	}

	/**
	 * Get template name from configuration.
	 * 
	 * @param config
	 * @throws BuilderException
	 */
	protected void getName(JSONObject config) throws BuilderException
	{
		String name = getString(config, "name");
		if (name == null) throw new BuilderException("No 'name' string in configuration");
		mName = name;
	}

	/**
	 * Get package name from configuration.
	 * 
	 * @param config
	 * @throws BuilderException
	 */
	protected void getPackage(JSONObject config) throws BuilderException
	{
		String pack = getString(config, "package");
		if (pack == null) throw new BuilderException("No 'package' string in configuration");
		mPackageName = pack;
		mPackagePath = pack.replaceAll("\\.", "/");
	}

	/**
	 * Get author from configuration.
	 * 
	 * @param config
	 * @throws BuilderException
	 */
	protected void getAuthor(JSONObject config) throws BuilderException
	{
		String author = getString(config, "author");
		if (author == null) return;
		mAuthor = author;
		mAuthorEmail = getString(config, "authorEmail");
	}

	/**
	 * Add a completed file to the archive.
	 * 
	 * @param filename
	 * @param content
	 * @throws IOException
	 */
	protected void addFile(String filename, String content) throws IOException
	{
		ZipEntry entry = new ZipEntry(filename);

		mZOS.putNextEntry(entry);
		mZOS.write(content.getBytes("UTF-8"));
		mZOS.closeEntry();
	}

	/**
	 * Read an embedded resource.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public String readResource(String name) throws IOException
	{
		StringBuilder builder = new StringBuilder();

		try (InputStream in = FileBuilder.class.getResourceAsStream(name); InputStreamReader isr = new InputStreamReader(in, UTF8); BufferedReader bufr = new BufferedReader(isr))
		{
			String line;

			for (;;)
			{
				line = bufr.readLine();
				if (line == null) break;

				builder.append(line).append(getEOL());
			}
		}

		return builder.toString();
	}

	/**
	 * Determine whether this implementation can build something with this
	 * configuration.
	 * 
	 * @param config
	 *           configuration to check
	 * @return 'true' if we can build it.
	 */
	public abstract boolean canBuild(JSONObject config);

	/**
	 * Get line separator sequence.
	 * 
	 * @return
	 */
	public String getEOL()
	{
		return System.getProperty("line.separator");
	}

	/**
	 * Get the template name.
	 * 
	 * @return
	 */
	public String getName()
	{
		return mName;
	}

	/**
	 * Get the package name.
	 * 
	 * @return
	 */
	public String getPackageName()
	{
		return mPackageName;
	}

	/**
	 * Get the package path.
	 * 
	 * @return
	 */
	public String getPackagePath()
	{
		return mPackagePath;
	}

	/**
	 * Get the author.
	 * 
	 * @return
	 */
	public String getAuthor()
	{
		return mAuthor;
	}

	/**
	 * Get the author email address..
	 * 
	 * @return
	 */
	public String getAuthorEmail()
	{
		return mAuthorEmail;
	}

	/**
	 * Go build the template.
	 * 
	 * @param config
	 * @param zos
	 * @throws BuilderException
	 * @throws IOException
	 */
	public void build(JSONObject config, ZipOutputStream zos) throws BuilderException, IOException
	{
		mZOS = zos;

		getName(config);
		getPackage(config);
		getAuthor(config);

		for (FileBuilder fb : getFileBuilders())
		{
			String content = fb.build(this, config);
			String filename = getPackagePath() + "/" + fb.getName(this, config);
			addFile(filename, content);
		}

		mZOS.finish();
	}
}
