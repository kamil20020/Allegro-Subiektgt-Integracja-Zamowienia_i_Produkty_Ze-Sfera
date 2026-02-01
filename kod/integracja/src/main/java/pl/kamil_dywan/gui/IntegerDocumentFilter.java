package pl.kamil_dywan.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IntegerDocumentFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {

        if(text == null || text.isBlank()){

            super.insertString(fb, offset, null, attr);
            return;
        }

        if (text.matches("\\d+")) {
            super.insertString(fb, offset, text, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {

        if(text == null || text.isBlank()){

            super.replace(fb, offset, length, null, attrs);
            return;
        }

        if (text.matches("\\d+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
