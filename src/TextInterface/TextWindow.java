/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

import Logic.code.Central;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.*;
import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Table;
import com.googlecode.lanterna.gui.layout.HorisontalLayout;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import javax.swing.JFrame;

public class TextWindow {

//<editor-fold defaultstate="collapsed" desc="pola + get/set">
    private SwingTerminal t;
    final GUIScreen guiScreen;
    final Window window;
    private Central cLeft, cRight;
    private Panel panelLeft, panelRight;
    private final Keyboard_listener listener;
    private Table leftTable, rightTable;
    private Explorer leftExplorer, rightExplorer;
    private ActionListBox leftListBox, rightListBox;


    public Explorer getLeftExplorer() {
        return leftExplorer;
    }

    public Explorer getRightExplorer() {
        return rightExplorer;
    }

    public Central getcLeft() {
        return cLeft;
    }

    public Central getcRight() {
        return cRight;
    }

    public Panel getPanelLeft() {
        return panelLeft;
    }

    public Panel getPanelRight() {
        return panelRight;
    }

    public Keyboard_listener getListener() {
        return listener;
    }

//</editor-fold>
    public TextWindow() {
        this.window = new Window("Menager plikow");
        leftListBox = new ActionListBox();
        rightListBox = new ActionListBox();
        this.leftExplorer = new Explorer(leftListBox);
        this.rightExplorer = new Explorer(rightListBox);
        t = new SwingTerminal(new TerminalSize(150, 35));
        guiScreen = TerminalFacade.createGUIScreen(t);
        cLeft = new Central();
        listener = new Keyboard_listener();
        cRight = new Central();
    }

    public void mainWindow() {

//<editor-fold defaultstate="collapsed" desc="  ustawienie paneli">
        window.setWindowSizeOverride(new TerminalSize(900, 900));
        window.setSoloWindow(true);
        Panel panelHolder = new Panel();
        panelHolder.setLayoutManager(new HorisontalLayout());
        panelHolder.setPreferredSize(new TerminalSize(200, 100));
        panelLeft = new Panel(cLeft.GetCurrentPath());
        panelLeft.setPreferredSize(new TerminalSize(200, 100));
        panelLeft.setLayoutManager(new HorisontalLayout());
        panelRight = new Panel(cRight.GetCurrentPath());
        panelRight.setLayoutManager(new HorisontalLayout());
        panelRight.setPreferredSize(new TerminalSize(200, 100));
//</editor-fold>

        leftTable = leftExplorer.fileExplorer(cLeft, panelLeft, guiScreen);
        rightTable = rightExplorer.fileExplorer(cRight, panelRight, guiScreen);

        panelLeft.addComponent(leftTable);

        panelRight.addComponent(rightTable);
        panelHolder.addComponent(panelLeft);
        panelHolder.addComponent(panelRight);

        Panel menu = new Panel("MENU");
        menu.addComponent(new Menu().menuoptions());
        menu.setPreferredSize(new TerminalSize(300, 3));
        window.addComponent(panelHolder);
        window.addComponent(menu);

        window.addWindowListener(listener);
        guiScreen.getScreen().startScreen();
        t.getJFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiScreen.showWindow(window);

    }
}
