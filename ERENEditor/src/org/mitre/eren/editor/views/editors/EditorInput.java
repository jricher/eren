/**
 * This extends a normal IEditorInput in that it allows Strings as input
 * and associates editors with models.
 */
package org.mitre.eren.editor.views.editors;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.mitre.eren.editor.model.ActNode;
import org.mitre.eren.editor.model.DlgNode;
import org.mitre.eren.editor.model.Model;

@SuppressWarnings("deprecation")
public class EditorInput implements IStorageEditorInput
{
    private String inputString;
    private String inputName;
    private Model mod;
    private DlgNode dlg;
    private ActNode act;
    
    public EditorInput(String inputName, Model mod, String inputString)
    {
        this.inputString = inputString;
        this.inputName = inputName;
        this.mod = mod;
    }
    
    public EditorInput(String inputName, Model mod)
    {
        this.inputString = "";
        this.inputName = inputName;
        this.mod = mod;
    }
    
    public EditorInput(String inputName, DlgNode dlg)
    {
        this.inputString = "";
        this.inputName = inputName;
        this.dlg = dlg;
        this.mod = dlg.getMod();
    }
    public EditorInput(String inputName, ActNode act)
    {
        this.inputString = "";
        this.inputName = inputName;
        this.act = act;
        this.mod = act.getMod();
    }

    public DlgNode getDlg()
    {
    	return this.dlg;
    }
    public boolean exists() {

        return false;

    }

    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }



    public IPersistableElement getPersistable()
    {
        return null;
    }



    @SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {

        return null;

    }



    public String getName()
    {
        return inputName;
    }



    public String getToolTipText()
    {
    	return "tool tip";
    }



    public IStorage getStorage() throws CoreException
    {
        return new IStorage()
        {
			public InputStream getContents() throws CoreException
			{

                return new StringBufferInputStream(inputString);

            }



            public IPath getFullPath() {

                return null;

            }



            public String getName()
            {

                return EditorInput.this.getName();

            }



            public boolean isReadOnly()
            {

                return false;

            }



            @SuppressWarnings("rawtypes")
			public Object getAdapter(Class adapter)
            {

                return null;

            }



        };

    }

	public Model getMod()
	{
		return mod;
	}

	/** /
	public void setMod(Model mod) {
		this.mod = mod;
	}
	/**/


}
