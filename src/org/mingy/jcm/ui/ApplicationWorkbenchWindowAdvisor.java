package org.mingy.jcm.ui;

import java.io.PrintStream;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.mingy.jcm.ui.util.PartAdapter;
import org.mingy.kernel.util.Langs;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		Rectangle screenSize = Display.getDefault().getClientArea();
		configurer
				.setInitialSize(new Point(screenSize.width, screenSize.height));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setTitle(Langs.getText("system.title"));
	}

	public void postWindowOpen() {
		initLogger(getWindowConfigurer().getWindow());
	}

	private void initLogger(IWorkbenchWindow workbenchWindow) {
		final PrintStream sysout = System.out;
		final PrintStream syserr = System.err;
		MessageConsole console = new MessageConsole("Console", null);
		MessageConsoleStream stream = console.newMessageStream();
		final PrintStream out = new PrintStream(stream);
		ConsolePlugin.getDefault().getConsoleManager()
				.addConsoles(new IConsole[] { console });
		workbenchWindow.getPartService().addPartListener(new PartAdapter() {
			@Override
			public void partOpened(IWorkbenchPart part) {
				if (part instanceof IConsoleView) {
					System.setOut(out);
					System.setErr(out);
				}
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				if (part instanceof IConsoleView) {
					System.setOut(sysout);
					System.setErr(syserr);
				}
			}
		});
	}
}
