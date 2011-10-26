package org.mitre.eren.editor.views.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
import org.mitre.eren.editor.views.editors.StaffTreeLabelProvider;
import org.mitre.eren.editor.views.editors.TreeContentProvider;
import org.mitre.eren.editor.views.editors.TreeLabelProvider;
import org.mitre.eren.protocol.scenario.Airport;
import org.mitre.eren.protocol.scenario.EOC;
import org.mitre.eren.protocol.scenario.Facilities;
import org.mitre.eren.protocol.scenario.Hospital;
import org.mitre.eren.protocol.scenario.NPC;
import org.mitre.eren.protocol.scenario.POD;
import org.mitre.eren.protocol.scenario.RSS;
import org.mitre.eren.protocol.scenario.Staff;

public class FacilitiesPage extends FormPage implements ISubscriber
{
	public static final String ID = "org.mitre.eren.editor.editors.pages.FacilitiesPage";
	public static final String title = "Facilities";
	private Facilities facilities;
	private Node root = new Node("");
	private TreeViewer viewer;
	
	private Model mod;
	
	private Object current;
	
	private Composite stackClient;
	private Section formSection;
	private StackLayout stack;
	
	private Composite EOCComp;
	private Text EOCIDText;
	private Text EOCNameText;
	private Combo EOCStatusCombo;
	
	private Composite PODComp;
	private Text PODIDText;
	private Text PODNameText;
	private Combo PODStatusCombo;
	private TreeViewer PODStaffTreeViewer;
	private Node PODStaffTreeRoot = new Node("");
	private Text PODStaffText;
	private Combo PODOwnerCombo;
	private Browser PODBrowser;
	private Text PODLat;
	private Text PODLng;
	
	private Composite AirportComp;
	private Text AirportIDText;
	private Text AirportNameText;
	private Combo AirportStatusCombo;
	
	private Composite RSSComp;
	private Text RSSIDText;
	private Text RSSNameText;
	private Combo RSSStatusCombo;
	private TreeViewer RSSStaffTreeViewer;
	private Node RSSStaffTreeRoot = new Node("");
	private Text RSSStaffText;
	
	private Composite HospitalComp;
	private Text HospitalIDText;
	private Text HospitalNameText;
	private Combo HospitalStatusCombo;
	
	private IOpenListener listener = new IOpenListener()
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
					
