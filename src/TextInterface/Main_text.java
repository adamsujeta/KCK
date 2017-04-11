/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

import java.io.IOException;

/**
 *
 * @author Vaviorky
 */
public class Main_text {

    public static void main(String[] args) throws IOException {
        //wywolanie
        TextWindow textWindow = WindowSingleton.getWindow();
        textWindow.mainWindow();
        
       // TxtEditor editor = new TxtEditor();
       // editor.showEditor();
    }
}
