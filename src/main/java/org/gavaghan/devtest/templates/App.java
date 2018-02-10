package org.gavaghan.devtest.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.gavaghan.devtest.templates.step.StepTemplateBuilder;
import org.gavaghan.json.CommentedJSONValueFactory;
import org.gavaghan.json.JSONException;
import org.gavaghan.json.JSONObject;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class App
{
	/** List of available template builders. */
	static private final List<TemplateBuilder> sTemplateBuilders = new ArrayList<TemplateBuilder>();

	static
	{
		// build the list of available builders
		sTemplateBuilders.add(new StepTemplateBuilder());
	}

	/**
	 * Load the configuration file.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private static JSONObject loadConfiguration(String filename) throws IOException
	{
		JSONObject config = null;

		try (InputStream fis = new FileInputStream(filename); InputStreamReader isr = new InputStreamReader(fis, "UTF-8"))
		{
			config = (JSONObject) CommentedJSONValueFactory.COMMENTED_DEFAULT.read(isr);
		}
		catch (FileNotFoundException exc)
		{
			System.out.println();
			System.out.println("Config file not found: " + filename);
			System.out.println();
			System.exit(-2);
		}
		catch (JSONException exc)
		{
			System.out.println();
			System.out.println("Invalid config file: " + exc.getMessage());
			System.out.println();
			System.exit(-3);
		}

		return config;
	}

	/**
	 * Select a template builder.
	 * 
	 * @param filename
	 * @param config
	 * @return
	 */
	private static TemplateBuilder selectTemplateBuilder(String filename, JSONObject config)
	{
		TemplateBuilder builder = null;

		for (TemplateBuilder candidate : sTemplateBuilders)
		{
			if (candidate.canBuild(config))
			{
				builder = candidate;
				break;
			}
		}

		if (builder == null)
		{
			System.out.println();
			System.out.println("No template builder available for: " + filename);
			System.out.println();
			System.exit(-1);
		}

		return builder;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		// check arguments
		if (args.length == 0)
		{
			System.out.println();
			System.out.println("usage: [config file]");
			System.out.println();
			System.exit(-1);
		}
		
		String configPath = args[0];

		// load the configuration
		JSONObject config = loadConfiguration(configPath);

		// find our template builder
		TemplateBuilder templateBuilder = selectTemplateBuilder(configPath, config);
		
		// determine output filename
		File configFile = new File(configPath);
		String filename = configFile.getName();
		int dot = filename.indexOf('.');
		if (dot > 0)  filename = filename.substring(0, dot);
		filename += ".zip";
		File file = new File(filename);
		
		// open output file and start building
		try (OutputStream os = new FileOutputStream(file); ZipOutputStream zos = new ZipOutputStream(os))
		{
			templateBuilder.build(config, zos);
		}
		catch (BuilderException exc)
		{
			System.out.println();
			System.out.println("Failed to build template: " + exc.getMessage());
			System.out.println();
			System.exit(-4);
		}
	}
}
