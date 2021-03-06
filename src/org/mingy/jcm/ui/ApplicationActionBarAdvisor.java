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
	private IAction saveAllAction;
	private IAction closeAction;
	private IAction closeAllAction;
	private IAction openResourceAction;
	private IAction openConsoleAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(final IWorkbenchWindow window) {
		{
			aboutAction = ActionFactory.ABOUT.create(window);
			aboutAction.setText("关于(&A)");
			aboutAction.setToolTipText("关于系统");
			register(aboutAction);
		}
		{
			quitAction = ActionFactory.QUIT.create(window);
			quitAction.setText("退出(&X)");
			quitAction.setToolTipText("退出系统");
			register(quitAction);
		}
		{
			saveAction = ActionFactory.SAVE.create(window);
			saveAction.setText("保存(&S)");
			saveAction.setToolTipText("保存当前编辑窗口的修改");
			register(saveAction);
		}
		{
			saveAllAction = ActionFactory.SAVE_ALL.create(window);
			saveAllAction.setText("全部保存(&A)");
			saveAllAction.setToolTipText("保存所有编辑窗口的修改");
			register(saveAllAction);
		}
		{
			closeAction = ActionFactory.CLOSE.create(window);
			closeAction.setText("关闭(&C)");
			closeAction.setToolTipText("关闭当前编辑窗口");
			register(closeAction);
		}
		{
			closeAllAction = ActionFactory.CLOSE_ALL.create(window);
			closeAllAction.setText("全部关闭");
			closeAllAction.setToolTipText("关闭所有编辑窗口");
			register(closeAllAction);
		}
		{
			openResourceAction = new OpenResourceAction(window, "资源浏览(&R)");
			openResourceAction.setText("打开资源浏览视图");
			register(openResourceAction);
		}
		{
			openConsoleAction = new OpenConsoleAction(window, "控制台(&L)");
			openConsoleAction.setText("打开控制台输出日志");
			register(openConsoleAction);
		}
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("文件(&F)",
				IWorkbenchActionConstants.M_FILE);
		MenuManager windowMenu = new MenuManager("窗口(&W)",
				IWorkbenchActionConstants.M_WINDOW);
		MenuManager helpMenu = new MenuManager("帮助(&H)",
				IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		fileMenu.add(closeAction);
		fileMenu.add(closeAllAction);
		fileMenu.add(new Separator());
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
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
		toolbar.add(saveAllAction);
		toolbar.add(new Separator());
		toolbar.add(openResourceAction);
		toolbar.add(new Separator());
		toolbar.add(openConsoleAction);
	}
}
