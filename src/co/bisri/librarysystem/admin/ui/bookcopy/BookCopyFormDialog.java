package co.bisri.librarysystem.admin.ui.bookcopy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import co.bisri.librarysystem.admin.ui.FormOperation;
import co.bisri.librarysystem.admin.ui.bookcopy.record.BookComboBoxItem;
import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopyEntity;

/**
 * Form Dialog for adding and updating copies
 *
 * @author Rian Reyes
 */
public class BookCopyFormDialog extends JDialog {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Back reference to owning panel
    protected BookCopyManagementPanel bookCopyManagementPanel;

    // Content Panel for input fields
    private final JPanel jpnlContent;

    // Old book copy, for updating
    private BookCopyEntity oldBookCopy;

    // Header label
    private final JLabel jlblHeader;

    // SQL operation to perform when OK is clicked (insert or update)
    private FormOperation currentOperation;

    // Field inputs
    private final JComboBox<BookComboBoxItem> jcmbBookIsbn;
    private final JComboBox<String> jcmbStatus;
    private final JComboBox<String> jcmbBorrowStatus;
    private final JTextField jtxtfldCopyNo;
    private final JTextField jtxtfldDateAcquired;
    private final JTextField jtxtfldCurrentWorth;

    public BookCopyFormDialog() {

        /* Dialog properties */
        setTitle("Save Book Copy");
        setBounds(100, 100, 450, 350);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        /* END OF Dialog properties */

        /* jpnlContent */
        jpnlContent = new JPanel();
        jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
        getContentPane().add(jpnlContent, BorderLayout.CENTER);
        GridBagLayout gbl_jpnlContent = new GridBagLayout();
        gbl_jpnlContent.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        jpnlContent.setLayout(gbl_jpnlContent);
        /* END OF jpnlContent */

        /* jlblHeader */
        jlblHeader = new JLabel("... Copy");
        jlblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        jlblHeader.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        GridBagConstraints gbc_jlblHeader = new GridBagConstraints();
        gbc_jlblHeader.anchor = GridBagConstraints.WEST;
        gbc_jlblHeader.gridwidth = 2;
        gbc_jlblHeader.insets = new Insets(0, 0, 5, 5);
        gbc_jlblHeader.gridx = 0;
        gbc_jlblHeader.gridy = 0;
        jpnlContent.add(jlblHeader, gbc_jlblHeader);
        /* END OF jlblHeader */

        /* jlblBookIsbn */
        JLabel jlblBookIsbn = new JLabel("Book:");
        jlblBookIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblBookIsbn = new GridBagConstraints();
        gbc_jlblBookIsbn.insets = new Insets(0, 0, 5, 5);
        gbc_jlblBookIsbn.anchor = GridBagConstraints.EAST;
        gbc_jlblBookIsbn.gridx = 0;
        gbc_jlblBookIsbn.gridy = 1;
        jpnlContent.add(jlblBookIsbn, gbc_jlblBookIsbn);
        /* END OF jlblBookIsbn */

        /* jcmbBookIsbn */
        jcmbBookIsbn = new JComboBox<>();
        jcmbBookIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jcmbBookIsbn = new GridBagConstraints();
        gbc_jcmbBookIsbn.insets = new Insets(0, 0, 5, 5);
        gbc_jcmbBookIsbn.fill = GridBagConstraints.HORIZONTAL;
        gbc_jcmbBookIsbn.gridx = 1;
        gbc_jcmbBookIsbn.gridy = 1;
        jpnlContent.add(jcmbBookIsbn, gbc_jcmbBookIsbn);
        /* END OF jcmbBookIsbn */

        /* jlblCopyNo */
        JLabel jlblCopyNo = new JLabel("Copy No:");
        jlblCopyNo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblCopyNo = new GridBagConstraints();
        gbc_jlblCopyNo.anchor = GridBagConstraints.EAST;
        gbc_jlblCopyNo.insets = new Insets(0, 0, 5, 5);
        gbc_jlblCopyNo.gridx = 0;
        gbc_jlblCopyNo.gridy = 2;
        jpnlContent.add(jlblCopyNo, gbc_jlblCopyNo);
        /* END OF jlblCopyNo */

        /* jtxtfldCopyNo */
        jtxtfldCopyNo = new JTextField();
        jtxtfldCopyNo.setMargin(new Insets(5, 5, 5, 5));
        jtxtfldCopyNo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jtxtfldCopyNo = new GridBagConstraints();
        gbc_jtxtfldCopyNo.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldCopyNo.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldCopyNo.gridx = 1;
        gbc_jtxtfldCopyNo.gridy = 2;
        jpnlContent.add(jtxtfldCopyNo, gbc_jtxtfldCopyNo);
        /* END OF jtxtfldCopyNo */

        /* jlblDateAcquired */
        JLabel jlblDateAcquired = new JLabel("Date Acquired:");
        jlblDateAcquired.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblDateAcquired = new GridBagConstraints();
        gbc_jlblDateAcquired.anchor = GridBagConstraints.EAST;
        gbc_jlblDateAcquired.insets = new Insets(0, 0, 5, 5);
        gbc_jlblDateAcquired.gridx = 0;
        gbc_jlblDateAcquired.gridy = 3;
        jpnlContent.add(jlblDateAcquired, gbc_jlblDateAcquired);
        /* END OF jlblDateAcquired */

        /* jtxtfldDateAcquired */
        jtxtfldDateAcquired = new JTextField();
        jtxtfldDateAcquired.setMargin(new Insets(5, 5, 5, 5));
        jtxtfldDateAcquired.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldDateAcquired.setColumns(10);
        GridBagConstraints gbc_jtxtfldDateAcquired = new GridBagConstraints();
        gbc_jtxtfldDateAcquired.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldDateAcquired.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldDateAcquired.gridx = 1;
        gbc_jtxtfldDateAcquired.gridy = 3;
        jpnlContent.add(jtxtfldDateAcquired, gbc_jtxtfldDateAcquired);
        /* END OF jtxtfldDateAcquired */

        /* jlblCurrentWorth */
        JLabel jlblCurrentWorth = new JLabel("Worth:");
        jlblCurrentWorth.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblCurrentWorth = new GridBagConstraints();
        gbc_jlblCurrentWorth.anchor = GridBagConstraints.EAST;
        gbc_jlblCurrentWorth.insets = new Insets(0, 0, 5, 5);
        gbc_jlblCurrentWorth.gridx = 0;
        gbc_jlblCurrentWorth.gridy = 4;
        jpnlContent.add(jlblCurrentWorth, gbc_jlblCurrentWorth);
        /* END OF jlblCurrentWorth */

        /* jtxtfldCurrentWorth */
        jtxtfldCurrentWorth = new JTextField();
        jtxtfldCurrentWorth.setMargin(new Insets(5, 5, 5, 5));
        jtxtfldCurrentWorth.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldCurrentWorth.setColumns(10);
        GridBagConstraints gbc_jtxtfldCurrentWorth = new GridBagConstraints();
        gbc_jtxtfldCurrentWorth.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldCurrentWorth.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldCurrentWorth.gridx = 1;
        gbc_jtxtfldCurrentWorth.gridy = 4;
        jpnlContent.add(jtxtfldCurrentWorth, gbc_jtxtfldCurrentWorth);
        /* END OF jtxtfldCurrentWorth */

        /* jlblStatus */
        JLabel jlblStatus = new JLabel("Condition:");
        jlblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblStatus = new GridBagConstraints();
        gbc_jlblStatus.anchor = GridBagConstraints.EAST;
        gbc_jlblStatus.insets = new Insets(0, 0, 5, 5);
        gbc_jlblStatus.gridx = 0;
        gbc_jlblStatus.gridy = 5;
        jpnlContent.add(jlblStatus, gbc_jlblStatus);
        /* END OF jlblStatus */

        /* jcmbStatus */
        jcmbStatus = new JComboBox<>();
        jcmbStatus.addItem("PERFECT");
        jcmbStatus.addItem("GOOD");
        jcmbStatus.addItem("MEDIOCRE");
        jcmbStatus.addItem("BAD");
        jcmbStatus.addItem("UNUSABLE");
        jcmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jcmbStatus = new GridBagConstraints();
        gbc_jcmbStatus.insets = new Insets(0, 0, 5, 5);
        gbc_jcmbStatus.fill = GridBagConstraints.HORIZONTAL;
        gbc_jcmbStatus.gridx = 1;
        gbc_jcmbStatus.gridy = 5;
        jpnlContent.add(jcmbStatus, gbc_jcmbStatus);
        /* END OF jcmbStatus */

        /* jlblBorrowStatus */
        JLabel jlblBorrowStatus = new JLabel("Borrow Status:");
        jlblBorrowStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblBorrowStatus = new GridBagConstraints();
        gbc_jlblBorrowStatus.insets = new Insets(0, 0, 5, 5);
        gbc_jlblBorrowStatus.anchor = GridBagConstraints.EAST;
        gbc_jlblBorrowStatus.gridx = 0;
        gbc_jlblBorrowStatus.gridy = 6;
        jpnlContent.add(jlblBorrowStatus, gbc_jlblBorrowStatus);
        /* END OF jlblBorrowStatus */

        /* jcmbBorrowStatus */
        jcmbBorrowStatus = new JComboBox<>();
        jcmbBorrowStatus.addItem("AVAILABLE");
        jcmbBorrowStatus.addItem("BORROWED");
        jcmbBorrowStatus.addItem("LOST");
        jcmbBorrowStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jcmbBorrowStatus = new GridBagConstraints();
        gbc_jcmbBorrowStatus.insets = new Insets(0, 0, 5, 5);
        gbc_jcmbBorrowStatus.fill = GridBagConstraints.HORIZONTAL;
        gbc_jcmbBorrowStatus.gridx = 1;
        gbc_jcmbBorrowStatus.gridy = 6;
        jpnlContent.add(jcmbBorrowStatus, gbc_jcmbBorrowStatus);
        /* END OF jcmbBorrowStatus */

        /* jpnlButtonActions */
        JPanel jpnlButtonActions = new JPanel();
        jpnlButtonActions.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jpnlButtonActions.setBorder(new EmptyBorder(0, 10, 10, 10));
        getContentPane().add(jpnlButtonActions, BorderLayout.SOUTH);
        /* END OF jpnlButtons */

        /* jbtnOk */
        JButton jbtnOk = new JButton("OK");
        jbtnOk.setActionCommand("OK");
        jbtnOk.addActionListener((event) -> {

            // BOOK ISBN
            int selectedBookIndex = jcmbBookIsbn.getSelectedIndex();
            if (selectedBookIndex == -1) {
                JOptionPane.showMessageDialog(
                        bookCopyManagementPanel,
                        "Please select a book first to add a copy.",
                        "Invalid input!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String isbn = ((BookComboBoxItem) jcmbBookIsbn.getSelectedItem()).isbn();

            // COPY NUMBER
            int copyNo = 0;
            try {
                copyNo = Integer.parseInt(jtxtfldCopyNo.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        bookCopyManagementPanel,
                        "Please enter a valid number for copy number.",
                        "Invalid input!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // DATE ACQUIRED
            LocalDate dateAcquired = null;
            try {
                dateAcquired = LocalDate.parse(jtxtfldDateAcquired.getText());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(
                        bookCopyManagementPanel,
                        "Please enter a valid date (yyyy-MM-dd) for date acquired field.",
                        "Invalid input!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // CURRENT WORTH
            double currentWorth = 0;
            try {
                currentWorth = Double.parseDouble(jtxtfldCurrentWorth.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        bookCopyManagementPanel,
                        "Please enter a valid price for worth.",
                        "Invalid input!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create a new BookCopy record
            BookCopyEntity bookCopy =
                    new BookCopyEntity(
                            isbn,
                            copyNo,
                            dateAcquired,
                            (String) jcmbStatus.getSelectedItem(),
                            currentWorth,
                            (String) jcmbBorrowStatus.getSelectedItem());

            // Save it to database with a SwingWorker Thread in background
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try (Connection connection = bookCopyManagementPanel.getConnection()) {
                        switch (currentOperation) {

                            // If form is in insert mode, perform an SQL INSERT
                            case INSERT:
                                try (PreparedStatement bookCopyInsertStatement = connection.prepareStatement(
                                        "INSERT INTO book_copy VALUES(?, ?, ?, ?, ?, ?)")) {
                                    bookCopyInsertStatement.setString(1, bookCopy.isbn());
                                    bookCopyInsertStatement.setInt(2, bookCopy.copyNo());
                                    bookCopyInsertStatement.setString(3, bookCopy.dateAcquired().toString());
                                    bookCopyInsertStatement.setString(4, bookCopy.status());
                                    bookCopyInsertStatement.setDouble(5, bookCopy.currentWorth());
                                    bookCopyInsertStatement.setString(6, bookCopy.borrowStatus());
                                    bookCopyInsertStatement.execute();
                                }
                                break;

                            // Else, perform an SQL UPDATE with the old name
                            case UPDATE:
                                try (PreparedStatement bookCopyUpdateStatement = connection.prepareStatement(
                                        "UPDATE book_copy SET status = ?, current_worth = ?, borrow_status = ? " +
                                                "WHERE isbn = ? AND copy_no = ?")) {
                                    bookCopyUpdateStatement.setString(1, bookCopy.status());
                                    bookCopyUpdateStatement.setDouble(2, bookCopy.currentWorth());
                                    bookCopyUpdateStatement.setString(3, bookCopy.borrowStatus());
                                    bookCopyUpdateStatement.setString(4, oldBookCopy.isbn());
                                    bookCopyUpdateStatement.setInt(5, oldBookCopy.copyNo());
                                    bookCopyUpdateStatement.execute();
                                }
                                break;

                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        // If success, show dialog
                        JOptionPane.showMessageDialog(
                                bookCopyManagementPanel,
                                "Successfully saved copy to database. Refreshing your panel.",
                                "Success!",
                                JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                        bookCopyManagementPanel.refreshPage();
                    } catch (InterruptedException | ExecutionException e) {
                        // If an SQL Primary Key Integrity Constraint error occurred, display a proper message.
                        if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                            SQLIntegrityConstraintViolationException exc = (SQLIntegrityConstraintViolationException) e.getCause();
                            if (exc.getErrorCode() == 1062) {
                                JOptionPane.showMessageDialog(
                                        bookCopyManagementPanel,
                                        "Copy number already exists for the specified book. Please specify another number.",
                                        "Cannot create copy.",
                                        JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }

                        // If an error occured, show dialog and inform user.
                        JOptionPane.showMessageDialog(
                                bookCopyManagementPanel,
                                "An error occurred while trying to save to database.\n\nError: " + e.getMessage(),
                                "Database access error!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();

            // Close the dialog
        });
        jbtnOk.setBackground(Color.WHITE);
        jbtnOk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jpnlButtonActions.add(jbtnOk);
        getRootPane().setDefaultButton(jbtnOk);
        /* END OF jbtnOk */

        /* jbtnCancel */
        JButton jbtnCancel = new JButton("Cancel");
        jbtnCancel.setActionCommand("Cancel");
        jbtnCancel.setBackground(Color.WHITE);
        jbtnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnCancel.addActionListener((event) -> {
            setVisible(false);
        });
        jpnlButtonActions.add(jbtnCancel);
        /* END OF jbtnCancel */
    }

    /**
     * Prepares the form for inserting a new copy.
     */
    public void initialize() {
        // Change mode to insert
        currentOperation = FormOperation.INSERT;

        // Change the header
        jlblHeader.setText("Add Copy");

        // Reset the fields and input components
        oldBookCopy = null;
        jtxtfldCopyNo.setEditable(true);
        jtxtfldCopyNo.setText("");
        jtxtfldDateAcquired.setEditable(true);
        jtxtfldDateAcquired.setText("");
        jtxtfldCurrentWorth.setText("");

        // Fetch all books then display in combo box
        try (Connection connection = bookCopyManagementPanel.getConnection();
             Statement retrieveBooksStatement = connection.createStatement();
             ResultSet bookResultSet = retrieveBooksStatement.executeQuery("SELECT isbn, title FROM book")) {

            // List of all books as combo box items
            List<BookComboBoxItem> comboBoxItems = new ArrayList<>();

            // Parse each record into a BookComboBoxItem
            while (bookResultSet.next()) {
                comboBoxItems.add(
                        new BookComboBoxItem(
                                bookResultSet.getString("isbn"),
                                bookResultSet.getString("title")));
            }

            // Create a ComboBoxModel then set it as jcmbBookIsbn's model
            DefaultComboBoxModel<BookComboBoxItem> comboBoxModel = new DefaultComboBoxModel<>();
            comboBoxModel.addAll(comboBoxItems);
            jcmbBookIsbn.setModel(comboBoxModel);

            jcmbBookIsbn.setEnabled(true);
        } catch (SQLException e) {
            // If an error occured, show dialog and inform user.
            JOptionPane.showMessageDialog(
                    bookCopyManagementPanel,
                    "An error occurred while populating book choices.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database access error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Prepares the form for updating a book copy.
     */
    public void initialize(BookCopyEntity bookCopy) {
        // Change mode to update
        currentOperation = FormOperation.UPDATE;

        // Change the header
        jlblHeader.setText("Modify Copy");

        // Reset the fields and input components
        oldBookCopy = bookCopy;
        jtxtfldCopyNo.setText("" + bookCopy.copyNo());
        jtxtfldCopyNo.setEditable(false);
        jtxtfldDateAcquired.setText("" + bookCopy.dateAcquired());
        jtxtfldDateAcquired.setEditable(false);
        jtxtfldCurrentWorth.setText("" + bookCopy.currentWorth());
        jcmbStatus.setSelectedItem(bookCopy.status());
        jcmbBorrowStatus.setSelectedItem(bookCopy.borrowStatus());

        // Fetch all books then display in combo box
        try (Connection connection = bookCopyManagementPanel.getConnection();
             Statement retrieveBooksStatement = connection.createStatement();
             ResultSet bookResultSet = retrieveBooksStatement.executeQuery("SELECT isbn, title FROM book")) {

            // List of all books as combo box items
            List<BookComboBoxItem> comboBoxItems = new ArrayList<>();

            // Parse each record into a BookComboBoxItem
            while (bookResultSet.next()) {
                comboBoxItems.add(
                        new BookComboBoxItem(
                                bookResultSet.getString("isbn"),
                                bookResultSet.getString("title")));
            }

            // Create a ComboBoxModel then set it as jcmbBookIsbn's model
            DefaultComboBoxModel<BookComboBoxItem> comboBoxModel = new DefaultComboBoxModel<>();
            comboBoxModel.addAll(comboBoxItems);
            jcmbBookIsbn.setModel(comboBoxModel);

            // Set book choice to current book copy's reference
            jcmbBookIsbn.setSelectedItem(new BookComboBoxItem(bookCopy.isbn(), null));

            jcmbBookIsbn.setEnabled(false);
        } catch (SQLException e) {
            // If an error occured, show dialog and inform user.
            JOptionPane.showMessageDialog(
                    bookCopyManagementPanel,
                    "An error occurred while populating book choices.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database access error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

}
