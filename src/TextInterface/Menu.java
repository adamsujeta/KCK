/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Table;

/**
 *
 * @author Vaviorky
 */
public class Menu {

    public Table menuoptions() {
        Component[] option = new Component[8];
        Table alloptions = new Table(8);
        alloptions.setColumnPaddingSize(2);
        Label napis = new Label("CTRL+N: Stworz katalog", Boolean.TRUE);
        option[0] = napis;
        napis = new Label("F5: Stworz plik", Boolean.TRUE);
        option[1] = napis;
        napis = new Label("CTRL+X: Wytnij", Boolean.TRUE);
        option[2] = napis;
        napis = new Label("CTRL+C: Kopiuj", Boolean.TRUE);
        option[3] = napis;
        napis = new Label("CTRL+V: Wklej", Boolean.TRUE);
        option[4] = napis;
        napis = new Label("DELETE: Usun", Boolean.TRUE);
        option[5] = napis;
        napis = new Label("CTRL+R: Zmien nazwe", Boolean.TRUE);
        option[6] = napis;
        napis = new Label("CTRL+F: Wyszukaj", Boolean.TRUE);
        option[7] = napis;
        alloptions.addRow(option);
        
        return alloptions;
    }

}
