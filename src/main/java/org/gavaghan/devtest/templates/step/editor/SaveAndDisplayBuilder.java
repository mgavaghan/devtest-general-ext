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
public class SaveAndDisplayBuilder implements MemberBuilder
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

	@Override
	public void build(TemplateBuilder parent, JSONObject config, StringBuilder builder) throws BuilderException, IOException
	{
		String format = parent.readResource("step/editor/SaveAndDisplay.txt");

		// build save and display
		JSONObject fields = ImplFieldsBuilder.getUnqualifiedFields(config);
		StringBuilder save = new StringBuilder();
		StringBuilder display = new StringBuilder();

		if (fields != null)
		{
			for (String key : fields.keySet())
			{
				save.append(MessageFormat.format("      step.set{0}(get{0}().getText());{1}", ImplFieldsBuilder.camelCase(key), parent.getEOL()));
				display.append(MessageFormat.format("      get{0}().setText(step.get{0}());{1}", ImplFieldsBuilder.camelCase(key), parent.getEOL()));
			}
		}

		builder.append(MessageFormat.format(format, parent.getName(), save.toString(), display.toString()));
	}
}
