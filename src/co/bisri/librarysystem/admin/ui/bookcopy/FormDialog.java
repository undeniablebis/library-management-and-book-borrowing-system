package co.bisri.librarysystem.admin.ui.bookcopy;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import co.bisri.librarysystem.admin.ui.bookcopy.record.BookComboBoxItem;
import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopy;
import co.bisri.librarysystem.admin.ui.util.FormOperation;

/**
 * Form Dialog for adding and updating categories
 * 
 * @author Rian Reyes
 *
 */
public class FormDialog extends JDialog {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to
	 * remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Back reference to owning panel.
	 */
	protected BookCopyManagementPanel bookCopyManagementPanel;

	// Content Panel for input fields
	private final JPanel jpnlContent;
	
	// Header label
	private JLabel jlblHeader;
	
	// SQL operation to perform when OK is clicked (insert or update)
	private FormOperation currentOperation;
	
	// Field inputs
	private JComboBox<BookComboBoxItem> jcmbBookIsbn;
	private JComboBox<String> jcmbStatus;
	private JComboBox<String> jcmbBorrowStatus;
	private JTextField jtxtfldCopyNo;
	private JTextField jtxtfldDateAcquired;
	private JTextField jtxtfldCurrentWorth;

	/**
	 * Create the dialog.
	 */
	public FormDialog() {
		
		/* Dialog properties */
		setTitle("Save Book Copy");
		setBounds(100, 100, 450, 217);
		getContentPane().setLayout(new BorderLayout());
		/* END OF Dialog properties */

		/* jpnlContent */
		jpnlContent = new JPanel();
		jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
		getContentPane().add(jpnlContent, BorderLayout.CENTER);
		GridBagLayout gbl_jpnlContent = new GridBagLayout();
		gbl_jpnlContent.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_jpnlContent.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_jpnlContent.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_jpnlContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		jpnlContent.setLayout(gbl_jpnlContent);
		/* END OF jpnlContent */

		/* jlblHeader */
		jlblHeader = new JLabel("... Copy");
		jlblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		jlblHeader.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		GridBagConstraints gbc_jlblHeader = new GridBagConstraints();
		gbc_jlblHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblHeader.gridwidth = 2;
		gbc_jlblHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jlblHeader.gridx = 0;
		gbc_jlblHeader.gridy = 0;
		jpnlContent.add(jlblHeader, gbc_jlblHeader);
		/* END OF jlblHeader */

		/* jlblName */
		JLabel jlblBookIsbn = new JLabel("Book:");
		jlblBookIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblBookIsbn = new GridBagConstraints();
		gbc_jlblBookIsbn.insets = new Insets(0, 0, 5, 5);
		gbc_jlblBookIsbn.anchor = GridBagConstraints.EAST;
		gbc_jlblBookIsbn.gridx = 0;
		gbc_jlblBookIsbn.gridy = 1;
		jpnlContent.add(jlblBookIsbn, gbc_jlblBookIsbn);
		/* END OF jtxtfldName */

		/* jlblDescription */
		
		jcmbBookIsbn = new JComboBox<>();
		jcmbBookIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jcmbBookIsbn = new GridBagConstraints();
		gbc_jcmbBookIsbn.insets = new Insets(0, 0, 5, 5);
		gbc_jcmbBookIsbn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbBookIsbn.gridx = 1;
		gbc_jcmbBookIsbn.gridy = 1;
		jpnlContent.add(jcmbBookIsbn, gbc_jcmbBookIsbn);
		
		JLabel jlblCopyNo = new JLabel("Copy No:");
		jlblCopyNo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblCopyNo = new GridBagConstraints();
		gbc_jlblCopyNo.anchor = GridBagConstraints.EAST;
		gbc_jlblCopyNo.insets = new Insets(0, 0, 5, 5);
		gbc_jlblCopyNo.gridx = 2;
		gbc_jlblCopyNo.gridy = 1;
		jpnlContent.add(jlblCopyNo, gbc_jlblCopyNo);
		
		jtxtfldCopyNo = new JTextField();
		jtxtfldCopyNo.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldCopyNo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jtxtfldCopyNo = new GridBagConstraints();
		gbc_jtxtfldCopyNo.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldCopyNo.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldCopyNo.gridx = 3;
		gbc_jtxtfldCopyNo.gridy = 1;
		jpnlContent.add(jtxtfldCopyNo, gbc_jtxtfldCopyNo);
		jtxtfldCopyNo.setColumns(10);
		
		JLabel jlblDateAcquired = new JLabel("Date Acquired:");
		jlblDateAcquired.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblDateAcquired = new GridBagConstraints();
		gbc_jlblDateAcquired.anchor = GridBagConstraints.EAST;
		gbc_jlblDateAcquired.insets = new Insets(0, 0, 5, 5);
		gbc_jlblDateAcquired.gridx = 0;
		gbc_jlblDateAcquired.gridy = 2;
		jpnlContent.add(jlblDateAcquired, gbc_jlblDateAcquired);
		
		jtxtfldDateAcquired = new JTextField();
		jtxtfldDateAcquired.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldDateAcquired.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldDateAcquired.setColumns(10);
		GridBagConstraints gbc_jtxtfldDateAcquired = new GridBagConstraints();
		gbc_jtxtfldDateAcquired.insets = new Insets(0, 0, 5, 5);
		gbc_jtxtfldDateAcquired.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldDateAcquired.gridx = 1;
		gbc_jtxtfldDateAcquired.gridy = 2;
		jpnlContent.add(jtxtfldDateAcquired, gbc_jtxtfldDateAcquired);
		
		JLabel jlblCurrentWorth = new JLabel("Worth:");
		jlblCurrentWorth.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblCurrentWorth = new GridBagConstraints();
		gbc_jlblCurrentWorth.anchor = GridBagConstraints.EAST;
		gbc_jlblCurrentWorth.insets = new Insets(0, 0, 5, 5);
		gbc_jlblCurrentWorth.gridx = 2;
		gbc_jlblCurrentWorth.gridy = 2;
		jpnlContent.add(jlblCurrentWorth, gbc_jlblCurrentWorth);
		
		jtxtfldCurrentWorth = new JTextField();
		jtxtfldCurrentWorth.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldCurrentWorth.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldCurrentWorth.setColumns(10);
		GridBagConstraints gbc_jtxtfldCurrentWorth = new GridBagConstraints();
		gbc_jtxtfldCurrentWorth.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldCurrentWorth.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldCurrentWorth.gridx = 3;
		gbc_jtxtfldCurrentWorth.gridy = 2;
		jpnlContent.add(jtxtfldCurrentWorth, gbc_jtxtfldCurrentWorth);
		
		JLabel jlblStatus = new JLabel("Condition:");
		jlblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblStatus = new GridBagConstraints();
		gbc_jlblStatus.anchor = GridBagConstraints.EAST;
		gbc_jlblStatus.insets = new Insets(0, 0, 0, 5);
		gbc_jlblStatus.gridx = 0;
		gbc_jlblStatus.gridy = 3;
		jpnlContent.add(jlblStatus, gbc_jlblStatus);
		
		jcmbStatus = new JComboBox<>();
		jcmbStatus.addItem("PERFECT");
		jcmbStatus.addItem("GOOD");
		jcmbStatus.addItem("MEDIOCRE");
		jcmbStatus.addItem("BAD");
		jcmbStatus.addItem("UNUSABLE");
		jcmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jcmbStatus = new GridBagConstraints();
		gbc_jcmbStatus.insets = new Insets(0, 0, 0, 5);
		gbc_jcmbStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbStatus.gridx = 1;
		gbc_jcmbStatus.gridy = 3;
		jpnlContent.add(jcmbStatus, gbc_jcmbStatus);
		
		JLabel jlblBorrowStatus = new JLabel("Borrow Status:");
		jlblBorrowStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblBorrowStatus = new GridBagConstraints();
		gbc_jlblBorrowStatus.insets = new Insets(0, 0, 0, 5);
		gbc_jlblBorrowStatus.anchor = GridBagConstraints.EAST;
		gbc_jlblBorrowStatus.gridx = 2;
		gbc_jlblBorrowStatus.gridy = 3;
		jpnlContent.add(jlblBorrowStatus, gbc_jlblBorrowStatus);
		
		jcmbBorrowStatus = new JComboBox<>();
		jcmbBorrowStatus.addItem("AVAILABLE");
		jcmbBorrowStatus.addItem("BORROWED");
		jcmbBorrowStatus.addItem("LOST");
		jcmbBorrowStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jcmbBorrowStatus = new GridBagConstraints();
		gbc_jcmbBorrowStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbBorrowStatus.gridx = 3;
		gbc_jcmbBorrowStatus.gridy = 3;
		jpnlContent.add(jcmbBorrowStatus, gbc_jcmbBorrowStatus);
		/* END OF jtxtareaDescription */

		/* jpnlButtons */
		JPanel jpnlButtons = new JPanel();
		jpnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(jpnlButtons, BorderLayout.SOUTH);
		/* END OF jpnlButtons */

		/* jbtnOk */
		JButton jbtnOk = new JButton("OK");
		jbtnOk.setActionCommand("OK");
		jbtnOk.addActionListener((event) -> {
			
			// Fetch data from input fields
			
			// Selected book isbn
			int selectedBookIndex = jcmbBookIsbn.getSelectedIndex();
			if(selectedBookIndex == -1) {
				JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
					"Please select a book first to add a copy on.",
					"Invalid input!",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			String isbn = ((BookComboBoxItem) jcmbBookIsbn.getSelectedItem()).isbn();
			
			// Copy number
			int copyNo = 0;
			try {
				copyNo = Integer.parseInt(jtxtfldCopyNo.getText());
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
					"Please enter a valid number for copy number.",
					"Invalid input!",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Date acquired
			LocalDate dateAcquired = null;
			try {
				dateAcquired = LocalDate.parse(jtxtfldDateAcquired.getText());
			} catch(DateTimeParseException e) {
				JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
					"Please enter a valid date (yyyy-MM-dd) for date acquired field.",
					"Invalid input!",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Current worth
			double currentWorth = 0;
			try {
				currentWorth = Double.parseDouble(jtxtfldCurrentWorth.getText());
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
					"Please enter a valid price for worth.",
					"Invalid input!",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Create a new BookCopy record
			BookCopy bookCopy = new BookCopy(isbn, copyNo, dateAcquired, (String) jcmbStatus.getSelectedItem(), currentWorth, (String) jcmbBorrowStatus.getSelectedItem());
			
			// Save it to database with a SwingWorker Thread in background
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					try(Connection connection = bookCopyManagementPanel.dataSource.getConnection()) {
						switch(currentOperation) {
						
						// If form is in insert mode, perform an SQL INSERT
						case INSERT:
							try(PreparedStatement bookCopyInsertStatement = connection.prepareStatement(
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
							try(PreparedStatement bookCopyUpdateStatement = connection.prepareStatement(
									"UPDATE book_copy SET status = ?, current_worth = ?, borrow_status = ? WHERE isbn = ? AND copy_no = ?")) {
								
								bookCopyUpdateStatement.setString(1, bookCopy.status());
								bookCopyUpdateStatement.setDouble(2, bookCopy.currentWorth());
								bookCopyUpdateStatement.setString(3, bookCopy.borrowStatus());
								bookCopyUpdateStatement.setString(4, bookCopy.isbn());
								bookCopyUpdateStatement.setInt(5, bookCopy.copyNo());
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
						bookCopyManagementPanel.setCurrentPage(bookCopyManagementPanel.getCurrentPage());
					} catch (InterruptedException | ExecutionException e) {
						// If an error occured, show dialog and inform user.
						JOptionPane.showMessageDialog(
							bookCopyManagementPanel,
							"An error occured while trying to save to database.\n\nError: " + e.getMessage(),
							"Database access error!",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}.execute();
			
			// Close the dialog
		});
		jpnlButtons.add(jbtnOk);
		getRootPane().setDefaultButton(jbtnOk);
		/* END OF jbtnOk */

		/* jbtnCancel */
		JButton jbtnCancel = new JButton("Cancel");
		jbtnCancel.setActionCommand("Cancel");
		jbtnCancel.addActionListener((event) -> {
			setVisible(false);
		});
		jpnlButtons.add(jbtnCancel);
		/* END OF jbtnCancel */
	}
	
	/**
	 * Prepares the form for inserting a new category.
	 */
	public void reset() {
		currentOperation = FormOperation.INSERT;
		
		jlblHeader.setText("Add Copy");
		
		jtxtfldCopyNo.setEditable(true);
		jtxtfldCopyNo.setText("");
		jtxtfldDateAcquired.setEditable(true);
		jtxtfldDateAcquired.setText("");
		jtxtfldCurrentWorth.setText("");
		
		new SwingWorker<List<BookComboBoxItem>, Void>() {
			@Override
			protected List<BookComboBoxItem> doInBackground() throws Exception {
				try(Connection connection = bookCopyManagementPanel.dataSource.getConnection();
					Statement retrieveBookStatement = connection.createStatement();
					ResultSet bookResultSet = retrieveBookStatement.executeQuery("SELECT isbn, title FROM book")) {
					List<BookComboBoxItem> bookItems = new ArrayList<>();
					while(bookResultSet.next())
						bookItems.add(new BookComboBoxItem(bookResultSet.getString("isbn"), bookResultSet.getString("title")));
					return bookItems;
				}
			}
			@Override
			protected void done() {
				try {
					List<BookComboBoxItem> bookItems = get();
					DefaultComboBoxModel<BookComboBoxItem> bookItemModel = new DefaultComboBoxModel<>();
					bookItemModel.addAll(bookItems);
					jcmbBookIsbn.setEditable(true);
					jcmbBookIsbn.removeAllItems();
					jcmbBookIsbn.setModel(bookItemModel);
				} catch (InterruptedException | ExecutionException e) {
					// If an error occured while fetching members, show dialog
					JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
						"An error occured while trying to fetch books from the database. Error: " + e.getMessage(),
						"Error!",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}.execute();
	}
	
	/**
	 * Prepares the form for uppdating a book category
	 */
	public void reset(BookCopy bookCopy) {
		currentOperation = FormOperation.UPDATE;
		
		jlblHeader.setText("Modify Copy");
		
		jcmbBookIsbn.removeAllItems();
		
		jtxtfldCopyNo.setText("" + bookCopy.copyNo());
		jtxtfldCopyNo.setEditable(false);
		
		jtxtfldDateAcquired.setText("" + bookCopy.dateAcquired());
		jtxtfldDateAcquired.setEditable(false);
		
		jtxtfldCurrentWorth.setText("" + bookCopy.currentWorth());
		jcmbStatus.setSelectedItem(bookCopy.status());
		jcmbBorrowStatus.setSelectedItem(bookCopy.borrowStatus());
		
		new SwingWorker<List<BookComboBoxItem>, Void>() {
			@Override
			protected List<BookComboBoxItem> doInBackground() throws Exception {
				try(Connection connection = bookCopyManagementPanel.dataSource.getConnection();
					Statement retrieveBookStatement = connection.createStatement();
					ResultSet bookResultSet = retrieveBookStatement.executeQuery("SELECT isbn, title FROM book")) {
					List<BookComboBoxItem> bookItems = new ArrayList<>();
					while(bookResultSet.next())
						bookItems.add(new BookComboBoxItem(bookResultSet.getString("isbn"), bookResultSet.getString("title")));
					return bookItems;
				}
			}
			@Override
			protected void done() {
				try {
					List<BookComboBoxItem> bookItems = get();
					DefaultComboBoxModel<BookComboBoxItem> bookItemModel = new DefaultComboBoxModel<>();
					bookItemModel.addAll(bookItems);
					jcmbBookIsbn.setModel(bookItemModel);
					jcmbBookIsbn.setSelectedItem(new BookComboBoxItem(bookCopy.isbn(), null));
					jcmbBookIsbn.setEditable(false);
				} catch (InterruptedException | ExecutionException e) {
					// If an error occured while fetching members, show dialog
					JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
						"An error occured while trying to fetch books from the database. Error: " + e.getMessage(),
						"Error!",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}.execute();
	}

}
