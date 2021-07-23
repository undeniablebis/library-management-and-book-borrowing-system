package co.bisri.librarysystem.admin.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic panel component for management panels with paginating data.
 *
 * @author Rian Reyes
 */
public class PageButtonPanel extends JPanel {

    // Ignore, this is to remove serializability-related warnings
    private static final long serialVersionUID = 1L;

    // Current total page count. Used to determine how many buttons to render
    private int totalPageCount;
    // Current page rendered
    private int currentPage;

    // All page number buttons shown are stored here
    private final List<JComponent> currentShownPageButtons;
    private final ActionListener pageActionListener;

    // All page number buttons are displayed in this JPanel
    private final JPanel jpnlCurrentShownPageButtons;

    // Previous Page button
    private final JButton jbtnPreviousPage;

    // Next Page button
    private final JButton jbtnNextPage;

    /**
     * Constructs a PageButtonPanel with the given ActionListeners for each button.
     * <p>
     * NOTE that the ActionListeners are constructed by the ManagementPanels themselves so
     * they have references to their internal workings, but in a somehow-safe manner.
     *
     * @param prevActionListener
     * @param nextActionListener
     * @param pageActionListener
     */
    public PageButtonPanel(ActionListener prevActionListener, ActionListener nextActionListener, ActionListener pageActionListener) {
        /* self properties */
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.pageActionListener = pageActionListener;
        /* END OF self properties */

        /* jbtnPreviousPage */
        jbtnPreviousPage = new JButton("<");
        jbtnPreviousPage.setBackground(Color.WHITE);
        jbtnPreviousPage.addActionListener(prevActionListener);
        add(jbtnPreviousPage);
        /* END OF jbtnPreviousPage */

        /* jpnlCurrentShownPageButtons */
        jpnlCurrentShownPageButtons = new JPanel();
        add(jpnlCurrentShownPageButtons);
		/* END OF jpnlCurrentShownPageButtons *
		
		/* jbtnNextPage */
        jbtnNextPage = new JButton(">");
        jbtnNextPage.setBackground(Color.WHITE);
        jbtnNextPage.addActionListener(nextActionListener);
        add(jbtnNextPage);
        /* END OF jbtnNextPage */

        // Others
        currentShownPageButtons = new ArrayList<>();
        totalPageCount = 0;
        currentPage = 0;
    }

    /**
     * Update the new total page count.
     *
     * @param totalPageCount
     */
    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    /**
     * Set the new current page. The panel recalculates and re-renders all page buttons
     * as necessary.
     *
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        // If given invalid values, do nothing
        if (totalPageCount == 0 || this.currentPage == currentPage || currentPage < 1 || currentPage > totalPageCount)
            return;

        // Update current page
        this.currentPage = currentPage;

        // Remove all page-specific buttons
        currentShownPageButtons.clear();
        jpnlCurrentShownPageButtons.removeAll();

        // Check what page to start rendering page buttons.
        int startPage = 1;
        if (currentPage == 1)
            startPage = 1;
        else if (currentPage == totalPageCount) {
            startPage = totalPageCount > 2 ? totalPageCount - 2 : 1;
        } else
            startPage = currentPage - 1;

        // Check if an ellipsis is needed on the left (if the current page is far from the beginning)
        if (startPage > 1) {
            JLabel jlblEllipsis = new JLabel("...");
            currentShownPageButtons.add(jlblEllipsis);
            jpnlCurrentShownPageButtons.add(jlblEllipsis);
        }

        // Create all page buttons (max. 3 based on current page)
        for (int i = startPage, noOfAddedButtonPages = 0; i <= totalPageCount && noOfAddedButtonPages < 3; i++, noOfAddedButtonPages++) {
            JButton jbtnPage = new JButton(Integer.toString(i));
            if (i == currentPage)
                jbtnPage.setBackground(Color.GRAY);
            else
                jbtnPage.setBackground(Color.WHITE);
            jbtnPage.addActionListener(pageActionListener);
            currentShownPageButtons.add(jbtnPage);
            jpnlCurrentShownPageButtons.add(jbtnPage);
        }

        // Check if an ellipsis is needed on the right (if there are more pages other than those rendered)
        int lastRenderedButtonPage = Integer.parseInt(((JButton) currentShownPageButtons.get(currentShownPageButtons.size() - 1)).getText());
        if (totalPageCount - lastRenderedButtonPage > 0) {
            JLabel jlblEllipsis = new JLabel("...");
            currentShownPageButtons.add(jlblEllipsis);
            jpnlCurrentShownPageButtons.add(jlblEllipsis);
        }

        // Update
        revalidate();
    }

}
