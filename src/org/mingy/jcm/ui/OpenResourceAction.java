package org.mingy.jcm.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.mingy.jcm.Activator;

public class OpenResourceAction extends Action {

	private static final Log logger = LogFactory
			.getLog(OpenResourceAction.class);

	private IWorkbenchWindow window;

	public OpenResourceAction(IWorkbenchWindow window, String text) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_OPEN_RESOURCE);
		setActionDefinitionId(ICommandIds.CMD_OPEN_RESOURCE);
		setImageDescriptor(Activator.getImageDescriptor("/icons/folder.gif"));
	}

	@Override
	public void run() {
		try {
			window.getActivePage().showView(ResourceView.ID, null,
					IWorkbenchPage.VIEW_VISIBLE);
		} catch (PartInitException e) {
			if (logger.isErrorEnabled()) {
				logger.error("error on open resource", e);
			}
			MessageDialog.openError(window.getShell(), "Error",
					"Error opening resource:" + e.getMessage());
		}
	}
}
