package org.gavaghan.devtest.autostep;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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
import javax.swing.JScrollPane;
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

   /** Map of property names to their Property instance. */
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
    * Convenience method for getting localized AutoStep resources.
    * 
    * @param key  resource key
    * @param args arguments to the source string
    * @return a rendered localized string including
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
    * Add a JCheckBox control to the main panel.
    * 
    * @param insets
    * @param mainPanel
    * @param row
    * @param comp
    * @return next row to render
    */
   private int setupCheckBox(Insets insets, JPanel mainPanel, int row, JComponent comp)
   {
      GridBagConstraints gbc;

      comp.setFont(new Font(comp.getFont().getFontName(), Font.BOLD, 14));

      // add the checkbox
      gbc = new GridBagConstraints();
      gbc.insets = insets;
      gbc.gridx = 0;
      gbc.gridy = row;
      gbc.gridwidth = 2;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(comp, gbc);

      return row + 1;
   }

   /**
    * Add a JTextField control to the main panel.
    * 
    * @param leftInsets
    * @param rightInsets
    * @param mainPanel
    * @param row
    * @param prop
    * @param comp
    * @return next row to render
    */
   private int setupTextField(Insets leftInsets, Insets rightInsets, JPanel mainPanel, int row, Property prop, JComponent comp)
   {
      GridBagConstraints gbc;

      // add the label
      JLabel label = new JLabel(mPrototype.getDescription(prop.name()) + ": ");
      label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 14));

      gbc = new GridBagConstraints();
      gbc.insets = leftInsets;
      gbc.gridx = 0;
      gbc.gridy = row;
      gbc.gridwidth = 1;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.BASELINE;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(label, gbc);

      // add the component
      comp.setFont(new Font(comp.getFont().getFontName(), Font.PLAIN, 14));

      gbc = new GridBagConstraints();
      gbc.insets = rightInsets;
      gbc.gridx = 1;
      gbc.gridy = row;
      gbc.gridwidth = 1;
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.BASELINE;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      mainPanel.add(comp, gbc);

      return row + 1;
   }

   /**
    * Add a JTextField control inside a JScrollPane control to the main panel.
    * 
    * @param insets
    * @param mainPanel
    * @param row
    * @param prop
    * @param comp
    * @return next row to render
    */
   private int setupTextArea(Insets insets, JPanel mainPanel, int row, Property prop, JComponent comp)
   {
      GridBagConstraints gbc;
      
      int rows = prop.rows();
      
      // FIXME are we localizing exceptions?
      if (rows <= 0)  throw new RuntimeException("Row count must be positive on '" + prop.name() + "'");

      // add the label
      JLabel label = new JLabel(mPrototype.getDescription(prop.name()));
      label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 14));

      gbc = new GridBagConstraints();
      gbc.insets = insets;
      gbc.gridx = 0;
      gbc.gridy = row;
      gbc.gridwidth = 2;
      gbc.weightx = 0;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.WEST;
      gbc.fill = GridBagConstraints.BOTH;
      mainPanel.add(label, gbc);

      // add the component
      comp.setFont(new Font(comp.getFont().getFontName(), Font.PLAIN, 14));

      gbc = new GridBagConstraints();
      gbc.insets = insets;
      gbc.gridx = 1;
      gbc.gridy = row + 1;
      gbc.gridwidth = 2;
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc.anchor = GridBagConstraints.BASELINE;
      gbc.fill = GridBagConstraints.HORIZONTAL;

      // measure the JTextArea size by lines
      FontMetrics metrics = mainPanel.getFontMetrics(comp.getFont());

      int height = rows * metrics.getHeight();

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setMaximumSize(new Dimension(400, height));
      scrollPane.setPreferredSize(new Dimension(400, height));

      scrollPane.setViewportView(comp);

      mainPanel.add(scrollPane, gbc);

      return row + 2;
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

         LOG.debug("About to reflect step properties");
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

      Insets insets = new Insets(10, 10, 0, 10);
      Insets leftInsets = new Insets(10, 10, 0, 1);
      Insets rightInsets = new Insets(10, 1, 0, 10);

      GridBagConstraints gbc;

      // build the main editor panel
      JPanel mainPanel = new JPanel(new GridBagLayout());

      mainPanel.setFont(new Font(mainPanel.getFont().getFontName(), Font.BOLD, 12));

      int row = 0;

      for (Property prop : mProperties)
      {
         JComponent comp = getComponent(prop.name());

         // is it a checkbox?
         if (comp instanceof JCheckBox)
         {
            row = setupCheckBox(insets, mainPanel, row, comp);
         }

         // is it a text area?
         else if (comp instanceof JTextArea)
         {
            row = setupTextArea(insets, mainPanel, row, prop, comp);
         }

         // assume a JTextField
         else
         {
            row = setupTextField(leftInsets, rightInsets, mainPanel, row, prop, comp);
         }
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
    * create custom <code>JComponent</code>s.
    * 
    * @param property the property of the field to create a component for
    * @return the new component
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
         else comp = new JTextField("", 30);
      }
      // initialize a String in an Integer property as long as it parses
      else if (Integer.class.equals(propType))
      {
         comp = new JTextField("", 30);
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
         // if it's a JTextArea
         else if (comp instanceof JTextArea)
         {
            value = ((JTextArea) comp).getText();
         }
         // else, assume it's a JTextArea
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
         // if it's a text area
         else if (comp instanceof JTextArea)
         {
            // FIXME validate text area
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
         // if it's a JTextArea
         else if (comp instanceof JTextArea)
         {
            ((JTextArea) comp).setText((String) value);
         }
         // else, assume a JTextField
         else
         {
            ((JTextField) comp).setText((String) value);
         }
      }
   }
}
