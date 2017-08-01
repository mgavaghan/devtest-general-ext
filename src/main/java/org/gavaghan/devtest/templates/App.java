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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gavaghan.devtest.templates.step.StepTemplateBuilder;
import org.gavaghan.json.JSONException;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONValueFactory;

/**
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class App
{
	/** Logger. */
	static private final Logger LOG = LogManager.getLogger(App.class);

	/** List of available builders. */
	static private final List<TemplateBuilder> sBuilders = new ArrayList<TemplateBuilder>();

	static
	{
		// build the list of available builders
		sBuilders.add(new StepTemplateBuilder());
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
			config = (JSONObject) JSONValueFactory.DEFAULT.read(isr);
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
	private static TemplateBuilder selectBuilder(String filename, JSONObject config)
	{
		TemplateBuilder builder = null;

		for (TemplateBuilder candidate : sBuilders)
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
		args = new String[] { "src/test/resources/NewStep.json" };

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

		// find our builder
		TemplateBuilder builder = selectBuilder(configPath, config);
		
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
			builder.build(config, zos);
		}
		catch (BuilderException exc)
		{
			System.out.println();
			System.out.println("Failed to build template: " + exc.getMessage());
			System.out.println();
			System.exit(-4);
		}
		
		System.out.println(builder.getPackagePath());
	}
}
