package org.gavaghan.devtest.templates.step.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.FileBuilder;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class EditorFileBuilder extends FileBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = new HashSet<String>();

	/** The list members in this type. */
	static private final List<MemberBuilder> sMembers = new ArrayList<MemberBuilder>();

	static
	{
		// build package list
		sPackages.add("com.itko.lisa.editor.CustomEditor");
		
		// build member list
		sMembers.add(new EditorFieldsBuilder());
		sMembers.add(new EditorAccessorsBuilder());
		sMembers.add(new IsEditorValidBuilder());
		sMembers.add(new SaveAndDisplayBuilder());
		sMembers.add(new SetupEditorBuilder());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.FileBuilder#getMemberBuilders()
	 */
	@Override
	protected List<MemberBuilder> getMemberBuilders()
	{
		return sMembers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages()
	 */
	@Override
	public Set<String> getPackages(JSONObject config)
	{
		return sPackages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.FileBuilder#getName(org.gavaghan.devtest.
	 * templates.TemplateBuilder, org.gavaghan.json.JSONObject)
	 */
	@Override
	public String getName(TemplateBuilder parent, JSONObject config) throws BuilderException, IOException
	{
		return parent.getName() + "Editor.java";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.FileBuilder#writeOpenType(org.gavaghan.
	 * devtest.templates.TemplateBuilder, org.gavaghan.json.JSONObject,
	 * java.lang.StringBuilder)
	 */
	@Override
	public void writeOpenType(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append(MessageFormat.format("public class {0}Editor extends CustomEditor", parent.getName()));
		builder.append(parent.getEOL());
		builder.append("{").append(parent.getEOL());
	}
}
