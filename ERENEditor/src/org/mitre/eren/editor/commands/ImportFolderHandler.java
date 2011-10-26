package org.mitre.eren.editor.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.PlatformUI;
import org.mitre.eren.editor.gamedefinition.GameDef;
import org.mitre.eren.editor.gamedefinition.GameDefManager;

public class ImportFolderHandler extends AbstractHandler implements IHandler
{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{		 
		DirectoryDialog dialog = new DirectoryDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell());
		boolean missing = false;
		String fileName = dialog.open();
        if (fileName != null)
		{
        	File openedFile = new File(fileName);
        	GameDefManager manager = GameDefManager.getInstance();
        	
        	manager.openGameDefs(openedFile);
        	
        	for (GameDef def : manager.getGameDefs())
        	{
        		ImportProjectHandler.createModel(def);
        	}
		}
		
		return null;
	}

}
