/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import Logic.code.Central;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Vaviorky
 */
public class GUIWindow extends JFrame {

    private JTable tableLeft, tableRight;
    private final Dimension dim;
    private final Central cLeft, cRight;
    private final JSplitPane splitPane;
    private List<String[]> leftList, rightList;
    private FileTable leftTable, rightTable;
    private TableRowClickListener listenerLeft, listenerRight;
    private ButtonListener buttonLeft, buttonRight;
    private String solveEnter = "Solve";
    private ProgressMonitor progressMonitor;

    public ProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    public Central getcLeft() {
        return cLeft;
    }

    public Central getcRight() {
        return cRight;
    }

    public List<String[]> getLeftList() {
        return leftList;
    }

    public List<String[]> getRightList() {
        return rightList;
    }

    public FileTable getLeftTable() {
        return leftTable;
    }

    public FileTable getRightTable() {
        return rightTable;
    }

    public GUIWindow() {
        super("Bieda Commander");
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        cLeft = new Central();
        cRight = new Central();
        splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
    }

    public void showWindow() {
        setMinimumSize(new Dimension(800, 600));
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        leftList = cLeft.GetFolderContent();
        rightList = cRight.GetFolderContent();

        leftTable = new FileTable(leftList);
        rightTable = new FileTable(rightList);

        tableLeft = new JTable(leftTable);
        tableRight = new JTable(rightTable);

        tableLeft.setDropMode(DropMode.INSERT_ROWS);
        tableLeft.setDragEnabled(true);
        tableLeft.setTransferHandler(new DragandDrop(tableLeft, cLeft));

        tableRight.setDropMode(DropMode.INSERT_ROWS);
        tableRight.setDragEnabled(true);
        tableRight.setTransferHandler(new DragandDrop(tableRight, cRight));

        listenerLeft = new TableRowClickListener(cLeft, leftTable, leftList);
        listenerRight = new TableRowClickListener(cRight, rightTable, rightList);
        tableLeft.addMouseListener(listenerLeft);
        tableRight.addMouseListener(listenerRight);
        tableLeft.setFillsViewportHeight(true);
        tableRight.setFillsViewportHeight(true);

        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

        tableLeft.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, solveEnter);
        tableLeft.getActionMap().put(solveEnter, listenerLeft.new EnterAction());
        tableRight.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, solveEnter);
        tableRight.getActionMap().put(solveEnter, listenerRight.new EnterAction());

        JPanel leftSearchPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Wyszukaj: ");
        JTextField leftSearchField = new JTextField();
        JButton wyszukajLeft = new JButton("Szukaj");
        buttonLeft = new ButtonListener(cLeft, leftList, leftTable, leftSearchField);
        wyszukajLeft.addActionListener(buttonLeft);
        leftSearchPanel.add(label, BorderLayout.WEST);
        leftSearchPanel.add(leftSearchField, BorderLayout.CENTER);
        leftSearchPanel.add(wyszukajLeft, BorderLayout.EAST);

        JPanel rightSearchPanel = new JPanel(new BorderLayout());
        JLabel textright = new JLabel("Wyszukaj: ");
        JTextField rightSearchField = new JTextField();
        JButton wyszukajRight = new JButton("Szukaj");
        buttonRight = new ButtonListener(cRight, rightList, rightTable, rightSearchField);
        wyszukajRight.addActionListener(buttonRight);
        rightSearchPanel.add(textright, BorderLayout.WEST);
        rightSearchPanel.add(rightSearchField, BorderLayout.CENTER);
        rightSearchPanel.add(wyszukajRight, BorderLayout.EAST);

        JScrollPane searchLeft = new JScrollPane(leftSearchPanel);
        JScrollPane searchRight = new JScrollPane(rightSearchPanel);

        JSplitPane searchers = new JSplitPane();
        searchers.setResizeWeight(0.5);
        searchers.setLeftComponent(searchLeft);
        searchers.setRightComponent(searchRight);

        JScrollPane scrollPaneLeft = new JScrollPane(tableLeft);
        JScrollPane scrollPaneRight = new JScrollPane(tableRight);

        scrollPaneLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder("Mój komputer")));
        scrollPaneRight.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder("Mój komputer")));
        splitPane.setLeftComponent(scrollPaneLeft);
        splitPane.setRightComponent(scrollPaneRight);
        tableLeft.setShowHorizontalLines(false);
        tableLeft.setShowVerticalLines(false);
        tableLeft.setRowHeight(30);
        tableRight.setShowHorizontalLines(false);
        tableRight.setShowVerticalLines(false);
        tableRight.setRowHeight(30);
        TableColumnModel columnModel = tableLeft.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel = tableRight.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);
        tableRight.setShowHorizontalLines(false);
        tableLeft.setFont(new Font("Arial", Font.PLAIN, 16));
        tableRight.setFont(new Font("Arial", Font.PLAIN, 16));
        add(searchers, BorderLayout.SOUTH);
        add(splitPane, BorderLayout.CENTER);
        setVisible(true);

        //setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }

}
