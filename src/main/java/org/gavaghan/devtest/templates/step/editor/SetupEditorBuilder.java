package org.gavaghan.devtest.templates.step.editor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.devtest.templates.step.impl.ImplFieldsBuilder;
import org.gavaghan.json.JSONObject;

import com.ibm.icu.text.MessageFormat;

/**
 * Render setupEditor.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class SetupEditorBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final Set<String> sPackages = new HashSet<String>();
	
	static
	{
		// build package list
		sPackages.add("java.awt.Dimension");
		sPackages.add("java.awt.GridBagConstraints");
		sPackages.add("java.awt.GridBagLayout");
		sPackages.add("javax.swing.JLabel");
		sPackages.add("javax.swing.JPanel");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages(org.gavaghan.json.JSONObject)
	 */
	@Override
	public Set<String> getPackages(JSONObject config) throws BuilderException
	{
		return sPackages;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.MemberBuilder#build(org.gavaghan.devtest.templates.TemplateBuilder, org.gavaghan.json.JSONObject, java.lang.StringBuilder)
	 */
	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		String format = parent.readResource("step/editor/SetupEditor.txt");
		String comp = parent.readResource("step/editor/SetupEditorComponent.txt");
		
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		StringBuilder comps = new StringBuilder();
		boolean first = true;
		
		if (fields != null)
		{
			int index = 0;
			
			for (String key : fields.keySet())
			{
				if (first)  first = false;
				else comps.append(parent.getEOL());

				comps.append(MessageFormat.format(comp, ImplFieldsBuilder.camelCase(key), new Integer(index)));
				
				index++;
			}
		}
		
		builder.append(MessageFormat.format(format, comps));
	}
}
