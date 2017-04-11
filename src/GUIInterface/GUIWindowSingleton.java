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
public class GUIWindowSingleton {
    private GUIWindowSingleton() {

    }
    private static GUIWindow window;

    public static GUIWindow getWindow() {
        if (window == null) {
            synchronized (GUIWindowSingleton.class) {
                if (window == null) {
                    window = new GUIWindow();
                }
            }
        }
        return window;
    }
    
}
