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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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

/**
 * Form Dialog for adding and updating borrows
 * 
 * @author Rian Reyes
 *
 */
public class ReturnDialog extends JDialog {

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
	
	// Bound borrow object
	private Borrow borrow;
	
	// HashMap of all included book items (with their rendered JComponents)
	private HashMap<BookCopy, JComponent[]> bookCopyIsReturnedCheckboxes;
	
	// JPanel of all Items displayed
	private JPanel jpnlBorrowItems;
	private JTextField jtxtfldBorrowedOn;
	private JTextField jtxtfldTargetReturnDate;
	private JTextField jtxtfldMemberName;
	private JTextField jtxtfldReturnFee;

	/**
	 * Create the dialog.
	 */
	public ReturnDialog() {
		setMinimumSize(new Dimension(500, 500));
		// Reference for nested anonymous contexts
		ReturnDialog returnDialog = this;
		
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
		gbl_jpnlContent.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_jpnlContent.columnWeights = new double[]{0.15, 1.0, 0.15, 1.0, Double.MIN_VALUE};
		gbl_jpnlContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		jpnlContent.setLayout(gbl_jpnlContent);
		/* END OF jpnlContent */

		/* jlblHeader */
		jlblHeader = new JLabel("Return Borrow");
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
		JLabel jlblMemberName = new JLabel("Name:");
		jlblMemberName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblMemberName = new GridBagConstraints();
		gbc_jlblMemberName.insets = new Insets(0, 0, 5, 5);
		gbc_jlblMemberName.anchor = GridBagConstraints.EAST;
		gbc_jlblMemberName.gridx = 0;
		gbc_jlblMemberName.gridy = 1;
		jpnlContent.add(jlblMemberName, gbc_jlblMemberName);
		
		jtxtfldMemberName = new JTextField();
		jtxtfldMemberName.setEditable(false);
		jtxtfldMemberName.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldMemberName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldMemberName.setColumns(10);
		GridBagConstraints gbc_jtxtfldMemberName = new GridBagConstraints();
		gbc_jtxtfldMemberName.insets = new Insets(0, 0, 5, 5);
		gbc_jtxtfldMemberName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldMemberName.gridx = 1;
		gbc_jtxtfldMemberName.gridy = 1;
		jpnlContent.add(jtxtfldMemberName, gbc_jtxtfldMemberName);
		
		JLabel jlblBorrowedOn = new JLabel("Borrow Date:");
		jlblBorrowedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblBorrowedOn = new GridBagConstraints();
		gbc_jlblBorrowedOn.anchor = GridBagConstraints.EAST;
		gbc_jlblBorrowedOn.insets = new Insets(0, 0, 5, 5);
		gbc_jlblBorrowedOn.gridx = 2;
		gbc_jlblBorrowedOn.gridy = 1;
		jpnlContent.add(jlblBorrowedOn, gbc_jlblBorrowedOn);
		
		jtxtfldBorrowedOn = new JTextField();
		jtxtfldBorrowedOn.setEditable(false);
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
		jtxtfldTargetReturnDate.setEditable(false);
		jtxtfldTargetReturnDate.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldTargetReturnDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldTargetReturnDate.setColumns(10);
		GridBagConstraints gbc_jtxtfldTargetReturnDate = new GridBagConstraints();
		gbc_jtxtfldTargetReturnDate.insets = new Insets(0, 0, 5, 5);
		gbc_jtxtfldTargetReturnDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldTargetReturnDate.gridx = 1;
		gbc_jtxtfldTargetReturnDate.gridy = 2;
		jpnlContent.add(jtxtfldTargetReturnDate, gbc_jtxtfldTargetReturnDate);
		
		JLabel jlblReturnFee = new JLabel("Return Fee:");
		jlblReturnFee.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblReturnFee = new GridBagConstraints();
		gbc_jlblReturnFee.anchor = GridBagConstraints.EAST;
		gbc_jlblReturnFee.insets = new Insets(0, 0, 5, 5);
		gbc_jlblReturnFee.gridx = 2;
		gbc_jlblReturnFee.gridy = 2;
		jpnlContent.add(jlblReturnFee, gbc_jlblReturnFee);
		
		jtxtfldReturnFee = new JTextField();
		jtxtfldReturnFee.setMargin(new Insets(5, 5, 5, 5));
		jtxtfldReturnFee.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		jtxtfldReturnFee.setColumns(10);
		GridBagConstraints gbc_jtxtfldReturnFee = new GridBagConstraints();
		gbc_jtxtfldReturnFee.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldReturnFee.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldReturnFee.gridx = 3;
		gbc_jtxtfldReturnFee.gridy = 2;
		jpnlContent.add(jtxtfldReturnFee, gbc_jtxtfldReturnFee);
		
		JLabel jlblItemsHeader = new JLabel("Items");
		jlblItemsHeader.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		GridBagConstraints gbc_jlblItemsHeader = new GridBagConstraints();
		gbc_jlblItemsHeader.gridwidth = 2;
		gbc_jlblItemsHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblItemsHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jlblItemsHeader.gridx = 0;
		gbc_jlblItemsHeader.gridy = 3;
		jpnlContent.add(jlblItemsHeader, gbc_jlblItemsHeader);
		
		JScrollPane jsclpnBorrowItems = new JScrollPane();
		GridBagConstraints gbc_jsclpnBorrowItems = new GridBagConstraints();
		gbc_jsclpnBorrowItems.gridwidth = 4;
		gbc_jsclpnBorrowItems.fill = GridBagConstraints.BOTH;
		gbc_jsclpnBorrowItems.gridx = 0;
		gbc_jsclpnBorrowItems.gridy = 4;
		jpnlContent.add(jsclpnBorrowItems, gbc_jsclpnBorrowItems);
		
		jpnlBorrowItems = new JPanel();
		jpnlBorrowItems.setBorder(new EmptyBorder(10, 10, 10, 10));
		jsclpnBorrowItems.setColumnHeaderView(jpnlBorrowItems);
		GridBagLayout gbl_jpnlBorrowItems = new GridBagLayout();
		gbl_jpnlBorrowItems.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_jpnlBorrowItems.rowHeights = new int[]{0, 0};
		gbl_jpnlBorrowItems.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
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
		
		JLabel jlblWorthHeader = new JLabel("Worth");
		jlblWorthHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblWorthHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblWorthHeader = new GridBagConstraints();
		gbc_jlblWorthHeader.insets = new Insets(0, 0, 0, 5);
		gbc_jlblWorthHeader.gridx = 3;
		gbc_jlblWorthHeader.gridy = 0;
		jpnlBorrowItems.add(jlblWorthHeader, gbc_jlblWorthHeader);
		
		JLabel jlblIsReturnedHeader = new JLabel("Is Returned?");
		jlblIsReturnedHeader.setHorizontalAlignment(SwingConstants.TRAILING);
		jlblIsReturnedHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
		GridBagConstraints gbc_jlblIsReturnedHeader = new GridBagConstraints();
		gbc_jlblIsReturnedHeader.gridx = 4;
		gbc_jlblIsReturnedHeader.gridy = 0;
		jpnlBorrowItems.add(jlblIsReturnedHeader, gbc_jlblIsReturnedHeader);
		/* END OF jtxtareaDescription */

		/* jpnlButtons */
		JPanel jpnlButtons = new JPanel();
		jpnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(jpnlButtons, BorderLayout.SOUTH);
		/* END OF jpnlButtons */

		/* jbtnOk */
		JButton jbtnOk = new JButton("Finalize");
		jbtnOk.setActionCommand("OK");
		jbtnOk.addActionListener((event) -> {
			// Get return fee
			double returnFee = 0.0;
			try {
				returnFee = Double.parseDouble(jtxtfldReturnFee.getText());
			} catch(NumberFormatException e) {
				// If an error occured while fetching book copies, show dialog
				JOptionPane.showMessageDialog(
					returnDialog,
					"Invalid return fee. Must be a valid number.",
					"Invalid input!",
					JOptionPane.WARNING_MESSAGE);
				return;
			}
			double finalReturnFee = returnFee;
			
			// Get date now as returned-on date
			LocalDateTime returnedOn = LocalDateTime.now();
			
			// Update the borrow and borrowed book copy statuses with a SwingWorker Thread
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					try(Connection connection = borrowManagementPanel.dataSource.getConnection()) {
						
						// Set autocommit to false
						connection.setAutoCommit(false);
						
						// Update the borrow details
						try(PreparedStatement updateBorrowStatement =
								connection.prepareStatement("UPDATE borrow SET returned_on = ?, status = ?, return_fee = ? WHERE member_id = ? AND borrowed_on = ?")) {

							// Bind the new borrow details and PK
							updateBorrowStatement.setString(1, returnedOn.toString());
							updateBorrowStatement.setString(2, "RETURNED");
							updateBorrowStatement.setDouble(3, finalReturnFee);
							updateBorrowStatement.setInt(4, borrow.memberId());
							updateBorrowStatement.setString(5, borrow.borrowedOn().toString());
							updateBorrowStatement.execute();
						} catch(SQLException e) {
							connection.rollback();
							throw e;
						}
						
						// Update book copy details
						try(PreparedStatement updateBookCopyDetails =
								connection.prepareStatement("UPDATE book_copy SET borrow_status = ? WHERE isbn = ? AND copy_no = ?")) {
							
							for(BookCopy bookCopy : bookCopyIsReturnedCheckboxes.keySet()) {
								JCheckBox jchkIsReturned = (JCheckBox) bookCopyIsReturnedCheckboxes.get(bookCopy)[4];
								updateBookCopyDetails.setString(1, jchkIsReturned.isSelected() ? "AVAILABLE" : "LOST");
								updateBookCopyDetails.setString(2, bookCopy.isbn());
								updateBookCopyDetails.setInt(3, bookCopy.copyNo());
								updateBookCopyDetails.addBatch();
							}
							
							updateBookCopyDetails.executeBatch();
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
							"Successfully returned borrow items.",
							"Success!",
							JOptionPane.INFORMATION_MESSAGE);
						borrowManagementPanel.setCurrentPage(borrowManagementPanel.getCurrentPage());
					} catch (InterruptedException | ExecutionException e) {
						// If an error occured, show dialog and inform user.
						JOptionPane.showMessageDialog(
							borrowManagementPanel,
							"An error occured while trying to update borrow details.\n\nError: " + e.getMessage(),
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
		
		/* bookCopyIsReturnedCheckboxes */
		bookCopyIsReturnedCheckboxes = new HashMap<>();
		/* END OF bookCopyIsReturnedCheckboxes */
	}
	
	/**
	 * Prepares the form for returning a borrow.
	 */
	public void initialize(Borrow borrow) {
		this.borrow = borrow;
		
		// Set the input fields to borrow details
		jtxtfldMemberName.setText(borrow.memberName());
		jtxtfldBorrowedOn.setText(borrow.borrowedOn().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
		jtxtfldTargetReturnDate.setText(borrow.targetReturnDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
		jtxtfldReturnFee.setText("0.00");
		
		// Remove the UI components in borrow items panel
		for(BookCopy bookCopy : bookCopyIsReturnedCheckboxes.keySet())
			for(JComponent jcmpBookCopyComponent : bookCopyIsReturnedCheckboxes.get(bookCopy))
				jpnlBorrowItems.remove(jcmpBookCopyComponent);
		bookCopyIsReturnedCheckboxes.clear();
		
		// Generate the new UI Components
		int i = 1;
		for(BookCopy bookCopy : borrow.itemList()) {
			
			JComponent[] bookCopyUIComponents = new JComponent[5];
			
			// ISBN Label
			JLabel jlblCopyIsbn = new JLabel(bookCopy.isbn());
			GridBagConstraints gbc_jlblCopyIsbn = new GridBagConstraints();
			gbc_jlblCopyIsbn.insets = new Insets(0, 0, 5, 5);
			gbc_jlblCopyIsbn.gridx = 0;
			gbc_jlblCopyIsbn.gridy = i;
			bookCopyUIComponents[0] = jlblCopyIsbn;
			jpnlBorrowItems.add(jlblCopyIsbn, gbc_jlblCopyIsbn);
			
			// Copy Number Label
			JLabel jlblCopyNumber = new JLabel("Copy" + bookCopy.copyNo());
			GridBagConstraints gbc_jlblCopyNumber = new GridBagConstraints();
			gbc_jlblCopyNumber.insets = new Insets(0, 0, 5, 5);
			gbc_jlblCopyNumber.gridx = 1;
			gbc_jlblCopyNumber.gridy = i;
			bookCopyUIComponents[1] = jlblCopyNumber;
			jpnlBorrowItems.add(jlblCopyNumber, gbc_jlblCopyNumber);
			
			// Title Label
			JLabel jlblCopyTitle = new JLabel(bookCopy.title());
			GridBagConstraints gbc_jlblCopyTitle = new GridBagConstraints();
			gbc_jlblCopyTitle.insets = new Insets(0, 0, 5, 5);
			gbc_jlblCopyTitle.gridx = 2;
			gbc_jlblCopyTitle.gridy = i;
			bookCopyUIComponents[2] = jlblCopyTitle;
			jpnlBorrowItems.add(jlblCopyTitle, gbc_jlblCopyTitle);
			
			// Worth Label
			JLabel jlblWorth = new JLabel("" + bookCopy.currentWorth());
			GridBagConstraints gbc_jlblWorth = new GridBagConstraints();
			gbc_jlblWorth.insets = new Insets(0, 0, 5, 5);
			gbc_jlblWorth.gridx = 3;
			gbc_jlblWorth.gridy = i;
			bookCopyUIComponents[3] = jlblWorth;
			jpnlBorrowItems.add(jlblWorth, gbc_jlblWorth);
			
			// Is Returned Checkbox
			JCheckBox jchkIsReturned = new JCheckBox("Returned?");
			GridBagConstraints gbc_jchkIsReturned = new GridBagConstraints();
			gbc_jchkIsReturned.insets = new Insets(0, 0, 5, 5);
			gbc_jchkIsReturned.gridx = 4;
			gbc_jchkIsReturned.gridy = i;
			bookCopyUIComponents[4] = jchkIsReturned;
			jpnlBorrowItems.add(jchkIsReturned, gbc_jchkIsReturned);
			
			bookCopyIsReturnedCheckboxes.put(bookCopy, bookCopyUIComponents);
			
			i++;
		}
		
		validate();
		repaint();
	}
	
}
