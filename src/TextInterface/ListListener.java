/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

import Logic.code.Central;
import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.component.InteractableComponent;
import com.googlecode.lanterna.gui.listener.ComponentListener;

/**
 *
 * @author Vaviorky
 */
public class ListListener implements ComponentListener {

    private Central c = new Central();
    private final TextWindow tw = WindowSingleton.getWindow();
    
    public ListListener() {
    }
    public Central getC() {
        return c;
    }

    public void setC(Central c) {
        this.c = c;
    }

    @Override
    public void onComponentInvalidated(Component component) {

    }

    @Override
    public void onComponentReceivedFocus(InteractableComponent interactableComponent) {
        System.out.println("Jestem w " + c.GetCurrentPath());
        tw.getListener().setC(c);
    }

    @Override
    public void onComponentLostFocus(InteractableComponent interactableComponent) {
        System.out.println("Wyszłem z "+c.GetCurrentPath());
    }
}
