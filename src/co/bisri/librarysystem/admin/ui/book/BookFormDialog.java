package co.bisri.librarysystem.admin.ui.book;

import co.bisri.librarysystem.admin.ui.FormOperation;
import co.bisri.librarysystem.admin.ui.book.record.BookCategoryComboBoxItem;
import co.bisri.librarysystem.admin.ui.book.record.BookEntity;
import co.bisri.librarysystem.admin.ui.book.record.BookTableRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.Book;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Form Dialog for adding and updating books
 *
 * @author Bismillah Constantino
 * @author Rian Reyes
 */
public class BookFormDialog extends JDialog {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Back reference to owning panel
    protected BookManagementPanel bookManagementPanel;

    // Content Panel for input fields
    private final JPanel jpnlContent;

    // Old Book
    private BookEntity oldBook;

    // Header label
    private JLabel jlblHeader;
    private JTextField jtxtfldIsbn;
    private JTextField jtxtfldTitle;
    private JComboBox<BookCategoryComboBoxItem> jcmbCategory;
    private JTextField jtxtfldAuthor;
    private JTextField jtxtfldPublisher;
    private JTextField jtxtfldPublishedOn;

    // SQL operation to perform when OK is clicked (insert or update)
    private FormOperation currentOperation;

    public BookFormDialog() {
        // Reference for nested anonymous contexts
        BookFormDialog formDialog = this;

        /* Dialog properties */
        setTitle("Save Book");
        setBounds(100, 100, 500, 350);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        /* END OF Dialog properties */

        /* jpnlContent */
        jpnlContent = new JPanel();
        jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
        getContentPane().add(jpnlContent, BorderLayout.CENTER);
        GridBagLayout gbl_jpnlContent = new GridBagLayout();
        gbl_jpnlContent.columnWeights = new double[]{0.0, 1.0D, Double.MIN_VALUE};
        jpnlContent.setLayout(gbl_jpnlContent);
        /* END OF jpnlContent */

        /* jlblHeader */
        jlblHeader = new JLabel("Book");
        jlblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        jlblHeader.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        GridBagConstraints gbc_jlblHeader = new GridBagConstraints();
        gbc_jlblHeader.anchor = GridBagConstraints.WEST;
        gbc_jlblHeader.gridwidth = 4;
        gbc_jlblHeader.insets = new Insets(0, 0, 5, 5);
        gbc_jlblHeader.gridx = 0;
        gbc_jlblHeader.gridy = 0;
        jpnlContent.add(jlblHeader, gbc_jlblHeader);
        /* END OF jpnlContent */

        /* jlblIsbn */
        JLabel jlblIsbn = new JLabel("ISBN");
        jlblIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblIsbn = new GridBagConstraints();
        gbc_jlblIsbn.anchor = GridBagConstraints.EAST;
        gbc_jlblIsbn.insets = new Insets(0, 0, 5, 5);
        gbc_jlblIsbn.gridx = 0;
        gbc_jlblIsbn.gridy = 1;
        jpnlContent.add(jlblIsbn, gbc_jlblIsbn);
        /* END OF jlblIsbn */

        /* jtxtfldIsbn */
        jtxtfldIsbn = new JTextField();
        jtxtfldIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldIsbn.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldIsbn = new GridBagConstraints();
        gbc_jtxtfldIsbn.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldIsbn.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldIsbn.gridx = 1;
        gbc_jtxtfldIsbn.gridy = 1;
        jpnlContent.add(jtxtfldIsbn, gbc_jtxtfldIsbn);
        /* END OF jtxtfldIsbn */

        /* jlblTitle */
        JLabel jlblTitle = new JLabel("Title");
        jlblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblTitle = new GridBagConstraints();
        gbc_jlblTitle.anchor = GridBagConstraints.EAST;
        gbc_jlblTitle.insets = new Insets(0, 0, 5, 5);
        gbc_jlblTitle.gridx = 0;
        gbc_jlblTitle.gridy = 2;
        jpnlContent.add(jlblTitle, gbc_jlblTitle);
        /* END OF jlblTitle */

        /* jtxtfldTitle */
        jtxtfldTitle = new JTextField();
        jtxtfldTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldTitle.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldTitle = new GridBagConstraints();
        gbc_jtxtfldTitle.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldTitle.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldTitle.gridx = 1;
        gbc_jtxtfldTitle.gridy = 2;
        jpnlContent.add(jtxtfldTitle, gbc_jtxtfldTitle);
        /* END OF jtxtfldTitle */

        /* jlblCategoryName */
        JLabel jlblCategoryName = new JLabel("Category Name");
        jlblCategoryName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblCategoryName = new GridBagConstraints();
        gbc_jlblCategoryName.anchor = GridBagConstraints.EAST;
        gbc_jlblCategoryName.insets = new Insets(0, 0, 5, 5);
        gbc_jlblCategoryName.gridx = 0;
        gbc_jlblCategoryName.gridy = 3;
        jpnlContent.add(jlblCategoryName, gbc_jlblCategoryName);
        /* END OF jlblCategoryName */

        /* jcmbCategory */
        jcmbCategory = new JComboBox<>();
        jcmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jcmbCategory = new GridBagConstraints();
        gbc_jcmbCategory.insets = new Insets(0, 0, 5, 5);
        gbc_jcmbCategory.fill = GridBagConstraints.HORIZONTAL;
        gbc_jcmbCategory.gridx = 1;
        gbc_jcmbCategory.gridy = 3;
        jpnlContent.add(jcmbCategory, gbc_jcmbCategory);
        /* END OF jcmbCategory */

        /* jlblAuthor */
        JLabel jlblAuthor = new JLabel("Author");
        jlblAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblAuthor = new GridBagConstraints();
        gbc_jlblAuthor.anchor = GridBagConstraints.EAST;
        gbc_jlblAuthor.insets = new Insets(0, 0, 5, 5);
        gbc_jlblAuthor.gridx = 0;
        gbc_jlblAuthor.gridy = 4;
        jpnlContent.add(jlblAuthor, gbc_jlblAuthor);
        /* END OF jlblAuthor */

        /* jtxtfldAuthor */
        jtxtfldAuthor = new JTextField();
        jtxtfldAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldAuthor.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldAuthor = new GridBagConstraints();
        gbc_jtxtfldAuthor.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldAuthor.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldAuthor.gridx = 1;
        gbc_jtxtfldAuthor.gridy = 4;
        jpnlContent.add(jtxtfldAuthor, gbc_jtxtfldAuthor);
        /* END OF jtxtfldAuthor */

        /* jlblPublisher */
        JLabel jlblPublisher = new JLabel("Publisher");
        jlblPublisher.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblPublisher = new GridBagConstraints();
        gbc_jlblPublisher.anchor = GridBagConstraints.EAST;
        gbc_jlblPublisher.insets = new Insets(0, 0, 5, 5);
        gbc_jlblPublisher.gridx = 0;
        gbc_jlblPublisher.gridy = 5;
        jpnlContent.add(jlblPublisher, gbc_jlblPublisher);
        /* END OF jlblPublisher */

        /* jtxtfldPublisher */
        jtxtfldPublisher = new JTextField();
        jtxtfldPublisher.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldPublisher.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldPublisher = new GridBagConstraints();
        gbc_jtxtfldPublisher.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldPublisher.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldPublisher.gridx = 1;
        gbc_jtxtfldPublisher.gridy = 5;
        jpnlContent.add(jtxtfldPublisher, gbc_jtxtfldPublisher);
        /* END OF jtxtfldPublisher */

        /* jlblPublishedOn */
        JLabel jlblPublishedOn = new JLabel("Published On");
        jlblPublishedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblPublishedOn = new GridBagConstraints();
        gbc_jlblPublishedOn.anchor = GridBagConstraints.EAST;
        gbc_jlblPublishedOn.insets = new Insets(0, 0, 5, 5);
        gbc_jlblPublishedOn.gridx = 0;
        gbc_jlblPublishedOn.gridy = 6;
        jpnlContent.add(jlblPublishedOn, gbc_jlblPublishedOn);
        /* END OF jlblPublishedOn */

        /* jtxtfldPublishedOn */
        jtxtfldPublishedOn = new JTextField();
        jtxtfldPublishedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldPublishedOn.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldPublishedOn = new GridBagConstraints();
        gbc_jtxtfldPublishedOn.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldPublishedOn.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldPublishedOn.gridx = 1;
        gbc_jtxtfldPublishedOn.gridy = 6;
        jpnlContent.add(jtxtfldPublishedOn, gbc_jtxtfldPublishedOn);
        /* END OF jtxtfldPublishedOn */

        /* jpnlButtonActions */
        JPanel jpnlButtonActions = new JPanel();
        jpnlButtonActions.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jpnlButtonActions.setBorder(new EmptyBorder(0, 10, 10, 10));
        getContentPane().add(jpnlButtonActions, BorderLayout.SOUTH);
        /* END OF jpnlButtonActions */

        /* jbtnOk */
        JButton jbtnOk = new JButton("OK");
        jbtnOk.setActionCommand("OK");
        jbtnOk.addActionListener((event) -> {
            // ISBN
            String isbn = jtxtfldIsbn.getText();
            if (isbn.contentEquals("") || isbn.length() != 13) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for ISBN. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- must be 13 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // TITLE
            String title = jtxtfldTitle.getText();
            if (title.contentEquals("") || title.length() > 64) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for title. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 64 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // CATEGORY
            int selectedCategoryItemIndex = jcmbCategory.getSelectedIndex();
            String categoryName = selectedCategoryItemIndex == -1 || selectedCategoryItemIndex == 0 ?
                    null : ((BookCategoryComboBoxItem) jcmbCategory.getSelectedItem()).name();

            // AUTHOR
            String author = jtxtfldAuthor.getText();
            if (author.contentEquals("") || author.length() > 128) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for Author. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 128 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // PUBLISHER
            String publisher = jtxtfldPublisher.getText();
            if (publisher.contentEquals("") || publisher.length() > 128) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for publisher. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 128 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // PUBLISHED ON
            LocalDate publishedOn = null;
            try {
                publishedOn = LocalDate.parse(jtxtfldPublishedOn.getText());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid date for published on. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- format should be yyyy-MM-dd",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            /* END OF Validation Layer */

            // Create a new Member record
            BookTableRecord book = new BookTableRecord(isbn, categoryName, title, author, publishedOn, publisher);

            // Save it to database with a SwingWorker Thread in background
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try (Connection connection = bookManagementPanel.getConnection()) {
                        switch (currentOperation) {

                            // If form is in insert mode, perform an SQL INSERT
                            case INSERT:
                                try (PreparedStatement booksInsertStatement = connection.prepareStatement(
                                        "INSERT INTO book(isbn, category_name, title, author, published_on, publisher) VALUES(?,?,?,?,?,?)")) {
                                    booksInsertStatement.setString(1, book.isbn());
                                    if (book.categoryName() == null)
                                        booksInsertStatement.setNull(2, Types.VARCHAR);
                                    else
                                        booksInsertStatement.setString(2, book.categoryName());
                                    booksInsertStatement.setString(3, book.title());
                                    booksInsertStatement.setString(4, book.author());
                                    booksInsertStatement.setString(5, book.publishedOn().toString());
                                    booksInsertStatement.setString(6, book.publisher());
                                    booksInsertStatement.execute();
                                }

                                break;

                            // Else, perform an SQL UPDATE with the old name
                            case UPDATE:
                                try (PreparedStatement booksUpdateStatement = connection.prepareStatement(
                                        "UPDATE book SET isbn = ?, category_name = ?, title = ?, author = ?, published_on = ?, "
                                                + "publisher = ? WHERE isbn = ?")) {
                                    booksUpdateStatement.setString(1, book.isbn());
                                    if (book.categoryName() == null)
                                        booksUpdateStatement.setNull(2, Types.VARCHAR);
                                    else
                                        booksUpdateStatement.setString(2, book.categoryName());
                                    booksUpdateStatement.setString(3, book.title());
                                    booksUpdateStatement.setString(4, book.author());
                                    booksUpdateStatement.setString(5, book.publishedOn().toString());
                                    booksUpdateStatement.setString(6, book.publisher());
                                    booksUpdateStatement.setString(7, oldBook.isbn());
                                    booksUpdateStatement.execute();
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
                                bookManagementPanel,
                                "Successfully saved book to database. Refreshing your panel.",
                                "Success!",
                                JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                        bookManagementPanel.refreshPage();
                    } catch (InterruptedException | ExecutionException e) {
                        // If an error occured, show dialog and inform user.
                        JOptionPane.showMessageDialog(
                                bookManagementPanel,
                                "An error occurred while trying to save to database.\n\nError: " + e.getMessage(),
                                "Database access error!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();


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
        jpnlButtonActions.add(jbtnCancel);
        /* END OF jbtnCancel */
    }

    /**
     * Prepares the form for inserting a new book.
     */
    public void initialize() {
        // Change mode to insert
        currentOperation = FormOperation.INSERT;

        // Change the header
        jlblHeader.setText("Add Book");

        // Reset the fields and input components
        oldBook = null;
        jtxtfldIsbn.setText("");
        jcmbCategory.removeAllItems();
        jtxtfldAuthor.setText("");
        jtxtfldPublisher.setText("");
        jtxtfldTitle.setText("");
        jtxtfldPublishedOn.setText("");

        // Fetch all categories then display in combo box
        try (Connection connection = bookManagementPanel.getConnection();
             Statement retrieveCategoriesStatement = connection.createStatement();
             ResultSet categoryResultSet = retrieveCategoriesStatement.executeQuery("SELECT name FROM book_category")) {

            // List of all categories as combo box items
            List<BookCategoryComboBoxItem> comboBoxItems = new ArrayList<>();
            comboBoxItems.add(new BookCategoryComboBoxItem(""));

            // Parse each record into a BookCategoryComboBoxItem
            while (categoryResultSet.next()) {
                comboBoxItems.add(
                        new BookCategoryComboBoxItem(categoryResultSet.getString("name")));
            }

            // Create a ComboBoxModel then set it as jcmbCategory's model
            DefaultComboBoxModel<BookCategoryComboBoxItem> comboBoxModel = new DefaultComboBoxModel<>();
            comboBoxModel.addAll(comboBoxItems);
            jcmbCategory.setModel(comboBoxModel);
        } catch (SQLException e) {
            // If an error occured, show dialog and inform user.
            JOptionPane.showMessageDialog(
                    bookManagementPanel,
                    "An error occurred while populating category choices.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database access error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Prepares the form for updating an existing book.
     */
    public void initialize(BookEntity book) {
        // Change mode to update
        currentOperation = FormOperation.UPDATE;

        // Change the header
        jlblHeader.setText("Modify Book");

        // Reset the fields and input components
        oldBook = book;
        jtxtfldIsbn.setText(book.isbn());
        jtxtfldAuthor.setText(book.author());
        jtxtfldPublisher.setText(book.publisher());
        jtxtfldTitle.setText(book.title());
        jtxtfldPublishedOn.setText(book.publishedOn().toString());

        // Fetch all categories then display in combo box
        try (Connection connection = bookManagementPanel.getConnection();
             Statement retrieveCategoriesStatement = connection.createStatement();
             ResultSet categoryResultSet = retrieveCategoriesStatement.executeQuery("SELECT name FROM book_category")) {

            // List of all categories as combo box items
            List<BookCategoryComboBoxItem> comboBoxItems = new ArrayList<>();
            comboBoxItems.add(new BookCategoryComboBoxItem(""));

            // Parse each record into a BookCategoryComboBoxItem
            while (categoryResultSet.next()) {
                comboBoxItems.add(
                        new BookCategoryComboBoxItem(categoryResultSet.getString("name")));
            }

            // Create a ComboBoxModel then set it as jcmbCategory's model
            DefaultComboBoxModel<BookCategoryComboBoxItem> comboBoxModel = new DefaultComboBoxModel<>();
            comboBoxModel.addAll(comboBoxItems);
            jcmbCategory.setModel(comboBoxModel);

            // Set the selected item to book's current category
            jcmbCategory.setSelectedItem(new BookCategoryComboBoxItem(book.categoryName()));
        } catch (SQLException e) {
            // If an error occured, show dialog and inform user.
            JOptionPane.showMessageDialog(
                    bookManagementPanel,
                    "An error occurred while populating category choices.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database access error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

}
