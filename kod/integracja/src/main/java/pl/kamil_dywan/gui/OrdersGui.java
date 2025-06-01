package pl.kamil_dywan.gui;

import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.Payment;
import pl.kamil_dywan.external.allegro.generated.buyer.Buyer;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.external.allegro.generated.order.Summary;
import pl.kamil_dywan.service.InvoiceService;
import pl.kamil_dywan.service.OrderService;
import pl.kamil_dywan.service.ReceiptService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class OrdersGui implements ChangeableGui {

    private JPanel mainPanel;

    private JPanel ordersPanelPlaceholder;
    private PaginationTableGui paginationTableGui;

    private JButton saveInvoicesButton;
    private JButton saveReceiptsButton;

    private final List<Order> ordersWithInvoices = new ArrayList<>();
    private final List<Order> ordersWithReceipts = new ArrayList<>();

    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final ReceiptService receiptService;

    private final Runnable handleLogout;

    public OrdersGui(OrderService orderService, InvoiceService invoiceService, ReceiptService receiptService, Runnable handleLogout) {

        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.receiptService = receiptService;
        this.handleLogout = handleLogout;

        $$$setupUI$$$();

        saveInvoicesButton.addActionListener(e -> saveInvoicesToFile());
        saveReceiptsButton.addActionListener(e -> saveReceiptsToFile());
    }

    private PaginationTableGui.PaginationTableData loadData(int offset, int limit) {

        OrderResponse orderResponse;

        try {
            orderResponse = orderService.getPage(offset, limit);
        }
        catch (UnloggedException e) {

            handleLogout.run();

            return null;
        }

        ordersWithInvoices.clear();
        ordersWithReceipts.clear();

        List<Order> allegroOrders = orderResponse.getOrders();

        allegroOrders
            .forEach(allegroOrder -> {

                Invoice allegroInvoice = allegroOrder.getInvoice();

                if (allegroInvoice.isRequired()) {
                    ordersWithInvoices.add(allegroOrder);
                } else {
                    ordersWithReceipts.add(allegroOrder);
                }
            });

        int totalNumberOfRows = orderResponse.getTotalCount();

        PaginationTableGui.PaginationTableData data = new PaginationTableGui.PaginationTableData(
            allegroOrders,
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
            clientName,
            String.valueOf(orderOrderItems.size()),
            orderSummary.getTotalToPay().getAmount().toString() + " zł",
            orderPayment.getFinishedAt().toLocalDate().toString(),
            order.hasInvoice() ? "Tak" : "Nie",
            new ComplexJButtonCellData(
                order.hasDocument() ? "Wysłano" : ("Wyślij " + (order.hasInvoice() ? "fakturę" : "paragon")),
                order.getId().toString()
            )
        };
    }

    private void saveInvoicesToFile() {

        String savedFilePath = FileDialogHandler.getSaveFileDialogSelectedPath(
                "Zapisywanie faktur do pliku",
                "faktury",
                ".xml"
        );

        if (savedFilePath.isBlank()) {
            return;
        }

        try {
            invoiceService.writeInvoicesToFile(ordersWithInvoices, savedFilePath);
        } catch (IllegalStateException e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się zapisać faktur do pliku",
                    "Powiadomienie o błędzie",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                mainPanel,
                "Zapisano faktury do pliku " + savedFilePath,
                "Powiadomienie",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void saveReceiptsToFile() {

        String savedFilePath = FileDialogHandler.getSaveFileDialogSelectedPath(
                "Zapisywanie paragonów do pliku",
                "paragony",
                ".epp"
        );

        if (savedFilePath.isBlank()) {
            return;
        }

        try {

            receiptService.writeReceiptsToFile(ordersWithReceipts, savedFilePath);
        } catch (IllegalStateException e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się zapisać paragonów do pliku",
                    "Powiadomienie o błędzie",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                mainPanel,
                "Zapisano paragony do pliku " + savedFilePath,
                "Powiadomienie",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private boolean handleSaveDocumentInAllegro(String orderId) {

        Optional<File> gotFileOpt = FileDialogHandler.getLoadFileDialogSelectedPath(
                "Wczytywanie pliku z dokumentem dla Allegro",
                "*.pdf"
        );

        if (gotFileOpt.isEmpty()) {

            return false;
        }

        try {

            orderService.uploadDocument(orderId, gotFileOpt.get());
        }
        catch (IOException e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się wczytać dokumentu",
                    "Powiadomienie o błędzie",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        } catch (IllegalStateException e) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nie udało się dodać dokumentu do zamówienia w Allegro",
                    "Powiadomienie o błędzie",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        } catch (UnloggedException e) {

            handleLogout.run();

            return false;
        }

        JOptionPane.showMessageDialog(
                mainPanel,
                "Dodano dokument do zamówienia w Allegro",
                "Powiadomienie",
                JOptionPane.INFORMATION_MESSAGE
        );

        return true;
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

        String[] tableHeaders = {"Identyfikator", "Kupujący", "Liczba ofert", "Kwota brutto", "Data", "Czy wybrano fakturę", "Allegro"};

        paginationTableGui = new PaginationTableGui(tableHeaders, this::loadData, this::convertToRow, this::handleSaveDocumentInAllegro);

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
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(16, 50, 40, 50), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
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
        saveInvoicesButton = new JButton();
        saveInvoicesButton.setText("Zapisz faktury do pliku");
        toolBar1.add(saveInvoicesButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator1);
        saveReceiptsButton = new JButton();
        saveReceiptsButton.setText("Zapisz paragony do pliku");
        toolBar1.add(saveReceiptsButton);
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
