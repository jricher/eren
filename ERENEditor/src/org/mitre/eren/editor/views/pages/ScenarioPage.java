package org.mitre.eren.editor.views.pages;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.mitre.eren.editor.model.DlgNode;
import org.mitre.eren.editor.model.ISubscriber;
import org.mitre.eren.editor.model.Model;
import org.mitre.eren.protocol.scenario.ERENscenario;

public class ScenarioPage extends FormPage implements ISubscriber
{
	public static final String ID = "org.mitre.eren.editor.views.pages.ScenarioPage";
	private static final String title = "Scenario";
	private static final String toolTip = "This is the graphical scenario editor interface.";
	private Model mod;
	
	private Text IDText;
	private Text nameText;
	private Text descriptionText;
	private Text baseURLText;
	private DateTime scenarioDate;
	private DateTime scenarioTime;
	
	private FormEditor editor;
	private IManagedForm managedForm;
	
	ERENscenario scenario;
	
	private FocusListener focusListener = new FocusListener()
	{
		@Override
		public void focusGained(FocusEvent e)
		{			
		}

		@Override
		public void focusLost(FocusEvent e)
		{	
			validateForm();
			System.out.println("Hai");
		}
		
	};
	
	public ScenarioPage(FormEditor editor, Model model)
	{
		super(editor, "second", "SecondPage.label");
		mod = model;
		scenario = mod.getGameDef().getERENScenario();
		this.editor = editor;
		mod.getPub().attach(this);
	}
	
	
	protected void createFormContent(IManagedForm managedForm)
	{
		this.managedForm = managedForm;
		
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Scenario File");
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
		
		
		toolkit.createLabel(form.getBody(), "ID");
		IDText = toolkit.createText(form.getBody(), scenario.getID());
		IDText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(form.getBody(), "Name");;
		nameText = toolkit.createText(form.getBody(), scenario.getName());
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(form.getBody(), "Description");
		descriptionText = toolkit.createText(form.getBody(), scenario.getDescription());
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(form.getBody(), "Base URL");
		baseURLText = toolkit.createText(form.getBody(), scenario.getBaseUrl());
		baseURLText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(form.getBody(), "Date");
		scenarioDate = new DateTime(form.getBody(), SWT.DATE);
		toolkit.adapt(scenarioDate);
		scenarioDate.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		toolkit.createLabel(form.getBody(), "Time");
		scenarioTime = new DateTime(form.getBody(), SWT.TIME);
		toolkit.adapt(scenarioTime);
		scenarioTime.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		Button addButton = toolkit.createButton(form.getBody(), "Add Dialog File", SWT.PUSH);
		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		addButton.setLayoutData(gridData);
		addButton.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(), SWT.MULTI);
				String[] filterExtentions = {"*.XML"};
				String[] filterNames = {"EREN Dialog Files (*.XML)"};
				dlg.setFilterNames(filterNames);
		        dlg.setFilterExtensions(filterExtentions);
		        dlg.setText("Open an existing EREN dialog file...");
		        String fileName = dlg.open();
		        
		        if (fileName != null)
				{
		        	File openedFile = new File(fileName);
		        	mod.addDialogue(new DlgNode(openedFile, mod));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				
			}
		});
		
		toolkit.createLabel(form.getBody(), "");
		
		Button newButton = toolkit.createButton(form.getBody(), "New Dialog File", SWT.PUSH);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		newButton.setLayoutData(gridData);
		newButton.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				
			}
		        

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				
			}
		});
	}
	
	private boolean validateForm()
	{
		return true;
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public Image getTitleImage()
	{
		return null;
	}

	@Override
	public String getTitleToolTip()
	{
		return toolTip;
	}

	@Override
	public void setFocus()
	{
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{		
	}

	@Override
	public boolean isDirty()
	{
		return false;
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		return true;
	}

	@Override
	public boolean isSaveOnCloseNeeded()
	{
		return false;
	}

	@Override
	public boolean canLeaveThePage()
	{
		return true;
	}

	@Override
	public FormEditor getEditor()
	{
		return editor;
	}

	@Override
	public String getId()
	{
		return ID;
	}

	@Override
	public IManagedForm getManagedForm()
	{
		return managedForm;
	}


	@Override
	public boolean isEditor()
	{
		return false;
	}

	@Override
	public void update()
	{
		ERENscenario scenario = mod.getGameDef().getERENScenario();
		IDText.setText(scenario.getID());
		nameText.setText(scenario.getName());
		descriptionText.setText(scenario.getDescription());
		baseURLText.setText(scenario.getBaseUrl());
	}


	@Override
	public void push()
	{
		scenario.setID(IDText.getText());
		scenario.setName(nameText.getText());
		scenario.setDescription(descriptionText.getText());
		scenario.setBaseUri(baseURLText.getText());
	}

}
