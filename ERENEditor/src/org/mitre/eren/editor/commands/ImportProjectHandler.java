/**
 *  Handles the import of project files and any subsequent file imports required.
 *  If the user attempts to open a file already opened in the workspace,
 *  it will give focus to that projects scenario file.
 *  If a user tries to open a project that has the same scenario ID as an already
 *  opened project, but is not of the same file, it will throw an error.
 */
package org.mitre.eren.editor.commands;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.mitre.eren.editor.gamedefinition.GameDef;
import org.mitre.eren.editor.gamedefinition.GameDefManager;
import org.mitre.eren.editor.model.DlgNode;
import org.mitre.eren.editor.model.Master;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.editor.parser.XMLFilter;

public class ImportProjectHandler extends AbstractHandler implements IHandler
{	
	final JFileChooser directory = new JFileChooser();
	final XMLFilter filter = new XMLFilter();
	private static final String[] filterExtentions = {"*.EREN"};
	private static final String[] filterNames = {"EREN Project Files (*.EREN)"};
	private String basePath;
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
        	
        	GameDef def = new GameDef(openedFile);
        	
        	if (Master.getInstance().contains(def))
        	{
        		return null;
        	}
        	else
        	{
        		Model mod = createModel(def);
        		CallEditorHandler.callScenarioEditor(mod);
        	}
		}
		
		return null;
		
		/** /
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), SWT.MULTI);
        dlg.setFilterNames(filterNames);
        dlg.setFilterExtensions(filterExtentions);
        dlg.setText("Open an existing EREN project...");
        String fileName = dlg.open();
		boolean missing = false;
        if (fileName != null)
		{
        	File openedFile = new File(fileName);
        	GameDef def = new GameDef(openedFile);
        	
        	if (Master.getInstance().contains(def))
        	{
        		return null;
        	}
        	else
        	{
        		Model mod = createModel(def);
        		CallEditorHandler.callScenarioEditor(mod);
        	}
		}
		
		return null;
		/**/
	}
	
	/**
	 * Generates a Model based off of files extraced from the
	 * specified game defintion.
	 * 
	 * @param def The definition the model will be based off of.
	 */
	
	public static Model createModel(GameDef def)
	{
		if (Master.getInstance().contains(def))
		{
			return null;
		}
		else
		{
			Model mod = new Model(def);
			Master.getInstance().addModel(mod);
			importDlgs(mod, def);
			return mod;
		}
	}
	
	/**
	 * Adds incoming dialogue nodes to the appropriate model.
	 * 
	 * @param mod The Model the dialogue files will be added too.
	 * @param def Definition the dialogue files are being pulled from.
	 * @return
	 */
	
	private static Vector <DlgNode> importDlgs(Model mod, GameDef def)
	{
		for (File file : def.getDialogueFiles())
		{
			mod.addDialogue(new DlgNode(file, mod));
		}
		return mod.getDialogues();
	}
	
}
