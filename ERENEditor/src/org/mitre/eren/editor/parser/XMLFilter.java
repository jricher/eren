package org.mitre.eren.editor.parser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XMLFilter extends FileFilter {

	private final String[] allowedExtensions = new String[] {"xml", "/", "\\"};
	
	@Override
	public boolean accept(File file) {
		for (String extension : allowedExtensions)
		{
			if (file.getName().toLowerCase().endsWith(extension) || file.isDirectory())
			{
				return true;
			}
		}
	    return false;
	}

	@Override
	public String getDescription() {
		return null;
	}

}
