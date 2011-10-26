package org.mitre.eren.editor.model;

import org.eclipse.jface.viewers.TreeNode;

public class Node extends TreeNode
{
	public Node(Object value) {
		super(value);
	}

	public void setValue(Object object)
	{
		value = object;
	}
}
