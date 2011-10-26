/**
 * For reference, this class assumes all game definitions
 * take the following form on disk:
 * 
 * /<Scenario ID>
 *		<Scenario ID>.EREN
 *		Scenario.xml
 *		Actions.xml
 *		/Dialogues
 *			<*>.xml
 *		/Resources
 *			/Images
 *				/Icons
 *					<*>.*
 *				/NPCs
 *					<*>.*
 *			/KML
 *				/Events
 *					<*>.kml
 *				/Zipcodes
 *					<*>.kml
 */

package org.mitre.eren.editor.gamedefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;
import org.mitre.eren.editor.commands.CallEditorHandler;
import org.mitre.eren.editor.model.Master;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.protocol.kml.KMLExtensionFactory;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.ScenarioExtensionFactory;
import org.mitre.eren.protocol.startup.StartupExtensionFactory;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class GameDef
{
	private File root = null;
	private ERENscenario erenScenario = null;
	private File scenario = null;
	private File actions = null;
	private Vector <File> dlgs = null;
	private Vector <File> evts = null;
	private Vector <File> icns = null;
	private Vector <File> npcs = null;
	private Vector <File> zips = null;
	
	public GameDef(File file)
	{
		setRoot(file);
		openGameDef(root);
	}
	
	private File setRoot(File file)
	{
		if (file.isDirectory())
		{
			this.root = file;
		}
		else
		{
			this.root = new File(file.getParent());
		}
		openGameDef(root);
		return this.root;
	}
	
	private ERENscenario openERENScenario(File scenario)
	{
		if (scenario != null)
		{
			Abdera abdera = Abdera.getInstance();
			Factory factory = abdera.getFactory();
			factory.registerExtension(new ScenarioExtensionFactory());
			factory.registerExtension(new KMLExtensionFactory());
			factory.registerExtension(new StartupExtensionFactory());
			Parser parser = factory.newParser();
			org.apache.abdera.model.Document <ERENscenario> doc;
			try
			{
				doc = parser.parse(new FileInputStream(scenario));
				this.erenScenario = (ERENscenario) doc.getRoot();
				return this.erenScenario;
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
	private void openGameDef(File base)
	{
		System.out.println("Hi.");
		if (base == null)
		{
			//
		}
		if (base.listFiles() == null)
		{
			//
		}
		if ((base != null) && (base.listFiles() != null))
		{
			boolean rsrcMiss = true;
			for (File file : base.listFiles())
			{
				if((file.getName().equals("Scenario.xml")) || (file.getName().equals("Scenario.XML")))
				{
					setScenario(file);
				}
				if(file.getName().equals("Actions.xml"))
				{
					setActions(file);
				}
				if ((file.isDirectory()) && (file.getName().equals("Dialogues")) && (file.listFiles() != null))
				{
					openDlgsFolder(file);
				}
				if ((file.isDirectory()) && (file.getName().equals("Resources")) && (file.listFiles() != null))
				{
					openRsrcsFolder(file);
					rsrcMiss = false;
				}
			}
			if (getScenario() == null)
			{
				new File(root.getParentFile().getPath() + "Scenario.xml");
			}
			if (getActions() == null)
			{
				new File(root.getParentFile().getPath() + "Actions.xml");
			}
			if (getDialogueFiles() == null)
			{
				System.out.println("Making dlg folder.");
				new File(root.getParentFile().getPath() + "Dialogues").mkdir();
			}
			if (rsrcMiss == true)
			{
				//
			}
			System.out.println("Hi.");
		}
		openERENScenario(scenario);
	}
	
	public Vector <File> getDialogueFiles()
	{
		return dlgs;
	}
	
	public Vector <File> getEventFiles()
	{
		return evts;
	}
	
	public Vector <File> getIconFiles()
	{
		return icns;
	}
	
	public Vector <File> getNPCFiles()
	{
		return npcs;
	}
	
	public Vector <File> getZipcodeFiles()
	{
		return zips;
	}
	
	private void openRsrcsFolder(File rsrcFolder)
	{
		for (File file : rsrcFolder.listFiles())
		{
			if ((file.isDirectory()) && (file.getName().equals("Images")) && (file.listFiles() != null))
			{
				openImagesFolder(file);
			}
			if ((file.isDirectory()) && (file.getName().equals("KML")) && (file.listFiles() != null))
			{
				openKMLFolder(file);
			}
		}
	}
	
	private void openKMLFolder(File kmlFolder)
	{
		for (File file : kmlFolder.listFiles())
		{
			if ((file.isDirectory()) && (file.getName().equals("Events")) && (file.listFiles() != null))
			{
				openEventsFolder(file);
			}
			if ((file.isDirectory()) && (file.getName().equals("Zipcodes")) && (file.listFiles() != null))
			{
				openZipFolder(file);
			}
		}
	}
	
	private void openZipFolder(File zipsFolder)
	{
		zips = new Vector <File> ();
		for (File file : zipsFolder.listFiles())
		{
			if (file.getName().endsWith(".kml"))
			{
				zips.add(file);
			}
		}
	}
	
	private void openEventsFolder(File evtsFolder)
	{
		evts = new Vector <File> ();
		for (File file : evtsFolder.listFiles())
		{
			if (file.getName().endsWith(".kml"))
			{
				evts.add(file);
			}
		}
	}
	
	private void openImagesFolder(File imgsFolder)
	{
		for (File file : imgsFolder.listFiles())
		{
			if ((file.isDirectory()) && (file.getName().equals("Icons")) && (file.listFiles() != null))
			{
				openIconsFolder(file);
			}
			if ((file.isDirectory()) && (file.getName().equals("NPCs")) && (file.listFiles() != null))
			{
				openNPCsFolder(file);
			}
		}
	}
	
	private void openIconsFolder(File icnsFolder)
	{
		icns = new Vector <File> ();
		for (File file : icnsFolder.listFiles())
		{
			if (!file.isDirectory())
			{
				icns.add(file);
			}
		}
	}
	
	private void openNPCsFolder(File npcsFolder)
	{
		npcs = new Vector <File> ();
		for (File file : npcsFolder.listFiles())
		{
			if (!file.isDirectory())
			{
				npcs.add(file);
			}
		}
	}
	
	private void openDlgsFolder(File dlgsFolder)
	{
		dlgs = new Vector <File> ();
		if (dlgsFolder.listFiles() != null)
		{
			for (File file : dlgsFolder.listFiles())
			{
				if (file.getName().endsWith(".xml"))
				{
					dlgs.add(file);
				}
			}
		}
	}

	public void setScenario(File scenario) {
		this.scenario = scenario;
	}

	public File getScenario() {
		return scenario;
	}

	public void setActions(File actions)
	{
		this.actions = actions;
	}

	public File getActions()
	{
		return actions;
	}
	public void setERENScenario(ERENscenario scenario)
	{
		this.erenScenario = scenario;
	}

	public ERENscenario getERENScenario()
	{
		return erenScenario;
	}
}
