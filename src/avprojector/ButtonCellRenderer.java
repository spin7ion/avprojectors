/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package avprojector;

import avprojector.controller.AVProjectorController;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                if(e.getClickCount() == 2)
                {
                    int column = table.getColumnModel().getColumnIndexAtX(e.getX());
                    int row    = e.getY()/table.getRowHeight();

                    // go to the projector webserver
                    if ( column == 0 )
                    {
                        try {
                            AVProjectorController.GoToWebServer(row, column);
                        } catch (IOException ex) {
                            Logger.getLogger(ButtonCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    // turn the projector on/off
                    else if(column == 1)
                    {
                        AVProjectorController.TurnOnOff(row, column);
                    }

                }
            }
        });
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {

        String status = (String)value;
        if (status.compareTo("On")== 0 )
        {
            button.setForeground(Color.BLACK);

            if(isSelected)
            {
                //Spring green 0-255-127
                button.setBackground(new Color( 0, 255, 127 ));
            }
            else
            {
                button.setBackground(Color.GREEN);
            }

            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else if(status.compareTo("Off")== 0 )
        {
            button.setForeground(Color.BLACK);
            if(isSelected)
            {
                // Tomato 255-99-71
                button.setBackground(new Color(255, 99, 71 ));
            }
            else
            {
                button.setBackground(Color.RED);
            }
            
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else if(status.compareTo("Cooling")== 0 )
        {
            button.setForeground(Color.BLACK);
            if(isSelected)
            {
                //Light Sky Blue 135-206-250
                button.setBackground(new Color(135, 206, 250));
            }
            else
            {
                button.setBackground(Color.BLUE);
            }
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else if(status.compareTo("Warmup")== 0 )
        {
            button.setForeground(Color.BLACK);
                        if(isSelected)
            {
                // orange 255-165-0
                button.setBackground(new Color(255, 165, 0));
            }
            else
            {
                // dark orange 255-140-0
                button.setBackground(new Color(255, 140, 0));
            }
            
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
        }

        else
        {
            button.setForeground(Color.BLACK);
            button.setBackground(Color.LIGHT_GRAY);
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