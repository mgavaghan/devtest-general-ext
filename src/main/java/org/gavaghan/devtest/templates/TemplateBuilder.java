package org.gavaghan.devtest.templates;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tools.ant.BuildException;
import org.gavaghan.json.JSONNull;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONString;
import org.gavaghan.json.JSONValue;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class TemplateBuilder
{
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
	 * Implementation specific building.
	 * 
	 * @param config
	 * @throws BuildException
	 * @throws IOException
	 */
	protected abstract void build(JSONObject config) throws BuildException, IOException;

	/**
	 * Null safe value get of a string.
	 * 
	 * @param json
	 * @param name
	 * @return
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
	 * Determine whether this implementation can build something with this
	 * configuration.
	 * 
	 * @param config
	 *           configuration to check
	 * @return 'true' if we can build it.
	 */
	public abstract boolean canBuild(JSONObject config);

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
		
		build(config);

		mZOS.finish();
	}
}
