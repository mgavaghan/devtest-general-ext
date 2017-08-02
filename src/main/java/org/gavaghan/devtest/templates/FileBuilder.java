package org.gavaghan.devtest.templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gavaghan.json.JSONObject;

/**
 * Base implementation of a class that renders a template class.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public abstract class FileBuilder implements HasDependencies
{
	/** Comparator for packages. */
	static private final PackageComparator sPackageCompare = new PackageComparator();

	/**
	 * Get the list of member builder.
	 * 
	 * @return
	 */
	protected abstract List<MemberBuilder> getMemberBuilders();

	/**
	 * Get the name of this file.
	 * 
	 * @param parent
	 * @param config
	 * @return
	 * @throws BuilderException
	 * @throws IOException
	 */
	public abstract String getName(TemplateBuilder parent, JSONObject config) throws BuilderException, IOException;

	/**
	 * Write the package name.
	 * 
	 * @param parent
	 * @param config
	 * @param builder
	 * @throws BuilderException
	 * @throws IOException
	 */
	public void writePackage(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append("package ").append(parent.getPackageName()).append(";").append(parent.getEOL());
	}

	/**
	 * Write the imports.
	 * 
	 * @param parent
	 * @param config
	 * @param builder
	 * @throws BuilderException
	 * @throws IOException
	 */
	public void writeImports(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		Map<String, String> uniquePackages = new HashMap<String, String>();

		// get the type's dependencies
		for (String pack : getPackages())
		{
			uniquePackages.put(pack, null);
		}

		// add member dependencies
		for (MemberBuilder memberBuilder : getMemberBuilders())
		{
			for (String pack : memberBuilder.getPackages())
			{
				uniquePackages.put(pack, null);
			}
		}

		// put into a list and sort
		List<String> packages = new ArrayList<String>();

		for (String pack : uniquePackages.keySet())
		{
			packages.add(pack);
		}
		
		packages.sort(sPackageCompare);

		// write them out
		String lastComp = "";
		
		for (String pack : packages)
		{
			// get first component
			int dot = pack.indexOf('.');
			String comp = (dot < 0) ? pack : pack.substring(0, dot);
			
			if (!lastComp.equals(comp))
			{
				lastComp = comp;
				builder.append(parent.getEOL());
			}
			
			builder.append("import ").append(pack).append(";").append(parent.getEOL());
		}
	}

	/**
	 * Write the type comment.
	 * 
	 * @param parent
	 * @param config
	 * @param builder
	 * @throws BuilderException
	 * @throws IOException
	 */
	public void writeTypeComment(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append(parent.getEOL());
		builder.append("/**").append(parent.getEOL());
		builder.append(" *").append(parent.getEOL());

		if (parent.getAuthor() != null)
		{
			builder.append(" * @author ");

			if (parent.getAuthorEmail() != null)
			{
				builder.append("<a href=\"mailto:");
				builder.append(parent.getAuthorEmail());
				builder.append("\">");
				builder.append(parent.getAuthor());
				builder.append("</a>");
			}
			else
			{
				builder.append(parent.getAuthor());
			}

			builder.append(parent.getEOL());
		}

		builder.append(" */").append(parent.getEOL());
	}

	/**
	 * Write the type opening.
	 * 
	 * @param parent
	 * @param config
	 * @param builder
	 * @throws BuilderException
	 * @throws IOException
	 */
	public void writeOpenType(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append("public class ").append(parent.getName()).append(parent.getEOL());
		builder.append("{").append(parent.getEOL());
	}

	/**
	 * Write the type closing.
	 * 
	 * @param parent
	 * @param config
	 * @param builder
	 * @throws BuilderException
	 * @throws IOException
	 */
	public void writeCloseType(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append("}").append(parent.getEOL());
	}

	/**
	 * Build the file.
	 * 
	 * @param parent
	 * @param config
	 * @return
	 * @throws BuilderException
	 * @throws IOException
	 */
	public String build(TemplateBuilder parent, JSONObject config) throws BuilderException, IOException
	{
		StringBuilder builder = new StringBuilder();

		writePackage(parent, config, builder);
		writeImports(parent, config, builder);
		writeTypeComment(parent, config, builder);
		writeOpenType(parent, config, builder);
		
		boolean first = true;
		
		for (MemberBuilder memberBuilder : getMemberBuilders())
		{
			if (first)  first = false;
			else builder.append(parent.getEOL());
			
			memberBuilder.build(parent, config, builder);
		}
		
		writeCloseType(parent, config, builder);

		return builder.toString();
	}
}
