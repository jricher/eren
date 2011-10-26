package org.mitre.eren.editor.views;

/**
 * Generates labels for the tree based on what kind of object
 * a tree node is containing. If the object isn't recognized,
 * it will attempt to cast it to a string.
 */

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.graphics.Image;
import org.mitre.eren.editor.model.ActNode;
import org.mitre.eren.editor.model.DlgNode;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.javautil.swing.ImageCache;

public class ScenarioTreeLabelProvider extends LabelProvider {

	
	/**
	 * Determines the label for a tree node based on what object it contains.
	 */
	@Override
	public String getText(Object element)
	{
		Object val = ((TreeNode) element).getValue();
		String ret = "";
		if (val instanceof Model)
		{
			ret = "Scenario.xml";
		}
		else if (val instanceof ERENscenario)
		{
			ret = "Scenario.xml";
			
		}
		else if (val instanceof DlgNode)
		{
			ret = ((DlgNode) val).getFile().getName();
		}
		else if (val instanceof ActNode)
		{
			ret = "Actions.xml";
		}
		else
		{
			ret = (String) val;
		}
		return ret;
	}
}