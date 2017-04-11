/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextInterface;

import Logic.code.Central;
import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Interactable;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.ActionListBox;
import com.googlecode.lanterna.gui.dialog.ActionListDialog;
import com.googlecode.lanterna.gui.dialog.TextInputDialog;
import com.googlecode.lanterna.gui.listener.WindowListener;
import com.googlecode.lanterna.input.Key;
import java.util.List;

/**
 *
 * @author Vaviorky
 */
public class Keyboard_listener implements WindowListener {

    private Central c;
    private ActionListBox actionListBox;
    private List<String[]> list;
    private Explorer x;

    public Keyboard_listener() {
        c = new Central();
    }

    public Central getC() {
        return c;
    }

    public void setC(Central c) {
        this.c = c;
    }

    @Override
    public void onUnhandledKeyboardInteraction(Window window, Key key) {
        if (key.getKind().equals(Key.Kind.Escape)) {
            window.close();
            System.exit(0);
        }

        switch (key.getKind()) {
            case Escape:
                window.close();
                System.exit(0);
                break;
            case F1:

                break;

            case F5:
                //<editor-fold defaultstate="collapsed" desc="nowy plik"> 
                if (c.GetCurrentPath() != null && c.GetCurrentPath() != "") {
                    Action[] typy_plikow = new Action[4];
                    typy_plikow[0] = new Action() {

                        @Override
                        public void doAction() {
                            String nazwa = TextInputDialog.showTextInputBox(WindowSingleton.getWindow().guiScreen, "Nowy plik tekstowy", "Podaj nazwę pliku:", "");
                            if (!(nazwa == null)) {
                                nazwa = nazwa + ".txt";
                                if (!c.newFile(c.GetCurrentPath() + "\\" + nazwa)) {
                                    DialogPopUp d = new DialogPopUp("Nie mozna utworzyć pliku / nieprawidłowa nazwa", "Błąd", WindowSingleton.getWindow().guiScreen);
                                }
                                refresh();
                            }
                        }

                        @Override
                        public String toString() {
                            return "Plik tekstowy (.txt)";
                        }

                    };
                    typy_plikow[1] = new Action() {

                        @Override
                        public void doAction() {
                            String nazwa = TextInputDialog.showTextInputBox(WindowSingleton.getWindow().guiScreen, "Nowy plik graficzny", "Podaj nazwę pliku:", "");
                            if (!(nazwa == null)) {
                                nazwa = nazwa + ".png";
                                if (!c.newFile(c.GetCurrentPath() + "\\" + nazwa)) {
                                    new DialogPopUp("Nie mozna utworzyć pliku / nieprawidłowa nazwa", "Błąd", WindowSingleton.getWindow().guiScreen);
                                }
                                refresh();
                            }
                        }

                        @Override
                        public String toString() {
                            return "Plik graficzny (.png)";
                        }

                    };
                    typy_plikow[2] = new Action() {

                        @Override
                        public void doAction() {
                            String nazwa = TextInputDialog.showTextInputBox(WindowSingleton.getWindow().guiScreen, "Nowy dokument Worda", "Podaj nazwę pliku:", "");
                            if (!(nazwa == null)) {
                                nazwa = nazwa + ".docx";
                                if (!c.newFile(c.GetCurrentPath() + "\\" + nazwa)) {
                                    new DialogPopUp("Nie mozna utworzyć pliku / nieprawidłowa nazwa", "Błąd", WindowSingleton.getWindow().guiScreen);
                                }
                                refresh();
                            }
                        }

                        @Override
                        public String toString() {
                            return "Dokument Worda (.docx)";
                        }

                    };
                    typy_plikow[3] = new Action() {

                        @Override
                        public void doAction() {
                            String nazwa = TextInputDialog.showTextInputBox(WindowSingleton.getWindow().guiScreen, "Nowy plik", "Podaj nazwę pliku wraz z rozszerzeniem:", "");
                            if (!(nazwa == null)) {
                                if (!c.newFile(c.GetCurrentPath() + "\\" + nazwa)) {
                                    new DialogPopUp("Nie mozna utworzyć pliku / nieprawidłowa nazwa", "Błąd", WindowSingleton.getWindow().guiScreen);
                                }
                                refresh();
                            }
                        }

                        @Override
                        public String toString() {
                            return "Inny plik";
                        }

                    };

                    ActionListDialog.showActionListDialog(WindowSingleton.getWindow().guiScreen, "Nowy plik", "Jaki rodzaj pliku chcesz utworzyć", 0, true, typy_plikow);
                } else {
                    new DialogPopUp("Nie mozna utworzyć tu pliku", "Błąd", WindowSingleton.getWindow().guiScreen);
                }
                break;
//</editor-fold>
            case Backspace:
                refresh();

                break;
            case Delete:
                //<editor-fold defaultstate="colapsed" desc="usuanie">
                if (c.GetCurrentPath() != null && c.GetCurrentPath() != "") {
                    Action[] czy = new Action[2];
                    czy[0] = new Action() {

                        @Override
                        public void doAction() {
                            if (WindowSingleton.getWindow().getLeftExplorer().getFilelist().hasFocus()) {
                                if (!WindowSingleton.getWindow().getLeftExplorer().delete()) {
                                    DialogPopUp dialogPopUp = new DialogPopUp("Nie mozna usunąć tego pliku/folderu", "Błąd", WindowSingleton.getWindow().guiScreen);
                                } else {
                                    refresh();
                                }

                            } else if (WindowSingleton.getWindow().getRightExplorer().getFilelist().hasFocus()) {

                                if (!WindowSingleton.getWindow().getRightExplorer().delete()) {
                                    DialogPopUp dialogPopUp = new DialogPopUp("Nie mozna usunąć tego pliku/folderu", "Błąd", WindowSingleton.getWindow().guiScreen);
                                } else {
                                    refresh();
                                }
                            }
                        }

                        @Override
                        public String toString() {
                            return "Tak";
                        }

                    };
                    czy[1] = new Action() {

                        @Override
                        public void doAction() {

                        }

                        @Override
                        public String toString() {
                            return "Nie";
                        }

                    };
                    ActionListDialog.showActionListDialog(WindowSingleton.getWindow().guiScreen, "Usuwanie", "Czy chcesz usunąć ten plik ?", 0, true, czy);
                } else {
                    new DialogPopUp("Nie mozna usunąć tego pliku/folderu", "Błąd", WindowSingleton.getWindow().guiScreen);
                }

                break;
            //</editor-fold>
            default:
                break;
        }
        switch (key.getCharacter()) {
            case 'r':
                if (key.isCtrlPressed()) {
                    if (!(c.GetCurrentPath() == "")) {
                        String nam = TextInputDialog.showTextInputBox(WindowSingleton.getWindow().guiScreen, "Nazwa", "Podaj nazwe Nazwe:", "");
                        if (nam != null) {

                            if (WindowSingleton.getWindow().getLeftExplorer().getFilelist().hasFocus()) {
                                if (!WindowSingleton.getWindow().getLeftExplorer().rename(nam)) {
                                    new DialogPopUp("Taka nazwa już istnieje!", "Błąd", WindowSingleton.getWindow().guiScreen);

                                }
                            } else if (WindowSingleton.getWindow().getRightExplorer().getFilelist().hasFocus()) {
                                if (!WindowSingleton.getWindow().getRightExplorer().rename(nam)) {
                                    new DialogPopUp("Taka nazwa już istnieje!", "Błąd", WindowSingleton.getWindow().guiScreen);
                                }
                            }
                            refresh();
                        }
                    } else {
                        new DialogPopUp("Nie możesz zmienić litery dysku!", "Błąd", WindowSingleton.getWindow().guiScreen);

                    }
                }
                break;
            case 'n':
                if (c.GetCurrentPath() != null && c.GetCurrentPath() != "") {
                    if (key.isCtrlPressed()) {
                        String nazwa = TextInputDialog.showTextInputBox(WindowSingleton.getWindow().guiScreen, "Nowy folder", "Podaj nazwe folderu:", "");
                        if (!(nazwa == null)) {
                            if (WindowSingleton.getWindow().getLeftExplorer().getFilelist().hasFocus()) {
                                if (!WindowSingleton.getWindow().getLeftExplorer().newDir(c.GetCurrentPath(), nazwa)) {
                                    new DialogPopUp("Taka nazwa już istnieje!", "Błąd", WindowSingleton.getWindow().guiScreen);

                                }
                            } else if (WindowSingleton.getWindow().getRightExplorer().getFilelist().hasFocus()) {
                                if (!WindowSingleton.getWindow().getRightExplorer().newDir(c.GetCurrentPath(), nazwa)) {
                                    new DialogPopUp("Taka nazwa już istnieje!", "Błąd", WindowSingleton.getWindow().guiScreen);
                                }
                            }
                            refresh();

                        }
                    }

                } else {
                    DialogPopUp dialogPopUp = new DialogPopUp("Nie mozna utworzyc folderu w tym miejscu", "Błąd", WindowSingleton.getWindow().guiScreen);
                }

                break;

            case 'f':
                if (c.GetCurrentPath() != null && c.GetCurrentPath() != "") {
                    if (key.isCtrlPressed()) {
                        String nazwa = TextInputDialog.showTextInputBox(WindowSingleton.getWindow().guiScreen, "Szukanie pliku", "Podaj nazwe pliku:", "");
                        if (!(nazwa == null)) {

                            if (WindowSingleton.getWindow().getLeftExplorer().getFilelist().hasFocus()) {

                                WindowSingleton.getWindow().getPanelLeft().removeAllComponents();
                                WindowSingleton.getWindow().getPanelLeft().addComponent(
                                        WindowSingleton.getWindow().getLeftExplorer().showFinded(
                                                c, WindowSingleton.getWindow().getPanelLeft(), WindowSingleton.getWindow().guiScreen, nazwa));
                            } else if (WindowSingleton.getWindow().getRightExplorer().getFilelist().hasFocus()) {
                                WindowSingleton.getWindow().getPanelRight().removeAllComponents();
                                WindowSingleton.getWindow().getPanelRight().addComponent(
                                        WindowSingleton.getWindow().getRightExplorer().showFinded(
                                                c, WindowSingleton.getWindow().getPanelRight(), WindowSingleton.getWindow().guiScreen, nazwa));

                            }

                        }
                    }
                }
                break;
            case 'x':
                if (key.isCtrlPressed()) {
                    if (!(c.GetCurrentPath() == "")) {
                        System.out.println("aaaa");
                        if (WindowSingleton.getWindow().getLeftExplorer().getFilelist().hasFocus()) {
                            WindowSingleton.getWindow().getLeftExplorer().cut();
                        } else if (WindowSingleton.getWindow().getRightExplorer().getFilelist().hasFocus()) {
                            WindowSingleton.getWindow().getRightExplorer().cut();
                        }
                        refresh();
                    } else {
                        new DialogPopUp("Nie można tego wyciąć!", "Błąd", WindowSingleton.getWindow().guiScreen);

                    }
                }
                break;
            case 'c':
                if (key.isCtrlPressed()) {
                    if (!(c.GetCurrentPath() == "")) {
                        if (WindowSingleton.getWindow().getLeftExplorer().getFilelist().hasFocus()) {
                            WindowSingleton.getWindow().getLeftExplorer().copy();
                        } else if (WindowSingleton.getWindow().getRightExplorer().getFilelist().hasFocus()) {
                            WindowSingleton.getWindow().getRightExplorer().copy();
                        }
                    } else {
                        new DialogPopUp("Nie można tego skopiować!", "Błąd", WindowSingleton.getWindow().guiScreen);

                    }
                }
                break;
            case 'v':
                if (key.isCtrlPressed()) {
                    if (!(c.GetCurrentPath() == "")) {
                        c.Paste(c.GetCurrentPath());
                        refresh();
                    } else {
                        new DialogPopUp("Nie można tutaj tego wkleić!", "Błąd", WindowSingleton.getWindow().guiScreen);

                    }
                }

                break;

        }
    }

    @Override
    public void onWindowInvalidated(Window window
    ) {
    }

    @Override
    public void onWindowShown(Window window
    ) {
    }

    @Override
    public void onWindowClosed(Window window
    ) {
    }

    @Override
    public void onFocusChanged(Window window, Interactable i, Interactable i1
    ) {
    }

    private void refresh() {
        WindowSingleton.getWindow().getPanelLeft().removeAllComponents();
        WindowSingleton.getWindow().getPanelLeft().addComponent(WindowSingleton.getWindow().getLeftExplorer().fileExplorer(WindowSingleton.getWindow().getcLeft(), WindowSingleton.getWindow().getPanelLeft(), WindowSingleton.getWindow().guiScreen));
        WindowSingleton.getWindow().getPanelRight().removeAllComponents();
        WindowSingleton.getWindow().getPanelRight().addComponent(WindowSingleton.getWindow().getRightExplorer().fileExplorer(WindowSingleton.getWindow().getcRight(), WindowSingleton.getWindow().getPanelRight(), WindowSingleton.getWindow().guiScreen));
    }
}
