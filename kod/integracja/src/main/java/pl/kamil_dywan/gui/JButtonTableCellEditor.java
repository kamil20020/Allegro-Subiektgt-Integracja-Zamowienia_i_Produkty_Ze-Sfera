package pl.kamil_dywan.gui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class JButtonTableCellEditor extends DefaultCellEditor {

    private ComplexJButtonCellData complexValue;

    private final JButton jButton;

    public JButtonTableCellEditor(JCheckBox checkBox, Predicate<String> action, String messageAfterClick) {
        super(checkBox);

        jButton = new JButton();
        jButton.addActionListener(e -> {

            String value = complexValue.getValue();

            if(action.test(value)){

                complexValue.setMessage(messageAfterClick);
            }

            fireEditingStopped();
        });
    }

    @Override
    public Object getCellEditorValue() {

        return complexValue;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object complexValue, boolean isSelected, int row, int column) throws IllegalArgumentException{

        if(!(complexValue instanceof ComplexJButtonCellData complexJButtonCellData)){

           throw new IllegalArgumentException("Table cell editor got something other than complex jbutton cell data");
        }

        this.complexValue = complexJButtonCellData;

        String message = complexJButtonCellData.getMessage();

        jButton.setText(message);

        return jButton;
    }

}
