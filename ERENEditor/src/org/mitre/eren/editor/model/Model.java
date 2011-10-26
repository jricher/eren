package org.mitre.eren.editor.model;

/**
 * Keeps track of all the files and objects relevant to a single
 * project. If changes are made anywhere, it will make sure
 * everything else stays in  sync.
 */

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mitre.eren.editor.gamedefinition.GameDef;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Model
{
	private Publisher pub = new Publisher();
	private GameDef gameDef;
	private Vector<DlgNode> dlgList = null;
	private ActNode act = null;
	
	public Model(GameDef def)
	{
		this.setGameDef(def);
		dlgList = new Vector <DlgNode> ();
		Master.getInstance().addModel(this);
		this.setAct(new ActNode(def.getActions(), this));
	}

	public void setPub(Publisher pub)
	{
		this.pub = pub;
	}

	public Publisher getPub()
	{
		return pub;
	}


	public void setGameDef(GameDef gameDef)
	{
		this.gameDef = gameDef;
		pub.updateAll();
	}

	public GameDef getGameDef()
	{
		return gameDef;
	}
	
	public Vector <DlgNode> getDialogues()
	{
		return this.dlgList;
	}
	
	public DlgNode addDialogue(DlgNode node)
	{
		dlgList.add(node);
		getPub().updateAll();
		Master.getInstance().getPub().updateAll();
		return node;
	}

	public void setAct(ActNode act) {
		this.act = act;
	}

	public ActNode getAct() {
		return act;
	}
}
