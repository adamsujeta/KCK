/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic.code;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author adams
 */
public class Central {

    private String current = null;

    private static File copyfile;
    private static int temp;
    private static File CopyPom;

    public String GetCurrentPath() {
        if (current != null) {
            File Cur = new File(current);
            if (Cur.isFile() == true || Cur.isDirectory() == true) {
                return current;
            }
        }
        return "";
    }

    public void SetCurrentPath(String path) {
        if (path != null) {
            File Cur = new File(path);
            if (Cur.isFile() == true || Cur.isDirectory() == true) {

                current = Cur.getPath();
            }
        } else {
            current = null;
        }
    }

    public void newDir(String path) {
        if (current != null) {
            File Cur = new File(path);
            Cur.mkdir();
        }
    }

    public List<String[]> GetFolderContent() {
        List<String[]> content = new LinkedList<>();
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String[] pom = new String[2];

        try {

            if (current == null) {
                paths = File.listRoots();
                for (File path : paths) {
                    pom = new String[3];
                    pom[0] = path.toString();
                    pom[1] = fsv.getSystemTypeDescription(path);
                    pom[2] = path.getAbsolutePath();
                    content.add(pom);
                }
            } else {
                pom[0] = "...";
                pom[1] = "";
                content.add(pom);
                File Cur = new File(current);
                if (Cur.isDirectory() == true) {
                    paths = Cur.listFiles();
                    for (File path : paths) {
                        if (!path.isHidden()) {
                            pom = new String[3];
                            pom[0] = path.getName();
                            pom[1] = fsv.getSystemTypeDescription(path);
                            pom[2] = path.getAbsolutePath();
                            content.add(pom);
                        }
                    }
                }

            }
        } catch (NullPointerException e) {

        }
        return content;
    }

    public void Delete() {
        System.out.println("=========================a" + GetCurrentPath());
        System.out.println(current);
        if (current != null) {
            if (typeOf()) {
                List<String[]> paths = GetFolderContent();

                for (String[] x : paths) {
                    if (!x[0].equals("...")) {
                        SetCurrentPath(GetCurrentPath() + "\\" + x[0]);
                        Delete();

                    }
                }
            }
            File Cur = new File(current);
            Cur.delete();
            SetCurrentPath(parentDirectory());
        }
        System.out.println("=========================b" + GetCurrentPath());
    }

    public void Copy() {
        if (!current.equals("...")) {
            File Cur = new File(current);
            CopyPom = Cur;
            copyfile = Cur;
            temp = 0;
        }
    }

    public void PasteRec(String path) {
        InputStream is = null;
        OutputStream os = null;
        if (path.endsWith("...")) {
            return;
        }
        try {
            if (CopyPom.isDirectory()) {
                File temp1 = new File(path + "\\" + CopyPom.getName());
                path = path + "\\" + CopyPom.getName();
                if (!temp1.exists()) {
                    temp1.mkdir();
                }
                File[] paths = CopyPom.listFiles();

                for (File x : paths) {

                    CopyPom = new File(CopyPom.getAbsolutePath() + "\\" + x.getName());
                    PasteRec(path);
                    CopyPom = new File(CopyPom.getParent());
                }
            } else {
                path = path + "\\" + CopyPom.getName();
                is = new BufferedInputStream(new ProgressMonitorInputStream(null, "Przetwarzanie: " + path, new FileInputStream(CopyPom)));
                os = new FileOutputStream(new File(path));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                is.close();
                os.close();

            }

        } catch (IOException ex) {
            Logger.getLogger(Central.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Paste(String path) {

        PasteRec(path);
        if (temp == 1) {
            String pom = current;
            SetCurrentPath(copyfile.getAbsolutePath());
            Delete();
            SetCurrentPath(pom);
            CopyPom = null;
            copyfile = null;
            temp = 0;
        }
    }

    public void Cut() {
        if (current != "...") {
            File Cur = new File(current);
            copyfile = Cur;
            CopyPom = Cur;
            temp = 1;
        }
    }

    public boolean RunFile() {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(current));
        } catch (IOException ex) {
            return true;
        }
        return false;
    }

    public boolean typeOf() {
        if (current == null) {
            return true;
        }
        File Cur = new File(current);
        if (Cur.isDirectory() == true) {
            return true;
        }
        return false;
    }

    public String parentDirectory() {
        if (current != null) {
            int endIndex = current.lastIndexOf("\\");
            String newstr = null;
            if (endIndex != -1) {
                newstr = current.substring(0, endIndex);
            }
            if (current.charAt(current.length() - 2) == ':') {
                current = null;
                newstr = "";
            }
            if (newstr.length() == 2) {
                newstr += "\\";
            }
            return newstr;
        }
        return null;
    }

    public void rename(String name) {
        File Cur = new File(current);
        Cur.renameTo(new File(name));
    }

    public boolean newFile(String path) { // true gdy udało sie stworzyc plik 
        if (current != null) {
            File Cur = new File(path);
            try {
                if (Cur.createNewFile()) {
                    return true;
                } else {
                    return false;

                }
            } catch (IOException ex) {
                Logger.getLogger(Central.class
                        .getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        }
        return false;
    }

    public List<String[]> Search(String name, File directory, List<String[]> finded) {
        File[] files = directory.listFiles();

        if (files != null) {

            /* for (File f : files) {
            if (f.isDirectory()) {
            Search(name, directory, finded);
            } else {
            nrOfFiles++;
            System.out.println(nrOfFiles);
            }
            }*/
            for (File f : files) {
                if (f.isDirectory()) {
                    if (name.equalsIgnoreCase(f.getName())) {
                        String[] fileProperties = new String[3];
                        fileProperties[2] = f.getName();
                        fileProperties[1] = f.getAbsolutePath();
                        fileProperties[0] = f.getAbsolutePath();
                        finded.add(fileProperties);
                    }

                    Search(name, f, finded);
                } else if (name.equalsIgnoreCase(f.getName())) {
                    String[] fileProperties = new String[3];
                    fileProperties[0] = f.getName();
                    fileProperties[1] = f.getAbsolutePath();
                    fileProperties[2] = f.getAbsolutePath();
                    finded.add(fileProperties);
                }
            }
        }
        return finded;
    }

    public static int nrOfFiles(File file) {
        File[] files = file.listFiles();
        int count = 0;
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    count++;
                    count += nrOfFiles(f);

                } else {
                    count++;
                }
            }
        }
        return count;
    }

}