					if (val instanceof EOC)
					{
						current = val;
						stack.topControl = EOCComp;
						formSection.setText("Edit an EOC");
						formSection.setVisible(true);
						updateEOCForm();
					}
					else if (val instanceof Airport)
					{
						current = val;
						stack.topControl = AirportComp;
						formSection.setText("Edit an Airport");
						formSection.setVisible(true);
						updateAirportForm();
					}
					else if (val instanceof Hospital)
					{
						current = val;
						stack.topControl = HospitalComp;
						formSection.setText("Edit a Hospital");
						formSection.setVisible(true);
						updateHospitalForm();
					}
					else if (val instanceof RSS)
					{
						current = val;
						stack.topControl = RSSComp;
						formSection.setText("Edit a RSS");
						formSection.setVisible(true);
						updateRSSForm();
					}
					else if (val instanceof POD)
					{
						current = val;
						stack.topControl = PODComp;
						formSection.setText("Edit a POD");
						formSection.setVisible(true);
						updatePODForm();
					}
					else if (val instanceof Staff)
					{
						if(current instanceof POD)
						{
							setPODStaffText((Staff) val);
						}
						else if(current instanceof RSS)
						{
							setRSSStaffText((Staff) val);
						}
					}
					formSection.layout();
				}
			}
			
		}
	};
	
	public FacilitiesPage(FormEditor editor, Model model)
	{
		super(editor, ID, title);
		mod = model;
		mod.getPub().attach(this);
		updateFacilities();
	}

	private void updateFacilities()
	{
		facilities = mod.getGameDef().getERENScenario().getFacilities();
	}
	
	protected void createFormContent(IManagedForm managedForm)
	{
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Edit Facilities");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
		
		createTreeSection(form, toolkit, "Existing Facilities");
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
		
		createEOCForm(form, toolkit);
		createPODForm(form, toolkit);
		createRSSForm(form, toolkit);
		createHospitalForm(form, toolkit);
		createAirportForm(form, toolkit);
		
		stack = new StackLayout();
		stackClient.setLayout(stack);
		
		toolkit.paintBordersFor(stackClient);
		
		formSection.setText("");
		formSection.setDescription("");
		formSection.setClient(stackClient);
		formSection.setExpanded(true);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		formSection.setLayoutData(gridData);
		
		formSection.setVisible(false);
		
	}

	private void updateFacilitiesTree()
	{
		updateFacilities();
		updateTree();
		viewer.getTree().update();
		viewer.getTree().redraw();
		viewer.refresh();
	}
	
	private void updateTree()
	{
		
		Node[] tmp = new Node[5];
		int count;
		
		Node[] EOC = new Node[1];
		EOC[0] = new Node(facilities.getEoc());
		
		Node EOCNode = new Node("EOC");
		EOCNode.setChildren(EOC);
		tmp[0] = EOCNode;
		
		Node[] airports = new Node[facilities.getAirports().size()];
		count = 0;
		for (Airport port : facilities.getAirports())
		{
			Node node = new Node(port);
			airports[count] = node;
			count++;
		}
		
		Node airportNode = new Node("Airports");
		airportNode.setChildren(airports);
		tmp[1] = airportNode;
		
		Node[] hospitals = new Node[facilities.getHospitals().size()];
		count = 0;
		for (Hospital hospital : facilities.getHospitals())
		{
			Node node = new Node(hospital);
			hospitals[count] = node;
			count++;
		}
		
		Node hospitalNode = new Node("Hospitals");
		hospitalNode.setChildren(hospitals);
		tmp[2] = hospitalNode;
		
		Node[] RSSes = new Node[facilities.getRSSes().size()];
		count = 0;
		for (RSS rss : facilities.getRSSes())
		{
			Node node = new Node(rss);
			RSSes[count] = node;
			count++;
		}
		
		Node RSSNode = new Node("RSSes");
		RSSNode.setChildren(RSSes);
		tmp[3] = RSSNode;
		
		Node[] PODs = new Node[facilities.getPods().size()];
		count = 0;
		for (POD pod : facilities.getPods())
		{
			Node node = new Node(pod);
			PODs[count] = node;
			count++;
		}
		
		Node PODNode = new Node("PODs");
		PODNode.setChildren(PODs);
		tmp[4] = PODNode;
		
		/** /
		facilities.getEoc();
		facilities.getAirports();
		facilities.getHospitals();
		facilities.getRSSes();
		facilities.getPods();
		/**/

		root.setChildren(tmp);
		
	}
	
	/**/
	@SuppressWarnings("deprecation")
	private void createTreeSection(final ScrolledForm form, FormToolkit toolkit, String title)
	{
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(FormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		client.setLayout(layout);
		
		PatternFilter filter = new PatternFilter();
		FilteredTree tree = new FilteredTree(client, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, filter, true);
		viewer = tree.getViewer();
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new TreeLabelProvider());
		
		updateFacilitiesTree();
		
		viewer.setInput(root);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 375;
		gridData.widthHint = 100;
		
		tree.setLayoutData(gridData);
		toolkit.paintBordersFor(client);
		
		final String[] input = new String[5];
		input[0] = "RSS";
		input[1] = "Hospital";
		input[2] = "Airport";
		input[3] = "POD";
		input[4] = "EOC";
		
		Button addButton = toolkit.createButton(client, "New", SWT.PUSH);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.VERTICAL_ALIGN_END;
		gridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_CENTER;
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
				listDialog.setTitle("New Facility");
				listDialog.open();
				
				if (listDialog.getResult() != null)
				{
					if (listDialog.getResult()[0] == "RSS")
					{
						InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new RSS: ", "ID: ", "", new IInputValidator()
						{
							
							@Override
							public String isValid(String newText)
							{
								for (RSS r : mod.getGameDef().getERENScenario().getFacilities().getRSSes())
								{
									if (r.getID().equals(newText))
									{
										return "A RSS facility with that ID already exists.";
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
							RSS newRSS = mod.getGameDef().getERENScenario().getFacilities().addRss();
							newRSS.setID(dlg.getValue());
							newRSS.setName("");
							mod.getPub().updateAll();
							current = newRSS;
							updateRSSForm();
							updateFacilitiesTree();
							stack.topControl = RSSComp;
							formSection.setText("Edit a RSS");
							formSection.setVisible(true);
							formSection.setVisible(true);
							formSection.layout();
							formSection.redraw();
						}
						
					}
					if (listDialog.getResult()[0] == "POD")
					{
						InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new POD: ", "ID: ", "", new IInputValidator()
						{
							
							@Override
							public String isValid(String newText)
							{
								for (POD r : mod.getGameDef().getERENScenario().getFacilities().getPods())
								{
									if (r.getID().equals(newText))
									{
										return "A POD facility with that ID already exists.";
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
							POD newPOD = mod.getGameDef().getERENScenario().getFacilities().addPod();
							newPOD.setID(dlg.getValue());
							newPOD.setName("");
							mod.getPub().updateAll();
							current = newPOD;
							updatePODForm();
							updateFacilitiesTree();
							stack.topControl = PODComp;
							formSection.setText("Edit a POD");
							formSection.setVisible(true);
							formSection.setVisible(true);
							formSection.layout();
							formSection.redraw();
						}
						
					}
					if (listDialog.getResult()[0] == "Airport")
					{
						InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new airport: ", "ID: ", "", new IInputValidator()
						{
							
							@Override
							public String isValid(String newText)
							{
								for (Airport r : mod.getGameDef().getERENScenario().getFacilities().getAirports())
								{
									if (r.getID().equals(newText))
									{
										return "An airport facility with that ID already exists.";
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
							Airport newAirport = mod.getGameDef().getERENScenario().getFacilities().addAirport();
							newAirport.setID(dlg.getValue());
							newAirport.setName("");
							mod.getPub().updateAll();
							current = newAirport;
							updateAirportForm();
							updateFacilitiesTree();
							stack.topControl = AirportComp;
							formSection.setText("Edit an airport");
							formSection.setVisible(true);
							formSection.setVisible(true);
							formSection.layout();
							formSection.redraw();
						}
						
					}
					if (listDialog.getResult()[0] == "Hospital")
					{
						InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new Hospital: ", "ID: ", "", new IInputValidator()
						{
							
							@Override
							public String isValid(String newText)
							{
								for (Hospital r : mod.getGameDef().getERENScenario().getFacilities().getHospitals())
								{
									if (r.getID().equals(newText))
									{
										return "A Hospital facility with that ID already exists.";
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
							Hospital newHospital = mod.getGameDef().getERENScenario().getFacilities().addHospital();
							newHospital.setID(dlg.getValue());
							newHospital.setName("");
							mod.getPub().updateAll();
							current = newHospital;
							updateHospitalForm();
							updateFacilitiesTree();
							stack.topControl = HospitalComp;
							formSection.setText("Edit a Hospital");
							formSection.setVisible(true);
							formSection.setVisible(true);
							formSection.layout();
							formSection.redraw();
						}						
					}
					if (listDialog.getResult()[0] == "EOC")
					{
						InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), "Enter an unique ID for new EOC: ", "ID: ", "", new IInputValidator()
						{
							
							@Override
							public String isValid(String newText)
							{
									if ("".equals(newText))
									{
										return "ID cannot be empty.";
									}
								return null;
							}
						});
						if (dlg.open() == Window.OK)
			         	{
							EOC newEOC = mod.getGameDef().getERENScenario().getFacilities().addEoc();
							newEOC.setID(dlg.getValue());
							newEOC.setName("");
							mod.getPub().updateAll();
							current = newEOC;
							updateEOCForm();
							updateFacilitiesTree();
							stack.topControl = EOCComp;
							formSection.setText("Edit an EOC");
							formSection.setVisible(true);
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
		
		gridData = new GridData();
		gridData.widthHint = 200;
		section.setLayoutData(gridData);
	}
	
	private void createEOCForm(final ScrolledForm form, FormToolkit toolkit)
	{
		EOCComp = toolkit.createComposite(stackClient, Section.DESCRIPTION);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		EOCComp.setLayout(layout);

		toolkit.createLabel(EOCComp, "ID");
		EOCIDText = toolkit.createText(EOCComp, "");
		EOCIDText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.paintBordersFor(EOCComp);
		toolkit.createLabel(EOCComp, "Name");
		EOCNameText = toolkit.createText(EOCComp, "");
		EOCNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(EOCComp, "Status");;
		EOCStatusCombo = new Combo(EOCComp, SWT.DROP_DOWN);
		setStatusCombo(EOCStatusCombo);
		toolkit.adapt(EOCStatusCombo);
		EOCStatusCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	}
	
	private void createAirportForm(final ScrolledForm form, FormToolkit toolkit)
	{
		AirportComp = toolkit.createComposite(stackClient, Section.DESCRIPTION);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		AirportComp.setLayout(layout);
		
		toolkit.paintBordersFor(AirportComp);
		
		toolkit.createLabel(AirportComp, "ID");
		AirportIDText = toolkit.createText(AirportComp, "");
		AirportIDText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(AirportComp, "Name");;
		AirportNameText = toolkit.createText(AirportComp, "");
		AirportNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(AirportComp, "Status");;
		AirportStatusCombo = new Combo(AirportComp, SWT.DROP_DOWN);
		setStatusCombo(AirportStatusCombo);
		AirportStatusCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	}
	
	private void createHospitalForm(final ScrolledForm form, FormToolkit toolkit)
	{
		HospitalComp = toolkit.createComposite(stackClient, Section.DESCRIPTION);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		HospitalComp.setLayout(layout);
		
		toolkit.paintBordersFor(HospitalComp);
		
		toolkit.createLabel(HospitalComp, "ID");
		HospitalIDText = toolkit.createText(HospitalComp, "");
		HospitalIDText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(HospitalComp, "Name");;
		HospitalNameText = toolkit.createText(HospitalComp, "");
		HospitalNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(HospitalComp, "Status");;
		HospitalStatusCombo = new Combo(HospitalComp, SWT.DROP_DOWN);
		setStatusCombo(HospitalStatusCombo);
		HospitalStatusCombo.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
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
	
	private void setOwnerComboState(Combo combo)
	{

		for (int i = 0; i < mod.getGameDef().getERENScenario().getPeople().getNPC().size(); i++)
		{
			if (mod.getGameDef().getERENScenario().getPeople().getNPC().get(i).getID().toString().equals(combo.getItem(i)))
			{
				combo.select(i);
			}
		}
	}
	
	private void setOwnerCombo(Combo combo)
	{
		/**/
		if ((mod.getGameDef().getERENScenario().getPeople().getNPC() != null) && (combo != null))
		{
			String[] tmp = new String[mod.getGameDef().getERENScenario().getPeople().getNPC().size()];
			
			for (int i = 0; i < mod.getGameDef().getERENScenario().getPeople().getNPC().size(); i++)
			{
				tmp[i] = mod.getGameDef().getERENScenario().getPeople().getNPC().get(i).getID().toString();
			}
			combo.setItems(tmp);
		}
		/**/
		/** /
		if ((mod.getGameDef().getERENScenario().getPeople().getNPC() != null) && (combo != null))
		{
			for (NPC npc : mod.getGameDef().getERENScenario().getPeople().getNPC())
			{
				if (npc.getID().toString() != null)
				{
					combo.add(npc.getID().toString());
				}
			}
		}
		/**/
		//setOwnerComboState(combo);
	}
	
	private void createPODForm(final ScrolledForm form, FormToolkit toolkit)
	{
		PODComp = toolkit.createComposite(stackClient, Section.DESCRIPTION);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		PODComp.setLayout(layout);
		
		toolkit.paintBordersFor(PODComp);
		/** /
		toolkit.createLabel(PODComp, "ID");
		PODIDText = toolkit.createText(PODComp, "");
		GridData PODIDGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		PODIDGridData.horizontalSpan = 2;
		PODIDText.setLayoutData(PODIDGridData);
		/**/
		
		toolkit.createLabel(PODComp, "Name");
		PODNameText = toolkit.createText(PODComp, "");
		GridData PODNameGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		PODNameGridData.horizontalSpan = 2;
		PODNameText.setLayoutData(PODNameGridData);
		PODNameText.setText(current != null ? ((POD) current).getName() : "");
		
		toolkit.createLabel(PODComp, "Status");;
		PODStatusCombo = new Combo(PODComp, SWT.DROP_DOWN);
		setStatusCombo(PODStatusCombo);
		GridData PODStatusGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		PODStatusGridData.horizontalSpan = 2;
		PODStatusCombo.setLayoutData(PODStatusGridData);
		
		Label emptyCell = toolkit.createLabel(PODComp, "");
		
		toolkit.createLabel(PODComp, "Staff");
		
	//	PODStaffTree = new FilteredTree(PODComp, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER, filter, true);
	//	PODStaffTree.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		
		PODStaffTreeViewer = new TreeViewer(PODComp);
		PODStaffTreeViewer.setContentProvider(new TreeContentProvider());
		PODStaffTreeViewer.setLabelProvider(new StaffTreeLabelProvider());
		GridData PODStaffTreeViewerGridData = new GridData();
		PODStaffTreeViewerGridData.heightHint = 45;
		PODStaffTreeViewer.getTree().setLayoutData(PODStaffTreeViewerGridData);
		updatePODForm();
		PODStaffTreeViewer.setInput(PODStaffTreeRoot);
		
		PODStaffText = new Text(PODComp, SWT.READ_ONLY | SWT.MULTI);
		PODStaffText.setText("");
		PODStaffText.setBackground(new Color(Display.getCurrent(), 225, 225, 225));
		GridData PODStaffTextGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		PODStaffTextGridData.heightHint = 60;
		PODStaffText.setLayoutData(PODStaffTextGridData);
		
		Label ownerLabel = toolkit.createLabel(PODComp, "Owner: ");
		
		PODOwnerCombo = new Combo(PODComp, SWT.DROP_DOWN);
		setOwnerCombo(PODOwnerCombo);
		GridData PODOwnerGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		PODStatusGridData.horizontalSpan = 1;
		PODOwnerCombo.setLayoutData(PODOwnerGridData);
		
		Label otherEmptyCell = toolkit.createLabel(PODComp, "");
		
		Section PODLocSec = toolkit.createSection(PODComp, Section.TWISTIE | Section.DESCRIPTION);
		Composite PODLocComp = toolkit.createComposite(PODLocSec, Section.TWISTIE | Section.DESCRIPTION);
		
		/** /
		PODStatusCombo = new Combo(PODComp, SWT.DROP_DOWN);
		setStatusCombo(PODStatusCombo);
		GridData PODStatusGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		PODStatusGridData.horizontalSpan = 2;
		PODStatusCombo.setLayoutData(PODStatusGridData);
		/**/
		
		layout = new GridLayout();
		layout.numColumns = 2;
		PODLocComp.setLayout(layout);
		
		toolkit.paintBordersFor(PODLocComp);
		
		PODBrowser = new Browser(PODLocComp, SWT.NONE);
		GridData mapGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		mapGridData.horizontalSpan = 2;
		mapGridData.verticalSpan = 1;
	    PODBrowser.setLayoutData(mapGridData);
		
	    PODBrowser.addControlListener(new ControlListener()
        {
            public void controlResized(ControlEvent e)
            {
            	PODBrowser.execute("document.getElementById('map_canvas').style.width= " + (PODBrowser.getSize().x -30) + ";");
            	PODBrowser.execute("document.getElementById('map_canvas').style.height= " + (PODBrowser.getSize().y -30) + ";");
            }
 
            public void controlMoved(ControlEvent e)
            {
            }
        });
	    
	    String path = "";
	    try
	    {
		    URL installURL = Platform.getConfigurationLocation().getURL();
		    String protocol = FileLocator.toFileURL(installURL).getProtocol();
		    path = FileLocator.toFileURL(installURL).getPath()
		    + "MapsPage.html";
		    URL url = new URL(protocol + ":" + path);
		
		    if (url != null)
		    {
		    InputStream inputStream = url.openStream();
			    if (inputStream != null)
			    {
				    BufferedReader br = new BufferedReader(
				    new InputStreamReader(inputStream));
				    if (br != null)
				    {
				    	br.close();
				    }
			    }
		    }
        }
	    catch (Exception e)
        {
	    	e.printStackTrace();
	    	
        }
	    
	    File file = new File(path);
	    file = new File("C:\\Documents and Settings\\icheung\\workspace\\ERENEditor\\Exports\\ERENEditor\\ERENEditor\\configuration\\MapsPage.html");
	    PODBrowser.setUrl(file.toURI().toString());
	    
	    GridData latGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false);
	    Label latLabel = toolkit.createLabel(PODLocComp, "Lat");
	    latLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
	    PODLat = toolkit.createText(PODLocComp, "", SWT.SINGLE);
	    PODLat.setLayoutData(latGridData);
	    
	    GridData lngGridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false);
	    Label lngLabel = toolkit.createLabel(PODLocComp, "Lng");
	    lngLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false));
	    PODLng = toolkit.createText(PODLocComp, "", SWT.SINGLE);
	    PODLng.setLayoutData(lngGridData);
	    
	    new AddLocFunction (PODBrowser, "addLoc");
	    
	    PODLocSec.setText("Edit Location");
	//	PODLocSec.setDescription("Edit location of currently selected POD.");
	    PODLocSec.setClient(PODLocComp);
	    PODLocSec.setExpanded(false);
		
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
	//	gridData.widthHint = 200;
		PODLocSec.setLayoutData(gridData);
		
		updatePODForm();
		PODStaffTreeViewer.addOpenListener(listener);
	}
	
	private void setStatusComboState(Combo combo, String status)
	{
		if (status != null)
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
	
	private void setPODStaffText(Staff staff)
	{
		if (staff == null)
		{
			PODStaffText.setText("");
		}
		else
		{
			PODStaffText.setText("Function: " + staff.getFunction() + "\n" + "Min: " + staff.getMin() + "\n" + "Target: " + staff.getTarget() + "\n" + "Current: " + staff.getCurrent() + "\n");
		}
		
	}
	
	private void setRSSStaffText(Staff staff)
	{
		if (staff == null)
		{
			RSSStaffText.setText("");
		}
		else
		{
			RSSStaffText.setText("Function: " + staff.getFunction() + "\n" + "Min: " + staff.getMin() + "\n" + "Target: " + staff.getTarget() + "\n" + "Current: " + staff.getCurrent() + "\n");
		}
		
	}
	
	private void updatePODForm()
	{
		Node[] rootKids;
		if ((current != null) && (((POD) current).getStaff().size() > 0))
		{
			PODLat.setText(((POD) current).getLocation().getKMLLocation().getLatitude().toString());
			PODLng.setText(((POD) current).getLocation().getKMLLocation().getLongitude().toString());
			PODNameText.setText(((POD) current).getName());
			setStatusComboState(PODStatusCombo, ((POD) current).getStatus());
			List <Staff> staffList = ((POD) current).getStaff();
			PODStaffTreeRoot = new Node("Staff");
			rootKids = new Node[staffList.size()];
			for (int i = 0; i < staffList.size(); i++)
			{
				rootKids[i] =  new Node(staffList.get(i));	
			}
		}
		else
		{
			rootKids = new Node[1];
			rootKids[0] = new Node("No staff assigned yet.");
		}
		PODStaffTreeRoot.setChildren(rootKids);
		PODStaffTreeViewer.setInput(PODStaffTreeRoot);
		PODStaffTreeViewer.refresh(PODStaffTreeRoot, true);
		if ((PODBrowser != null) && (current instanceof POD) && (PODLat.getText() != "") && (PODLng.getText() != ""))
		{
			PODBrowser.evaluate("addMarker(new google.maps.LatLng(" + Double.parseDouble(PODLat.getText()) + ", " + Double.parseDouble(PODLng.getText()) + "));");
			
			PODBrowser.execute("document.getElementById('map_canvas').style.width= "+ (PODBrowser.getSize().x -30) + ";");
	        PODBrowser.execute("document.getElementById('map_canvas').style.height= "+ (PODBrowser.getSize().y -30) + ";");
	        PODBrowser.evaluate("map.setCenter(new google.maps.LatLng(" + Double.parseDouble(PODLat.getText()) + ", " + Double.parseDouble(PODLng.getText()) + "));");
	    //    PODBrowser.evaluate("map.setZoom(8);");
		}
		setOwnerCombo(PODOwnerCombo);
	}
	
	private void updateRSSForm()
	{
		Node[] rootKids;
		if ((current != null) && (((RSS) current).getStaff().size() > 0))
		{
			RSSNameText.setText(((RSS) current).getName());
			setStatusComboState(RSSStatusCombo, ((RSS) current).getStatus());
			List <Staff> staffList = ((RSS) current).getStaff();
			RSSStaffTreeRoot = new Node("Staff");
			rootKids = new Node[staffList.size()];
			for (int i = 0; i < staffList.size(); i++)
			{
				rootKids[i] =  new Node(staffList.get(i));	
			}
		}
		else
		{
			rootKids = new Node[1];
			rootKids[0] = new Node("No staff assigned yet.");
		}
		RSSStaffTreeRoot.setChildren(rootKids);
		RSSStaffTreeViewer.setInput(RSSStaffTreeRoot);
		RSSStaffTreeViewer.refresh(RSSStaffTreeRoot, true);
	}
	
	private void updateHospitalForm()
	{
		if (current != null)
		{
			HospitalIDText.setText(((Hospital) current).getID());
			HospitalNameText.setText(((Hospital) current).getName());
			setStatusComboState(HospitalStatusCombo, ((Hospital) current).getStatus());
		}
	}
	
	private void updateAirportForm()
	{
		if (current != null)
		{
			AirportIDText.setText(((Airport) current).getID());
			AirportNameText.setText(((Airport) current).getName());
			setStatusComboState(AirportStatusCombo, ((Airport) current).getStatus());
		}
	}
	
	private void updateEOCForm()
	{
		if (current != null)
		{
			EOCIDText.setText(((EOC) current).getID());
			EOCNameText.setText(((EOC) current).getName());
			setStatusComboState(EOCStatusCombo, ((EOC) current).getStatus());
		}
	}
	
	private void createRSSForm(final ScrolledForm form, FormToolkit toolkit)
	{
		RSSComp = toolkit.createComposite(stackClient, Section.DESCRIPTION);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		RSSComp.setLayout(layout);
		
		toolkit.paintBordersFor(RSSComp);
		/** /
		toolkit.createLabel(RSSComp, "ID");
		RSSIDText = toolkit.createText(RSSComp, "");
		GridData RSSIDGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		RSSIDGridData.horizontalSpan = 2;
		RSSIDText.setLayoutData(RSSIDGridData);
		/**/
		
		toolkit.createLabel(RSSComp, "Name");
		RSSNameText = toolkit.createText(RSSComp, "");
		GridData RSSNameGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		RSSNameGridData.horizontalSpan = 2;
		RSSNameText.setLayoutData(RSSNameGridData);
		RSSNameText.setText(current != null ? ((RSS) current).getName() : "");
		
		toolkit.createLabel(RSSComp, "Status");;
		RSSStatusCombo = new Combo(RSSComp, SWT.DROP_DOWN);
		setStatusCombo(RSSStatusCombo);
		GridData RSSStatusGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		RSSStatusGridData.horizontalSpan = 2;
		RSSStatusCombo.setLayoutData(RSSStatusGridData);
		
		
		
		toolkit.createLabel(RSSComp, "Staff");
		
	//	RSSStaffTree = new FilteredTree(RSSComp, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER, filter, true);
	//	RSSStaffTree.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		
		RSSStaffTreeViewer = new TreeViewer(RSSComp);
		RSSStaffTreeViewer.setContentProvider(new TreeContentProvider());
		RSSStaffTreeViewer.setLabelProvider(new StaffTreeLabelProvider());
		GridData RSSStaffTreeViewerGridData = new GridData();
		RSSStaffTreeViewerGridData.heightHint = 45;
		RSSStaffTreeViewer.getTree().setLayoutData(RSSStaffTreeViewerGridData);
		updateRSSForm();
		RSSStaffTreeViewer.setInput(RSSStaffTreeRoot);
		
		RSSStaffText = new Text(RSSComp, SWT.READ_ONLY | SWT.MULTI);
		RSSStaffText.setText("");
		RSSStaffText.setBackground(new Color(Display.getCurrent(), 225, 225, 225));
		GridData RSSStaffTextGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		RSSStaffTextGridData.heightHint = 60;
		RSSStaffText.setLayoutData(RSSStaffTextGridData);
		updateRSSForm();
		RSSStaffTreeViewer.addOpenListener(listener);
	}
	
	@Override
	public void update()
	{
		updateFacilities();
		updatePODForm();
		updateAirportForm();
		updateEOCForm();
		updateHospitalForm();
		updateRSSForm();
	}

	@Override
	public void push()
	{
		if (current instanceof EOC)
		{
			EOC val = (EOC) current;
			val.setID(EOCIDText.getText());
			val.setName(EOCNameText.getText());
		}
		else if (current instanceof Airport)
		{
			Airport val = (Airport) current;
			val.setID(AirportIDText.getText());
			val.setName(AirportNameText.getText());
		}
		else if (current instanceof Hospital)
		{
			Hospital val = (Hospital) current;
			val.setID(HospitalIDText.getText());
			val.setName(HospitalNameText.getText());
		}
		else if (current instanceof RSS)
		{
			RSS val = (RSS) current;
			val.setID(RSSIDText.getText());
			val.setName(RSSNameText.getText());
		}
		else if (current instanceof POD)
		{
			POD val = (POD) current;
			val.setID(PODIDText.getText());
			val.setName(PODNameText.getText());
			val.getLocation().getKMLLocation().setLatitude(PODLat.getText());
			val.getLocation().getKMLLocation().setLongitude(PODLng.getText());
			val.setOwner(PODOwnerCombo.getText());
		}
	}
	
	 class AddLocFunction extends BrowserFunction
     {
		 AddLocFunction (Browser browser, String name)
        {
            super (browser, name);
        }
        public Object function (Object[] arguments)
        {
            double lat = ((Double) arguments[0]).doubleValue();
            double lng = ((Double) arguments[1]).doubleValue();
            PODLat.setText(Double.toString(lat));
            PODLng.setText(Double.toString(lng));
            
            this.getBrowser().execute("document.getElementById('map_canvas').style.width= "+ (this.getBrowser().getSize().x -30) + ";");
            this.getBrowser().execute("document.getElementById('map_canvas').style.height= "+ (this.getBrowser().getSize().y -30) + ";");
            return null;
        }
     }
}