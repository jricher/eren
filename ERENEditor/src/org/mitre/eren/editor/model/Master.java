package org.mitre.eren.editor.model;

import java.io.File;
import java.util.Vector;

import org.mitre.eren.editor.gamedefinition.GameDef;

public class Master
{
	private Vector<Model> modelList;
	private static Master master;
	private Publisher pub;
	
	private Master()
	{
		modelList = new Vector<Model>();
		setPub(new Publisher());
	}
	
	public static Master getInstance()
	{
		if (master == null)
		{
			master = new Master();
		}
		return master;
	}
	
	public Vector<Model> getModels()
	{
		return modelList;
	}
	
	public Model addModel(Model model)
	{
		if (!modelList.contains(model))
		{
			modelList.add(model);
			pub.updateAll();
		}
		return model;
	}

	public Publisher getPub()
	{
		return pub;
	}

	public void setPub(Publisher pub)
	{
		this.pub = pub;
	}
	
	public boolean contains(GameDef def)
	{
		if (modelList != null)
		{
			for (Model mod : modelList)
			{
				if (mod.getGameDef().getERENScenario().getID().equals(def.getERENScenario().getID()))
				{
					return true;
				}
			}
			
			return false;
		}
		return false;
	}
}

