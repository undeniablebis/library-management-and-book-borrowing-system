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
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import co.bisri.librarysystem.admin.ui.borrow.record.BookCopy;
import co.bisri.librarysystem.admin.ui.borrow.record.Borrow;
import co.bisri.librarysystem.admin.ui.borrow.record.MemberComboBoxItem;

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
	
	// HashMap of all included book items (with their rendered JComponents
	PriorityQueue<Integer> borrowItemGapRows;
	HashMap<BookCopy, JComponent[]> addedBookCopyComponents;
	
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
	
	// Book copy choice combobox
	private JComboBox<BookCopy> jcmbBookCopy;

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
		gbl_jpnlContent.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_jpnlContent.columnWeights = new double[]{0.15, 0.35, 0.15, 0.35, Double.MIN_VALUE};
		gbl_jpnlContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		
		JLabel jlblItemsHeader = new JLabel("Borrow Items");
		jlblItemsHeader.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		GridBagConstraints gbc_jlblItemsHeader = new GridBagConstraints();
		gbc_jlblItemsHeader.gridwidth = 2;
		gbc_jlblItemsHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblItemsHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jlblItemsHeader.gridx = 0;
		gbc_jlblItemsHeader.gridy = 3;
		jpnlContent.add(jlblItemsHeader, gbc_jlblItemsHeader);
		
		jlblSearchMessage = new JLabel("Search Results:");
		jlblSearchMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblSearchMessage = new GridBagConstraints();
		gbc_jlblSearchMessage.insets = new Insets(0, 0, 5, 5);
		gbc_jlblSearchMessage.gridx = 2;
		gbc_jlblSearchMessage.gridy = 3;
		jpnlContent.add(jlblSearchMessage, gbc_jlblSearchMessage);
		
		jbtnAddBookCopy = new JButton("Add Item");
		jbtnAddBookCopy.addActionListener((event) -> {
			// Get selected item index
			int selectedItemIndex = jcmbBookCopy.getSelectedIndex();
			
			// Check if nothing was selected
			if(selectedItemIndex == -1) {
				JOptionPane.showMessageDialog(
						formDialog,
						"Please select a book copy to add to this borrow.",
						"Select an item first!",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// If something was selected, retrieve the BookCopy instance
			BookCopy bookCopy = (BookCopy) jcmbBookCopy.getSelectedItem();
			
			// If it's already in the HashMap, prompt user not to add it
			if(addedBookCopyComponents.containsKey(bookCopy)) {
				JOptionPane.showMessageDialog(
						formDialog,
						"You already added this specific book copy. Choose another.",
						"Choose another book.",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Create a jcomponent pointer array for this book copy
			JComponent[] bookCopyUIComponents = new JComponent[4];
			
			// Add the book copy and jcomponents to the hashmap
			addedBookCopyComponents.put(bookCopy, bookCopyUIComponents);
			
			// Find what row index in the Grid to display this item
			int rowIndexDisplay = borrowItemGapRows.peek() == null ? addedBookCopyComponents.size() : borrowItemGapRows.poll();
			
			// ISBN Label
			JLabel jlblCopyIsbn = new JLabel(bookCopy.isbn());
			GridBagConstraints gbc_jlblCopyIsbn = new GridBagConstraints();
			gbc_jlblCopyIsbn.insets = new Insets(0, 0, 5, 5);
			gbc_jlblCopyIsbn.gridx = 0;
			gbc_jlblCopyIsbn.gridy = rowIndexDisplay;
			bookCopyUIComponents[0] = jlblCopyIsbn;
			jpnlBorrowItems.add(jlblCopyIsbn, gbc_jlblCopyIsbn);
			
			// Copy Number Label
			JLabel jlblCopyNumber = new JLabel("Copy" + bookCopy.copyNo());
			GridBagConstraints gbc_jlblCopyNumber = new GridBagConstraints();
			gbc_jlblCopyNumber.insets = new Insets(0, 0, 5, 5);
			gbc_jlblCopyNumber.gridx = 1;
			gbc_jlblCopyNumber.gridy = rowIndexDisplay;
			bookCopyUIComponents[1] = jlblCopyNumber;
			jpnlBorrowItems.add(jlblCopyNumber, gbc_jlblCopyNumber);
			
			// Title Label
			JLabel jlblCopyTitle = new JLabel(bookCopy.title());
			GridBagConstraints gbc_jlblCopyTitle = new GridBagConstraints();
			gbc_jlblCopyTitle.insets = new Insets(0, 0, 5, 5);
			gbc_jlblCopyTitle.gridx = 2;
			gbc_jlblCopyTitle.gridy = rowIndexDisplay;
			bookCopyUIComponents[2] = jlblCopyTitle;
			jpnlBorrowItems.add(jlblCopyTitle, gbc_jlblCopyTitle);
			
			// Remove button
			JButton jbtnCopyRemove = new JButton("X");
			jbtnCopyRemove.addActionListener((event1) -> {
				// Fetch all associated components then remove it from the panel
				JComponent[] bookCopyComponents = addedBookCopyComponents.get(bookCopy);
				for(JComponent bookCopyComponent : bookCopyComponents)
					jpnlBorrowItems.remove(bookCopyComponent);
				
				// Remove it from the hashmap
				addedBookCopyComponents.remove(bookCopy);
				
				// Add the index to the gap rows
				borrowItemGapRows.offer(rowIndexDisplay);
				
				// Revalidate the UI tree
				validate();
				repaint();
			});
			GridBagConstraints gbc_jbtnCopyRemove = new GridBagConstraints();
			gbc_jbtnCopyRemove.insets = new Insets(0, 0, 5, 5);
			gbc_jbtnCopyRemove.gridx = 3;
			gbc_jbtnCopyRemove.gridy = rowIndexDisplay;
			bookCopyUIComponents[3] = jbtnCopyRemove;
			jpnlBorrowItems.add(jbtnCopyRemove, gbc_jbtnCopyRemove);
			
			// Revalidate the UI tree
			validate();
			repaint();
		});
		jbtnAddBookCopy.setEnabled(false);
		jbtnAddBookCopy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jbtnAddBookCopy = new GridBagConstraints();
		gbc_jbtnAddBookCopy.insets = new Insets(0, 0, 5, 0);
		gbc_jbtnAddBookCopy.gridx = 3;
		gbc_jbtnAddBookCopy.gridy = 3;
		jpnlContent.add(jbtnAddBookCopy, gbc_jbtnAddBookCopy);
		
		JLabel jlblSearchBookCopy = new JLabel("Search ISBN/Title:");
		jlblSearchBookCopy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblSearchBookCopy = new GridBagConstraints();
		gbc_jlblSearchBookCopy.anchor = GridBagConstraints.EAST;
		gbc_jlblSearchBookCopy.insets = new Insets(0, 0, 5, 5);
		gbc_jlblSearchBookCopy.gridx = 0;
		gbc_jlblSearchBookCopy.gridy = 4;
		jpnlContent.add(jlblSearchBookCopy, gbc_jlblSearchBookCopy);
		
		jtxtfldSearchIsbnTitle = new JTextField();
		jtxtfldSearchIsbnTitle.addActionListener((event) -> {
			
			// Get search key
			String searchKey = jtxtfldSearchIsbnTitle.getText();
			
			// If search key is empty, do nothing
			if(searchKey.length() < 1) {
				jcmbBookCopy.removeAllItems();
				jlblSearchMessage.setText("Empty search string.");
				jbtnAddBookCopy.setEnabled(false);
				return;
			}
			
			new SwingWorker<List<BookCopy>, Void>() {
				@Override
				protected List<BookCopy> doInBackground() throws Exception {
					List<BookCopy> bookCopyItems = new ArrayList<>();
					
					try(Connection connection = borrowManagementPanel.dataSource.getConnection();
						PreparedStatement retrieveBookCopyStatement =
								connection.prepareStatement(
										"SELECT b.isbn, bc.copy_no, b.title "
										+ "FROM book_copy bc "
										+ "INNER JOIN book b ON b.isbn = bc.isbn "
										+ "WHERE (b.isbn LIKE ? OR b.title LIKE ?) AND bc.borrow_status = 'AVAILABLE' "
										+ "AND bc.status IN ('PERFECT', 'GOOD', 'MEDIOCRE')")) {
						
						retrieveBookCopyStatement.setString(1, "%" + searchKey + "%");
						retrieveBookCopyStatement.setString(2, "%" + searchKey + "%");
						
						try(ResultSet bookCopyResultSet = retrieveBookCopyStatement.executeQuery()) {
							while(bookCopyResultSet.next())
								bookCopyItems.add(
										new BookCopy(
												bookCopyResultSet.getString("isbn"),
												bookCopyResultSet.getInt("copy_no"),
												bookCopyResultSet.getString("title"),
												0.00));
						}
					}
					
					return bookCopyItems;
				}
				@Override
				protected void done() {
					try {
						jcmbBookCopy.removeAllItems();
						DefaultComboBoxModel<BookCopy> bookCopyComboBoxModel = new DefaultComboBoxModel<>();
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
		gbc_jtxtfldSearchIsbnTitle.gridy = 4;
		jpnlContent.add(jtxtfldSearchIsbnTitle, gbc_jtxtfldSearchIsbnTitle);
		
		jcmbBookCopy = new JComboBox<BookCopy>();
		jcmbBookCopy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jcmbBookCopy = new GridBagConstraints();
		gbc_jcmbBookCopy.gridwidth = 2;
		gbc_jcmbBookCopy.insets = new Insets(0, 0, 5, 0);
		gbc_jcmbBookCopy.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbBookCopy.gridx = 2;
		gbc_jcmbBookCopy.gridy = 4;
		jpnlContent.add(jcmbBookCopy, gbc_jcmbBookCopy);
		
		JScrollPane jsclpnBorrowItems = new JScrollPane();
		GridBagConstraints gbc_jsclpnBorrowItems = new GridBagConstraints();
		gbc_jsclpnBorrowItems.gridwidth = 4;
		gbc_jsclpnBorrowItems.fill = GridBagConstraints.BOTH;
		gbc_jsclpnBorrowItems.gridx = 0;
		gbc_jsclpnBorrowItems.gridy = 5;
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
		gbc_jlblBookIsbnHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jlblBookIsbnHeader.gridx = 0;
		gbc_jlblBookIsbnHeader.gridy = 0;
		jpnlBorrowItems.add(jlblBookIsbnHeader, gbc_jlblBookIsbnHeader);
		
		JLabel jlblCopyNumberHeader = new JLabel("Copy #");
		jlblCopyNumberHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblCopyNumberHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblCopyNumberHeader = new GridBagConstraints();
		gbc_jlblCopyNumberHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jlblCopyNumberHeader.gridx = 1;
		gbc_jlblCopyNumberHeader.gridy = 0;
		jpnlBorrowItems.add(jlblCopyNumberHeader, gbc_jlblCopyNumberHeader);
		
		JLabel jlblBookTitleHeader = new JLabel("Title");
		jlblBookTitleHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblBookTitleHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblBookTitleHeader = new GridBagConstraints();
		gbc_jlblBookTitleHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jlblBookTitleHeader.gridx = 2;
		gbc_jlblBookTitleHeader.gridy = 0;
		jpnlBorrowItems.add(jlblBookTitleHeader, gbc_jlblBookTitleHeader);
		
		JLabel jlblRemoveItemHeader = new JLabel("Remove");
		jlblRemoveItemHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblRemoveItemHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblRemoveItemHeader = new GridBagConstraints();
		gbc_jlblRemoveItemHeader.insets = new Insets(0, 0, 5, 5);
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
			
			// Get selected member
			int selectedMemberIndex = jcmbMember.getSelectedIndex();
			if(selectedMemberIndex == -1) {
				JOptionPane.showMessageDialog(
						formDialog,
						"Please select a valid member.",
						"Invalid input.",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			int memberId = ((MemberComboBoxItem) jcmbMember.getSelectedItem()).id();
			
			// Check if selected member has a pending borrow
			try(Connection connection = borrowManagementPanel.dataSource.getConnection();
				PreparedStatement checkerStatement = connection.prepareStatement("SELECT member_id, borrowed_on FROM borrow WHERE member_id = ? AND status = 'STILL_BORROWING'")) {
				checkerStatement.setInt(1, memberId);
				try(ResultSet pendingBorrowResultSet = checkerStatement.executeQuery()) {
					if(pendingBorrowResultSet.next()) {
						// If an error occured, show dialog and inform user.
						JOptionPane.showMessageDialog(
								formDialog,
								"This member has borrowed already and have not yet returned the items.",
								"Cannot proceed.",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			} catch(SQLException e) {
				// If an error occured, show dialog and inform user.
				JOptionPane.showMessageDialog(
						borrowManagementPanel,
						"An error occured while trying to check if member has borrows.\n\nError: " + e.getMessage(),
						"Database access error!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Get borrowed on
			LocalDate borrowedOn = null;
			try {
				 borrowedOn = LocalDate.parse(jtxtfldBorrowedOn.getText());
			} catch(DateTimeParseException e) {
				JOptionPane.showMessageDialog(
						formDialog,
						"Invalid borrowed on date. Must be of the format yyyy-MM-dd",
						"Invalid input.",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Get target return date
			LocalDate targetReturnDate = null;
			try {
				targetReturnDate = LocalDate.parse(jtxtfldTargetReturnDate.getText());
			} catch(DateTimeParseException e) {
				JOptionPane.showMessageDialog(
						formDialog,
						"Invalid target return date. Must be of the format yyyy-MM-dd",
						"Invalid input.",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Get all book copies added, transform into item instances
			if(addedBookCopyComponents.size() == 0) {
				JOptionPane.showMessageDialog(
						formDialog,
						"Cannot make a borrow without any items. Please add at least one.",
						"Invalid input.",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			List<BookCopy> borrowItemList = new LinkedList<>();
			for(BookCopy bookCopy : addedBookCopyComponents.keySet())
				borrowItemList.add(bookCopy);
			// Create a borrow
			Borrow borrow = new Borrow(memberId, null, borrowedOn, targetReturnDate, null, "STILL_BORROWING", 0.00, borrowItemList);
			
			// Save it to database with a SwingWorker Thread in background
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					try(Connection connection = borrowManagementPanel.dataSource.getConnection()) {
						// Start database transaction
						connection.setAutoCommit(false);
						
						// Save the root object (borrow) first
						try(PreparedStatement insertBorrowStatement =
								connection.prepareStatement(
										"INSERT INTO borrow(member_id, borrowed_on, target_return_date, status) "
										+ "VALUES (?, ?, ?, ?)")) {
							insertBorrowStatement.setInt(1, borrow.memberId());
							insertBorrowStatement.setString(2, borrow.borrowedOn().toString());
							insertBorrowStatement.setString(3, borrow.targetReturnDate().toString());
							insertBorrowStatement.setString(4, borrow.status());
							insertBorrowStatement.execute();
						} catch(SQLException e) {
							connection.rollback();
							throw e;
						}
						
						// Save the items in a batch insert, and update book copy statuses to BORROWED
						try(PreparedStatement insertItemsStatement =
								connection.prepareStatement(
										"INSERT INTO borrow_item(borrow_member_id, borrow_borrowed_on, book_isbn, book_copy_no) "
										+ "VALUES (?, ?, ?, ?)");
							PreparedStatement updateBorrowStatusStatement =
								connection.prepareStatement(
										"UPDATE book_copy SET borrow_status = 'BORROWED' WHERE isbn = ? AND copy_no = ?")) {
							for(BookCopy bookCopy : borrow.itemList()) {
								insertItemsStatement.setInt(1, borrow.memberId());
								insertItemsStatement.setString(2, borrow.borrowedOn().toString());
								insertItemsStatement.setString(3, bookCopy.isbn());
								insertItemsStatement.setInt(4, bookCopy.copyNo());
								insertItemsStatement.addBatch();
								
								updateBorrowStatusStatement.setString(1, bookCopy.isbn());
								updateBorrowStatusStatement.setInt(2, bookCopy.copyNo());
								updateBorrowStatusStatement.addBatch();
							}
							insertItemsStatement.executeBatch();
							updateBorrowStatusStatement.executeBatch();
						} catch(SQLException e) {
							connection.rollback();
							throw e;
						}
						
						connection.commit();
					}
						
					return null;
				}
				@Override
				protected void done() {
					try {
						get();
						// If success, show dialog
						JOptionPane.showMessageDialog(
							borrowManagementPanel,
							"Successfully created a borrow transaction. Refreshing your panel.",
							"Success!",
							JOptionPane.INFORMATION_MESSAGE);
						borrowManagementPanel.setCurrentPage(borrowManagementPanel.getCurrentPage());
					} catch (InterruptedException | ExecutionException e) {
						if(e.getCause() instanceof SQLIntegrityConstraintViolationException) {
							SQLIntegrityConstraintViolationException exc = (SQLIntegrityConstraintViolationException) e.getCause();
							if(exc.getErrorCode() == 1062) {
								// If an error occured, show dialog and inform user.
								JOptionPane.showMessageDialog(
									formDialog,
									"This user has already made a borrow for the current borrow date.",
									"Cannot make borrow!",
									JOptionPane.WARNING_MESSAGE);
								return;
							}
						}
						
						// If an error occured, show dialog and inform user.
						JOptionPane.showMessageDialog(
							borrowManagementPanel,
							"An error occured while trying to save to database.\n\nError: " + e.getMessage(),
							"Database access error!",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}.execute();
			
			// Close the dialog
			setVisible(false);
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
		
		/* addedBookCopyComponents */
		addedBookCopyComponents = new HashMap<>();
		/* END OF addedBookCopyComponents */
		
		/* borrowItemGapRows */
		borrowItemGapRows = new PriorityQueue<>();
		/* END OF borrowItemGapRows */
	}
	
	/**
	 * Prepares the form for inserting a new category.
	 */
	public void reset() {
		jlblHeader.setText("Add Borrow");
		
		// Clear all fields
		jtxtfldSearchIsbnTitle.setText("");
		jcmbBookCopy.removeAll();
		jbtnAddBookCopy.setEnabled(false);
		jtxtfldBorrowedOn.setText(LocalDate.now().toString());
		jtxtfldTargetReturnDate.setText("");
		
		// Remove the UI components in borrow items panel
		for(BookCopy bookCopy : addedBookCopyComponents.keySet())
			for(JComponent jcmpBookCopyComponent : addedBookCopyComponents.get(bookCopy))
				jpnlBorrowItems.remove(jcmpBookCopyComponent);
		addedBookCopyComponents.clear();
		
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
	}
	
}
