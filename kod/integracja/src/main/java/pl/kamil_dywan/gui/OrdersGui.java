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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrdersGui implements ChangeableGui {

    private JPanel mainPanel;

    private JPanel ordersPanelPlaceholder;
    private PaginationTableGui paginationTableGui;

    private JButton saveOrdersButton;
    private JButton saveDocumentsButton;
    private JButton selectAllButton;
    private JButton unselectAllButton;

    private List<Order> ordersPage = new ArrayList<>();

    private final OrderService orderService;
    private final SferaOrderService sferaOrderService;

    private final Runnable handleLogout;

    private static final int SUBIEKT_ID_SENT_COL_INDEX = 1;
    private static final int ALLEGRO_DOCUMENT_SENT_COL_INDEX = 7;

    public OrdersGui(OrderService orderService, SferaOrderService sferaOrderService, Runnable handleLogout) {

        this.orderService = orderService;
        this.sferaOrderService = sferaOrderService;

        this.handleLogout = handleLogout;

        $$$setupUI$$$();

        selectAllButton.addActionListener(e -> selectAll());
        unselectAllButton.addActionListener(e -> unselectAll());
        saveOrdersButton.addActionListener(e -> saveOrders());
        saveDocumentsButton.addActionListener(e -> saveDocuments());
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

        PaginationTableGui.PaginationTableData data = new PaginationTableGui.PaginationTableData(
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
                order.getExternalId() != null ? order.getExternalId() : "Brak",
                clientName,
                String.valueOf(orderOrderItems.size()),
                orderSummary.getTotalToPay().getAmount().toString() + " zł",
                orderPayment.getFinishedAt().toLocalDate().toString(),
                order.hasInvoice() ? "Tak" : "Nie",
                (order.hasDocument() ? "" : "Nie ") + "Wysłano"
        };
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

    private void updateTableRowCol(int rowIndex, int colIndex, Object newValue) {

        paginationTableGui.updateRowCol(rowIndex, colIndex, newValue);
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

            int numberOfSavedOrders = sferaOrderService.create(selectedOrders);

            SwingUtilities.invokeLater(() -> {

                for (int i = 0; i < selectedOrders.size(); i++) {

                    Order selectedOrder = selectedOrders.get(i);

                    int rowIndex = selectedRowsIndices[i];

                    if (selectedOrder.getExternalId() == null) {
                        continue;
                    }

                    updateTableRowCol(rowIndex, SUBIEKT_ID_SENT_COL_INDEX, selectedOrder.getExternalId());
                }

                mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Zapisano " + numberOfSavedOrders + " zamówień w Subiekcie",
                        "Powiadomienie",
                        JOptionPane.INFORMATION_MESSAGE
                );
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

            List<Integer> savedOrdersDocumentsIndices;

            try {

                savedOrdersDocumentsIndices = orderService.uploadDocuments(selectedOrders);
            } catch (UnloggedException e) {

                handleLogout.run();

                return;
            }

            SwingUtilities.invokeLater(() -> {

                for (int i = 0; i < savedOrdersDocumentsIndices.size(); i++) {

                    int savedOrderDocumentIndex = savedOrdersDocumentsIndices.get(i);
                    int rowIndex = selectedRowsIndices[savedOrderDocumentIndex];

                    updateTableRowCol(rowIndex, ALLEGRO_DOCUMENT_SENT_COL_INDEX, "Wysłano");
                }

                mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Zapisano " + savedOrdersDocumentsIndices.size() + " dokumenty sprzedaży w Allegro",
                        "Powiadomienie",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });

        }).start();
    }

    @Override
    public void load() {

        paginationTableGui.handleLoadTableExceptions();
    }

    public JPanel getMainPanel() {

        return mainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        String[] tableHeaders = {"Allegro Id", "Subiekt Id", "Kupujący", "Liczba pozycji", "Kwota brutto", "Data", "Wybrano fakturę", "Allegro dokument"};

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
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(label1, gbc);
        ordersPanelPlaceholder.setOpaque(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(ordersPanelPlaceholder, gbc);
        ordersPanelPlaceholder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        toolBar1.setOpaque(false);
        toolBar1.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
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
