package org.mitre.eren.editor.views;


import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;
import org.mitre.eren.editor.commands.CallEditorHandler;
import org.mitre.eren.editor.model.ActNode;
import org.mitre.eren.editor.model.DlgNode;
import org.mitre.eren.editor.model.ISubscriber;
import org.mitre.eren.editor.model.Master;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.editor.model.Node;
import org.mitre.eren.protocol.scenario.ERENscenario;



public class ScenarioTreeView extends ViewPart implements ISubscriber
{
	
	public static final String ID = "org.mitre.eren.editor.views.ScenarioTreeView";
	public static final String label = "Scenarios";
	private Master model = Master.getInstance();

	private TreeViewer viewer;
	private Node root = new Node("");
	
	public ScenarioTreeView()
	{
		model.getPub().attach(this);
	}

	public void createPartControl(Composite parent)
	{
		
		PatternFilter filter = new PatternFilter();
		FilteredTree tree = new FilteredTree(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL, filter, true);

		viewer = tree.getViewer();
		viewer.setContentProvider(new ScenarioTreeContentProvider());
		viewer.setLabelProvider(new ScenarioTreeLabelProvider());
		viewer.setInput(getRootNode());

		IOpenListener listener = new IOpenListener()
		{
			@Override
			public void open(OpenEvent event)
			{
				TreePath[] paths = ((TreeSelection)event.getSelection()).getPaths();
				
				for (TreePath path : paths)
				{
					if ((path != null) && !(((Node) path.getLastSegment()).getValue() instanceof String))
					{
						Object val = ((Node) path.getLastSegment()).getValue();
						
						if (val instanceof ERENscenario)
						{
							ERENscenario scenario = (ERENscenario) val;
							try
							{
							//	CallEditorHandler.callScenarioEditor(scenario);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
						if (val instanceof Model)
						{
							Model mod = (Model) val;
							try
							{
								CallEditorHandler.callScenarioEditor(mod);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
						if (val instanceof DlgNode)
						{
							DlgNode dlg = (DlgNode) val;
							try
							{
								CallEditorHandler.callDlgEditor(dlg);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
						if (val instanceof ActNode)
						{
							ActNode act = (ActNode) val;
							try
							{
								CallEditorHandler.callActEditor(act);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
					}
				}
				
			}
		};
		viewer.addOpenListener(listener);	
	//	viewer.getTree().setVisible(false);
	}
	
	private Node getRootNode()
	{
		root = new Node("");
		Node[] kids = new Node[1];
		kids[0] = new Node("There are no open projects to display.");
		root.setChildren(kids);
	    return root;
	}

	public void setFocus()
	{
		viewer.getControl().setFocus();
	}
	
	private void updateTree()
	{
		/** /
		if (Model.getInstance().getScenario() != null)
		{
			scenarioNode.setValue((Object) Model.getInstance().getScenario());
			if (Model.getInstance().getDlgList() != null)
			{	
				Node[] scenarioKids = new Node[Model.getInstance().getDlgList().size()];
				for (int i = 0; i < Model.getInstance().getDlgList().size(); i++)
				{
					scenarioKids[i] = new Node(Model.getInstance().getDlgList().get(i));
				}
				scenarioNode.setChildren(scenarioKids);
			}
		}
		/**/
		if (Master.getInstance().getModels().size() != 0)
		{
			Node[] kids = new Node[Master.getInstance().getModels().size()];
			for (int i = 0; i < Master.getInstance().getModels().size(); i++)
			{
				Model mod = Master.getInstance().getModels().get(i);
				Node modNode = new Node(mod.getGameDef().getERENScenario().getID().toString());
				kids[i] = modNode;
				Node dlgNode = new Node("Dialogues");
				Node[] modNodeKids = new Node[3];
				modNodeKids[1] = new Node(mod);
				modNodeKids[2] = new Node(mod.getAct());
				modNodeKids[0] = dlgNode;
				modNode.setChildren(modNodeKids);
				Node[] dlgNodes = null;
				if (mod.getDialogues().size() != 0)
				{
					dlgNodes = new Node[mod.getDialogues().size()];
					for (int j = 0; j < mod.getDialogues().size(); j++)
					{
						Node node = new Node(mod.getDialogues().get(j));
						dlgNodes[j] = node;
					}					
				}
				else
				{
					dlgNodes = new Node[1];
					dlgNodes[0] = new Node("There are no dialogues to display.");
				}
				dlgNode.setChildren(dlgNodes);
			}
			root.setChildren(kids);
		}
	}

	@Override
	public void update()
	{
		updateTree();
		viewer.refresh(true);
		viewer.refresh(root, true);
	}
	class OpenListener implements IOpenListener
	{

		@Override
		public void open(OpenEvent event)
		{
			
		}
	
	}
	@Override
	public void push() {
		// TODO Auto-generated method stub
		
	}

}
