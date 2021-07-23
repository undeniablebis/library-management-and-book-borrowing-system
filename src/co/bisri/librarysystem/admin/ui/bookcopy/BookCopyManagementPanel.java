package co.bisri.librarysystem.admin.ui.bookcopy;

import co.bisri.librarysystem.admin.ui.ManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopyEntity;
import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopyTableRecord;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing book copies.
 *
 * @author Rian Reyes
 */
public class BookCopyManagementPanel extends ManagementPanel {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Table Model
    private final BookCopyTableModel bookCopyTableModel;

    // Current table cache
    protected List<BookCopyTableRecord> currentCache;

    // Form Dialog
    protected BookCopyFormDialog bookCopyFormDialog;

    public BookCopyManagementPanel() {
        super();

        // Set header label
        jlblHeader.setText("Manage Copies");

        /* jbtnShowAddForm */
        JButton jbtnAdd = new JButton("Add");
        jbtnAdd.addActionListener((event) -> {
            // Reset and show the form dialog
            bookCopyFormDialog.initialize();
            bookCopyFormDialog.setVisible(true);
        });
        jbtnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnAdd.setBackground(Color.WHITE);
        jbtnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnAdd);
        /* END OF jbtnShowAddForm */

        /* jbtnUpdate */
        JButton jbtnUpdate = new JButton("Update");
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
            int copyNo = currentCache.get(selectedRow).copyNo();
            BookCopyEntity bookCopy = null;

            // Retrieve the corresponding record from the database
            try (Connection connection = getConnection();
                 PreparedStatement retrieveCopyStatement = connection.prepareStatement(
                         "SELECT isbn, copy_no, date_acquired, status, current_worth, borrow_status " +
                                 "FROM book_copy WHERE isbn = ? AND copy_no = ?")) {

                // Bind the PK
                retrieveCopyStatement.setString(1, isbn);
                retrieveCopyStatement.setInt(2, copyNo);

                // Fetch the result set, extract entity
                try (ResultSet copyResultSet = retrieveCopyStatement.executeQuery()) {
                    if (copyResultSet.next()) {
                        // If a record was found, parse into a book copy object
                        bookCopy = new BookCopyEntity(
                                copyResultSet.getString("isbn"),
                                copyResultSet.getInt("copy_no"),
                                LocalDate.parse(copyResultSet.getString("date_acquired")),
                                copyResultSet.getString("status"),
                                copyResultSet.getDouble("current_worth"),
                                copyResultSet.getString("borrow_status"));
                    } else {
                        // If no records were found, show an error
                        JOptionPane.showMessageDialog(
                                this,
                                "There was no corresponding book copy found. Refresh your panel.",
                                "Retrieve error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (SQLException e1) {
                // If an error occurred, show message then exit
                JOptionPane.showMessageDialog(
                        this,
                        "An error occurred while retrieving book copy from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                        "Database connectivity error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset and show the form dialog
            bookCopyFormDialog.initialize(bookCopy);
            bookCopyFormDialog.setVisible(true);
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
            String isbn = currentCache.get(selectedRow).isbn();
            int copyNo = currentCache.get(selectedRow).copyNo();

            // Confirm the user first
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove copy " + copyNo + " of book isbn: " + isbn + "?",
                    "Remove book copy",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                // Delete the corresponding record from the database
                try (Connection connection = getConnection();
                     PreparedStatement deleteCopyStatement = connection.prepareStatement(
                             "DELETE FROM book_copy WHERE isbn = ? AND copy_no = ?")) {

                    // Bind the PK
                    deleteCopyStatement.setString(1, isbn);
                    deleteCopyStatement.setInt(2, copyNo);
                    // Execute the delete statement
                    deleteCopyStatement.execute();
                } catch (SQLException e1) {
                    // If an error occurred, show message then exit
                    JOptionPane.showMessageDialog(
                            this,
                            "An error occurred while removing book copy from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                            "Database connectivity error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Show success message
                JOptionPane.showMessageDialog(
                        this,
                        "Successfully removed book copy " + copyNo + " with isbn: " + isbn,
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

        /* bookCopyTableModel */
        bookCopyTableModel = new BookCopyTableModel();
        bookCopyTableModel.bookCopyManagementPanel = this;
        jtblMainTable.setModel(bookCopyTableModel);
        /* END OF memberTableModel */

        /* formDialog */
        bookCopyFormDialog = new BookCopyFormDialog();
        bookCopyFormDialog.bookCopyManagementPanel = this;
        /* END OF formDialog */

        /* currentCache */
        currentCache = new ArrayList<>();
        /* END OF currentCache */
    }

    @Override
    public void setPage(int page) {

        // Fetch book copy count
        try (Connection connection = getConnection();
             Statement retrieveCountStatement = connection.createStatement();
             ResultSet countResultSet = retrieveCountStatement.executeQuery("SELECT COUNT(isbn) AS record_count FROM book_copy")) {

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
             Statement retrieveCopiesStatement = connection.createStatement();
             ResultSet copyResultSet = retrieveCopiesStatement.executeQuery(
                     "SELECT bc.isbn, bc.copy_no, b.title, bc.status, bc.borrow_status FROM book_copy bc " +
                             "INNER JOIN book b ON b.isbn = bc.isbn " +
                             "LIMIT " + (currentPage - 1) * PAGE_SIZE + ", " + PAGE_SIZE)) {

            // Parse each row into a BookCopyTableRecord
            while (copyResultSet.next()) {
                currentCache.add(
                        new BookCopyTableRecord(
                                copyResultSet.getString("isbn"),
                                copyResultSet.getInt("copy_no"),
                                copyResultSet.getString("title"),
                                copyResultSet.getString("status"),
                                copyResultSet.getString("borrow_status")));
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
        bookCopyTableModel.fireTableDataChanged();

        // Tell the page button panel that the page has changed
        pageButtonPanel.setTotalPageCount(totalPageCount);
        pageButtonPanel.setCurrentPage(currentPage);
    }

}
	
