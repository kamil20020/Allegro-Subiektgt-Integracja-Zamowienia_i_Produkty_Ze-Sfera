package pl.kamil_dywan.gui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class ComplexJButtonCellData {

    private String message;
    private final String value;

    public void setMessage(String message) {

        this.message = message;
    }
}
