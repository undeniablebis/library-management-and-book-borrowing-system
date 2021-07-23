package co.bisri.librarysystem.admin.ui;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A generic management panel.
 *
 * @author Bismillah Constantino
 * @author Rian Reyes
 */
public abstract class ManagementPanel extends JPanel {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Page size
    protected static final int PAGE_SIZE = 10;

    // Main DataSource
    protected DataSource dataSource;

    // Header Label
    protected JLabel jlblHeader;

    // Button Actions Panel
    protected JPanel jpnlButtonActions;

    // Main Table
    protected JTable jtblMainTable;

    // Paging Information
    protected PageButtonPanel pageButtonPanel;
    protected int totalPageCount;
    protected int currentPage;

    public ManagementPanel() {
        // Set border to EmptyBorder for spacing
        setBorder(new EmptyBorder(10, 10, 10, 10));
        // Use BoxLayout to lay the internal 3 panels: Header, Table, Pagination Actions
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        /* jpnlHeader - Header Panel */
        JPanel jpnlHeader = new JPanel();
        jpnlHeader.setAlignmentY(0.0f);
        jpnlHeader.setAlignmentX(0.0f);
        jpnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        jpnlHeader.setMinimumSize(new Dimension(10, 45));
        jpnlHeader.setMaximumSize(new Dimension(32767, 55));
        add(jpnlHeader);
        jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
        /* END OF jpnlHeader */

        /* jlblHeader - Header label */
        jlblHeader = new JLabel();
        jlblHeader.setAlignmentY(0.0f);
        jlblHeader.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        jpnlHeader.add(jlblHeader);
        /* END OF jlblHeader */

        /* jpnlButtonActions - panel for buttons */
        jpnlButtonActions = new JPanel();
        jpnlButtonActions.setAlignmentY(0.0f);
        jpnlButtonActions.setAlignmentX(0.0f);
        FlowLayout fl_jpnlButtonActions = (FlowLayout) jpnlButtonActions.getLayout();
        fl_jpnlButtonActions.setAlignment(FlowLayout.RIGHT);
        jpnlHeader.add(jpnlButtonActions);
        /* END OF jpnlButtonActions */

        /* jscrlpnTable - Scrollable Table Panel */
        JScrollPane jscrlpnTable = new JScrollPane();
        jscrlpnTable.setMaximumSize(new Dimension(32767, 32767));
        jscrlpnTable.setAlignmentX(0.0f);
        jscrlpnTable.setAlignmentY(0.0f);
        add(jscrlpnTable);
        /* END OF jscrlpnAccounts */

        /* jtblMainTable - Main Panel Table */
        jtblMainTable = new JTable();
        jtblMainTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        jtblMainTable.setMaximumSize(new Dimension(32767, 32767));
        jtblMainTable.setRowHeight(22);
        jtblMainTable.setIntercellSpacing(new Dimension(4, 4));
        jscrlpnTable.setViewportView(jtblMainTable);
        /* END OF jtblMainTable */

        /* pageButtonPanel - paginating buttons */
        pageButtonPanel = new PageButtonPanel(
                // ActionListener for previous button
                (event) -> {
                    // If it's already the first page, previous button should do nothing
                    if (currentPage == 1)
                        return;

                    // Else, set the current page to the previous page
                    setPage(--currentPage);
                },
                // ActionListener for next button
                (event) -> {
                    // If it's already the last page, next button should do nothing
                    if (currentPage == totalPageCount)
                        return;

                    // Else, set the current page to the next page
                    setPage(++currentPage);
                },
                // ActionListener for normal page button
                (event) -> {
                    // Get page value of button
                    int pageNumber = Integer.parseInt(((JButton) event.getSource()).getText());

                    // If current page is already rendered, then button should do nothing
                    if (currentPage == pageNumber)
                        return;

                    // Else, set the current page to the clicked button's page
                    setPage(pageNumber);
                });
        pageButtonPanel.setMaximumSize(new Dimension(32767, 100));
        pageButtonPanel.setAlignmentX(0.0f);
        pageButtonPanel.setAlignmentY(0.0f);
        add(pageButtonPanel);
        /* END OF pageButtonPanel */
    }

    /**
     * Sets the datasource this panel will use.
     *
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Gets the management panel button description
     */
    public String getButtonDescription() {
        return jlblHeader.getText();
    }

    /**
     * Sets the management panel data page to 1.
     */
    public void firstPage() {
        setPage(1);
    }

    /**
     * Refreshes the current page.
     */
    public void refreshPage() {
        setPage(currentPage);
    }

    /**
     * Sets the displayed page.
     *
     * @param page
     */
    public abstract void setPage(int page);

    /**
     * Gets a connection from the datasource set.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    
}
