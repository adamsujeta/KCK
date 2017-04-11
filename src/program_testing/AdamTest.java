/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package program_testing;

import Logic.code.Central;
import java.io.File;
import java.util.List;

/**
 *
 * @author adams
 */
public class AdamTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Central c = new Central();
        List<String[]> paths = c.GetFolderContent();

        for (String[] x : paths) {
            System.out.println(x[0]);
            System.out.println(x[1]);
        }
        System.out.println("------------------------------------------");
        c.SetCurrentPath("D:\\");
       // c.Delete();
        paths = c.GetFolderContent();
       // c.SetCurrentPath(c.GetCurrentPath() + "\\" + x[0]);
        //String nazwa="nazwa\\aja.jpg";
File s=new File("d:\\dsaasdas");
        System.out.println("sds-------------------------" + s.getAbsolutePath());
        
       // c.newFile(c.GetCurrentPath() + "\\" + nazwa);
        for (String[] x : paths) {
            System.out.println(x[0]);
           // System.out.println(x[1]);
        }

    }
}
