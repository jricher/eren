/**
 * People are edited here.
 */
package org.mitre.eren.editor.views.pages;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.ListDialog;
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
import org.mitre.eren.protocol.scenario.NPC;
import org.mitre.eren.protocol.scenario.Staff;

public class PeoplePage extends FormPage implements ISubscriber
{
	public static final String ID = "org.mitre.eren.editor.views.editors.views.PeoplePage";
	public static final String title = "People";
	private Node root = new Node("");
	private TreeViewer viewer;
	private Model mod;
	
	private Object current;
	
	private Composite stackClient;
	private Section formSection;
	private StackLayout stack;
	
	private Composite staffComp;
	private Combo staffStatusCombo;
//	private Spinner staffAvailabilitySpinner;
	private Spinner staffQuantitySpinner;
	private Combo staffFunctionCombo;
	
	private Composite NPCComp;
	private Text NPCFirstNameText;
	private Text NPCLastNameText;
	private Text NPCOrgNameText;
//	private Combo NPCDialogCombo;
	
	private List <Staff> staff = null;
	private List <NPC> NPCs = null;
	
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
					
					if (val instanceof Staff)
					{
						current = val;
						stack.topControl = staffComp;
						updateStaff();
						formSection.setVisible(true);
						
					}
					else if (val instanceof NPC)
					{
						current = val;
						stack.topControl = NPCComp;	
						updateNPC();
						formSection.setVisible(true);
					}
					formSection.layout();
					formSection.redraw();
				}
			}
			
		}
	};
	
	public PeoplePage(FormEditor editor, Model model)
	{
		super(editor, ID, title);
		mod = model;
		mod.getPub().attach(this);
		updatePeopleList();
	}
	
	protected void createFormContent(IManagedForm managedForm)
	{
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Edit People");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
		
		createTreeSection(form, toolkit, "Existing People");
		createStackSection(form, toolkit);
				
		viewer.addOpenListener(listener);
		
	}
	
	@SuppressWarnings("deprecation")
	private void createStackSection(ScrolledForm form, FormToolkit toolkit)
	{
		formSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION);
		formSection.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		formSection.setToggleColor(toolkit.getColors().getColor(FormColors.SEPARATOR));
		toolkit.createCompositeSeparator(formSection);
		stackClient = toolkit.createComposite(formSection, SWT.WRAP);
		
		createNPCForm(form, toolkit);
		createStaffForm(form, toolkit);
		
		stack = new StackLayout();
		stackClient.setLayout(stack);
		
		toolkit.paintBordersFor(stackClient);
		
		formSection.setText("");
		formSection.setDescription("");
		formSection.setClient(stackClient);
		formSection.setExpanded(true);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		formSection.setLayoutData(gridData);
		
		updateNPC();
		updateStaff();
		
		formSection.setVisible(false);
	}

	private void updateTree()
	{
		
		Node[] staffTmp = new Node[staff.size()];
		Node[] NPCTmp = new Node[NPCs.size()];
		int count = 0;
		for (Staff person : staff)
		{
			Node node = new Node(person);
			staffTmp[count] = node;
			count++;
		}
		count = 0;
		for (NPC person : NPCs)
		{
			Node node = new Node(person);
			NPCTmp[count] = node;
			count++;
		}
		Node staffNode = new Node("Staff");
		Node NPCNode = new Node("NPCs");
		staffNode.setChildren(staffTmp);
		NPCNode.setChildren(NPCTmp);
		Node[] tmp = new Node[2];
		tmp[0] = staffNode;
		tmp[1] = NPCNode;
		root.setChildren(tmp);
	}
	
	/**/
	@SuppressWarnings("deprecation")
	private void createTreeSection(final ScrolledForm form, FormToolkit toolkit, String title)
	{
		Section section = toolkit.createSection(form.getBody(), Section.TWISTIE | Section.DESCRIPTION);
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
		
		updatePeopleTree();
		
		viewer.setInput(root);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		gridData.widthHint = 100;
		
		tree.setLayoutData(gridData);
		toolkit.paintBordersFor(client);
		
		toolkit.createLabel(client, "");
		
		final String[] input = new String[2];
		input[0] = "Staff";
		input[1] = "NPC";
		
		Button addButton = toolkit.createButton(client, "New", SWT.PUSH);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		addButton.setLayoutData(gridData);
		addButton.addSelectionListener(new SelectionListener()
		{			
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ListDialog listDialog = new ListDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell());
				listDialog.setAddCancelButton(true);
				listDialog.setContentProvider(new ArrayContentProvider());
				listDialog.setLabelProvider(new LabelProvider());
				listDialog.setInput(input);
				//listdialog.setInitialSelections(list.toArray());
				listDialog.setTitle("New Person");
				listDialog.open();
				
				if (listDialog.getResult() != null)
				{
					if (listDialog.getResult()[0] == "Staff")
					{
						InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new staff: ", "ID: ", "", new IInputValidator()
						{
							
							@Override
							public String isValid(String newText)
							{
								for (Staff s : staff)
								{
									if (s.getID().equals(newText))
									{
										return "A staff with that ID already exists.";
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
							Staff newStaff = mod.getGameDef().getERENScenario().getPeople().addStaff();
							newStaff.setID(dlg.getValue());
							newStaff.setQuantity("0");
							mod.getPub().updateAll();
							current = newStaff;
							updateStaff();
							stack.topControl = staffComp;
							formSection.setVisible(true);
							formSection.layout();
							formSection.redraw();
						}
						
					}
					if (listDialog.getResult()[0] == "NPC")
					{
						InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new NPC: ", "ID: ", "", new IInputValidator()
						{
							
							@Override
							public String isValid(String newText)
							{
								for (NPC n : NPCs)
								{
									if (n.getID().equals(newText))
									{
										return "A NPC with that ID already exists.";
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
							NPC newNPC = mod.getGameDef().getERENScenario().getPeople().addNPC();
							newNPC.setID(dlg.getValue());
							newNPC.setFirstName("");
							newNPC.setLastName("");
							newNPC.setOrgname("");
							mod.getPub().updateAll();
							current = newNPC;
							updateNPC();
							stack.topControl = NPCComp;
							formSection.setVisible(true);
							formSection.layout();
							formSection.redraw();
						}
					}
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
	
	private void updatePeopleTree()
	{
		if(viewer != null)
		{
			updatePeopleList();
			updateTree();
			viewer.getTree().update();
			viewer.getTree().redraw();
			viewer.refresh();
		}
	}
	
	public void updatePeopleList()
	{
		staff = mod.getGameDef().getERENScenario().getPeople().getStaff();
		NPCs = mod.getGameDef().getERENScenario().getPeople().getNPC();
	}
	
	private void createNPCForm(final ScrolledForm form, FormToolkit toolkit)
	{
		NPCComp = toolkit.createComposite(stackClient, Section.DESCRIPTION);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		NPCComp.setLayout(layout);
		
		toolkit.paintBordersFor(NPCComp);

		toolkit.createLabel(NPCComp, "First Name");
		NPCFirstNameText = toolkit.createText(NPCComp, "");
		NPCFirstNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		
		toolkit.createLabel(NPCComp, "Last Name");
		NPCLastNameText = toolkit.createText(NPCComp, "");
		NPCLastNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(NPCComp, "Orginization Name");
		NPCOrgNameText = toolkit.createText(NPCComp, "");
		NPCOrgNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		/** /
		toolkit.createLabel(NPCComp, "Status");;
		NPCDialogCombo = new Combo(NPCComp, SWT.DROP_DOWN);
		setStatusCombo(NPCDialogCombo);
		toolkit.adapt(NPCDialogCombo);
		NPCDialogCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		/**/
	}
	
	private void setStatusCombo(Combo combo)
	{
		combo.add("Available");
		combo.add("Requisitioned");
		combo.add("Committed");
		combo.add("Ready");
		combo.add("In Use");
		combo.add("Released");
		combo.add("Non-Functional");
	}
	
	private void createStaffForm(final ScrolledForm form, FormToolkit toolkit)
	{
		staffComp = toolkit.createComposite(stackClient, Section.TWISTIE | Section.DESCRIPTION);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		staffComp.setLayout(layout);
		
		toolkit.paintBordersFor(staffComp);
		
		toolkit.createLabel(staffComp, "Status");
		staffStatusCombo = new Combo(staffComp, SWT.DROP_DOWN);
		setStatusCombo(staffStatusCombo);
		GridData staffStatusGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		staffStatusGridData.horizontalSpan = 2;
		staffStatusCombo.setLayoutData(staffStatusGridData);
		
		/** /
		toolkit.createLabel(staffComp, "Availability");
		staffAvailabilitySpinner = new Spinner(staffComp, SWT.WRAP);
		staffAvailabilitySpinner.setMinimum(0);
		staffAvailabilitySpinner.setMaximum(800);
		staffAvailabilitySpinner.setIncrement(1);
		GridData staffAvailabilityGridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		staffAvailabilityGridData.horizontalSpan = 1;
		staffAvailabilitySpinner.setLayoutData(staffAvailabilityGridData);
		
		toolkit.createLabel(staffComp, "");
		/**/
		
		toolkit.createLabel(staffComp, "Quantity");
		staffQuantitySpinner = new Spinner(staffComp, SWT.WRAP);
		staffQuantitySpinner.setMinimum(0);
		staffQuantitySpinner.setMaximum(800);
		staffQuantitySpinner.setIncrement(1);
		GridData staffQuantityGridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		staffQuantityGridData.horizontalSpan = 1;
		staffQuantitySpinner.setLayoutData(staffQuantityGridData);
		
		toolkit.createLabel(staffComp, "");
		
		toolkit.createLabel(staffComp, "Function");
		staffFunctionCombo = new Combo(staffComp, SWT.DROP_DOWN);
		setFunctionCombo(staffFunctionCombo);
		GridData staffFunctionGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		staffFunctionGridData.horizontalSpan = 2;
		staffFunctionCombo.setLayoutData(staffFunctionGridData);
		
	}
	
	private void setFunctionCombo(Combo combo)
	{
		combo.add("Security");
		combo.add("Medical");
		combo.add("Support");
		combo.add("Truck Driver");
		combo.add("Letter Carrier");
	}
	
	@Override
	public void update()
	{
		updatePeopleTree();
	}

	@Override
	public void push()
	{
		if (current instanceof Staff)
		{
			((Staff) current).setQuantity(staffQuantitySpinner.getText());
		//	((Staff) current).setMin(staffAvailabilitySpinner.getText());
		}
		else if (current instanceof NPC)
		{
			((NPC) current).setFirstName(NPCFirstNameText.getText());
			((NPC) current).setLastName(NPCLastNameText.getText());
			((NPC) current).setOrgname(NPCOrgNameText.getText());
		}
	}
	
	/** /
	private Composite staffComp;
	private Combo staffStatusCombo;
	private Spinner staffAvailabilitySpinner;
	private Spinner staffQuantitySpinner;
	private Combo staffFunctionCombo;
	
	
	/**/
	
	private void setStatusComboState(Combo combo, String status)
	{
		if(status != null)
		{
			if (status.equals("AVAILABLE"))
			{
				combo.select(0);
			}
			else if (status.equals("REQUISITIONED"))
			{
				combo.select(1);
			}
			else if (status.equals("COMMITTED"))
			{
				combo.select(2);
			}
			else if (status.equals("READY"))
			{
				combo.select(3);
			}
			else if (status.equals("IN_USE"))
			{
				combo.select(4);
			}
			else if (status.equals("RELEASED"))
			{
				combo.select(5);
			}
			else if (status.equals("NON_FUNCTIONAL"))
			{
				combo.select(6);
			}	
		}
	}
	
	private void setFunctionComboState(Combo combo, String state)
	{
		if (state != null)
		{
			if (state.equals("Security"))
			{
				combo.select(0);
			}
			else if (state.equals("Medical"))
			{
				combo.select(1);
			}
			else if (state.equals("Support"))
			{
				combo.select(2);
			}
			else if (state.equals("Truck Driver"))
			{
				combo.select(3);
			}
			else if (state.equals("LETTER_CARRIER"))
			{
				combo.select(4);
			}
		}
	}
	
	private void updateNPC()
	{
		if (current != null && current instanceof NPC)
		{
			NPCFirstNameText.setText(((NPC) current).getFirstName());
			NPCLastNameText.setText(((NPC) current).getLastName());
			if (((NPC) current).getOrgname() != null)
			{
				NPCOrgNameText.setText(((NPC) current).getOrgname());
			}
			else
			{
				NPCOrgNameText.setText("");
			}
		//	NPCDialogCombo;
		}
	}
	
	private void updateStaff()
	{
		if (current != null && current instanceof Staff)
		{
			setStatusComboState(staffStatusCombo, ((Staff) current).getStatus());
			if (((Staff) current).getQuantity() != null)
			{
				staffQuantitySpinner.setSelection(Integer.parseInt(((Staff) current).getQuantity()));
			}
			else
			{
				staffQuantitySpinner.setSelection(0);
			}
			setFunctionComboState(staffFunctionCombo, ((Staff) current).getFunction());
		//	staffStatusCombo;
		//	staffAvailabilitySpinner;
		//	staffQuantitySpinner;
		//	 setFunctionCombo(staffFunctionCombo);
		}
	}
}