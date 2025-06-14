package pl.kamil_dywan.gui;

import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProduct;
import pl.kamil_dywan.api.allegro.response.OfferProductResponse;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.subiektgt.own.product.ProductType;
import pl.kamil_dywan.service.ProductService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProductsGui extends ChangeableGui {

    private JPanel mainPanel;

    private JPanel productsPanelPlaceholder;
    private PaginationTableGui paginationTableGui;

    private JButton exportButton;
    private JButton deliveryButton;
    private JButton setExternalButton;

    private JTextField searchFieldInput;
    private JButton searchButton;

    private List<ProductOfferResponse> products;

    private final ProductService productService;
    private final Runnable handleLogout;

    public ProductsGui(ProductService productService, Runnable handleLogout) {

        this.productService = productService;
        this.handleLogout = handleLogout;

        $$$setupUI$$$();

        searchButton.addActionListener(e -> handleSearch());

        exportButton.addActionListener(e -> saveProductsToFile());

        deliveryButton.addActionListener(e -> saveDeliveryToFile());

        setExternalButton.addActionListener(e -> setExternal());
    }

    private PaginationTableGui.PaginationTableData loadProductsPage(int offset, int limit) {

        String search = searchFieldInput.getText();

        OfferProductResponse generalProductsPage;

        try {
            generalProductsPage = productService.getGeneralProductsPage(search, offset, limit);
        } catch (UnloggedException e) {

            handleLogout.run();

            return null;
        }

        List<OfferProduct> gotGeneralProducts = generalProductsPage.getOffersProducts();

        List<Long> productsIds = gotGeneralProducts.stream()
                .map(offerProduct -> offerProduct.getId())
                .collect(Collectors.toList());

        products = productService.getDetailedProductsByIds(productsIds);

        int totalNumberOfRows = generalProductsPage.getTotalCount();

        PaginationTableGui.PaginationTableData<Object> tableData = new PaginationTableGui.PaginationTableData(
                products,
                totalNumberOfRows
        );

        return tableData;
    }

    private Object[] convertProductToRow(Object rawProductOffer) {

        ProductOfferResponse productOfferResponse = (ProductOfferResponse) rawProductOffer;

        String externalIdValue = productOfferResponse.getExternalIdValue();

        String producerCode = null;
        String ean = null;

        if (externalIdValue != null) {

            ExternalId externalId = productOfferResponse.getExternalId();

            producerCode = externalId.getProducerCode();
            ean = externalId.getEanCode();
        }

        return new Object[]{
                productOfferResponse.getId(),
                producerCode != null ? producerCode : "Brak",
                ean != null ? ean : "Brak",
                productOfferResponse.getSubiektId() != null ? productOfferResponse.getSubiektId() : "Brak",
                productOfferResponse.getName(),
                productOfferResponse.getPriceWithoutTax().toString() + " zł",
                productOfferResponse.getPriceWithTax() + " zł",
                productOfferResponse.getTaxRate().toString() + '%',
                productOfferResponse.getCreatedAt().toLocalDate().toString()
        };
    }

    private void handleChangeSearchFieldInput(String newValue) {


    }

    private void handleSearch() {

        paginationTableGui.handleLoadTableExceptions();
    }

    private void saveDeliveryToFile() {

        String savedFilePath = FileDialogHandler.getSaveFileDialogSelectedPath(
                "Zapisywanie dostawy do pliku",
                "dostawa",
                ".epp"
        );

        if (savedFilePath.isBlank()) {
            return;
        }

        try {

            productService.writeDeliveryToFile(savedFilePath);
        } catch (IllegalStateException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się zapisać dostawy do pliku",
                    "Powiadomienie o błędzie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                mainPanel,
                "Zapisano dostawę do pliku " + savedFilePath,
                "Powiadomienie",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void saveProductsToFile() {

        if (products == null) {

            JOptionPane.showMessageDialog(mainPanel, "Brak produktów do zapisania", "Błąd", JOptionPane.ERROR_MESSAGE);

            return;
        }

        String savedFilePath = FileDialogHandler.getSaveFileDialogSelectedPath(
                "Zapisywanie produktów do pliku",
                "produkty",
                ".epp"
        );

        if (savedFilePath.isBlank()) {
            return;
        }

        try {

            productService.writeProductsToFile(products, savedFilePath, ProductType.GOODS);
        } catch (IllegalStateException e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się zapisać produktów do pliku",
                    "Powiadomienie o błędzie",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                mainPanel,
                "Zapisano produkty do pliku " + savedFilePath,
                "Powiadomienie",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void setExternal() {

        if (products == null) {

            JOptionPane.showMessageDialog(mainPanel, "Brak produktów do zaktualizowania w Allegro", "Błąd", JOptionPane.ERROR_MESSAGE);

            return;
        }

        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {

            productService.setExternalIdForAllOffers(products);

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Zaktualizowano produkty w Allegro",
                    "Powiadomienie",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się zaktualizować produktów w Allegro",
                    "Powiadomienie o błędzie",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void load() {

        if (isLoaded()) {
            return;
        }

        super.load();

        paginationTableGui.handleLoadTableExceptions();
    }

    public JPanel getMainPanel() {

        return mainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        String[] columnsHeaders = {"Allegro Id", "Kod producenta", "EAN (GTIN)", "Subiekt Id", "Nazwa", "Cena netto", "Cena brutto", "Podatek", "Data dodania"};

        paginationTableGui = new PaginationTableGui(columnsHeaders, this::loadProductsPage, this::convertProductToRow);

        productsPanelPlaceholder = paginationTableGui.getMainPanel();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setAlignmentX(0.0f);
        mainPanel.setAlignmentY(0.0f);
        mainPanel.setMinimumSize(new Dimension(478, 138));
        mainPanel.setOpaque(true);
        mainPanel.setPreferredSize(new Dimension(1920, 980));
        mainPanel.setRequestFocusEnabled(true);
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(18, 50, 40, 50), "            ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 26, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Produkty");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(label1, gbc);
        productsPanelPlaceholder.setFocusable(false);
        productsPanelPlaceholder.setOpaque(false);
        productsPanelPlaceholder.setRequestFocusEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(productsPanelPlaceholder, gbc);
        productsPanelPlaceholder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setBorderPainted(false);
        toolBar1.setFloatable(false);
        toolBar1.setOpaque(false);
        toolBar1.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(toolBar1, gbc);
        exportButton = new JButton();
        exportButton.setMaximumSize(new Dimension(180, 30));
        exportButton.setMinimumSize(new Dimension(180, 30));
        exportButton.setOpaque(true);
        exportButton.setPreferredSize(new Dimension(180, 30));
        exportButton.setText("Zapisz produkty do pliku");
        exportButton.setVisible(true);
        toolBar1.add(exportButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator1);
        deliveryButton = new JButton();
        deliveryButton.setMaximumSize(new Dimension(180, 30));
        deliveryButton.setMinimumSize(new Dimension(180, 30));
        deliveryButton.setPreferredSize(new Dimension(180, 30));
        deliveryButton.setText("Zapisz dostawę do pliku");
        toolBar1.add(deliveryButton);
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator2);
        setExternalButton = new JButton();
        setExternalButton.setMaximumSize(new Dimension(180, 30));
        setExternalButton.setMinimumSize(new Dimension(180, 30));
        setExternalButton.setPreferredSize(new Dimension(180, 30));
        setExternalButton.setText("Zaktualizuj zewnętrzne id");
        toolBar1.add(setExternalButton);
        final JToolBar toolBar2 = new JToolBar();
        toolBar2.setFloatable(false);
        toolBar2.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(toolBar2, gbc);
        toolBar2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(32, 0, 20, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        searchFieldInput = new JTextField();
        searchFieldInput.setAutoscrolls(true);
        searchFieldInput.setMinimumSize(new Dimension(100, 30));
        searchFieldInput.setPreferredSize(new Dimension(100, 30));
        searchFieldInput.setToolTipText("Tytuł oferty");
        toolBar2.add(searchFieldInput);
        searchButton = new JButton();
        searchButton.setText("Wyszukaj");
        toolBar2.add(searchButton);
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

    /**
     * @noinspection ALL
     */
    private Font $$$getFont2$$$(String fontName, int style, int size, Font currentFont) {
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

}
