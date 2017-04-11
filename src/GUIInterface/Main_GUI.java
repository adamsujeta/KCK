/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

/**
 *
 * @author Vaviorky
 */
public class Main_GUI {
    public static void main(String[] args) {
        GUIWindow gui = GUIWindowSingleton.getWindow();
        gui.showWindow();
        
    }
}
