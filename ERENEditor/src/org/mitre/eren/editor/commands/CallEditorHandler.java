package org.mitre.eren.editor.commands;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.mitre.eren.editor.gamedefinition.GameDef;
import org.mitre.eren.editor.model.ActNode;
import org.mitre.eren.editor.model.DlgNode;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.editor.views.editors.ActEditor;
import org.mitre.eren.editor.views.editors.DlgEditor;
import org.mitre.eren.editor.views.editors.EditorInput;
import org.mitre.eren.editor.views.editors.ScenarioFormEditor;

public class CallEditorHandler
{
	private static boolean exists = false;
	public static void callScenarioEditor(Model mod)
	{
		GameDef def = mod.getGameDef();
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		EditorInput input = new EditorInput(def.getERENScenario().getID().toString(), mod);
		for (IEditorReference ref : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences())
		{
			try
			{
				if (ref.getEditorInput().getName().equals(def.getERENScenario().getID().toString()))
				{
					ref.getEditor(true);
					exists = true;
				}
			}
			catch (PartInitException e)
			{
				e.printStackTrace();
			}
		}
		if (exists == false)
		{
			try
			{
				IEditorPart newEditor = page.openEditor(input, ScenarioFormEditor.ID);				
			}
			catch (PartInitException e)
			{
				e.printStackTrace();
			}
		}
		exists = false;
	}
	public static void callDlgEditor(DlgNode dlg)
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		EditorInput input = new EditorInput(dlg.getFile().getName().toString(), dlg);
		for (IEditorReference ref : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences())
		{
			try
			{
				if (ref.getEditorInput().getName().equals(dlg.getFile().getName()))
				{
					ref.getEditor(true);
					exists = true;
				}
			}
			catch (PartInitException e)
			{
				e.printStackTrace();
			}
		}
		if (exists == false)
		{
			/**/
			try
			{
				IEditorPart newEditor = page.openEditor(input, DlgEditor.ID);				
			}
			catch (PartInitException e)
			{
				e.printStackTrace();
			}
			/**/
		}
	}
	
	public static void callActEditor(ActNode act)
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		EditorInput input = new EditorInput(act.getFile().getName().toString(), act);
		for (IEditorReference ref : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences())
		{
			try
			{
				if (ref.getEditorInput().getName().equals(act.getFile().getName()))
				{
					ref.getEditor(true);
					exists = true;
				}
			}
			catch (PartInitException e)
			{
				e.printStackTrace();
			}
		}
		if (exists == false)
		{
			/**/
			try
			{
				IEditorPart newEditor = page.openEditor(input, ActEditor.ID);				
			}
			catch (PartInitException e)
			{
				e.printStackTrace();
			}
			/**/
		}
	}
}
