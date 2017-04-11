/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import Logic.code.Central;
import javax.swing.SwingWorker;

/**
 *
 * @author Vaviorky
 */
public class ProgressFile extends SwingWorker<Void, Void> {

    private String name;
    private Central c;
    private DragandDrop dd;

    public ProgressFile(String name, Central c, DragandDrop dd) {
        this.name = name;
        this.c = c;
        this.dd = dd;
    }

    @Override
    protected Void doInBackground() throws Exception {
        c.Paste(name);
        return null;
    }

    @Override
    protected void done() {
        System.out.println("KONIEC");
        dd.showContent();
    }

}
