package org.mitre.eren.editor.views.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.EditorPart;

public class DlgEditor extends EditorPart
{
	
	public static final String ID = "org.mitre.eren.editor.views.editors.DlgEditor";
	private IManagedForm manForm;
	private ScrolledForm form;
	private FormToolkit toolkit;

	@Override
	public void doSave(IProgressMonitor monitor)
	{
		
	}

	@Override
	public void doSaveAs()
	{
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
		this.setSite(site);
		this.setInput(input);
		this.setTitle(((EditorInput) this.getEditorInput()).getDlg().getFile().getName());
	}

	@Override
	public boolean isDirty()
	{
		return false;
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		return false;
	}

	@Override
	public void createPartControl(Composite parent)
	{
		manForm = new ManagedForm(parent);
		toolkit = manForm.getToolkit();
		form = manForm.getForm();
		form.setText("Edit Dialogue");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
	}

	@Override
	public void setFocus()
	{
		
	}

}
