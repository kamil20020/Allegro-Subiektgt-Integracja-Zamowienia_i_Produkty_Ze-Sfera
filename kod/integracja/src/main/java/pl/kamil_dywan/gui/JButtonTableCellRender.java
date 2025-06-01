package pl.kamil_dywan.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JButtonTableCellRender extends JButton implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object complexValue, boolean isSelected, boolean hasFocus, int row, int column) {

        ComplexJButtonCellData complexJButtonCellData = (ComplexJButtonCellData) complexValue;

        String message = complexJButtonCellData.getMessage();

        setText(message);

        return this;
    }
}
