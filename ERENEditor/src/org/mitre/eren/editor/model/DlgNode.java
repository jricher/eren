package org.mitre.eren.editor.model;

/**
 * The dialogue node container.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.Digester;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.mitre.eren.dialogue.custom.DuplicateSC;
import org.mitre.eren.dialogue.custom.EvalExpression;
import org.mitre.eren.dialogue.custom.SendOpenPod;
import org.mitre.eren.dialogue.custom.SendResourceMessage;
import org.mitre.eren.dialogue.custom.SendUserMessage;
import org.mitre.eren.dialogue.custom.WaitTimer;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class DlgNode
{
	private Model mod;
	private File file;
	
	public DlgNode(File file, Model mod)
	{
		this.setMod(mod);
		this.setFile(file);
	}

	private void setMod(Model mod)
	{
		this.mod = mod;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public Model getMod()
	{
		return mod;
	}
	
	private void parseDlg()
	{
		/**/
		try
		{	
			Digester dig = SCXMLParser.newInstance(null, null, null);
			dig.setNamespaceAware(true);
			dig.setXIncludeAware(true);
			
				dig.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", false);
			
			dig.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
			// dig.setFeature("http://xml.org/sax/features/namespaces", true); //
			// default is true
			SCXML stateMachine;
			stateMachine = (SCXML) dig.parse(this.getFile());
			SCXMLParser.updateSCXML(stateMachine);
			System.out.println(stateMachine.getChildren().toString());
						
		}
		catch (SAXNotRecognizedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXNotSupportedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ModelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**/
	}
}
