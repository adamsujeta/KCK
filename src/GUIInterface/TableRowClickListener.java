/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import Logic.code.Central;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Vaviorky
 */
public class TableRowClickListener extends MouseAdapter {

    private Central c;
    private FileTable fileTable;
    private List<String[]> list;
    private List<String[]> temp;
    private DragandDrop dd;

    public TableRowClickListener(Central c, FileTable fileTable, List<String[]> list) {
        this.c = c;
        this.fileTable = fileTable;
        this.list = list;
        dd = new DragandDrop();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JTable table = (JTable) e.getSource();

        if (e.getClickCount() == 2) {
            String name = table.getValueAt(table.getSelectedRow(), 1).toString();
            if (!name.equals("...")) {
                c.SetCurrentPath(c.GetCurrentPath() + "\\" + name);
            }
            if (name.equals("...")) {
                c.SetCurrentPath(c.parentDirectory());
            } else if (c.typeOf()) {
                c.SetCurrentPath(c.GetCurrentPath());
            } else if (!c.typeOf()) {
                if (c.RunFile()) {
                    JOptionPane.showMessageDialog(null, "Nie można otworzyć tego pliku", "Błąd", 0);
                }
                c.SetCurrentPath(c.parentDirectory());
            }
            showContent();

            JViewport jViewport = (JViewport) table.getParent();
            JScrollPane jScrollPane = (JScrollPane) jViewport.getParent();

            if (c.GetCurrentPath().equals("")) {
                jScrollPane.setBorder(BorderFactory.createTitledBorder("Mój komputer"));

            } else {
                jScrollPane.setBorder(BorderFactory.createTitledBorder(c.GetCurrentPath()));
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int r = table.rowAtPoint(e.getPoint());
        if (r >= 0 && r < table.getRowCount()) {
            table.setRowSelectionInterval(r, r);
        } else {
            table.clearSelection();
        }

        int rowindex = table.getSelectedRow();
        if (rowindex < 0) {
            return;
        }
        if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
            if (!c.GetCurrentPath().equals("")) {
                PopUpActionListener createFiles = new PopUpActionListener();
                JPopupMenu menu = new JPopupMenu();
                JMenu jm = new JMenu("Nowy");
                JMenuItem item = new JMenuItem("Folder");
                item.addActionListener(createFiles);
                jm.add(item);
                jm.addSeparator();

                item = new JMenuItem("Plik tekstowy (*.txt)");
                item.addActionListener(createFiles);
                jm.add(item);

                item = new JMenuItem("Dokument Worda (*.docx)");
                item.addActionListener(createFiles);
                jm.add(item);

                item = new JMenuItem("Plik graficzny (*.png)");
                item.addActionListener(createFiles);
                jm.add(item);

                item = new JMenuItem("Inny plik");
                item.addActionListener(createFiles);
                jm.add(item);

                menu.add(jm);
                menu.addSeparator();
                item = new JMenuItem("Wytnij");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = table.getValueAt(table.getSelectedRow(), 1).toString();
                        c.SetCurrentPath(c.GetCurrentPath() + "\\" + name);
                        c.Cut();
                        c.SetCurrentPath(c.parentDirectory());
                    }
                });
                menu.add(item);

                item = new JMenuItem("Kopiuj");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = table.getValueAt(table.getSelectedRow(), 1).toString();
                        c.SetCurrentPath(c.GetCurrentPath() + "\\" + name);
                        c.Copy();
                        c.SetCurrentPath(c.parentDirectory());
                    }
                });
                menu.add(item);

                item = new JMenuItem("Wklej");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!c.GetCurrentPath().equals("")) {
                            String name = c.GetCurrentPath();
                            EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ProgressFile pf = new ProgressFile(name, c, dd);
                                        pf.execute();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            JOptionPane.showMessageDialog(null, "Nie można tutaj tego wkleić!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                });
                menu.add(item);

                item = new JMenuItem("Usuń");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = JOptionPane.showConfirmDialog(null, "Czy chcesz usunąć ten plik?", "Usunięcie pliku", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            String name = table.getValueAt(table.getSelectedRow(), 1).toString();
                            c.SetCurrentPath(c.GetCurrentPath() + "\\" + name);
                            c.Delete();
                            dd.showContent();
                        }

                    }
                });
                menu.add(item);
                menu.addSeparator();

                item = new JMenuItem("Zmień nazwę");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = (String) JOptionPane.showInputDialog(null, "Podaj nową nazwę", "Zmień nazwę", JOptionPane.PLAIN_MESSAGE);
                        List<String[]> namelist = c.GetFolderContent();
                        for (String[] x : namelist) {
                            if (x[0].equals(name)) {
                                JOptionPane.showMessageDialog(null, "Podana nazwa już istnieje!", "Nie można zmienić nazwy pliku", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        c.SetCurrentPath(c.GetCurrentPath() + "\\" + table.getValueAt(table.getSelectedRow(), 1).toString());
                        c.rename(c.parentDirectory() + "\\" + name);
                        c.SetCurrentPath(c.parentDirectory());
                        showContent();
                    }
                });

                menu.add(item);
                menu.addSeparator();

                item = new JMenuItem("Właściwości");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String name = table.getValueAt(table.getSelectedRow(), 1).toString();
                            c.SetCurrentPath(c.GetCurrentPath() + "\\" + name);

                            String filepath = c.GetCurrentPath();
                            c.SetCurrentPath(c.parentDirectory());
                            File f = new File(filepath);
                            FileSystemView fsv = FileSystemView.getFileSystemView();
                            BasicFileAttributes attrs = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                            //image scale on purpose
                            ImageIcon icon = (ImageIcon) fsv.getSystemIcon(f);
                            Image img = icon.getImage();
                            Image newimg = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(newimg);

                            JLabel filename = new JLabel(name, icon, JLabel.CENTER);
                            long filesize = 0;
                            if (f.isFile()) {
                                System.out.println(f.getName() + " " + f.length());
                                filesize = f.length();
                            } else if (f.isDirectory()) {
                                filesize = directorySize(f);
                            }

                            FileTime created = attrs.creationTime();
                            FileTime modified = attrs.lastModifiedTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

                            final JComponent[] details = new JComponent[]{
                                filename,
                                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT),
                                new JSeparator(JSeparator.HORIZONTAL),
                                new JLabel("Ścieżka:                    " + f.getAbsolutePath()),
                                new JLabel("Lokalizacja:              " + f.getParent()),
                                new JLabel("Rozmiar:                   " + getSizeUnit(filesize)),
                                new JSeparator(JSeparator.HORIZONTAL),
                                new JLabel("Utworzony:               " + dateFormat.format(created.toMillis())),
                                new JLabel("Zmodyfikowany:     " + dateFormat.format(modified.toMillis())),};

                            int result = JOptionPane.showConfirmDialog(null, details, "Właściwości: " + name, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
//                        if (result == JOptionPane.OK_OPTION) {
//                            System.out.println("You entered "
//                                    + firstName.getText() + ", "
//                                    + lastName.getText() + ", "
//                                    + password.getText());
//                        } else {
//                            System.out.println("User canceled / closed the dialog, result = " + result);
//                        }

//                        c.Delete();
//                        showContent();
                        } catch (IOException ex) {
                            Logger.getLogger(TableRowClickListener.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                menu.add(item);

                menu.show(e.getComponent(), e.getX(), e.getY());
            }

        }
    }

    private class PopUpActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name;
            switch (e.getActionCommand()) {
                case "Folder":
                    name = (String) JOptionPane.showInputDialog(null, "Podaj nazwę nowego folderu:", "Nowy folder", JOptionPane.PLAIN_MESSAGE);
                    if (!(name == null)) {
                        c.newDir(c.GetCurrentPath() + "\\" + name);
                        dd.showContent();
                    }
                    break;
                case "Plik tekstowy (*.txt)":
                    name = (String) JOptionPane.showInputDialog(null, "Podaj nazwę nowego pliku tekstowego:", "Nowy plik tekstowy", JOptionPane.PLAIN_MESSAGE);
                    if (!(name == null)) {
                        c.newFile(c.GetCurrentPath() + "\\" + name + ".txt");
                        dd.showContent();
                    }
                    break;
                case "Dokument Worda (*.docx)":
                    name = (String) JOptionPane.showInputDialog(null, "Podaj nazwę nowego dokumentu:", "Nowy dokument Word", JOptionPane.PLAIN_MESSAGE);
                    if (!(name == null)) {
                        c.newFile(c.GetCurrentPath() + "\\" + name + ".docx");
                        dd.showContent();
                    }
                    break;
                case "Plik graficzny (*.png)":
                    name = (String) JOptionPane.showInputDialog(null, "Podaj nazwę nowego pliku graficznego:", "Nowy plik graficzny", JOptionPane.PLAIN_MESSAGE);
                    if (!(name == null)) {
                        c.newFile(c.GetCurrentPath() + "\\" + name + ".png");
                        dd.showContent();
                    }
                    break;
                case "Inny plik":
                    name = (String) JOptionPane.showInputDialog(null, "Podaj nazwę pliku wraz z rozszerzeniem:", "Nowy plik", JOptionPane.PLAIN_MESSAGE);

                    if (!(name == null)) {
                        while (name.contains(".") == false) {
                            JOptionPane.showMessageDialog(null, "Nie podałeś rozszerzenia!", "Błąd", JOptionPane.WARNING_MESSAGE);
                            name = (String) JOptionPane.showInputDialog(null, "Podaj nazwę pliku wraz z rozszerzeniem:", "Nowy plik", JOptionPane.PLAIN_MESSAGE);
                            String parts[] = name.split("\\.");
                            while (parts[0] == null || parts[1] == null) {
                                JOptionPane.showMessageDialog(null, "Błąd nazwy pliku!", "Błąd", JOptionPane.WARNING_MESSAGE);
                                name = (String) JOptionPane.showInputDialog(null, "Podaj nazwę pliku wraz z rozszerzeniem:", "Nowy plik", JOptionPane.PLAIN_MESSAGE);
                            }
                        }

                        c.newFile(c.GetCurrentPath() + "\\" + name);

                        dd.showContent();
                    } else {

                    }
                    break;
                default:
                    break;
            }
        }

    }

    private void clearList() {
        if (fileTable.getRowCount() > 0) {
            for (int i = fileTable.getRowCount() - 1; i >= 0; i--) {
                list.remove(i);
            }
        }
    }

    private void addContentToList() {
        for (int i = 0; i < temp.size(); i++) {
            String[] s = temp.get(i);
            list.add(s);
        }
        fileTable.fireTableDataChanged();
    }

    private void showContent() {
        clearList();

        temp = c.GetFolderContent();

        addContentToList();
    }

    private static long directorySize(File file) {

        long size;
        if (file.isDirectory()) {
            size = 0;
            for (File child : file.listFiles()) {
                size += directorySize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    private static String getSizeUnit(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    class EnterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable) e.getSource();
            String name = table.getValueAt(table.getSelectedRow(), 1).toString();
            if (!name.equals("...")) {
                c.SetCurrentPath(c.GetCurrentPath() + "\\" + name);
            }
            if (name.equals("...")) {
                c.SetCurrentPath(c.parentDirectory());
            } else if (c.typeOf()) {
                c.SetCurrentPath(c.GetCurrentPath());
            } else if (!c.typeOf()) {
                if (c.RunFile()) {
                    JOptionPane.showMessageDialog(null, "Nie można otworzyć tego pliku", "Błąd", 0);
                }
                c.SetCurrentPath(c.parentDirectory());
            }
            dd.showContent();
            JViewport jViewport = (JViewport) table.getParent();
            JScrollPane jScrollPane = (JScrollPane) jViewport.getParent();

            if (c.GetCurrentPath().equals("")) {
                jScrollPane.setBorder(BorderFactory.createTitledBorder("Mój komputer"));
            } else {
                jScrollPane.setBorder(BorderFactory.createTitledBorder(c.GetCurrentPath()));
            }
        }
    }

}
