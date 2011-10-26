package org.mitre.eren.editor.views.pages;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.mitre.eren.editor.model.ISubscriber;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.editor.model.Node;
import org.mitre.eren.editor.views.editors.TreeContentProvider;
import org.mitre.eren.editor.views.editors.TreeLabelProvider;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.Role;

public class RolesPage extends FormPage implements ISubscriber
{
	public static final String ID = "org.mitre.eren.editor.views.pages.RolesPage";
	public static final String title = "Roles";
	Model mod;
	
	private Text IDText;
	private Text titleText;
	private Text descriptionText;
	private Text briefingText;
	private List <Role> roles = null;
	private TreeViewer viewer;
	private Section formSection;
	private Node root = new Node("");
	Role currentRole = null;
	
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
					else if (val instanceof Role)
					{
						if (!formSection.getVisible())
						{
							transformForm();
						}
						loadRole((Role) val);
					}
				}
			}
			
		}
	};
	
	private Listener focusLostListener = new Listener()
	{

		@Override
		public void handleEvent(Event event)
		{
			validateForm();
		}
		
	};
	
	public RolesPage(FormEditor editor, Model model)
	{
		super(editor, ID, title);
		mod = model;
		mod.getPub().attach(this);
		updateRoleList();
	}
	
	protected void createFormContent(IManagedForm managedForm)
	{
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Edit Roles");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
		
		createTreeSection(form, toolkit, "Existing Roles");
		createFormSection(form, toolkit, "Edit a Role");
		
		viewer.addOpenListener(listener);
		
		form.addListener(SWT.FocusOut, focusLostListener);
	}
	
	private void updateRoleTree()
	{
		if (viewer != null)
		{
			updateRoleList();
			updateTree();
			viewer.getTree().update();
			viewer.getTree().redraw();
			viewer.refresh();
		}
	}
	
	private void updateTree()
	{
		Node[] tmp = new Node[roles.size()];
		int count = 0;
		for (Role role : roles)
		{
			Node node = new Node(role);
			tmp[count] = node;
			count++;
		}
		root.setChildren(tmp);
	}
	
	@SuppressWarnings("deprecation")
	private void createTreeSection(final ScrolledForm form, FormToolkit toolkit, String title)
	{
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(FormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		client.setLayout(layout);
		
		PatternFilter filter = new PatternFilter();
		FilteredTree tree = new FilteredTree(client, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, filter, true);
		viewer = tree.getViewer();
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new TreeLabelProvider());
		
		updateRoleTree();
		
		viewer.setInput(root);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		gridData.widthHint = 100;
		
		tree.setLayoutData(gridData);
		toolkit.paintBordersFor(client);
		
		toolkit.createLabel(client, "");
		
		Button addButton = toolkit.createButton(client, "New", SWT.PUSH);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		addButton.setLayoutData(gridData);
		addButton.addSelectionListener(new SelectionListener()
		{			
			@Override
			public void widgetSelected(SelectionEvent e)
			{
	
				InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new role: ", "ID: ", "", new IInputValidator()
				{
					
					@Override
					public String isValid(String newText)
					{
						for (Role r : mod.getGameDef().getERENScenario().getRoles().getRoles())
						{
							if (r.getID().equals(newText))
							{
								return "A role with that ID already exists.";
							}
							if ("".equals(newText))
							{
								return "ID cannot be empty.";
							}
						}
						return null;
					}
				});
				if (dlg.open() == Window.OK)
	         	{
					Role newRole = mod.getGameDef().getERENScenario().getRoles().addRole();
					newRole.setID(dlg.getValue());
					newRole.setTitle("");
					newRole.setDescription("");
					newRole.setBriefing("");
					mod.getPub().updateAll();
					loadRole(newRole);
					formSection.setVisible(true);
					formSection.layout();
					formSection.redraw();
					updateRoleTree();
				}
				
			}
		
			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
			}
			});
			
		section.setText(title);
		section.setDescription("");
		section.setClient(client);
		section.setExpanded(true);
		
		gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 200;
		section.setLayoutData(gridData);
	}
	
	@SuppressWarnings("deprecation")
	private void createFormSection(final ScrolledForm form, FormToolkit toolkit, String title)
	{
		formSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION);
		formSection.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		formSection.setToggleColor(toolkit.getColors().getColor(FormColors.SEPARATOR));
		toolkit.createCompositeSeparator(formSection);
		Composite client = toolkit.createComposite(formSection, SWT.WRAP);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		client.setLayout(layout);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		gridData.widthHint = 100;
		
		toolkit.paintBordersFor(client);
		toolkit.createLabel(client, "ID");
		IDText = toolkit.createText(client, "");
		IDText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(client, "Title");;
		titleText = toolkit.createText(client, "");
		titleText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(client, "Description");
		descriptionText = toolkit.createText(client, "", SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData desGridData = new GridData(SWT.HORIZONTAL);
		desGridData.heightHint = 50;
		descriptionText.setLayoutData(desGridData);
		
		toolkit.createLabel(client, "Briefing");
		briefingText = toolkit.createText(client, "", SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData briefGridData = new GridData(SWT.HORIZONTAL);
		briefGridData.heightHint = 200;
		briefingText.setLayoutData(briefGridData);
		
		formSection.setText(title);
		formSection.setDescription(" ");
		formSection.setClient(client);
		formSection.setExpanded(true);
		
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		formSection.setLayoutData(gridData);
		
		transformForm();
	}
	
	private void transformForm()
	{
		if (formSection.getVisible())
		{
			formSection.setVisible(false);
		}
		else
		{
			formSection.setVisible(true);
		}
	}
	
	private boolean validateForm()
	{
		return true;
	}

	@Override
	public void update()
	{
		updateRoleTree();
	}
	
	public void updateRoleList()
	{
		roles = mod.getGameDef().getERENScenario().getRoles().getRoles();
	}
	
	private void loadRole(Role val)
	{
		currentRole = val;
		this.IDText.setText(val.getID().toString());
		this.titleText.setText(val.getTitle().toString());
		this.descriptionText.setText(val.getDescription().toString());
		this.briefingText.setText(val.getBriefing().toString());
	}

	@Override
	public void push()
	{
		currentRole.setID(IDText.getText());
		currentRole.setTitle(titleText.getText());
		currentRole.setDescription(descriptionText.getText());
		currentRole.setBriefing(briefingText.getText());
	}

}
