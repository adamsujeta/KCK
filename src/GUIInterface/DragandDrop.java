/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import Logic.code.Central;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.IOException;
import java.util.List;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * @author Vaviorky
 */
public class DragandDrop extends TransferHandler {

    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(Integer.class, DataFlavor.javaJVMLocalObjectMimeType, "Files");
    private JTable table = null;
    private Central central;

    public DragandDrop() {
        super(null);
    }

    public DragandDrop(JTable table, Central c) {
        this.table = table;
        this.central = c;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        assert (c == table);
        DataHandler dataHandler = new DataHandler(table.getSelectedRow(), localObjectFlavor.getMimeType());
        if (!central.GetCurrentPath().equals("")) {
            String name = table.getValueAt(table.getSelectedRow(), 1).toString();
            central.SetCurrentPath(central.GetCurrentPath() + "\\" + name);
            central.Cut();
            central.SetCurrentPath(central.parentDirectory());
        } else {
            JOptionPane.showMessageDialog(null, "Nie można przenieść całej partycji!", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return dataHandler;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        boolean b = info.getComponent() == table && info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
        table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        return b;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        JTable target = (JTable) info.getComponent();
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int index = dl.getRow();

        int max = table.getModel().getRowCount();
        if (index < 0 || index > max) {
            index = max;
        }
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        try {
            Integer rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
            if (!central.GetCurrentPath().equals("")) {
                if (!GUIWindowSingleton.getWindow().getcLeft().GetCurrentPath().equals(GUIWindowSingleton.getWindow().getcRight().GetCurrentPath())) {
                    String name = central.GetCurrentPath();
                    ProgressFile pf = new ProgressFile(name, central, this);
                    pf.execute();
                } else {
                    JOptionPane.showMessageDialog(null, "Nie można wkleić tego do tego samego folderu!", "Błąd", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Nie można tutaj tego wkleić!", "Błąd", JOptionPane.ERROR_MESSAGE);

            }
            if (rowFrom != -1 && rowFrom != index) {
                ((Reorderable) table.getModel()).reorder(rowFrom, index);
                if (index > rowFrom) {
                    index--;
                }

                target.getSelectionModel().addSelectionInterval(index, index);
                return true;
            }
        } catch (UnsupportedFlavorException | IOException e) {
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        if ((act == TransferHandler.MOVE) || (act == TransferHandler.NONE)) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void showContent() {
        Central cLeft = GUIWindowSingleton.getWindow().getcLeft();
        Central cRight = GUIWindowSingleton.getWindow().getcRight();
        List<String[]> leftList = GUIWindowSingleton.getWindow().getLeftList();
        List<String[]> rightList = GUIWindowSingleton.getWindow().getRightList();

        FileTable leftTable = GUIWindowSingleton.getWindow().getLeftTable();
        FileTable rightTable = GUIWindowSingleton.getWindow().getRightTable();

        if (leftTable.getRowCount() > 0) {
            for (int i = leftTable.getRowCount() - 1; i >= 0; i--) {
                leftList.remove(i);
            }
        }

        List<String[]> tempLeftList = cLeft.GetFolderContent();

        for (int i = 0; i < tempLeftList.size(); i++) {
            String[] s = tempLeftList.get(i);
            leftList.add(s);
        }
        leftTable.fireTableDataChanged();

        if (rightTable.getRowCount() > 0) {
            for (int i = rightTable.getRowCount() - 1; i >= 0; i--) {
                rightList.remove(i);
            }
        }

        List<String[]> tempRightList = cRight.GetFolderContent();

        for (int i = 0; i < tempRightList.size(); i++) {
            String[] s = tempRightList.get(i);
            rightList.add(s);
        }
        rightTable.fireTableDataChanged();
    }

}
