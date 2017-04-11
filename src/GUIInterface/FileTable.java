/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import sun.awt.shell.ShellFolder;
import static sun.awt.shell.ShellFolder.getShellFolder;

/**
 *
 * @author Vaviorky
 */
public class FileTable extends DefaultTableModel implements Reorderable { //musi byc zmiana na defaulttablemodel

    private final String[] columns = {" ", "Nazwa", "Typ"};
    private List<String[]> list;
    private File f;
    private Icon i;

    public FileTable(List<String[]> list) {
        super();
        this.list = list;
        i = new ImageIcon();
    }

    public List<String[]> getList() {
        return list;
    }

    @Override
    public int getRowCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] s = list.get(rowIndex);
        if (!(s[0].equals("..."))) {

            f = new File(s[2]);
            i = getSystemIconing(f);
        } else {
            f = new File("");
            i = new ImageIcon(getClass().getClassLoader().getResource("resources/images/return.png"));
        }

        switch (columnIndex) {
            case 0:
                return i;
            case 1:
                return s[0];
            default:
                return s[1];
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            default:
                return String.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    //override from Java API, for some reason using filesystemview.getSystemIcon is very slow on disk view, but copy method and using this is fast enough...
    public Icon getSystemIconing(File f) {
        if (f == null) {
            return null;
        }

        ShellFolder sf;

        try {
            sf = getShellFolder(f);
        } catch (FileNotFoundException e) {
            ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/images/return.png"));
            return null;
            //return new ImageIcon(getClass().getClassLoader().getResource("resources/images/return-icon.ico"));

        }

        Image img = sf.getIcon(false);

        if (img != null) {
            return new ImageIcon(img, sf.getFolderType());
        } else {
            return UIManager.getIcon(f.isDirectory() ? "FileView.directoryIcon" : "FileView.fileIcon");
        }
    }

    @Override
    public void reorder(int from, int to) {
        Object o = getDataVector().remove(from);
        getDataVector().add(to, o);
        fireTableDataChanged();
    }

}
