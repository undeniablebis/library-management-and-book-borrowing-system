package co.bisri.librarysystem.admin.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Custom content panel
    private final JPanel jpnlContentPane;

    // Sidebar panel
    private JPanel jpnlSidebar;

    // Main content panel
    private final JPanel jpnlMainContentPanel;

    // All management panels
    private ManagementPanel[] managementPanels;

    public MainFrame() {
        setForeground(SystemColor.activeCaptionText);
        /* Frame Properties */
        setTitle("Library Management Administrator");
        setMinimumSize(new Dimension(1000, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* END OF Frame Properties */

        /* jpnlContentPane - custom content pane for this frame */
        jpnlContentPane = new JPanel();
        jpnlContentPane.setBorder(null);
        setContentPane(jpnlContentPane);
        jpnlContentPane.setLayout(new BoxLayout(jpnlContentPane, BoxLayout.X_AXIS));
        /* END OF jpnlContentPane */

        /*
         * jpnlSidebar - sidebar panel shown on the left, uses BoxLayout to lay its
         * components
         */
        jpnlSidebar = new JPanel();
        jpnlSidebar.setBorder(null);
        jpnlSidebar.setBackground(Color.BLACK);
        jpnlSidebar.setMaximumSize(new Dimension(225, 32767));
        jpnlSidebar.setMinimumSize(new Dimension(225, 10));
        jpnlSidebar.setLayout(new BoxLayout(jpnlSidebar, BoxLayout.Y_AXIS));
        jpnlContentPane.add(jpnlSidebar);
        /* END OF jpnlSidebar */

        /*
         * jpnlMainContentPanel - the main content panel where management panels are
         * displayed
         */
        jpnlMainContentPanel = new JPanel();
        jpnlMainContentPanel.setMaximumSize(new Dimension(32767, 32767));
        jpnlMainContentPanel.setLayout(new CardLayout());
        // Default show an empty panel
        jpnlMainContentPanel.add(new JPanel());
        jpnlContentPane.add(jpnlMainContentPanel);
        /* END OF jpnlMainContentPanel */

        /* jlblSidebarHeader - header label in the sidebar */
        JLabel jlblMiniHeader = new JLabel("<html>Polytechnic University of <br>the Philippines</html>");
        jlblMiniHeader.setAlignmentY(0.0f);
        jlblMiniHeader.setForeground(Color.WHITE);
        jlblMiniHeader.setFont(new Font("Roboto Light", Font.PLAIN, 11));
        jlblMiniHeader.setBorder(new EmptyBorder(20, 20, 0, 20));
        jpnlSidebar.add(jlblMiniHeader);

        JLabel jlblSidebarHeader = new JLabel("<html>Library Management<br>and Book Borrowing System</html>");
        jlblSidebarHeader.setAlignmentY(0.0f);
        jlblSidebarHeader.setBorder(new EmptyBorder(0, 20, 0, 20));
        jlblSidebarHeader.setForeground(Color.WHITE);
        jlblSidebarHeader.setFont(new Font("Roboto", Font.PLAIN, 20));
        jpnlSidebar.add(jlblSidebarHeader);
        /* END OF jlblSidebarHeader */

        // Spacing for the sidebar before the buttons
        jpnlSidebar.add(Box.createRigidArea(new Dimension(0, 50)));
    }

    /**
     * Initializes the management panels of this frame.
     *
     * @param managementPanels
     */
    public void setManagementPanels(ManagementPanel[] managementPanels) {
        this.managementPanels = managementPanels;

        for (int i = 0; i < this.managementPanels.length; i++) {
            // Panel Index for CardLayout
            final int panelIndex = i;

            // Add the panel to the main content panel
            jpnlMainContentPanel.add(managementPanels[i], Integer.toString(i));

            // Construct a sidebar button for this management panel
            JButton jbtnBookCategory = new JButton(managementPanels[i].getButtonDescription());
            jbtnBookCategory.addActionListener((event) -> {
                CardLayout mainContentPanelLayout = (CardLayout) jpnlMainContentPanel.getLayout();
                mainContentPanelLayout.show(jpnlMainContentPanel, Integer.toString(panelIndex));
                managementPanels[panelIndex].firstPage();
            });
            jbtnBookCategory.setAlignmentY(0.0f);
            jbtnBookCategory.setHorizontalAlignment(SwingConstants.LEFT);
            jbtnBookCategory.setMinimumSize(new Dimension(200, 40));
            jbtnBookCategory.setFont(new Font("Roboto", Font.PLAIN, 13));
            jbtnBookCategory.setForeground(Color.WHITE);
            jbtnBookCategory.setBackground(Color.BLACK);
            jbtnBookCategory.setBorder(new EmptyBorder(0, 20, 0, 20));
            jbtnBookCategory.setFocusPainted(false);
            jbtnBookCategory.setBorderPainted(false);
            jbtnBookCategory.setMaximumSize(new Dimension(32767, 40));
            jpnlSidebar.add(jbtnBookCategory);
        }
    }

}
