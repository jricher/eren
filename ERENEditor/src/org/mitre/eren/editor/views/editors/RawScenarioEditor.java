/**
 * This is the text editor for the Scenario File, it still needs change listeners and validation.
 */
package org.mitre.eren.editor.views.editors;

import org.eclipse.ui.examples.rcp.texteditor.editors.xml.XMLEditor;
import org.mitre.eren.editor.model.ISubscriber;
import org.mitre.eren.editor.model.Model;

public class RawScenarioEditor extends XMLEditor implements ISubscriber//, IFormPage
{
	/** /
	private FormEditor editor;
	private PageForm managedForm;
	private int index;
	private String id;
	/**/
	
	private Model mod;
	
	@SuppressWarnings("deprecation")
	public RawScenarioEditor(Model model)
	{
		super();
		this.setTitle("Raw Scenario File");
		mod = model;
		mod.getPub().attach(this);
	}
	
	@Override
	public void update()
	{
		
	}

	@Override
	public void push() {
		// TODO Auto-generated method stub
		
	}
}
