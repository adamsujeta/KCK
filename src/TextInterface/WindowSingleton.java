/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

/**
 *
 * @author Vaviorky
 */
public class WindowSingleton {

    private WindowSingleton() {

    }
    private static TextWindow window;

    public static TextWindow getWindow() {
        if (window == null) {
            synchronized (WindowSingleton.class) {
                if (window == null) {
                    window = new TextWindow();
                }
            }
        }
        return window;
    }

}
