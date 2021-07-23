package co.bisri.librarysystem.admin.ui.bookcategory;

import co.bisri.librarysystem.admin.ui.ManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcategory.record.BookCategoryEntity;
import co.bisri.librarysystem.admin.ui.bookcategory.record.BookCategoryTableRecord;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing book categories.
 *
 * @author Rian Reyes
 */
public class BookCategoryManagementPanel extends ManagementPanel {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Table Model
    private final BookCategoryTableModel bookCategoryTableModel;

    // Current table cache
    protected List<BookCategoryTableRecord> currentCache;

    // Form Dialog
    protected BookCategoryFormDialog bookCategoryFormDialog;

    public BookCategoryManagementPanel() {
        super();

        // Set header label
        jlblHeader.setText("Manage Categories");

        /* jbtnShowAddForm */
        JButton jbtnAdd = new JButton("Add");
        jbtnAdd.addActionListener(e -> {
            // Reset and show the form dialog
            bookCategoryFormDialog.initialize();
            bookCategoryFormDialog.setVisible(true);
        });
        jbtnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnAdd.setBackground(Color.WHITE);
        jbtnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnAdd);
        /* END OF jbtnShowAddForm */

        /* jbtnUpdate */
        JButton jbtnUpdate = new JButton("Update");
        jbtnUpdate.addActionListener(e -> {
            // Get current selected row
            int selectedRow = jtblMainTable.getSelectedRow();

            // If no row is selected, don't proceed
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a record first before clicking the update button.",
                        "Select first!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Retrieve the PK from the cache
            String name = currentCache.get(selectedRow).name();
            BookCategoryEntity bookCategory = null;

            // Retrieve the corresponding record from the database
            try (Connection connection = getConnection();
                 PreparedStatement retrieveCategoryStatement = connection.prepareStatement(
                         "SELECT name, description FROM book_category WHERE name = ?")) {

                // Bind the PK
                retrieveCategoryStatement.setString(1, name);

                // Fetch the result set, extract entity
                try (ResultSet categoryResultSet = retrieveCategoryStatement.executeQuery()) {
                    if (categoryResultSet.next()) {
                        // If a record was found, parse into a book category object
                        bookCategory = new BookCategoryEntity(
                                categoryResultSet.getString("name"),
                                categoryResultSet.getString("description"));
                    } else {
                        // If no records were found, show an error
                        JOptionPane.showMessageDialog(
                                this,
                                "There was no corresponding category found. Refresh your panel.",
                                "Retrieve error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (SQLException e1) {
                // If an error occurred, show message then exit
                JOptionPane.showMessageDialog(
                        this,
                        "An error occurred while retrieving category from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                        "Database connectivity error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset and show the form dialog
            bookCategoryFormDialog.initialize(bookCategory);
            bookCategoryFormDialog.setVisible(true);
        });
        jbtnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnUpdate.setBackground(Color.WHITE);
        jbtnUpdate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnUpdate);
        /* END OF jbtnUpdate */

        /* jbtnDelete */
        JButton jbtnDelete = new JButton("Delete");
        jbtnDelete.addActionListener(e -> {
            // Get current selected row
            int selectedRow = jtblMainTable.getSelectedRow();

            // If no row is selected, don't proceed
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a record first before clicking the delete button.",
                        "Select first!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Retrieve the PK from the cache
            String name = currentCache.get(selectedRow).name();

            // Confirm the user first
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove category with name: " + name + "?",
                    "Remove category",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                // Delete the corresponding record from the database
                try (Connection connection = getConnection();
                     PreparedStatement deleteCategoryStatement = connection.prepareStatement(
                             "DELETE FROM book_category WHERE name = ?")) {

                    // Bind the PK
                    deleteCategoryStatement.setString(1, name);
                    // Execute the delete statement
                    deleteCategoryStatement.execute();
                } catch (SQLException e1) {
                    // If an error occurred, show message then exit
                    JOptionPane.showMessageDialog(
                            this,
                            "An error occurred while removing category from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                            "Database connectivity error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Show success message
                JOptionPane.showMessageDialog(
                        this,
                        "Successfully removed category with name: " + name,
                        "Remove success",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refresh the panel
                refreshPage();
            }
        });
        jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnDelete.setBackground(Color.WHITE);
        jbtnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnDelete);
        /* END OF jbtnDelete */

        /* bookCategoryTableModel */
        bookCategoryTableModel = new BookCategoryTableModel();
        bookCategoryTableModel.bookCategoryManagementPanel = this;
        jtblMainTable.setModel(bookCategoryTableModel);

        // Column width configuration
        TableColumn nameColumn = jtblMainTable.getColumnModel().getColumn(0);
        nameColumn.setMaxWidth(160);
        nameColumn.setPreferredWidth(160);
        /* END OF bookCategoryTableModel */

        /* bookCategoryFormDialog */
        bookCategoryFormDialog = new BookCategoryFormDialog();
        bookCategoryFormDialog.bookCategoryManagementPanel = this;
        /* END OF bookCategoryFormDialog */

        /* currentCache */
        currentCache = new ArrayList<>();
        /* END OF currentCache */
    }

    @Override
    public void setPage(int page) {

        // Fetch category count
        try (Connection connection = getConnection();
             Statement retrieveCountStatement = connection.createStatement();
             ResultSet countResultSet = retrieveCountStatement.executeQuery("SELECT COUNT(name) AS record_count FROM book_category")) {

            // Traverse the result set once
            countResultSet.next();

            // Calculate the page count based on how many records are in the database, by page size
            int totalRecordCount = countResultSet.getInt("record_count");
            totalPageCount = (int) Math.ceil((double) totalRecordCount / PAGE_SIZE);
        } catch (SQLException e) {
            // If an error occurred, show message then exit
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while interacting with the database.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database connectivity error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the given page value is valid
        if (page < 1 || page > totalPageCount) {
            // Display error message
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid page to display.",
                    "Data paging error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Set the current page
        currentPage = page;

        // Fetch book categories
        currentCache.clear();
        try (Connection connection = getConnection();
             Statement retrieveCategoriesStatement = connection.createStatement();
             ResultSet categoryResultSet = retrieveCategoriesStatement.executeQuery(
                     "SELECT name, description FROM book_category " +
                             "LIMIT " + (currentPage - 1) * PAGE_SIZE + ", " + PAGE_SIZE)) {

            // Parse each row into a BookCategoryTableRecord
            while (categoryResultSet.next()) {
                currentCache.add(
                        new BookCategoryTableRecord(
                                categoryResultSet.getString("name"),
                                categoryResultSet.getString("description")));
            }
        } catch (SQLException e) {
            // If an error occurred, show message then exit
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while interacting with the database.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database connectivity error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tell the tablemodel that the data has changed
        bookCategoryTableModel.fireTableDataChanged();

        // Tell the page button panel that the page has changed
        pageButtonPanel.setTotalPageCount(totalPageCount);
        pageButtonPanel.setCurrentPage(currentPage);
    }

}
	
