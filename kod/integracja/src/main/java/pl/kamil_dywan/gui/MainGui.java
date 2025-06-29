package pl.kamil_dywan.gui;

import pl.kamil_dywan.service.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Optional;

public class MainGui {

    private JPanel mainPanel;

    private LoggedUserContent loggedUserContent;
    private final LoginGui loginGui;

    private GridBagConstraints mainPanelLayoutConstrains;

    private final AuthService authService;
    private final BasicInfoService basicInfoService;

    private final JFrame frame;

    public MainGui(AuthService authService, ProductService productService, SferaProductService sferaProductService, OrderService orderService, SferaOrderService sferaOrderService, BasicInfoService basicInfoService) {

        this.authService = authService;
        this.basicInfoService = basicInfoService;

        frame = new JFrame("Integracja Allegro i Subiekt GT");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setType(JFrame.Type.UTILITY);
        frame.setSize(1200, 820);
        frame.setLocationRelativeTo(null);

        loadMainPanelLayoutConstraints();

        loginGui = new LoginGui(authService, this::handleSuccessAuth);
        loggedUserContent = new LoggedUserContent(frame, productService, sferaProductService, orderService, sferaOrderService, basicInfoService, this::handleLogout);

        handleAuth();

        frame.add(mainPanel);

        frame.setVisible(true);
    }

    private void loadMainPanelLayoutConstraints() {

        mainPanelLayoutConstrains = new GridBagConstraints();

        mainPanelLayoutConstrains.gridx = 0;
        mainPanelLayoutConstrains.gridy = 0;
        mainPanelLayoutConstrains.fill = GridBagConstraints.BOTH;
        mainPanelLayoutConstrains.weightx = 1;
        mainPanelLayoutConstrains.weighty = 1;
    }

    private void handleAuth() {

        if (authService.doesUserPassedFirstLoginToApp() && authService.isUserLogged()) {

            handleSuccessAuth();
        } else {

            changeMainPanelContent(loginGui);
        }
    }

    private void handleSuccessAuth() {

        changeMainPanelContent(loggedUserContent);
    }

    private void changeMainPanelContent(ChangeableGui changeableGui) {

        changeableGui.load();

        mainPanel.removeAll();
        mainPanel.add(changeableGui.getMainPanel(), mainPanelLayoutConstrains);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void handleLogout() {

        frame.setJMenuBar(new JMenuBar());

        authService.logout();

        loginGui.handleLogout();
        changeMainPanelContent(loginGui);
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
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
