package org.mitre.eren.editor.views.editors;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProvider implements ITreeContentProvider
{
	public TreeContentProvider()
	{
		
	}
	
	@Override
	public Object[] getChildren(Object arg0)
	{
		return ((TreeNode)arg0).getChildren();
	}

	@Override
	public Object getParent(Object arg0)
	{
		return ((TreeNode)arg0).getParent();
	}

	@Override
	public boolean hasChildren(Object arg0)
	{
		TreeNode[] tmp = ((TreeNode)arg0).getChildren();
		if ((tmp != null) && (tmp.length != 0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Object[] getElements(Object arg0)
	{
		
		return ((TreeNode) arg0).getChildren();
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2)
	{
	}

}