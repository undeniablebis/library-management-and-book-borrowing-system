package co.bisri.librarysystem.admin.ui.book;

import co.bisri.librarysystem.admin.ui.ManagementPanel;
import co.bisri.librarysystem.admin.ui.book.record.BookEntity;
import co.bisri.librarysystem.admin.ui.book.record.BookTableRecord;
import co.bisri.librarysystem.admin.ui.bookcategory.record.BookCategoryEntity;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing books.
 *
 * @author Bismillah Constantino
 * @author Rian Reyes
 */
public class BookManagementPanel extends ManagementPanel {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Table Model
    private BookTableModel bookTableModel;

    // Current table cache
    protected List<BookTableRecord> currentCache;

    // Form Dialog
    protected BookFormDialog bookFormDialog;

    public BookManagementPanel() {
        super();

        // Set header label
        jlblHeader.setText("Manage Books");

        /* jbtnShowAddForm */
        JButton jbtnAdd = new JButton("Add");
        jbtnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnAdd.setBackground(Color.WHITE);
        jbtnAdd.setFont(new Font("Roboto", Font.PLAIN, 12));
        jbtnAdd.addActionListener((event) -> {
            // Reset and show the form dialog
            bookFormDialog.initialize();
            bookFormDialog.setVisible(true);
        });
        jpnlButtonActions.add(jbtnAdd);
        /* END OF jbtnShowAddForm */

        /* jbtnUpdate */
        JButton jbtnUpdate = new JButton("Update");
        jbtnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnUpdate.setBackground(Color.WHITE);
        jbtnUpdate.setFont(new Font("Roboto", Font.PLAIN, 12));
        jbtnUpdate.addActionListener((event) -> {
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
            String isbn = currentCache.get(selectedRow).isbn();
            BookEntity book = null;

            // Retrieve the corresponding record from the database
            try (Connection connection = getConnection();
                 PreparedStatement retrieveBookStatement = connection.prepareStatement(
                         "SELECT isbn, category_name, title, author, published_on, publisher FROM book " +
                                 "WHERE isbn = ?")) {

                // Bind the PK
                retrieveBookStatement.setString(1, isbn);

                // Fetch the result set, extract entity
                try (ResultSet bookResultSet = retrieveBookStatement.executeQuery()) {
                    if (bookResultSet.next()) {
                        // If a record was found, parse into a book category object
                        book = new BookEntity(
                                bookResultSet.getString("isbn"),
                                bookResultSet.getString("category_name"),
                                bookResultSet.getString("title"),
                                bookResultSet.getString("author"),
                                LocalDate.parse(bookResultSet.getString("published_on")),
                                bookResultSet.getString("publisher"));
                    } else {
                        // If no records were found, show an error
                        JOptionPane.showMessageDialog(
                                this,
                                "There was no corresponding book found. Refresh your panel.",
                                "Retrieve error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (SQLException e1) {
                // If an error occurred, show message then exit
                JOptionPane.showMessageDialog(
                        this,
                        "An error occurred while retrieving book from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                        "Database connectivity error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset and show the form dialog
            bookFormDialog.initialize(book);
            bookFormDialog.setVisible(true);
        });
        jpnlButtonActions.add(jbtnUpdate);
        /* END OF jbtnUpdate */

        /* jbtnDelete */
        JButton jbtnDelete = new JButton("Delete");
        jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnDelete.setBackground(Color.WHITE);
        jbtnDelete.setFont(new Font("Roboto", Font.PLAIN, 12));
        jbtnDelete.addActionListener((event) -> {
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
            String isbn = currentCache.get(selectedRow).isbn();

            // Confirm the user first
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove book with isbn: " + isbn + "?",
                    "Remove book",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                // Delete the corresponding record from the database
                try (Connection connection = getConnection();
                     PreparedStatement deleteBookStatement = connection.prepareStatement(
                             "DELETE FROM book WHERE isbn = ?")) {

                    // Bind the PK
                    deleteBookStatement.setString(1, isbn);
                    // Execute the delete statement
                    deleteBookStatement.execute();
                } catch (SQLException e1) {
                    // If an error occurred, show message then exit
                    JOptionPane.showMessageDialog(
                            this,
                            "An error occurred while removing book from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                            "Database connectivity error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Show success message
                JOptionPane.showMessageDialog(
                        this,
                        "Successfully removed book with isbn: " + isbn,
                        "Remove success",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refresh the panel
                refreshPage();
            }
        });
        jpnlButtonActions.add(jbtnDelete);
        /* END OF jbtnDelete */

        /* bookTableModel */
        bookTableModel = new BookTableModel();
        bookTableModel.bookManagementPanel = this;
        jtblMainTable.setModel(bookTableModel);
        /* END OF bookTableModel */

        /* formDialog */
        bookFormDialog = new BookFormDialog();
        bookFormDialog.bookManagementPanel = this;
        /* END OF formDialog */

        /* currentCache */
        currentCache = new ArrayList<>();
        /* END OF currentCache */
    }

    @Override
    public void setPage(int page) {

        // Fetch book count
        try (Connection connection = getConnection();
             Statement retrieveCountStatement = connection.createStatement();
             ResultSet countResultSet = retrieveCountStatement.executeQuery("SELECT COUNT(isbn) AS record_count FROM book")) {

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

        // Fetch books
        currentCache.clear();
        try (Connection connection = getConnection();
             Statement retrieveBooksStatement = connection.createStatement();
             ResultSet bookResultSet = retrieveBooksStatement.executeQuery(
                     "SELECT isbn, category_name, title, author, published_on, publisher FROM book " +
                             "LIMIT " + (currentPage - 1) * PAGE_SIZE + ", " + PAGE_SIZE)) {

            // Parse each row into a BookTableRecord
            while (bookResultSet.next()) {
                currentCache.add(
                        new BookTableRecord(
                                bookResultSet.getString("isbn"),
                                bookResultSet.getString("category_name"),
                                bookResultSet.getString("title"),
                                bookResultSet.getString("author"),
                                LocalDate.parse(bookResultSet.getString("published_on")),
                                bookResultSet.getString("publisher")));
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
        bookTableModel.fireTableDataChanged();

        // Tell the page button panel that the page has changed
        pageButtonPanel.setTotalPageCount(totalPageCount);
        pageButtonPanel.setCurrentPage(currentPage);
    }

}