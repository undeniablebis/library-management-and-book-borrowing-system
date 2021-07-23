package co.bisri.librarysystem.admin.ui.borrow;

import co.bisri.librarysystem.admin.ui.ManagementPanel;
import co.bisri.librarysystem.admin.ui.borrow.record.BookCopyEntity;
import co.bisri.librarysystem.admin.ui.borrow.record.BorrowEntity;
import co.bisri.librarysystem.admin.ui.borrow.record.BorrowTableRecord;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing borrows.
 *
 * @author Rian Reyes
 */
public class BorrowManagementPanel extends ManagementPanel {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Table Model
    private BorrowTableModel borrowTableModel;

    // Current table cache
    protected List<BorrowTableRecord> currentCache;

    // Form dialog
    protected BorrowFormDialog borrowFormDialog;

    // Return dialog
    protected BorrowReturnDialog borrowReturnDialog;

    public BorrowManagementPanel() {
        super();

        // Set header label
        jlblHeader.setText("Manage Borrows");

        /* jbtnAdd */
        JButton jbtnAdd = new JButton("Add");
        jbtnAdd.addActionListener((event) -> {
            // Reset and show the form dialog
            borrowFormDialog.initialize();
            borrowFormDialog.setVisible(true);
        });
        jbtnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnAdd.setBackground(Color.WHITE);
        jbtnAdd.setFont(new Font("Roboto", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnAdd);
        /* END OF jbtnAdd */

        /* jbtnReturn */
        JButton jbtnReturn = new JButton("Return");
        jbtnReturn.addActionListener((event) -> {
            // Get current selected row
            int selectedRow = jtblMainTable.getSelectedRow();

            // If no row is selected, don't proceed
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a record first before clicking the return button.",
                        "Select first!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get PK
            int memberId = currentCache.get(selectedRow).memberId();
            LocalDate borrowedOn = currentCache.get(selectedRow).borrowedOn();

            // Fetch respective borrow
            BorrowEntity borrowEntity = null;
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement retrieveBorrowStatement =
                         connection.prepareStatement(
                                 "SELECT b.member_id, m.first_name, m.last_name, b.borrowed_on, b.target_return_date, b.returned_on, b.status, b.return_fee, bi.book_isbn, bi.book_copy_no, bc.current_worth, bk.title "
                                         + "FROM borrow b "
                                         + "INNER JOIN borrow_item bi ON bi.borrow_member_id = b.member_id AND bi.borrow_borrowed_on = b.borrowed_on "
                                         + "INNER JOIN book_copy bc ON bc.isbn = bi.book_isbn AND bc.copy_no = bi.book_copy_no "
                                         + "INNER JOIN book bk ON bk.isbn = bc.isbn "
                                         + "INNER JOIN member m ON m.id = b.member_id "
                                         + "WHERE b.member_id = ? AND b.borrowed_on = ?")) {

                // Bind PK
                retrieveBorrowStatement.setInt(1, memberId);
                retrieveBorrowStatement.setString(2, borrowedOn.toString());

                try (ResultSet borrowResultSet = retrieveBorrowStatement.executeQuery()) {
                    if (borrowResultSet.next()) {
                        // Parse borrow
                        LocalDate targetReturnDate = LocalDate.parse(borrowResultSet.getString("target_return_date"));
                        String memberName = borrowResultSet.getString("first_name") + " " + borrowResultSet.getString("last_name");
                        String status = borrowResultSet.getString("status");

                        List<BookCopyEntity> bookCopies = new ArrayList<>();
                        // Parse first Item
                        BookCopyEntity firstBookCopyEntity =
                                new BookCopyEntity(
                                        borrowResultSet.getString("book_isbn"),
                                        borrowResultSet.getInt("book_copy_no"),
                                        borrowResultSet.getString("title"),
                                        borrowResultSet.getDouble("current_worth"));
                        bookCopies.add(firstBookCopyEntity);

                        // Parse remaining items
                        while (borrowResultSet.next()) {
                            BookCopyEntity bookCopyEntity =
                                    new BookCopyEntity(
                                            borrowResultSet.getString("book_isbn"),
                                            borrowResultSet.getInt("book_copy_no"),
                                            borrowResultSet.getString("title"),
                                            borrowResultSet.getDouble("current_worth"));
                            bookCopies.add(bookCopyEntity);
                        }

                        borrowEntity = new BorrowEntity(memberId, memberName, borrowedOn, targetReturnDate, null, status, 0.00, bookCopies);
                    } else {
                        // If no records were found, show dialog and inform user.
                        JOptionPane.showMessageDialog(
                                this,
                                "No borrow found with given member id and date.",
                                "Invalid fields!",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            } catch (SQLException e) {
                // If an error occured, show dialog and inform user.
                JOptionPane.showMessageDialog(
                        this,
                        "An error occured while trying to fetch borrow from database.\n\nError: " + e.getMessage(),
                        "Database access error!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if borrow is already returned
            if (borrowEntity.status().contentEquals("RETURNED")) {
                // If borrow is already returned, dont proceed
                JOptionPane.showMessageDialog(
                        this,
                        "This borrow is already returned.",
                        "Already returned",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Initialize return dialog with the retrieved borrow item
            borrowReturnDialog.initialize(borrowEntity);
            // Show return dialog
            borrowReturnDialog.setVisible(true);
        });
        jbtnReturn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnReturn.setBackground(Color.WHITE);
        jbtnReturn.setFont(new Font("Roboto", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnReturn);
        /* END OF jbtnReturn */

        /* jbtnDelete */
        JButton jbtnDelete = new JButton("Delete");
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

            // Get PK
            int memberId = currentCache.get(selectedRow).memberId();
            LocalDate borrowedOn = currentCache.get(selectedRow).borrowedOn();

            // For dialog confirmation
            String memberName = currentCache.get(selectedRow).memberFirstName() + " " + currentCache.get(selectedRow).memberLastName();

            // Confirm the user first
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete borrow by " + memberName + " in " + borrowedOn.toString() + "?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                // Delete the corresponding record from the database
                try (Connection connection = getConnection();
                     PreparedStatement deleteBorrowStatement = connection.prepareStatement(
                             "DELETE FROM borrow WHERE member_id = ? AND borrowed_on = ?")) {

                    // Bind the PK
                    deleteBorrowStatement.setInt(1, memberId);
                    deleteBorrowStatement.setString(2, borrowedOn.toString());
                    // Execute the delete statement
                    deleteBorrowStatement.execute();
                } catch (SQLException e1) {
                    // If an error occurred, show message then exit
                    JOptionPane.showMessageDialog(
                            this,
                            "An error occurred while removing borrow from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                            "Database connectivity error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Show success message
                JOptionPane.showMessageDialog(
                        this,
                        "Successfully removed borrow.",
                        "Remove success",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refresh the panel
                refreshPage();
            }
        });
        jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnDelete.setBackground(Color.WHITE);
        jbtnDelete.setFont(new Font("Roboto", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnDelete);
        /* END OF jbtnDelete */

        /* borrowTableModel */
        borrowTableModel = new BorrowTableModel();
        borrowTableModel.borrowManagementPanel = this;
        jtblMainTable.setModel(borrowTableModel);
        /* END OF borrowTableModel */

        /* borrowFormDialog */
        borrowFormDialog = new BorrowFormDialog();
        borrowFormDialog.borrowManagementPanel = this;
        /* END OF borrowFormDialog */

        /* borrowReturnDialog */
        borrowReturnDialog = new BorrowReturnDialog();
        borrowReturnDialog.borrowManagementPanel = this;
        /* END OF borrowReturnDialog */

        /* currentCache */
        currentCache = new ArrayList<>();
        /* END OF currentCache */
    }

    @Override
    public void setPage(int page) {

        // Fetch borrow count
        try (Connection connection = getConnection();
             Statement retrieveCountStatement = connection.createStatement();
             ResultSet countResultSet = retrieveCountStatement.executeQuery("SELECT COUNT(borrowed_on) AS record_count FROM borrow")) {

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

        // Fetch borrows
        currentCache.clear();
        try (Connection connection = getConnection();
             Statement retrieveBorrowsStatement = connection.createStatement();
             ResultSet borrowResultSet = retrieveBorrowsStatement.executeQuery(
                     "SELECT b.member_id, m.first_name, m.last_name, b.borrowed_on, b.target_return_date, " +
                             "b.returned_on, b.status, b.return_fee FROM borrow b " +
                             "INNER JOIN member m ON m.id = b.member_id " +
                             "LIMIT " + (currentPage - 1) * PAGE_SIZE + ", " + PAGE_SIZE)) {

            // Parse each row into a BorrowTableRecord
            while (borrowResultSet.next()) {
                String returnedOnStr = borrowResultSet.getString("returned_on");
                LocalDateTime returnedOn = returnedOnStr == null ? null :
                        LocalDateTime.parse(returnedOnStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                currentCache.add(
                        new BorrowTableRecord(
                                borrowResultSet.getInt("member_id"),
                                borrowResultSet.getString("first_name"),
                                borrowResultSet.getString("last_name"),
                                LocalDate.parse(borrowResultSet.getString("borrowed_on")),
                                LocalDate.parse(borrowResultSet.getString("target_return_date")),
                                returnedOn,
                                borrowResultSet.getString("status"),
                                borrowResultSet.getDouble("return_fee")));
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
        borrowTableModel.fireTableDataChanged();

        // Tell the page button panel that the page has changed
        pageButtonPanel.setTotalPageCount(totalPageCount);
        pageButtonPanel.setCurrentPage(currentPage);
    }

}
	
