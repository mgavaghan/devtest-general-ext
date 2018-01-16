package org.gavaghan.devtest.templates.step.editor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.gavaghan.devtest.templates.BuilderException;
import org.gavaghan.devtest.templates.MemberBuilder;
import org.gavaghan.devtest.templates.TemplateBuilder;
import org.gavaghan.devtest.templates.step.impl.ImplFieldsBuilder;
import org.gavaghan.json.JSONObject;
import org.gavaghan.json.JSONValue;

import com.ibm.icu.text.MessageFormat;

/**
 * Render step fields.
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class EditorFieldsBuilder implements MemberBuilder
{
	/**
	 * Get the fully qualified Swing type to use in the editor for a field type.
	 * 
	 * @param field
	 * @return
	 */
	static public String getQualifiedSwingTypeForField(JSONValue field)
	{
		String fieldType = field.getValue().toString();

		if ("Boolean".equals(fieldType)) return "javax.swing.JCheckBox";
		if ("boolean".equals(fieldType)) return "javax.swing.JCheckBox";

		return "javax.swing.JTextField";
	}

	/**
	 * Get the unqualified Swing type to use in the editor for a field type.
	 * 
	 * @param field
	 * @return
	 */
	static public String getUnqualifiedSwingTypeForField(JSONValue field)
	{
		String fqn = getQualifiedSwingTypeForField(field);
		int dot = fqn.lastIndexOf('.');
		return fqn.substring(dot+1);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gavaghan.devtest.templates.HasDependencies#getPackages(org.gavaghan.json.JSONObject)
	 */
	@Override
	public Set<String> getPackages(JSONObject config) throws BuilderException
	{
		Set<String> packages = new HashSet<String>();
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		
		for (String key : fields.keySet())
		{
			String swingType = getQualifiedSwingTypeForField(fields.get(key));
			packages.add(swingType);
		}
		
		return packages;
	}

	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append(MessageFormat.format("   /** Initialized flag. */{0}  private boolean mInit = false;{0}", parent.getEOL()));
		
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		if (fields == null) return;
		
		for (String key : fields.keySet())
		{
			builder.append(parent.getEOL());
			
			String swingType = getUnqualifiedSwingTypeForField(fields.get(key));
			
			builder.append(MessageFormat.format("   /** {0} */{1}", key, parent.getEOL()));
			builder.append(MessageFormat.format("   private {2} m{0} = new {2}();{1}", ImplFieldsBuilder.camelCase(key), parent.getEOL(), swingType));
		}
	}
}
