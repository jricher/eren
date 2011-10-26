package org.mitre.eren.editor.commands;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.editor.parser.XMLFilter;


public class SaveScenarioHandler extends AbstractHandler implements IHandler
{
	final JFileChooser directory = new JFileChooser();
	final XMLFilter filter = new XMLFilter();
	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
        IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
        IEditorInput editorInput = page.getActiveEditor().getEditorInput();
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(editorInput);


        
        
        if (editor == null)
		{
			System.out.println("Invalid Editor.");
		}
   
		return null;
	}
	
	public void saveScenarioFile(File file, Model model)
	{		
		 FileWriter fileWriter;
		try
		{
			fileWriter = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fileWriter);
	        out.write(model.getGameDef().getERENScenario().toString());
	        out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
