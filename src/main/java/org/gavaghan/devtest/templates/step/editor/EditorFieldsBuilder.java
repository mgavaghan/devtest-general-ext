package org.gavaghan.devtest.templates.step.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.devtest.templates.step.impl.ImplFieldsBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * Render step fields.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class EditorFieldsBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = new ArrayList<String>();
	
	static
	{
		// build package list
		sPackages.add("javax.swing.JTextField");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages(org.gavaghan.json.JSONObject)
	 */
	@Override
	public List<String> getPackages(JSONObject config) throws BuilderException
	{
		return sPackages;
	}

	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append(MessageFormat.format("   /** Initialized flag. */{0}	private boolean mInit = false;{0}", parent.getEOL()));
		
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		if (fields == null) return;
		
		for (String key : fields.keySet())
		{
			builder.append(parent.getEOL());
			
			builder.append(MessageFormat.format("   /** {0} */{1}", key, parent.getEOL()));
			builder.append(MessageFormat.format("   private JTextField m{0} = new JTextField();{1}", ImplFieldsBuilder.camelCase(key), parent.getEOL()));
		}
	}
}
