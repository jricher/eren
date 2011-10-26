/**
 * This is the container that holds all the Scenario editing pages (PeoplePage,
 * RolesPage, FacilitesPage, etc.). To add additional tabs to this form, do so in
 * addPages().
 */
package org.mitre.eren.editor.views.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.editor.views.pages.FacilitiesPage;
import org.mitre.eren.editor.views.pages.PeoplePage;
import org.mitre.eren.editor.views.pages.RolesPage;
import org.mitre.eren.editor.views.pages.ScenarioPage;

public class ScenarioFormEditor extends FormEditor
{
	
	public static final String ID = /*ScenarioFormEditor.class.getCanonicalName();*/"org.mitre.eren.editor.views.editors.ScenarioFormEditor";
	private EditorInput input;
	private Model mod;
	
	public ScenarioFormEditor()
	{
		mod = null;
	}
	
	public ScenarioFormEditor(EditorInput input)
	{
		this.input = input;
		mod = input.getMod();
	}

	protected FormToolkit createToolkit(Display display)
	{
		return new FormToolkit(display);
	}

	protected void addPages()
	{
		// Model is pulled from the input here because the input seems to be null on initiation.
		// Probably a bad spot though.
		mod = ((EditorInput) this.getEditorInput()).getMod();
		this.setTitle(mod.getGameDef().getERENScenario().getID().toString() + " Scenario");
		try
		{
			addPage(new ScenarioPage(this, mod));
			addPage(new RolesPage(this, mod));
			addPage(new FacilitiesPage(this, mod));
			addPage(new PeoplePage(this, mod));
		}
		catch (PartInitException e)
		{
			e.printStackTrace();
		}
	}
	
	public void doSave(IProgressMonitor monitor)
	{
		
	}
	

	public void doSaveAs()
	{
	}

	public boolean isSaveAsAllowed()
	{
		return false;
	}
}
