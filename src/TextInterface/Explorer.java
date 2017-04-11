/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

import Logic.code.Central;
import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Table;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vaviorky
 */
public class Explorer {

    private ActionListBox filelist;
    private List<String[]> list;
    private Central cc;
    private ListListener listener;

    public Explorer(ActionListBox listBox) {
        this.filelist = listBox;
    }

    public ActionListBox getFilelist() {
        return filelist;
    }

    public List<String[]> getList() {
        return list;
    }

    private String rowName(String part1, String part2) {
        StringBuilder temp;
        temp = new StringBuilder();
        temp.append(part1);
        temp.setLength(50);
        temp.setCharAt(48, '|');
        temp.setCharAt(49, ' ');
        part1 = temp.toString();
        if (part2.length() > 16) {
            temp = new StringBuilder();
            temp.append(part2);
            temp.delete(4, 4 + (part2.length() - 16));
            temp.setCharAt(4, ' ');
            temp.setCharAt(3, '.');
            temp.setLength(16);
            part2 = temp.toString();
        }

        return part1 + part2;
    }

    public boolean delete() {
        if (filelist != null) {
            cc.SetCurrentPath(cc.GetCurrentPath() + "\\" + list.get(filelist.getSelectedIndex())[0]);
            cc.Delete();
            return true;
        }
        return false;
    }

    public void copy() {
        if (filelist != null) {
            cc.SetCurrentPath(cc.GetCurrentPath() + "\\" + list.get(filelist.getSelectedIndex())[0]);
            cc.Copy();
            cc.SetCurrentPath(cc.parentDirectory());

        }
    }

    public void cut() {
        if (filelist != null) {
            cc.SetCurrentPath(cc.GetCurrentPath() + "\\" + list.get(filelist.getSelectedIndex())[0]);
            cc.Cut();
            cc.SetCurrentPath(cc.parentDirectory());

        }
    }

    public boolean newDir(String path, String name) {

        List<String[]> namelist = cc.GetFolderContent();
        for (String[] x : namelist) {
            if (x[0].equals(name)) {
                return false;
            }
        }

        cc.newDir(path + "\\" + name);
        return true;
    }

    public boolean rename(String name) {
        if (filelist != null) {
            List<String[]> namelist = cc.GetFolderContent();
            for (String[] x : namelist) {
                if (x[0].equals(name)) {
                    return false;
                }
            }
            cc.SetCurrentPath(cc.GetCurrentPath() + "\\" + namelist.get(filelist.getSelectedIndex())[0]);
            cc.rename(cc.parentDirectory() + "\\" + name);
            cc.SetCurrentPath(cc.parentDirectory());
        }
        return true;
    }

    public Table fileExplorer(Central c, Panel panel, GUIScreen GUI) {
        Table table = new Table(1);
        cc = c;
        filelist.clearItems();
        Component[] row = new Component[2];
        StringBuilder header = new StringBuilder();
        header.append("Nazwa");
        header.setLength(50);
        header.append("Typ");
        row[0] = new Label(header.toString());
        table.addRow(row);
        list = c.GetFolderContent();
        listener = new ListListener();
        listener.setC(c);
        filelist.addComponentListener(listener);
        for (String[] x : list) {
            filelist.addAction(rowName(x[0], x[1]), new Action() {
                @Override
                public void doAction() {
                    if (!x[0].equals("...")) {
                        c.SetCurrentPath(c.GetCurrentPath() + "\\" + x[0]);
                    }
                    if (x[0].equals("...")) {
                        c.SetCurrentPath(c.parentDirectory());
                        panel.removeAllComponents();
                        panel.addComponent(fileExplorer(c, panel, GUI));
                        panel.setTitle(c.GetCurrentPath());

                    } else if (c.typeOf()) {
                        panel.removeAllComponents();
                        panel.setTitle(c.GetCurrentPath());
                        panel.addComponent(fileExplorer(c, panel, GUI));
                    } else if (!c.typeOf()) {
                        if (c.RunFile()) {
                            new DialogPopUp("Nie mozna otworzyć pliku", "Błąd", GUI);
                        }
                        c.SetCurrentPath(c.parentDirectory());

                    }
                }

                @Override
                public String toString() {
                    return x[0];
                }

            });
        }
        row[0] = filelist;
        table.addRow(row);
        return table;
    }

    public Table showFinded(Central c, Panel panel, GUIScreen GUI, String nazwa) {
        List<String[]> temp = new ArrayList<>();
        File dir = new File(c.GetCurrentPath());
        List<String[]> lista = c.Search(nazwa, dir, temp);

        String[] fileProperties = new String[2];
        fileProperties[0] = "...";
        fileProperties[1] = "...";

        lista.add(fileProperties);
        Table table = new Table(1);
        filelist.clearItems();
        Component[] row = new Component[2];
        StringBuilder header = new StringBuilder();
        header.append("Nazwa");
        header.setLength(50);
        row[0] = new Label(header.toString());
        table.addRow(row);
        panel.setTitle("Wyszukiwanie");
        for (String[] x : lista) {
            filelist.addAction((x[1]), new Action() {
                @Override
                public void doAction() {
                    if (!x[0].equals("...")) {
                        c.SetCurrentPath(x[1]);

                    }
                    if (x[0].equals("...")) {
                        //c.SetCurrentPath(c.parentDirectory());
                        panel.removeAllComponents();
                        panel.addComponent(fileExplorer(c, panel, GUI));
                        panel.setTitle(c.GetCurrentPath());

                    } else if (c.typeOf()) {
                        panel.removeAllComponents();
                        panel.setTitle(c.GetCurrentPath());
                        panel.addComponent(fileExplorer(c, panel, GUI));
                    } else if (!c.typeOf()) {
                        if (c.RunFile()) {
                            new DialogPopUp("Nie mozna otworzyć pliku", "Błąd", GUI);
                        }
                        c.SetCurrentPath(c.parentDirectory());

                    }
                }
            });
        }

        row[0] = filelist;
        table.addRow(row);
        return table;
    }
}
