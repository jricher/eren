package org.mitre.eren.editor.model;

/**
 * The action file container.
 */

import java.io.File;

public class ActNode
{
	private Model mod;
	private File file;
	
	public ActNode(File file, Model mod)
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
}
