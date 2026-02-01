package pl.kamil_dywan.gui;

import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedData;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.own.offer.Signature;
import pl.kamil_dywan.service.ProductService;
import pl.kamil_dywan.service.SferaProductService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class ManageOfferSignatureGui extends JDialog {

    private JPanel mainPanel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField contentTitleLabel;
    private JPanel contentPanel;
    private JScrollPane contentScroll;

    private final ProductService productService;
    private final SferaProductService sferaProductService;

    private final ProductOfferResponse offer;

    private String subiektSymbol;
    private Integer quantity;

    public ManageOfferSignatureGui(ProductOfferResponse offer, ProductService productService, SferaProductService sferaProductService) {

        this.offer = offer;
        this.productService = productService;
        this.sferaProductService = sferaProductService;

        initGui();
        initSignatureItems();
        addEvents();
    }

    private void initGui() {

        setTitle("Ustawienie sygnatury oferty w Allegro");
        setPreferredSize(new Dimension(480, 560));
        setContentPane(mainPanel);
        setModal(true);

        contentTitleLabel.setText("Sygnatura oferty " + offer.getId());
        contentTitleLabel.setBorder(null);

        getRootPane().setDefaultButton(buttonOK);
        contentScroll.getVerticalScrollBar().setUnitIncrement(20);
    }

    private void addEvents() {

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        mainPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initSignatureItems() {

        String actualSignature = offer.getExternalIdValue();

        Signature signature = Signature.extract(actualSignature);

        subiektSymbol = signature.subiektSymbol();
        quantity = signature.quantity();

        addSignatureItemGui();
    }

    private void addSignatureItemGui() {

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(0, 0, 12, 0);
        JLabel offerSubiektIdLabel = new JLabel("Symbol w subiekcie: ");
        offerSubiektIdLabel.setPreferredSize(new Dimension(120, 26));
        contentPanel.add(offerSubiektIdLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(0, 22, 12, 0);
        JTextField offerSubiektIdInput = new JTextField(subiektSymbol);
        offerSubiektIdInput.getDocument().addDocumentListener(getOnChangeSubiektSymbolDocumentListener(offerSubiektIdInput));
        offerSubiektIdInput.setPreferredSize(new Dimension(120, 26));
        contentPanel.add(offerSubiektIdInput, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(0, 0, 26, 0);
        JCheckBox quantityCheckbox = new JCheckBox("Czy jest wiele sztuk?");
        quantityCheckbox.setPreferredSize(new Dimension(160, 26));
        contentPanel.add(quantityCheckbox, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(0, 22, 26, 0);
        JTextField quantityInput = new JTextField(quantity != null ? quantity.toString() : null);

        if (quantity == null) {

            quantityInput.setVisible(false);
        } else {
            quantityCheckbox.setSelected(true);
        }

        quantityInput.setPreferredSize(new Dimension(120, 26));
        ((AbstractDocument) quantityInput.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
        quantityInput.getDocument().addDocumentListener(getOnChangeQuantityDocumentListener(quantityInput));

        quantityCheckbox.addItemListener(e -> handleSwitchQuantityCheckbox(quantityInput, e));

        contentPanel.add(quantityInput, constraints);
    }

    private DocumentListener getOnChangeSubiektSymbolDocumentListener(JTextField subiektSymbolField) {

        return new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

                subiektSymbol = subiektSymbolField.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                subiektSymbol = subiektSymbolField.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                subiektSymbol = subiektSymbolField.getText();
            }
        };
    }

    private DocumentListener getOnChangeQuantityDocumentListener(JTextField quantityField) {

        return new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

                onChangeQuantityField(quantityField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                onChangeQuantityField(quantityField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                onChangeQuantityField(quantityField);
            }
        };
    }

    private void onChangeQuantityField(JTextField quantityField) {

        String newValue = quantityField.getText();

        if (newValue == null || newValue.isBlank()) {

            quantity = null;
            return;
        }

        quantity = Integer.valueOf(newValue);
    }

    private void handleSwitchQuantityCheckbox(JTextField quantityInput, ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {

            quantityInput.setVisible(true);
        } else {

            quantityInput.setText(null);
            quantityInput.setVisible(false);
            quantity = null;
        }

        revalidate();
        repaint();
    }

    private void onOK() {

        if (subiektSymbol == null || subiektSymbol.isBlank()) {
            return;
        }

        subiektSymbol = subiektSymbol.replaceAll("\\s", "");

        if (!sferaProductService.existsByCode(subiektSymbol)) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie znaleziono symbolu " + subiektSymbol + " w Subiekcie",
                    "Powiadomienie o błedzie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        Signature signature = new Signature(subiektSymbol, quantity);
        String signatureValue = signature.toString();

        ExternalId externalId = new ExternalId(signatureValue);
        offer.setExternalId(externalId);
        offer.setDoesExistInSubiekt(true);

        if (!saveExternalId(signatureValue)) {
            return;
        }

        closeWindow();
    }

    private boolean saveExternalId(String externalIdValue) {

        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {

            productService.setExternalIdForOffer(offer, externalIdValue);

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Zaktualizowano sygnaturę oferty w Allegro",
                    "Powiadomienie",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się zaktualizować sygnatury oferty w Allegro",
                    "Powiadomienie o błędzie",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }

        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        return true;
    }

    private void onCancel() {

        closeWindow();
    }

    private void closeWindow() {

        dispose();
    }

    public void showDialog() {

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {

        List<ProductOfferProductRelatedData> productSet = new ArrayList<>();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), new ArrayList<>());
        ProductOfferProductRelatedData productOfferProductRelatedData = new ProductOfferProductRelatedData(product);
        productSet.add(productOfferProductRelatedData);

        ProductOfferProduct product1 = new ProductOfferProduct(UUID.randomUUID(), new ArrayList<>());
        ProductOfferProductRelatedData productOfferProductRelatedData1 = new ProductOfferProductRelatedData(product1);
        productSet.add(productOfferProductRelatedData1);

        ProductOfferProduct product2 = new ProductOfferProduct(UUID.randomUUID(), new ArrayList<>());
        ProductOfferProductRelatedData productOfferProductRelatedData2 = new ProductOfferProductRelatedData(product2);
        productSet.add(productOfferProductRelatedData2);

        ProductOfferProduct product3 = new ProductOfferProduct(UUID.randomUUID(), new ArrayList<>());
        ProductOfferProductRelatedData productOfferProductRelatedData3 = new ProductOfferProductRelatedData(product3);
        productSet.add(productOfferProductRelatedData3);

        ExternalId externalId = new ExternalId("12#3 13#4 15 16#2");

        ProductOfferResponse offer = ProductOfferResponse.builder()
                .id(123456L)
                .externalId(externalId)
                .productSet(productSet)
                .build();

        ManageOfferSignatureGui gui = new ManageOfferSignatureGui(offer, null, null);
        gui.showDialog();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(32, 10, 20, 10), -1, -1));
        contentTitleLabel = new JTextField();
        contentTitleLabel.setEditable(false);
        Font contentTitleLabelFont = this.$$$getFont$$$(null, Font.BOLD, 22, contentTitleLabel.getFont());
        if (contentTitleLabelFont != null) contentTitleLabel.setFont(contentTitleLabelFont);
        contentTitleLabel.setText("Ustawienie sygnatury oferty");
        mainPanel.add(contentTitleLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentScroll = new JScrollPane();
        contentScroll.setEnabled(true);
        contentScroll.setInheritsPopupMenu(false);
        contentScroll.setOpaque(true);
        contentScroll.setVisible(true);
        mainPanel.add(contentScroll, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contentScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentScroll.setViewportView(contentPanel);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 20, -1, true, false));
        mainPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setMargin(new Insets(0, 0, 0, 0));
        buttonOK.setText("Zapisz");
        panel1.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 32), new Dimension(100, 32), new Dimension(100, 32), 1, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Anuluj");
        panel1.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 32), new Dimension(100, 32), new Dimension(100, 32), 1, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
