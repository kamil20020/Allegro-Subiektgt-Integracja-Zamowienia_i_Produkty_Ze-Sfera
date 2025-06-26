package pl.kamil_dywan.gui;

import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.Payment;
import pl.kamil_dywan.external.allegro.generated.buyer.Buyer;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.external.allegro.generated.order.Summary;
import pl.kamil_dywan.service.OrderService;
import pl.kamil_dywan.service.SferaOrderService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrdersGui extends ChangeableGui {

    private JPanel mainPanel;

    private JPanel ordersPanelPlaceholder;
    private PaginationTableGui paginationTableGui;

    private JButton saveOrdersButton;
    private JButton saveDocumentsButton;
    private JButton selectAllButton;
    private JButton unselectAllButton;

    private JComboBox<BooleanSelectOptions> isInvoiceRequiredSelect;
    private JComboBox<BooleanSelectOptions> doesExistInSubiektSelect;
    private JComboBox<BooleanSelectOptions> wasSendDocumentToAllegroSelect;
    private JButton clearFiltersButton;

    private List<Order> ordersPage = new ArrayList<>();

    private final OrderService orderService;
    private final SferaOrderService sferaOrderService;

    private final Runnable handleLogout;

    private static final int SUBIEKT_ID_SENT_COL_INDEX = 1;
    private static final int ALLEGRO_IS_INVOICE_COL_INDEX = 7;
    private static final int ALLEGRO_DOCUMENT_SENT_COL_INDEX = 8;

    private static final String NOT_GIVEN_VALUE = "Brak";

    public OrdersGui(OrderService orderService, SferaOrderService sferaOrderService, Runnable handleLogout) {

        this.orderService = orderService;
        this.sferaOrderService = sferaOrderService;

        this.handleLogout = handleLogout;

        $$$setupUI$$$();

        isInvoiceRequiredSelect.addItemListener(l -> handleIsInvoiceRequiredChange(l.getItem()));
        doesExistInSubiektSelect.addItemListener(l -> handleDoesExistInSubiektChange(l.getItem()));
        wasSendDocumentToAllegroSelect.addItemListener(l -> handleWasSendDocumentToAllegroChange(l.getItem()));

        selectAllButton.addActionListener(e -> selectAll());
        unselectAllButton.addActionListener(e -> unselectAll());
        saveOrdersButton.addActionListener(e -> saveOrders());
        saveDocumentsButton.addActionListener(e -> saveDocuments());
        clearFiltersButton.addActionListener(e -> clearFilters());
    }

    private PaginationTableGui.PaginationTableData loadData(int offset, int limit) {

        OrderResponse orderResponse;

        try {
            orderResponse = orderService.getPage(offset, limit);
        } catch (UnloggedException e) {

            handleLogout.run();

            return null;
        }

        ordersPage = orderResponse.getOrders();

        int totalNumberOfRows = orderResponse.getTotalCount();

        PaginationTableGui.PaginationTableData<Object> data = new PaginationTableGui.PaginationTableData(
                ordersPage,
                totalNumberOfRows
        );

        return data;
    }

    private Object[] convertToRow(Object rawOrder) {

        Order order = (Order) rawOrder;
        Buyer orderBuyer = order.getBuyer();
        Invoice invoice = order.getInvoice();
        List<OrderItem> orderOrderItems = order.getOrderItems();
        Summary orderSummary = order.getSummary();
        Payment orderPayment = order.getPayment();

        String clientName;

        if (invoice.isRequired()) {

            clientName = invoice.getClientName();
        } else {
            clientName = orderBuyer.getFirstName() + " " + orderBuyer.getLastName();
        }

        return new Object[]{
                order.getId().toString(),
                order.getExternalId() != null ? order.getExternalId() : NOT_GIVEN_VALUE,
                orderBuyer.getLogin(),
                clientName,
                String.valueOf(orderOrderItems.size()),
                orderSummary.getTotalToPay().getAmount().toString() + " zł",
                orderPayment.getFinishedAt().toLocalDate().toString(),
                order.hasInvoice() ? BooleanSelectOptions.YES : BooleanSelectOptions.NO,
                order.isHasDocument() ? BooleanSelectOptions.YES : BooleanSelectOptions.NO
        };
    }

    private void handleIsInvoiceRequiredChange(Object option) throws IllegalArgumentException {

        int filterIndex = 0;

        BooleanSelectOptions value = BooleanSelectOptions.getValue(option.toString());

        if (value == BooleanSelectOptions.NOT_GIVEN) {

            paginationTableGui.removeFilter(filterIndex);

            return;
        }

        paginationTableGui
                .addFilter(filterIndex, values -> values[ALLEGRO_IS_INVOICE_COL_INDEX].equals(value));
    }

    private void handleDoesExistInSubiektChange(Object option) throws IllegalArgumentException {

        int filterIndex = 1;

        BooleanSelectOptions value = BooleanSelectOptions.getValue(option.toString());

        if (value == BooleanSelectOptions.NOT_GIVEN) {

            paginationTableGui.removeFilter(filterIndex);

            return;
        }

        Predicate<Object[]> filter;

        if (value == BooleanSelectOptions.YES) {

            filter = values -> {

                Object gotValue = values[SUBIEKT_ID_SENT_COL_INDEX];

                return !Objects.equals(gotValue, NOT_GIVEN_VALUE);
            };
        } else {

            filter = values -> {

                Object gotValue = values[SUBIEKT_ID_SENT_COL_INDEX];

                return Objects.equals(gotValue, NOT_GIVEN_VALUE);
            };
        }

        paginationTableGui.addFilter(filterIndex, filter);
    }

    private void handleWasSendDocumentToAllegroChange(Object option) throws IllegalArgumentException {

        int filterIndex = 2;

        BooleanSelectOptions value = BooleanSelectOptions.getValue(option.toString());

        if (value == BooleanSelectOptions.NOT_GIVEN) {

            paginationTableGui.removeFilter(filterIndex);

            return;
        }

        paginationTableGui
                .addFilter(filterIndex, values -> values[ALLEGRO_DOCUMENT_SENT_COL_INDEX].equals(value));
    }

    private void clearFilters() {

        isInvoiceRequiredSelect.setSelectedIndex(0);
        doesExistInSubiektSelect.setSelectedIndex(0);
        wasSendDocumentToAllegroSelect.setSelectedIndex(0);

        paginationTableGui.clearFilters();
    }

    private void selectAll() {

        paginationTableGui.selectAll();
    }

    private void unselectAll() {

        paginationTableGui.unselectAll();
    }

    private List<Order> getSelectedOrders() {

        List<Object[]> selectedOrdersData = paginationTableGui.getSelectedData();

        if (selectedOrdersData.isEmpty()) {

            return new ArrayList<>();
        }

        List<String> selectedOrdersIds = selectedOrdersData.stream()
                .map(selectedOrderData -> selectedOrderData[0].toString())
                .collect(Collectors.toList());

        return ordersPage.stream()
                .filter(order -> selectedOrdersIds.contains(order.getId().toString()))
                .collect(Collectors.toList());

    }

    private void updateTableRowCol(int rowIndex, int colIndex, String orderId, Object newValue) {

        paginationTableGui.updateRowCol(rowIndex, colIndex, orderId, newValue);
    }

    private void saveOrders() {

        List<Order> selectedOrders = getSelectedOrders();

        int[] selectedRowsIndices = paginationTableGui.getSelectedRowIndices();

        if (selectedOrders.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie wybrano zamówień do zapisania do Subiekta",
                    "Powiadomienie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new Thread(() -> {

            Map<String, String> errors = new LinkedHashMap<>();

            int numberOfSavedOrders = sferaOrderService.create(selectedOrders, errors);

            SwingUtilities.invokeLater(() -> {

                for (int i = 0; i < selectedOrders.size(); i++) {

                    Order selectedOrder = selectedOrders.get(i);

                    int rowIndex = selectedRowsIndices[i];

                    if (selectedOrder.getExternalId() == null) {
                        continue;
                    }

                    String orderId = selectedOrder.getId().toString();

                    updateTableRowCol(rowIndex, SUBIEKT_ID_SENT_COL_INDEX, orderId, selectedOrder.getExternalId());
                }

                mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JOptionPane.showMessageDialog(
                    mainPanel,
                    "Zapisano " + numberOfSavedOrders + " zamówień w Subiekcie",
                    "Powiadomienie",
                    JOptionPane.INFORMATION_MESSAGE
                );

                displayOrdersErrors(errors, "Powiadomienia o błędach przy tworzeniu obiektów w Subiekcie");
            });

        }).start();
    }

    private void saveDocuments() {

        List<Order> selectedOrders = getSelectedOrders();

        int[] selectedRowsIndices = paginationTableGui.getSelectedRowIndices();

        if (selectedOrders.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie wybrano zamówień, dla których mają być wysłane dokumenty do Allegro",
                    "Powiadomienie",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new Thread(() -> {

            Map<String, String> errors = Collections.synchronizedMap(new LinkedHashMap<>());

            List<Integer> savedOrdersDocumentsIndices;

            try {

                savedOrdersDocumentsIndices = orderService.uploadDocuments(selectedOrders, errors);
            }
            catch (UnloggedException e) {

                handleLogout.run();

                return;
            }

            SwingUtilities.invokeLater(() -> {

                for (int savedOrderDocumentIndex : savedOrdersDocumentsIndices) {

                    Order order = ordersPage.get(savedOrderDocumentIndex);

                    String orderId = order.getId().toString();

                    int rowIndex = selectedRowsIndices[savedOrderDocumentIndex];

                    updateTableRowCol(rowIndex, ALLEGRO_DOCUMENT_SENT_COL_INDEX, orderId, BooleanSelectOptions.YES.toString());
                }

                mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Zapisano " + savedOrdersDocumentsIndices.size() + " dokumenty sprzedaży w Allegro",
                        "Powiadomienie",
                        JOptionPane.INFORMATION_MESSAGE
                );

                displayOrdersErrors(errors, "Powiadomienia o błędach przy wysyłaniu dokumentów potwierdzenia sprzedaży do Allegro");
            });

        }).start();
    }

    private void displayOrdersErrors(Map<String, String> errors, String dialogTitle) {

        if (errors.isEmpty()) {
            return;
        }

        GridBagLayout layout = new GridBagLayout();

        JPanel panel = new JPanel(layout);

        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        int row = 0;

        for (Map.Entry<String, String> error : errors.entrySet()) {

            String orderIdStr = error.getKey();
            String errorMessage = error.getValue();

            JLabel orderIdLabel = new JLabel(orderIdStr);
            JLabel orderErrorMessageLabel = new JLabel(errorMessage);

            int weightY = 0;

            if (row == errors.size() - 1) {

                weightY = 1;
            }

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.weighty = weightY;
            panel.add(orderIdLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 1;
            gbc.weighty = weightY;
            orderErrorMessageLabel.setHorizontalAlignment(SwingConstants.LEFT);
            panel.add(orderErrorMessageLabel, gbc);

            row++;
        }

        JScrollPane scrollPane = new JScrollPane(panel);

        JDialog dialog = new JDialog((Frame) null, dialogTitle, false);

        dialog.setSize(680, 400);
        dialog.setLocationRelativeTo(panel);

        dialog.add(scrollPane);

        dialog.setVisible(true);
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

        isInvoiceRequiredSelect = new JComboBox<>(BooleanSelectOptions.values());
        doesExistInSubiektSelect = new JComboBox<>(BooleanSelectOptions.values());
        wasSendDocumentToAllegroSelect = new JComboBox<>(BooleanSelectOptions.values());

        String[] tableHeaders = {"Allegro Id", "Subiekt Id", "Login kupującego", "Kupujący", "Liczba pozycji", "Kwota brutto", "Data", "Wybrano fakturę", "Wysłano dokument"};

        paginationTableGui = new PaginationTableGui(tableHeaders, this::loadData, this::convertToRow);

        ordersPanelPlaceholder = paginationTableGui.getMainPanel();
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
        mainPanel.setAutoscrolls(false);
        mainPanel.setMinimumSize(new Dimension(478, 138));
        mainPanel.setOpaque(true);
        mainPanel.setPreferredSize(new Dimension(1920, 980));
        mainPanel.setRequestFocusEnabled(true);
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(18, 50, 40, 50), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setAlignmentX(0.0f);
        Font label1Font = this.$$$getFont$$$(null, -1, 26, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Zamówienia");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(label1, gbc);
        ordersPanelPlaceholder.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(ordersPanelPlaceholder, gbc);
        ordersPanelPlaceholder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setBorderPainted(false);
        toolBar1.setFloatable(false);
        toolBar1.setOpaque(false);
        toolBar1.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(toolBar1, gbc);
        selectAllButton = new JButton();
        selectAllButton.setText("Zaznacz wszystkie dane");
        toolBar1.add(selectAllButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator1);
        unselectAllButton = new JButton();
        unselectAllButton.setText("Odznacz wszystkie dane");
        toolBar1.add(unselectAllButton);
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator2);
        saveOrdersButton = new JButton();
        saveOrdersButton.setText("Zapisz zamówienia w Subiekcie");
        toolBar1.add(saveOrdersButton);
        final JToolBar.Separator toolBar$Separator3 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator3);
        saveDocumentsButton = new JButton();
        saveDocumentsButton.setText("Zapisz dokumenty sprzedaży w Allegro");
        toolBar1.add(saveDocumentsButton);
        final JToolBar toolBar2 = new JToolBar();
        toolBar2.setBorderPainted(false);
        toolBar2.setEnabled(false);
        toolBar2.setFloatable(false);
        toolBar2.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(toolBar2, gbc);
        toolBar2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        label2.setText("Wymagana faktura");
        toolBar2.add(label2);
        final JToolBar.Separator toolBar$Separator4 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator4);
        isInvoiceRequiredSelect.setMinimumSize(new Dimension(100, 30));
        isInvoiceRequiredSelect.setName("aa");
        isInvoiceRequiredSelect.setToolTipText("");
        toolBar2.add(isInvoiceRequiredSelect);
        final JToolBar.Separator toolBar$Separator5 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator5);
        final JToolBar.Separator toolBar$Separator6 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator6);
        final JToolBar.Separator toolBar$Separator7 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator7);
        final JLabel label3 = new JLabel();
        label3.setText("Istnieje w Subiekcie");
        toolBar2.add(label3);
        final JToolBar.Separator toolBar$Separator8 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator8);
        doesExistInSubiektSelect.setMinimumSize(new Dimension(100, 30));
        doesExistInSubiektSelect.setName("aa");
        doesExistInSubiektSelect.setToolTipText("");
        toolBar2.add(doesExistInSubiektSelect);
        final JToolBar.Separator toolBar$Separator9 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator9);
        final JToolBar.Separator toolBar$Separator10 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator10);
        final JToolBar.Separator toolBar$Separator11 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator11);
        final JLabel label4 = new JLabel();
        label4.setText("Wysłano dokument do Allegro");
        toolBar2.add(label4);
        final JToolBar.Separator toolBar$Separator12 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator12);
        wasSendDocumentToAllegroSelect.setMinimumSize(new Dimension(100, 30));
        wasSendDocumentToAllegroSelect.setName("aa");
        wasSendDocumentToAllegroSelect.setToolTipText("");
        toolBar2.add(wasSendDocumentToAllegroSelect);
        final JToolBar.Separator toolBar$Separator13 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator13);
        final JToolBar.Separator toolBar$Separator14 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator14);
        final JToolBar.Separator toolBar$Separator15 = new JToolBar.Separator();
        toolBar2.add(toolBar$Separator15);
        clearFiltersButton = new JButton();
        clearFiltersButton.setText("Wyczyść filtrowanie");
        toolBar2.add(clearFiltersButton);
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
    private Font $$$getFont1$$$(String fontName, int style, int size, Font currentFont) {
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
