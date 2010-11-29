/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package avprojector;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Long
 */
class ButtonCellRenderer extends AbstractCellEditor implements
                                                   TableCellRenderer,
                                                   TableCellEditor {

    private JButton button;
    private JLabel label;

    public ButtonCellRenderer(final JTable table) {

        this.button = new JButton("Unknown");
        this.button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = table.getColumnModel().getColumnIndexAtX(e.getX());
                int row    = e.getY()/table.getRowHeight();

                System.out.println("Testing at: col: " + column + " row: " + row);
            }
        });
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {

        String status = (String)value;
        if (status.compareTo("On")== 0 )
        {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.GREEN);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else if(status.compareTo("Off")== 0 )
        {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.RED);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else if(status.compareTo("Cooling")== 0 )
        {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.BLUE);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else if(status.compareTo("Warmup")== 0 )
        {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.ORANGE);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else
        {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.DARK_GRAY);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        button.setText(status);
        return button;
    }

    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        return button;
    }

    public Object getCellEditorValue() {
        return button.getText();
    }

}