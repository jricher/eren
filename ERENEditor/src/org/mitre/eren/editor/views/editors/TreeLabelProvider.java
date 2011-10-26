package org.mitre.eren.editor.views.editors;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.mitre.eren.editor.model.ActNode;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.protocol.scenario.Airport;
import org.mitre.eren.protocol.scenario.EOC;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.Hospital;
import org.mitre.eren.protocol.scenario.NPC;
import org.mitre.eren.protocol.scenario.POD;
import org.mitre.eren.protocol.scenario.RSS;
import org.mitre.eren.protocol.scenario.Role;
import org.mitre.eren.protocol.scenario.Staff;

public class TreeLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element)
	{
		Object val = ((TreeNode) element).getValue();
		String ret = "";
		
		if (val instanceof Model)
		{
			ret = ((Model) val).getGameDef().getERENScenario().getID().toString();
		}
		else if (val instanceof ERENscenario)
		{
			ret = ((ERENscenario) val).getID().toString();
			
		}
		else if (val instanceof Role)
		{
			ret = ((Role) val).getID().toString();
		}
		else if (val instanceof EOC)
		{
			ret = ((EOC) val).getID().toString();
		}
		else if (val instanceof Airport)
		{
			ret = ((Airport) val).getID().toString();
		}
		else if (val instanceof Hospital)
		{
			ret = ((Hospital) val).getID().toString();
		}
		else if (val instanceof RSS)
		{
			ret = ((RSS) val).getID().toString();
		}
		else if (val instanceof POD)
		{
			ret = ((POD) val).getID().toString();
		}
		else if (val instanceof Staff)
		{
			ret = ((Staff) val).getID().toString();
		}
		else if (val instanceof NPC)
		{
			NPC npc = (NPC) val;
			ret = ((NPC) val).getID().toString();
		}
		else if (val instanceof ActNode)
		{
			ActNode act = (ActNode) val;
			ret = act.getFile().getName().toString();
		}
		else
		{
			ret = (String) val;
		}
		return ret;
	}
}