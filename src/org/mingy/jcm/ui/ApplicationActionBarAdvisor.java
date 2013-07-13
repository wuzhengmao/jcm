package org.mingy.jcm.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IAction aboutAction;
	private IAction quitAction;
	private IAction saveAction;
	private IAction closeAction;
	private IAction openResourceAction;
	private IAction openConsoleAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(final IWorkbenchWindow window) {
		{
			aboutAction = ActionFactory.ABOUT.create(window);
			register(aboutAction);
		}
		{
			quitAction = ActionFactory.QUIT.create(window);
			register(quitAction);
		}
		{
			saveAction = ActionFactory.SAVE.create(window);
			register(saveAction);
		}
		{
			closeAction = ActionFactory.CLOSE.create(window);
			register(closeAction);
		}
		{
			openResourceAction = new OpenResourceAction(window,
					"打开资源浏览(&E)");
			openResourceAction.setText("资源浏览(&R)");
			register(openResourceAction);
		}
		{
			openConsoleAction = new OpenConsoleAction(window, "Open Conso&le");
			openConsoleAction.setText("控制台(&L)");
			register(openConsoleAction);
		}
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		MenuManager windowMenu = new MenuManager("&Window",
				IWorkbenchActionConstants.M_WINDOW);
		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		fileMenu.add(saveAction);
		fileMenu.add(closeAction);
		fileMenu.add(new Separator());
		fileMenu.add(quitAction);
		menuBar.add(windowMenu);
		windowMenu.add(openResourceAction);
		windowMenu.add(new Separator());
		windowMenu.add(openConsoleAction);
		menuBar.add(helpMenu);
		helpMenu.add(aboutAction);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
		toolbar.add(saveAction);
		toolbar.add(new Separator());
		toolbar.add(openResourceAction);
		toolbar.add(new Separator());
		toolbar.add(openConsoleAction);
	}
}
