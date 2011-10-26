package org.mitre.eren.editor.gamedefinition;

import java.io.File;
import java.util.Vector;


public class GameDefManager
{
	private static GameDefManager defManager = null;
	Vector <GameDef> gameDefs= null;
	
	private GameDefManager()
	{
		
	}
	
	public boolean defExists(GameDef def)
	{
		for (GameDef gameDef : gameDefs)
		{
			if (gameDef.equals(def))
			{
				return true;
			}
		}
		return false;
	}
	
	public static GameDefManager getInstance()
	{
		if (defManager == null)
		{
			defManager = new GameDefManager();
		}	
		return defManager;
	}
	
	public GameDefManager openGameDefs(File root)
	{
		if (gameDefs == null)
		{
			gameDefs = new Vector <GameDef> ();
		}
		if ((root != null) && (root.listFiles() != null))
		{
			for (File file : root.listFiles())
			{
				if (file.isDirectory())
				{
					gameDefs.add(new GameDef(file));
				}
			}
		}
		return this;
	}
	
	public GameDef openGameDef(File file)
	{
		if (gameDefs == null)
		{
			gameDefs = new Vector <GameDef> ();
		}
		GameDef def = new GameDef(file);
		gameDefs.add(def);
		
		return def;
	}
	
	public GameDefManager addGameDef(GameDef def)
	{
		gameDefs.add(def);
		
		return this;
	}
	
	public Vector <GameDef> getGameDefs()
	{
		return gameDefs;
	}
	
}
