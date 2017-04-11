 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.dialog.DialogButtons;
import com.googlecode.lanterna.gui.dialog.MessageBox;


/**
 *
 * @author adams
 */
public class DialogPopUp {

    public DialogPopUp(String message,String title,GUIScreen GUI) {
        MessageBox.showMessageBox(GUI, title, message, DialogButtons.OK);
    }

}
