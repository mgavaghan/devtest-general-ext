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
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
public class IsEditorValidBuilder implements MemberBuilder
{
	/** The list of packages this builder depends on. */
	static private final List<String> sPackages = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.HasDependencies#getPackages(org.gavaghan.
	 * json.JSONObject)
	 */
	@Override
	public List<String> getPackages(JSONObject config) throws BuilderException
	{
		return sPackages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gavaghan.devtest.templates.MemberBuilder#build(org.gavaghan.devtest.
	 * templates.TemplateBuilder, org.gavaghan.json.JSONObject,
	 * java.lang.StringBuilder)
	 */
	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		builder.append(MessageFormat.format("   /*{0}    * (non-Javadoc){0}    * @see com.itko.lisa.editor.CustomEditor#isEditorValid(){0}    */{0}   @Override{0}   public String isEditorValid(){0}   '{'{0}", parent.getEOL()));

		// build save and display
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);

		if (fields != null)
		{
			for (String key : fields.keySet())
			{
				builder.append(MessageFormat.format("      if (m{1}.getText().trim().length() == 0) return \"Please specify a {0}\";{2}", key, ImplFieldsBuilder.camelCase(key), parent.getEOL()));
			}
		}

		builder.append(MessageFormat.format("      return null;{0}   }{0}", parent.getEOL()));
	}
}
