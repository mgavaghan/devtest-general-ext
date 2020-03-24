package org.gavaghan.devtest.autostep;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itko.lisa.editor.CustomEditor;

/**
 * Base class for DevTest editors that are defined declaratively.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public abstract class AutoEditor<T extends AutoStep> extends CustomEditor
{
   /** Logger. */
   static private final Logger LOG = LoggerFactory.getLogger(AutoEditor.class);

   /** Comparator for sorting properties. */
   static private final Comparator<Property> PROP_COMP = new Comparator<Property>()
   {
      @Override
      public int compare(Property left, Property right)
      {
         if (left.index() < right.index()) return -1;
         if (left.index() > right.index()) return +1;
         return 0;
      }
   };

   /** Initialized flag. */
   private boolean mInit = false;

   /** All of the properties on the step sorted by index. */
   private final List<Property> mProperties = new ArrayList<Property>();

   /** Map of property names to their UI components. */
   private final Map<String, JComponent> mPropComponents = new HashMap<String, JComponent>();

   /** Map of property names to their UI components. */
   private final Map<String, Property> mPropByName = new HashMap<String, Property>();

   /** Our factory for creating instances of the type parameter. */
   private final TypeParameterFactory<T> mStepFactory;

   /** Our step prototype. */
   private AutoStep mPrototype;

   /** Our concrete type. */
   private final Class<?> mSubClass;

   /** Our step key */
   private final String mStepKey;

   /**
    * Convenience method for getting AutoStep resources.
    * 
    * @param key
    * @param args
    * @return
    */
   private String getString(String key, Object... args)
   {
      return AutoStepUtils.getString(AutoStep.class, key, args);
   }

   /**
    * Find all of the properties from the step's @Property annotations and select
    * UI Components.
    */
   private void reflectStepProperties()
   {
      List<Property> props = AutoStepUtils.getProperties(mPrototype.getClass());

      for (Property prop : props)
      {
         // save the property
         mProperties.add(prop);
         mPropByName.put(prop.name(), prop);

         // create the component for this property
         JComponent comp = createComponent(prop);
         mPropComponents.put(prop.name(), comp);
      }

      // sort in index order
      mProperties.sort(PROP_COMP);

      if (props.size() == 0) LOG.warn(getString("LogNoProperties", mSubClass.getName()));
   }

   /**
    * Start reflecting on the concrete type
    * 
    * @param stepType the AutoStep we edit
    */
   protected AutoEditor(Class<T> stepType)
   {
      try
      {
         // build a type factory so we can get a prototype instance of the step
         mStepFactory = new TypeParameterFactory<T>(stepType);
         mPrototype = mStepFactory.get();

         mStepKey = mPrototype.getStepKey();

         // get concrete type
         mSubClass = getClass();
         if (LOG.isDebugEnabled()) LOG.debug("Constructing AutoEditor of type: " + mSubClass.getName());

         LOG.debug("About to reflecy step properties");
         reflectStepProperties();
      }
      catch (RuntimeException exc)
      {
         String text = getString("FailedToInstantiate", getClass().getName());
         LOG.error(text, exc);
         throw new RuntimeException(text, exc);
      }
   }

   /**
    * Build the UI.
    */
   protected void setupEditor()
   {
      LOG.debug("setupEditor()");

      GridBagConstraints gbc;

      // build the main editor panel
      JPanel mainPanel = new JPanel(new GridBagLayout());
      setMinimumSize(new Dimension(300, 300));
      
      int row = 0;
      
      for (Property prop : mProperties)
      {
         JComponent comp = getComponent(prop.name());

         // FIXME have we handled all types?  JTextArea?

         // is it a checkbox?
         if (comp instanceof JCheckBox)
         {
            // add the checkbox
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(comp, gbc);
         }

         // assume a JTextField
         else
         {
            // add the label
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 1;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(new JLabel(mPrototype.getDescription(prop.name()) + ": "), gbc);

            // add the component
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.gridwidth = 1;
            gbc.weightx = 1;
            gbc.weighty = 0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(comp, gbc);
         }

         row++;
      }

      // add main panel to editor
      setLayout(new GridBagLayout());
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1;
      gbc.weighty = 1;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      add(mainPanel, gbc);
   }

   /**
    * Create the UI component for the Property. Subclasses may override this to
    * create custom JComponents.
    * 
    * @param property the property of the field to create a component for.
    * @return the new component.
    */
   protected JComponent createComponent(Property property)
   {
      Class<?> propType = AutoStepUtils.getBoxedType(property.type());
      JComponent comp;

      // initialize a String in a text field
      if (String.class.equals(propType))
      {
         if (property.sensitive() && property.multiline())
         {
            // FIXME localize
            LOG.warn("Both 'sensitive' and 'multiline' are set on '" + property.name() + "' so 'sensitive' takes precedence.");
         }

         if (property.sensitive()) comp = new JPasswordField("");
         else if (property.multiline()) comp = new JTextArea("");
         else comp = new JTextField("");
      }
      // initialize a String in an Integer property as long as it parses
      else if (Integer.class.equals(propType))
      {
         comp = new JTextField("");
      }
      // initialize a boolean property
      else if (Boolean.class.equals(propType))
      {
         comp = new JCheckBox(mPrototype.getDescription(property.name()));
      }
      else
      {
         throw new IllegalArgumentException(getString("TypeNotSupported", propType, mSubClass.getName()));
      }

      return comp;
   }

   /**
    * Get the JComponent for the property name.
    * 
    * @param propName property name
    * @return the JComponent
    */
   protected final JComponent getComponent(String propName)
   {
      Objects.requireNonNull(propName);

      JComponent comp = mPropComponents.get(propName);

      if (comp == null)
      {
         if (comp == null) throw new RuntimeException(getString("NoSuchProperty", propName, mSubClass.getName()));
      }

      return comp;
   }

   /**
    * Determine if a populated component has a valid value. Subclasses may override
    * this to perform custom validation.
    * 
    * @param text  the text to validate
    * @param klass the target data type
    * @return a String describing why the value is invalid, or 'null' if valid
    */
   protected String isValid(Property prop, String text)
   {
      try
      {
         AutoStepUtils.parseString(text, prop.type());
         return null;
      }
      catch (IllegalArgumentException exc)
      {
         return getString("ValueNotValid", mPrototype.getDescription(prop.name()));
      }
   }

   /**
    * Get the context key.
    * 
    * @return the context key
    */
   public final String getStepKey()
   {
      return mStepKey;
   }

   /**
    * Save the contents of the editor to a step.
    */
   @Override
   public final void save()
   {
      AutoController controller = (AutoController) getController();
      controller.getTestCaseInfo().getTestExec().saveNodeResponse(controller.getName(), controller.getRet());
      AutoStep step = (AutoStep) controller.getAttribute(getStepKey());

      for (String propName : mPropByName.keySet())
      {
         JComponent comp = getComponent(propName);
         Object value;

         // FIXME are we accounting for all types?

         // if it's a JCheckBox
         if (comp instanceof JCheckBox)
         {
            value = new Boolean(((JCheckBox) comp).isSelected());
         }
         else
         {
            value = ((JTextField) comp).getText();
         }

         step.setProperty(propName, value);
      }
   }

   /**
    * Determine if the contents of the editor are valid. Subclasses may customize
    * this by overriding isValid().
    * 
    * @return a String describing why the editor is invalid, or 'null' if valid
    */
   @Override
   public final String isEditorValid()
   {
      for (String propName : mPropByName.keySet())
      {
         Property prop = mPropByName.get(propName);
         JComponent comp = getComponent(propName);

         // if it's a text field
         if (comp instanceof JTextField)
         {
            String text = ((JTextField) comp).getText().trim();

            if (text.length() == 0)
            {
               // make sure mandatory fields are populated
               if (prop.mandatory())
               {
                  return getString("ValueMissing", mPrototype.getDescription(propName));
               }
            }
            // make sure populated fields are valid
            else
            {
               String warning = isValid(prop, text);

               if (warning != null) return warning;
            }
         }
      }

      return null;
   }

   /**
    * Display the editor.
    */
   @Override
   public final void display()
   {
      LOG.debug("display()");

      if (!mInit) setupEditor();

      mInit = true;

      AutoController controller = (AutoController) getController();
      AutoStep step = (AutoStep) controller.getAttribute(controller.getStepKey());

      for (String propName : mPropByName.keySet())
      {
         JComponent comp = getComponent(propName);
         Object value = step.getProperty(propName);

         // FIXME - have we accounted for all types?

         // if it's a JCheckBox
         if (comp instanceof JCheckBox)
         {
            ((JCheckBox) comp).setSelected(((Boolean) value).booleanValue());
         }
         // else, assume a JTextField
         else
         {
            ((JTextField) comp).setText((String) value);
         }
      }
   }
}
