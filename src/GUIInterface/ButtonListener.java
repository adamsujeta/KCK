/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import Logic.code.Central;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Vaviorky
 */
public class ButtonListener implements ActionListener {

    private Central c;
    private List<String[]> list;
    private List<String[]> temp;
    private List<String[]> searchlist;

    private FileTable table;
    private JTextField textField;
    private File dir;

    private static int actualnr;

    public ButtonListener(Central c, List<String[]> list, FileTable table, JTextField text) {
        this.c = c;
        this.list = list;
        this.table = table;
        this.textField = text;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        dir = new File(c.GetCurrentPath());
        searchlist = new ArrayList<>();
        clearList();
        String[] pom = new String[2];
        pom[0] = "...";
        pom[1] = "";
        searchlist.add(pom);
        SearchProgress sp = null;
        try {
            sp = new SearchProgress();
        } catch (InterruptedException ex) {
            Logger.getLogger(ButtonListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        sp.execute();

    }

    private void clearList() {
        if (table.getRowCount() > 0) {
            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                list.remove(i);
            }
        }
    }

    private void addContentToList() {
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {
                String[] s = temp.get(i);
                list.add(s);
            }
            table.fireTableDataChanged();
        } else {
            System.out.println("Lista jest pusta");
        }

    }

    public static void setActualnr(int actualnr) {
        ButtonListener.actualnr = actualnr;
    }

    public static int getActualnr() {
        return actualnr;
    }

    class SearchProgress extends SwingWorker<Void, Integer> {

        private JDialog dialog;
        private JProgressBar progressBar;
        private JPanel items;

        public SearchProgress() throws InterruptedException {

            items = new JPanel(new FlowLayout());

            int filecount = Central.nrOfFiles(dir);

            progressBar = new JProgressBar(0, filecount);
            dialog = new JDialog((JDialog) null, "Wyszukiwanie...");
            dialog.setAlwaysOnTop(true);
            dialog.setSize(new Dimension(200, 75));
            dialog.setLocationRelativeTo(null);
            items.add(progressBar);
            dialog.add(items);
            dialog.setVisible(true);
            actualnr = 0;
            clearList();
        }

        @Override
        protected Void doInBackground() throws Exception {

            temp = Search(textField.getText(), dir, searchlist);
            c.SetCurrentPath(null);
            return null;
        }

        @Override
        protected void done() {
            dialog.setVisible(false);
            addContentToList();
        }

        @Override
        protected void process(List<Integer> chunks) {
            int i = chunks.get(chunks.size() - 1);
            progressBar.setValue(i);
        }

        public List<String[]> Search(String name, File directory, List<String[]> finded) {
            File[] files = directory.listFiles();
            FileSystemView fsv = FileSystemView.getFileSystemView();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        if (name.equalsIgnoreCase(f.getName())) {
                            String[] fileProperties = new String[3];
                            // fileProperties[2] = f.getName();
                            // fileProperties[1] = f.getAbsolutePath();
                            //  fileProperties[0] = f.getAbsolutePath();
                            fileProperties[0] = f.toString();
                            fileProperties[1] = fsv.getSystemTypeDescription(f);
                            fileProperties[2] = f.getAbsolutePath();
                            finded.add(fileProperties);

                        }
                        Search(name, f, finded);
                    } else if (name.equalsIgnoreCase(f.getName())) {
                        String[] fileProperties = new String[3];
                        // fileProperties[0] = f.getName();
                        // fileProperties[1] = f.getAbsolutePath();
                        //fileProperties[2] = f.getAbsolutePath();
                        fileProperties[0] = f.toString();
                        fileProperties[1] = fsv.getSystemTypeDescription(f);
                        fileProperties[2] = f.getAbsolutePath();
                        finded.add(fileProperties);

                    }
                    actualnr++;
                    publish(actualnr);

                }
            }
            return finded;
        }

    }

}
