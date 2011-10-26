package org.mitre.eren.editor.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.mitre.eren.editor.gamedefinition.GameDef;
import org.mitre.eren.editor.gamedefinition.GameDefManager;
import org.mitre.eren.editor.model.Master;
import org.mitre.eren.editor.model.Model;

public class NewProjectHandler extends AbstractHandler implements IHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Project ID", "Enter a unique ID for the new project: ", "", new IInputValidator()
		{
			
			@Override
			public String isValid(String newText)
			{
				for (Model mod : Master.getInstance().getModels())
				{
					if (mod.getGameDef().getERENScenario().getID().equals(newText))
					{
						return "A scenario with that ID already exists in the workspace.";
					}
					if ("".equals(newText))
					{
						return "ID cannot be empty.";
					}
				}
				return null;
			}
		});
		if (dlg.open() == Window.OK)
     	{
			DirectoryDialog dialog = new DirectoryDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell());
			boolean missing = false;
			String fileName = dialog.open();
	        if (fileName != null)
			{
	        	File openedFile = new File(fileName);
	        	GameDefManager manager = GameDefManager.getInstance();
	        	
	        	GameDef def = manager.openGameDef(openedFile);

	        	ImportProjectHandler.createModel(def);
	        	
	        	def.getERENScenario().setID(dlg.getValue());
	        	
			}
     	}
		return null;
	}

}
