package pl.kamil_dywan.gui;

import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProduct;
import pl.kamil_dywan.api.allegro.response.OfferProductResponse;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.subiektgt.own.product.ProductType;
import pl.kamil_dywan.service.ProductService;
import pl.kamil_dywan.service.SferaProductService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductsGui extends ChangeableGui {

    private JPanel mainPanel;

    private JPanel productsPanelPlaceholder;
    private PaginationTableGui paginationTableGui;

    private JButton exportProductsButton;
    private JButton deliveryButton;

    private JTextField searchFieldInput;
    private JButton searchButton;
    private List<ProductOfferResponse> products;

    private final ProductService productService;
    private final SferaProductService sferaProductService;
    private final Runnable handleLogout;

    private static final int ALLEGRO_PRODUCT_OFFER_ID_COLUMN_INDEX = 0;
    private static final int ALLEGRO_SIGNATURE_COLUMN_INDEX = 1;
    private static final int DOES_EXIST_SYMBOL_IN_SUBIEKT_INDEX = 2;

    public ProductsGui(ProductService productService, SferaProductService sferaProductService, Runnable handleLogout) {

        this.productService = productService;
        this.sferaProductService = sferaProductService;
        this.handleLogout = handleLogout;

        $$$setupUI$$$();

        searchButton.addActionListener(e -> handleSearch());

        exportProductsButton.addActionListener(e -> saveProductsToFile());

        deliveryButton.addActionListener(e -> saveDeliveryToFile());
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

        return new Object[]{
                productOfferResponse.getId(),
                externalIdValue != null ? externalIdValue : "Brak",
                productOfferResponse.isDoesExistInSubiekt() ? BooleanSelectOptions.YES : BooleanSelectOptions.NO,
                productOfferResponse.hasManyProducts() ? BooleanSelectOptions.YES : BooleanSelectOptions.NO,
                productOfferResponse.getName(),
                productOfferResponse.getPriceWithoutTax().toString() + " zł",
                productOfferResponse.getPriceWithTax() + " zł",
                productOfferResponse.getTaxRate().toString() + '%',
                productOfferResponse.getCreatedAt().toLocalDate().toString()
        };
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

        if (paginationTableGui.isLoading()) {
            return;
        }

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

    private void handleClickOnOffer(String allegroProductOfferId, MouseEvent mouseEvent) {

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem openAllegroOfferPopupMenuItem = new JMenuItem(new AbstractAction("Otworzenie oferty w Allegro") {
            @Override
            public void actionPerformed(ActionEvent e) {
                productService.redirectToOffer(allegroProductOfferId);
            }
        });
        popupMenu.add(openAllegroOfferPopupMenuItem);

        JMenuItem addSignaturePopupMenuItem = new JMenuItem(new AbstractAction("Ustawienie sygnatury oferty w Allegro") {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleManageOfferSignature(allegroProductOfferId);
            }
        });
        popupMenu.add(addSignaturePopupMenuItem);

        popupMenu.show(paginationTableGui.getMainPanel(), mouseEvent.getX(), mouseEvent.getY() + 40);
    }

    private void handleManageOfferSignature(String offerId) {

        Optional<ProductOfferResponse> selectedOfferOpt = products.stream()
                .filter(offer -> Objects.equals(offer.getId().toString(), offerId))
                .findFirst();

        if (selectedOfferOpt.isEmpty()) {
            return;
        }

        ProductOfferResponse selectedOffer = selectedOfferOpt.get();

        ManageOfferSignatureGui dialog = new ManageOfferSignatureGui(selectedOffer, productService, sferaProductService);
        dialog.showDialog();

        if (selectedOffer.getExternalId() == null) {

            return;
        }

        paginationTableGui.updateRowCol(ALLEGRO_SIGNATURE_COLUMN_INDEX, offerId, selectedOffer.getExternalIdValue());
        paginationTableGui.updateRowCol(DOES_EXIST_SYMBOL_IN_SUBIEKT_INDEX, offerId, selectedOffer.isDoesExistInSubiekt() ? BooleanSelectOptions.YES : BooleanSelectOptions.NO);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        String[] columnsHeaders = {"Allegro Id", "Sygnatura (Allegro)", "Istnieje w Subiekcie", "Zestaw", "Nazwa", "Cena netto", "Cena brutto", "Podatek", "Data dodania"};

        paginationTableGui = new PaginationTableGui(columnsHeaders, this::loadProductsPage, this::convertProductToRow);

        paginationTableGui.setOnRightClickRow(ALLEGRO_PRODUCT_OFFER_ID_COLUMN_INDEX, this::handleClickOnOffer);

        productsPanelPlaceholder = paginationTableGui.getMainPanel();
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
        label1.setText("Oferty z Allegro");
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
        exportProductsButton = new JButton();
        exportProductsButton.setText("Zapisz produkty do pliku");
        toolBar1.add(exportProductsButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator1);
        deliveryButton = new JButton();
        deliveryButton.setMaximumSize(new Dimension(180, 30));
        deliveryButton.setMinimumSize(new Dimension(180, 30));
        deliveryButton.setPreferredSize(new Dimension(180, 30));
        deliveryButton.setText("Zapisz dostawę do pliku");
        toolBar1.add(deliveryButton);
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
}
