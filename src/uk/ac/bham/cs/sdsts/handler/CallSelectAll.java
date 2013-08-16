package uk.ac.bham.cs.sdsts.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import uk.ac.bham.cs.sdsts.SDConsole;
import uk.ac.bham.cs.sdsts.editor.AlloyEditor;
import uk.ac.bham.cs.sdsts.editor.EqEditor;

public class CallSelectAll extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(editor instanceof AlloyEditor){
			AlloyEditor.selectAll();
		}
		return null;
	}


}
