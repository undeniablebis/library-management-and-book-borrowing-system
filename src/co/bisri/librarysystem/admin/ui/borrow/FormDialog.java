package co.bisri.librarysystem.admin.ui.borrow;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import co.bisri.librarysystem.admin.ui.borrow.record.BookCopyComboBoxItem;
import co.bisri.librarysystem.admin.ui.borrow.record.Borrow;
import co.bisri.librarysystem.admin.ui.borrow.record.MemberComboBoxItem;
import co.bisri.librarysystem.admin.ui.util.FormOperation;

/**
 * Form Dialog for adding and updating borrows
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
	protected BorrowManagementPanel borrowManagementPanel;

	// Content Panel for input fields
	private final JPanel jpnlContent;
	
	// Header label
	private JLabel jlblHeader;
	
	// Old PK, only used when updating
	private int oldMemberId;
	private LocalDate oldBorrowDate;
	
	// HashMap of all included book items (with their rendered JComponents
	
	// SQL operation to perform when OK is clicked (insert or update)
	private FormOperation currentOperation;
	
	// JPanel of all Items displayed
	private JPanel jpnlBorrowItems;
	
	// Search isbn/title input field
	private JLabel jlblSearchMessage;
	private JTextField jtxtfldSearchIsbnTitle;
	private JButton jbtnAddBookCopy;
	
	// Input Fields
	private JComboBox<MemberComboBoxItem> jcmbMember;
	private JTextField jtxtfldBorrowedOn;
	private JTextField jtxtfldTargetReturnDate;
	private JTextField jtxtfldReturnedOn;
	private JComboBox<String> jcmbStatus;
	private JTextField jtxtfldReturnFee;
	
	// Book copy choice combobox
	private JComboBox<BookCopyComboBoxItem> jcmbBookCopy;

	/**
	 * Create the dialog.
	 */
	public FormDialog() {
		setMinimumSize(new Dimension(500, 500));
		// Reference for nested anonymous contexts
		FormDialog formDialog = this;
		
		/* Dialog properties */
		setTitle("Save Borrow");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		/* END OF Dialog properties */

		/* jpnlContent */
		jpnlContent = new JPanel();
		jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
		getContentPane().add(jpnlContent, BorderLayout.CENTER);
		GridBagLayout gbl_jpnlContent = new GridBagLayout();
		gbl_jpnlContent.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_jpnlContent.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_jpnlContent.columnWeights = new double[]{0.15, 0.35, 0.15, 0.35, Double.MIN_VALUE};
		gbl_jpnlContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		jpnlContent.setLayout(gbl_jpnlContent);
		/* END OF jpnlContent */

		/* jlblHeader */
		jlblHeader = new JLabel("... Borrow");
		jlblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		jlblHeader.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		GridBagConstraints gbc_jlblHeader = new GridBagConstraints();
		gbc_jlblHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblHeader.gridwidth = 4;
		gbc_jlblHeader.insets = new Insets(0, 0, 5, 0);
		gbc_jlblHeader.gridx = 0;
		gbc_jlblHeader.gridy = 0;
		jpnlContent.add(jlblHeader, gbc_jlblHeader);
		/* END OF jlblHeader */

		/* jlblName */
		JLabel jlblMember = new JLabel("Name:");
		jlblMember.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblMember = new GridBagConstraints();
		gbc_jlblMember.insets = new Insets(0, 0, 5, 5);
		gbc_jlblMember.anchor = GridBagConstraints.EAST;
		gbc_jlblMember.gridx = 0;
		gbc_jlblMember.gridy = 1;
		jpnlContent.add(jlblMember, gbc_jlblMember);
		/* END OF jtxtfldName */

		/* jlblDescription */
		/* END OF jlblDescription */

		/* jscrlpnDescription */
		
		jcmbMember = new JComboBox<>();
		jcmbMember.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jcmbMember = new GridBagConstraints();
		gbc_jcmbMember.insets = new Insets(0, 0, 5, 5);
		gbc_jcmbMember.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbMember.gridx = 1;
		gbc_jcmbMember.gridy = 1;
		jpnlContent.add(jcmbMember, gbc_jcmbMember);
		
		JLabel jlblBorrowedOn = new JLabel("Borrow Date:");
		jlblBorrowedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblBorrowedOn = new GridBagConstraints();
		gbc_jlblBorrowedOn.anchor = GridBagConstraints.EAST;
		gbc_jlblBorrowedOn.insets = new Insets(0, 0, 5, 5);
		gbc_jlblBorrowedOn.gridx = 2;
		gbc_jlblBorrowedOn.gridy = 1;
		jpnlContent.add(jlblBorrowedOn, gbc_jlblBorrowedOn);
		
		jtxtfldBorrowedOn = new JTextField();
		jtxtfldBorrowedOn.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldBorrowedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jtxtfldBorrowedOn = new GridBagConstraints();
		gbc_jtxtfldBorrowedOn.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldBorrowedOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldBorrowedOn.gridx = 3;
		gbc_jtxtfldBorrowedOn.gridy = 1;
		jpnlContent.add(jtxtfldBorrowedOn, gbc_jtxtfldBorrowedOn);
		jtxtfldBorrowedOn.setColumns(10);
		
		JLabel jlblTargetReturnDate = new JLabel("Target Return:");
		jlblTargetReturnDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblTargetReturnDate = new GridBagConstraints();
		gbc_jlblTargetReturnDate.anchor = GridBagConstraints.EAST;
		gbc_jlblTargetReturnDate.insets = new Insets(0, 0, 5, 5);
		gbc_jlblTargetReturnDate.gridx = 0;
		gbc_jlblTargetReturnDate.gridy = 2;
		jpnlContent.add(jlblTargetReturnDate, gbc_jlblTargetReturnDate);
		
		jtxtfldTargetReturnDate = new JTextField();
		jtxtfldTargetReturnDate.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldTargetReturnDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldTargetReturnDate.setColumns(10);
		GridBagConstraints gbc_jtxtfldTargetReturnDate = new GridBagConstraints();
		gbc_jtxtfldTargetReturnDate.insets = new Insets(0, 0, 5, 5);
		gbc_jtxtfldTargetReturnDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldTargetReturnDate.gridx = 1;
		gbc_jtxtfldTargetReturnDate.gridy = 2;
		jpnlContent.add(jtxtfldTargetReturnDate, gbc_jtxtfldTargetReturnDate);
		
		JLabel jlblReturnedOn = new JLabel("Returned On:");
		jlblReturnedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblReturnedOn = new GridBagConstraints();
		gbc_jlblReturnedOn.anchor = GridBagConstraints.EAST;
		gbc_jlblReturnedOn.insets = new Insets(0, 0, 5, 5);
		gbc_jlblReturnedOn.gridx = 2;
		gbc_jlblReturnedOn.gridy = 2;
		jpnlContent.add(jlblReturnedOn, gbc_jlblReturnedOn);
		
		jtxtfldReturnedOn = new JTextField();
		jtxtfldReturnedOn.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldReturnedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldReturnedOn.setColumns(10);
		GridBagConstraints gbc_jtxtfldReturnedOn = new GridBagConstraints();
		gbc_jtxtfldReturnedOn.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldReturnedOn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldReturnedOn.gridx = 3;
		gbc_jtxtfldReturnedOn.gridy = 2;
		jpnlContent.add(jtxtfldReturnedOn, gbc_jtxtfldReturnedOn);
		
		JLabel jlblStatus = new JLabel("Status:");
		jlblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblStatus = new GridBagConstraints();
		gbc_jlblStatus.anchor = GridBagConstraints.EAST;
		gbc_jlblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_jlblStatus.gridx = 0;
		gbc_jlblStatus.gridy = 3;
		jpnlContent.add(jlblStatus, gbc_jlblStatus);
		
		jcmbStatus = new JComboBox<>();
		jcmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jcmbStatus = new GridBagConstraints();
		gbc_jcmbStatus.insets = new Insets(0, 0, 5, 5);
		gbc_jcmbStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbStatus.gridx = 1;
		gbc_jcmbStatus.gridy = 3;
		jpnlContent.add(jcmbStatus, gbc_jcmbStatus);
		
		JLabel jlblReturnFee = new JLabel("Return Fee:");
		jlblReturnFee.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblReturnFee = new GridBagConstraints();
		gbc_jlblReturnFee.anchor = GridBagConstraints.EAST;
		gbc_jlblReturnFee.insets = new Insets(0, 0, 5, 5);
		gbc_jlblReturnFee.gridx = 2;
		gbc_jlblReturnFee.gridy = 3;
		jpnlContent.add(jlblReturnFee, gbc_jlblReturnFee);
		
		jtxtfldReturnFee = new JTextField();
		jtxtfldReturnFee.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldReturnFee.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldReturnFee.setColumns(10);
		GridBagConstraints gbc_jtxtfldReturnFee = new GridBagConstraints();
		gbc_jtxtfldReturnFee.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldReturnFee.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldReturnFee.gridx = 3;
		gbc_jtxtfldReturnFee.gridy = 3;
		jpnlContent.add(jtxtfldReturnFee, gbc_jtxtfldReturnFee);
		
		JLabel jlblItemsHeader = new JLabel("Borrow Items");
		jlblItemsHeader.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		GridBagConstraints gbc_jlblItemsHeader = new GridBagConstraints();
		gbc_jlblItemsHeader.gridwidth = 2;
		gbc_jlblItemsHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblItemsHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jlblItemsHeader.gridx = 0;
		gbc_jlblItemsHeader.gridy = 4;
		jpnlContent.add(jlblItemsHeader, gbc_jlblItemsHeader);
		
		jlblSearchMessage = new JLabel("Search Results:");
		jlblSearchMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblSearchMessage = new GridBagConstraints();
		gbc_jlblSearchMessage.insets = new Insets(0, 0, 5, 5);
		gbc_jlblSearchMessage.gridx = 2;
		gbc_jlblSearchMessage.gridy = 4;
		jpnlContent.add(jlblSearchMessage, gbc_jlblSearchMessage);
		
		JLabel jlblSearchBookCopy = new JLabel("Search ISBN/Title:");
		jlblSearchBookCopy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblSearchBookCopy = new GridBagConstraints();
		gbc_jlblSearchBookCopy.anchor = GridBagConstraints.EAST;
		gbc_jlblSearchBookCopy.insets = new Insets(0, 0, 5, 5);
		gbc_jlblSearchBookCopy.gridx = 0;
		gbc_jlblSearchBookCopy.gridy = 5;
		jpnlContent.add(jlblSearchBookCopy, gbc_jlblSearchBookCopy);
		
		jtxtfldSearchIsbnTitle = new JTextField();
		jtxtfldSearchIsbnTitle.addActionListener((event) -> {
			
			// Get search key
			String searchKey = jtxtfldSearchIsbnTitle.getText();
			
			// If search key is empty, do nothing
			if(searchKey.length() < 1) {
				jcmbBookCopy.removeAllItems();
				jlblSearchMessage.setText("Empty search string.");
				return;
			}
			
			new SwingWorker<List<BookCopyComboBoxItem>, Void>() {
				@Override
				protected List<BookCopyComboBoxItem> doInBackground() throws Exception {
					List<BookCopyComboBoxItem> bookCopyItems = new ArrayList<>();
					
					try(Connection connection = borrowManagementPanel.dataSource.getConnection();
						PreparedStatement retrieveBookCopyStatement =
								connection.prepareStatement(
										"SELECT b.isbn, bc.copy_no, b.title "
										+ "FROM book_copy bc "
										+ "INNER JOIN book b ON b.isbn = bc.isbn "
										+ "WHERE b.isbn LIKE ? OR b.title LIKE ?")) {
						
						retrieveBookCopyStatement.setString(1, "%" + searchKey + "%");
						retrieveBookCopyStatement.setString(2, "%" + searchKey + "%");
						
						try(ResultSet bookCopyResultSet = retrieveBookCopyStatement.executeQuery()) {
							while(bookCopyResultSet.next())
								bookCopyItems.add(
										new BookCopyComboBoxItem(
												bookCopyResultSet.getString("isbn"),
												bookCopyResultSet.getInt("copy_no"),
												bookCopyResultSet.getString("title")));
						}
					}
					
					return bookCopyItems;
				}
				@Override
				protected void done() {
					try {
						jcmbBookCopy.removeAllItems();
						DefaultComboBoxModel<BookCopyComboBoxItem> bookCopyComboBoxModel = new DefaultComboBoxModel<>();
						bookCopyComboBoxModel.addAll(get());
						jcmbBookCopy.setModel(bookCopyComboBoxModel);
						
						if(bookCopyComboBoxModel.getSize() == 0) {
							jlblSearchMessage.setText("No books found.");
							jbtnAddBookCopy.setEnabled(false);
						} else {
							jlblSearchMessage.setText("Search results:");
							jbtnAddBookCopy.setEnabled(true);
						}
					} catch (InterruptedException | ExecutionException e) {
						// If an error occured while fetching book copies, show dialog
						JOptionPane.showMessageDialog(
							borrowManagementPanel,
							"An error occured while trying to fetch copies from the database. Error: " + e.getMessage(),
							"Error!",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}.execute();
		});
		jtxtfldSearchIsbnTitle.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldSearchIsbnTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldSearchIsbnTitle.setColumns(10);
		GridBagConstraints gbc_jtxtfldSearchIsbnTitle = new GridBagConstraints();
		gbc_jtxtfldSearchIsbnTitle.insets = new Insets(0, 0, 5, 5);
		gbc_jtxtfldSearchIsbnTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldSearchIsbnTitle.gridx = 1;
		gbc_jtxtfldSearchIsbnTitle.gridy = 5;
		jpnlContent.add(jtxtfldSearchIsbnTitle, gbc_jtxtfldSearchIsbnTitle);
		
		jcmbBookCopy = new JComboBox<BookCopyComboBoxItem>();
		jcmbBookCopy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jcmbBookCopy = new GridBagConstraints();
		gbc_jcmbBookCopy.insets = new Insets(0, 0, 5, 5);
		gbc_jcmbBookCopy.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbBookCopy.gridx = 2;
		gbc_jcmbBookCopy.gridy = 5;
		jpnlContent.add(jcmbBookCopy, gbc_jcmbBookCopy);
		
		jbtnAddBookCopy = new JButton("Add Item");
		jbtnAddBookCopy.setEnabled(false);
		jbtnAddBookCopy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jbtnAddBookCopy = new GridBagConstraints();
		gbc_jbtnAddBookCopy.insets = new Insets(0, 0, 5, 0);
		gbc_jbtnAddBookCopy.gridx = 3;
		gbc_jbtnAddBookCopy.gridy = 5;
		jpnlContent.add(jbtnAddBookCopy, gbc_jbtnAddBookCopy);
		
		JScrollPane jsclpnBorrowItems = new JScrollPane();
		GridBagConstraints gbc_jsclpnBorrowItems = new GridBagConstraints();
		gbc_jsclpnBorrowItems.gridwidth = 4;
		gbc_jsclpnBorrowItems.fill = GridBagConstraints.BOTH;
		gbc_jsclpnBorrowItems.gridx = 0;
		gbc_jsclpnBorrowItems.gridy = 6;
		jpnlContent.add(jsclpnBorrowItems, gbc_jsclpnBorrowItems);
		
		jpnlBorrowItems = new JPanel();
		jpnlBorrowItems.setBorder(new EmptyBorder(10, 10, 10, 10));
		jsclpnBorrowItems.setViewportView(jpnlBorrowItems);
		GridBagLayout gbl_jpnlBorrowItems = new GridBagLayout();
		gbl_jpnlBorrowItems.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_jpnlBorrowItems.rowHeights = new int[]{0, 0};
		gbl_jpnlBorrowItems.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_jpnlBorrowItems.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		jpnlBorrowItems.setLayout(gbl_jpnlBorrowItems);
		
		JLabel jlblBookIsbnHeader = new JLabel("ISBN");
		jlblBookIsbnHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		jlblBookIsbnHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_jlblBookIsbnHeader = new GridBagConstraints();
		gbc_jlblBookIsbnHeader.insets = new Insets(0, 0, 0, 5);
		gbc_jlblBookIsbnHeader.gridx = 0;
		gbc_jlblBookIsbnHeader.gridy = 0;
		jpnlBorrowItems.add(jlblBookIsbnHeader, gbc_jlblBookIsbnHeader);
		
		JLabel jlblCopyNumberHeader = new JLabel("Copy #");
		jlblCopyNumberHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblCopyNumberHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblCopyNumberHeader = new GridBagConstraints();
		gbc_jlblCopyNumberHeader.insets = new Insets(0, 0, 0, 5);
		gbc_jlblCopyNumberHeader.gridx = 1;
		gbc_jlblCopyNumberHeader.gridy = 0;
		jpnlBorrowItems.add(jlblCopyNumberHeader, gbc_jlblCopyNumberHeader);
		
		JLabel jlblBookTitleHeader = new JLabel("Title");
		jlblBookTitleHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblBookTitleHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblBookTitleHeader = new GridBagConstraints();
		gbc_jlblBookTitleHeader.insets = new Insets(0, 0, 0, 5);
		gbc_jlblBookTitleHeader.gridx = 2;
		gbc_jlblBookTitleHeader.gridy = 0;
		jpnlBorrowItems.add(jlblBookTitleHeader, gbc_jlblBookTitleHeader);
		
		JLabel jlblRemoveItemHeader = new JLabel("Remove");
		jlblRemoveItemHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblRemoveItemHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblRemoveItemHeader = new GridBagConstraints();
		gbc_jlblRemoveItemHeader.gridx = 3;
		gbc_jlblRemoveItemHeader.gridy = 0;
		jpnlBorrowItems.add(jlblRemoveItemHeader, gbc_jlblRemoveItemHeader);
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
			/*/ Fetch data from input fields
			String name = jtxtfldName.getText();
			String description = jtxtareaDescription.getText();
			
			// Validation Layer
			// Check name
			if(name.contentEquals("") || name.length() > 32) {
				JOptionPane.showMessageDialog(
						formDialog,
						"Invalid value for name. Please check that it matches the ff. criteria:\n"
						+ "- Not empty or blank\n"
						+ "- Up to 32 characters",
						"Invalid input.",
						JOptionPane.WARNING_MESSAGE);
			}
			// Check description
			if(description.contentEquals("") || description.length() > 256) {
				JOptionPane.showMessageDialog(
						formDialog,
						"Invalid value for description. Please check that it matches the ff. criteria:\n"
						+ "- Not empty or blank\n"
						+ "- Up to 256 characters",
						"Invalid input.",
						JOptionPane.WARNING_MESSAGE);
			}
			
			// Create a new BookCategory record
			BookCategory bookCategory = new BookCategory(name, description);
			
			// Save it to database with a SwingWorker Thread in background
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					try(Connection connection = booksCategoryManagementPanel.dataSource.getConnection()) {
						switch(currentOperation) {
						
						// If form is in insert mode, perform an SQL INSERT
						case INSERT:
							try(PreparedStatement bookCategoryInsertStatement = connection.prepareStatement(
									"INSERT INTO book_category(name, description) VALUES(?, ?)")) {
								bookCategoryInsertStatement.setString(1, bookCategory.name());
								bookCategoryInsertStatement.setString(2, bookCategory.description());
								bookCategoryInsertStatement.execute();
							}
							
							break;
						
						// Else, perform an SQL UPDATE with the old name
						case UPDATE:
							try(PreparedStatement bookCategoryUpdateStatement = connection.prepareStatement(
									"UPDATE book_category SET name = ?, description = ? WHERE name = ?")) {
								bookCategoryUpdateStatement.setString(1, bookCategory.name());
								bookCategoryUpdateStatement.setString(2, bookCategory.description());
								bookCategoryUpdateStatement.setString(3, oldCategoryName);
								bookCategoryUpdateStatement.execute();
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
							booksCategoryManagementPanel,
							"Successfully saved category to database. Refreshing your panel.",
							"Success!",
							JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
						booksCategoryManagementPanel.setCurrentPage(booksCategoryManagementPanel.getCurrentPage());
					} catch (InterruptedException | ExecutionException e) {
						// If an error occured, show dialog and inform user.
						JOptionPane.showMessageDialog(
							booksCategoryManagementPanel,
							"An error occured while trying to save to database.\n\nError: " + e.getMessage(),
							"Database access error!",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}.execute();
			
			// Close the dialog
			setVisible(false);*/
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
		
		jlblHeader.setText("Add Borrow");
		
		// Reset and fill jcmbMember with a SwingWorker thread
		jcmbMember.removeAllItems();
		new SwingWorker<List<MemberComboBoxItem>, Void>() {
			@Override
			protected List<MemberComboBoxItem> doInBackground() throws Exception {
				List<MemberComboBoxItem> memberItems = new ArrayList<>();
				
				try(Connection connection = borrowManagementPanel.dataSource.getConnection();
					Statement retrieveMembersStatement = connection.createStatement();
					ResultSet memberResultSet = retrieveMembersStatement.executeQuery("SELECT id, first_name, last_name FROM member")) {
					
					while(memberResultSet.next())
						memberItems.add(
								new MemberComboBoxItem(
										memberResultSet.getInt("id"),
										memberResultSet.getString("first_name") + " " + memberResultSet.getString("last_name")));
				}
				
				return memberItems;
			}
			@Override
			protected void done() {
				try {
					DefaultComboBoxModel<MemberComboBoxItem> memberComboBoxModel = new DefaultComboBoxModel<>();
					memberComboBoxModel.addAll(get());
					jcmbMember.setModel(memberComboBoxModel);
				} catch (InterruptedException | ExecutionException e) {
					// If an error occured while fetching members, show dialog
					JOptionPane.showMessageDialog(
						borrowManagementPanel,
						"An error occured while trying to fetch members from the database. Error: " + e.getMessage(),
						"Error!",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}.execute();
		
		// Clear all fields
		jtxtfldBorrowedOn.setText(LocalDate.now().toString());
		jtxtfldTargetReturnDate.setText("");
		jtxtfldReturnedOn.setText("");
		jtxtfldReturnFee.setText("0.00");
	}
	
	/**
	 * Prepares the form for updating an existing category.
	 */
	public void reset(Borrow borrow) {
		currentOperation = FormOperation.UPDATE;
		
		jlblHeader.setText("Modify Borrow");
	}

}
