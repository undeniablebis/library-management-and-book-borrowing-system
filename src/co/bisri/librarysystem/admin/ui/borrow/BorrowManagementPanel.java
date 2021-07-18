package co.bisri.librarysystem.admin.ui.borrow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import co.bisri.librarysystem.admin.ui.borrow.record.BookCopy;
import co.bisri.librarysystem.admin.ui.borrow.record.Borrow;
import co.bisri.librarysystem.admin.ui.borrow.record.BorrowTableRecord;
import co.bisri.librarysystem.admin.ui.util.PageButtonPanel;

public class BorrowManagementPanel extends JPanel {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to
	 * remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Page Size
	 */
	private final int PAGE_SIZE = 20;
	
	/**
	 * Main datasource
	 */
	protected DataSource dataSource;
	
	/**
	 * The main table of this panel.
	 */
	private JTable jtblBorrows;
	
	/**
	 * Table Model of jtblBooksCategory
	 */
	private BorrowTableModel borrowTableModel;
	
	/**
	 * Paginating page button panel.
	 */
	private PageButtonPanel pageButtonPanel;

	/**
	 * Form Dialog of this panel, for adding borrows
	 */
	protected FormDialog formDialog;
	
	/**
	 * Return Dialog for returning borrows
	 */
	protected ReturnDialog returnDialog;
	
	// Total number of pages available in book category table based on internal size
	private int totalPageCount;
	// Current rendered page
	private int currentPage;

	/**
	 * Construct the panel.
	 */
	public BorrowManagementPanel() {
		BorrowManagementPanel borrowManagementPanel = this;
		
		// Set border to EmptyBorder for spacing
		setBorder(new EmptyBorder(10, 10, 10, 10));
		// Use BoxLayout to lay the internal 3 panels: Header, Table, Pagination Actions
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		/* jpnlHeader - Header Panel */
		JPanel jpnlHeader = new JPanel();
		jpnlHeader.setAlignmentY(0.0f);
		jpnlHeader.setAlignmentX(0.0f);
		jpnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
		jpnlHeader.setMinimumSize(new Dimension(10, 45));
		jpnlHeader.setMaximumSize(new Dimension(32767, 55));
		add(jpnlHeader);
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
		/* END OF jpnlHeader */

		/* jlblHeader - Header label */
		JLabel jlblHeader = new JLabel("Manage Borrows");
		jlblHeader.setAlignmentY(0.0f);
		jlblHeader.setFont(new Font("Roboto Light", Font.BOLD, 24));
		jpnlHeader.add(jlblHeader);
		/* END OF jlblHeader */

		/* jpnlButtonActions - panel for buttons */
		JPanel jpnlButtonActions = new JPanel();
		jpnlButtonActions.setAlignmentY(0.0f);
		jpnlButtonActions.setAlignmentX(0.0f);
		FlowLayout fl_jpnlButtonActions = (FlowLayout) jpnlButtonActions.getLayout();
		fl_jpnlButtonActions.setAlignment(FlowLayout.RIGHT);
		jpnlHeader.add(jpnlButtonActions);
		/* END OF jpnlButtonActions */

		/* jbtnShowAddForm - button for adding an account */
		JButton jbtnAdd = new JButton("Add");
		jbtnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnAdd.setBackground(Color.WHITE);
		jbtnAdd.setFont(new Font("Roboto", Font.PLAIN, 12));
		jbtnAdd.addActionListener((event) -> {
			// Prepare the dialog for new entry
			formDialog.reset();
			// Show the dialog
			formDialog.setVisible(true);
		});
		jpnlButtonActions.add(jbtnAdd);
		/* END OF jbtnShowAddForm */

		/* jbtnUpdate - button for updating account */
		JButton jbtnReturn = new JButton("Return");
		jbtnReturn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnReturn.setBackground(Color.WHITE);
		jbtnReturn.setFont(new Font("Roboto", Font.PLAIN, 12));
		jbtnReturn.addActionListener((event) -> {
			// Get current selected row
			int selectedRow = jtblBorrows.getSelectedRow();
			
			// If no row is selected, don't proceed
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(
						borrowManagementPanel,
						"Please select a record first before clicking the return button.",
						"Select first!",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			

			// Get PK
			int memberId = borrowTableModel.getMemberIdAtRow(selectedRow);
			LocalDate borrowedOn = borrowTableModel.getBorrowDateAtRow(selectedRow);
			
			// Fetch respective borrow
			Borrow borrow = null;
			try(Connection connection = dataSource.getConnection();
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
				
				try(ResultSet borrowResultSet = retrieveBorrowStatement.executeQuery()) {
					if(borrowResultSet.next()) {
						// Parse borrow
						LocalDate targetReturnDate = LocalDate.parse(borrowResultSet.getString("target_return_date"));
						String memberName = borrowResultSet.getString("first_name") + " " + borrowResultSet.getString("last_name");
						String status = borrowResultSet.getString("status");
						
						List<BookCopy> bookCopies = new ArrayList<>();
						// Parse first Item
						BookCopy firstBookCopy =
								new BookCopy(
										borrowResultSet.getString("book_isbn"),
										borrowResultSet.getInt("book_copy_no"),
										borrowResultSet.getString("title"),
										borrowResultSet.getDouble("current_worth"));
						bookCopies.add(firstBookCopy);
						
						// Parse remaining items
						while(borrowResultSet.next()) {
							BookCopy bookCopy =
									new BookCopy(
											borrowResultSet.getString("book_isbn"),
											borrowResultSet.getInt("book_copy_no"),
											borrowResultSet.getString("title"),
											borrowResultSet.getDouble("current_worth"));
							bookCopies.add(bookCopy);
						}
						
						borrow = new Borrow(memberId, memberName, borrowedOn, targetReturnDate, null, status, 0.00, bookCopies);
					} else {
						// If no records were found, show dialog and inform user.
						JOptionPane.showMessageDialog(
							borrowManagementPanel,
							"No borrow found with given member id and date.",
							"Invalid fields!",
							JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			} catch(SQLException e) {
				// If an error occured, show dialog and inform user.
				JOptionPane.showMessageDialog(
					borrowManagementPanel,
					"An error occured while trying to fetch borrow from database.\n\nError: " + e.getMessage(),
					"Database access error!",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Check if borrow is already returned
			if(borrow.status().contentEquals("RETURNED")) {
				// If borrow is already returned, dont proceed
				JOptionPane.showMessageDialog(
					borrowManagementPanel,
					"This borrow is already returned.",
					"Already returned",
					JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			// Initialize return dialog with the retrieved borrow item
			returnDialog.initialize(borrow);
			// Show return dialog
			returnDialog.setVisible(true);
		});
		jpnlButtonActions.add(jbtnReturn);
		/* END OF jbtnUpdate */

		/* jbtnDelete - button for deleting account */
		JButton jbtnDelete = new JButton("Delete");
		jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnDelete.setBackground(Color.WHITE);
		jbtnDelete.setFont(new Font("Roboto", Font.PLAIN, 12));
		jbtnDelete.addActionListener((event) -> {
			// Get current selected row
			int selectedRow = jtblBorrows.getSelectedRow();
			
			// If no row is selected, don't proceed
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(
						borrowManagementPanel,
						"Please select a record first before clicking the delete button.",
						"Select first!",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Else, confirm the user, then proceed if user agrees
			
			// Get PK
			int memberId = borrowTableModel.getMemberIdAtRow(selectedRow);
			LocalDate borrowedOn = borrowTableModel.getBorrowDateAtRow(selectedRow);
			// For dialog confirmation
			String memberName = (String) borrowTableModel.getValueAt(selectedRow, 0);
			
			if(JOptionPane.showConfirmDialog(
					borrowManagementPanel,
					"Are you sure you want to delete borrow by " + memberName + " in " + borrowedOn.toString() + "?",
					"Confirmation",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				
				// Use a SwingWorker thread to perform delete
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						try(Connection connection = dataSource.getConnection();
							PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM borrow WHERE member_id = ? AND borrowed_on = ?")) {
							
							// Bind the PK
							deleteStatement.setInt(1, memberId);
							deleteStatement.setString(2, borrowedOn.toString());
							// Execute delete
							deleteStatement.execute();
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
									"Successfully deleted borrow.",
									"Success!",
									JOptionPane.INFORMATION_MESSAGE);
							// Update the management panel
							setCurrentPage(currentPage);
						} catch (InterruptedException | ExecutionException e) {
							// If an error occured, show dialog and inform user.
							JOptionPane.showMessageDialog(
								borrowManagementPanel,
								"An error occured while trying to delete borrow.\n\nError: " + e.getMessage(),
								"Database access error!",
								JOptionPane.ERROR_MESSAGE);
						}
					}
				}.execute();
			}
		});
		jpnlButtonActions.add(jbtnDelete);
		/* END OF jbtnDelete */

		/* jscrlpnAccounts - Scrollable Table Panel */
		JScrollPane jscrlpnAccounts = new JScrollPane();
		jscrlpnAccounts.setMaximumSize(new Dimension(32767, 32767));
		jscrlpnAccounts.setAlignmentX(0.0f);
		jscrlpnAccounts.setAlignmentY(0.0f);
		add(jscrlpnAccounts);
		/* END OF jscrlpnAccounts */

		/* jtblBorrows - Main Panel Table */
		jtblBorrows = new JTable();
		jtblBorrows.setMaximumSize(new Dimension(32767, 32767));
		jtblBorrows.setRowHeight(22);
		jtblBorrows.setIntercellSpacing(new Dimension(4, 4));
		jscrlpnAccounts.setViewportView(jtblBorrows);
		// Table Model
		borrowTableModel = new BorrowTableModel();
		jtblBorrows.setModel(borrowTableModel);
		/* END OF jtblBooksCategory */
		
		/* pageButtonPanel - paginating buttons */
		pageButtonPanel = new PageButtonPanel(
			// ActionListener for previous button
			(event) -> {
				// If it's already the first page, previous button should do nothing
				if(currentPage == 1)
					return;
				
				// Else, set the current page to the previous page
				setCurrentPage(--currentPage);
			},
			// ActionListener for next button
			(event) -> {
				// If it's already the last page, next button should do nothing
				if(currentPage == totalPageCount)
					return;
				
				// Else, set the current page to the next page
				setCurrentPage(++currentPage);
			},
			// ActionListener for normal page button
			(event) -> {
				// Get page value of button
				int pageNumber = Integer.parseInt(((JButton) event.getSource()).getText());
				
				// If current page is already rendered, then button should do nothing
				if(currentPage == pageNumber)
					return;
				
				// Else, set the current page to the clicked button's page
				setCurrentPage(pageNumber);
			});
		pageButtonPanel.setMaximumSize(new Dimension(32767, 100));
		pageButtonPanel.setAlignmentX(0.0f);
		pageButtonPanel.setAlignmentY(0.0f);
		add(pageButtonPanel);
		/* END OF pageButtonPanel */
		
		/* formDialog */
		formDialog = new FormDialog();
		formDialog.borrowManagementPanel = this;
		/* END OF formDialog */
		
		/* returnDialog */
		returnDialog = new ReturnDialog();
		returnDialog.borrowManagementPanel = this;
		/* END OF returnDialog */
	}

	/**
	 * Sets the datasource that this panel should use
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Updates the currently shown categories page
	 * @param newPage the new page number to show
	 */
	public void setCurrentPage(int newPage) {
		// Update current page
		currentPage = newPage;
		
		// Use a SwingWorker to fetch row count and calculate page count
		new SwingWorker<Integer, Void>() {
			@Override
			protected Integer doInBackground() throws Exception {
				return fetchBorrowCount();
			}
			@Override
			protected void done() {
				try {
					totalPageCount = get();
					pageButtonPanel.setTotalPageCount(totalPageCount);
					pageButtonPanel.setCurrentPage(newPage);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}.execute();
		
		// Use a SwingWorker to fetch all borrows on current page
		new SwingWorker<List<BorrowTableRecord>, Void>() {
			@Override
			protected List<BorrowTableRecord> doInBackground() throws Exception {
				return fetchBorrows(currentPage);
			}
			@Override
			protected void done() {
				try {
					borrowTableModel.updateCache(get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	/**
	 * Get the current page displayed by this panel.
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	
	// Fetch all book categories based on given page
	private List<BorrowTableRecord> fetchBorrows(int page) {
		int offset = (page - 1) * PAGE_SIZE;
		
		List<BorrowTableRecord> borrowTableRecordList = new ArrayList<>();
		
		try(Connection connection = dataSource.getConnection();
			Statement retrieveStatement = connection.createStatement();
			ResultSet borrowResultSet = retrieveStatement.executeQuery(
					"SELECT m.id, m.first_name, m.last_name, b.borrowed_on, b.target_return_date, b.returned_on, b.status, b.return_fee "
					+ "FROM borrow b "
					+ "INNER JOIN member m ON m.id = b.member_id LIMIT " + PAGE_SIZE + " OFFSET " + offset)) {
			
			while(borrowResultSet.next())
				borrowTableRecordList.add(
						new BorrowTableRecord(
								borrowResultSet.getInt("id"),
								borrowResultSet.getString("first_name"),
								borrowResultSet.getString("last_name"),
								LocalDate.parse(borrowResultSet.getString("borrowed_on")),
								LocalDate.parse(borrowResultSet.getString("target_return_date")),
								borrowResultSet.getString("returned_on") != null ?
										LocalDateTime.parse(borrowResultSet.getString("returned_on"),
												DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
								borrowResultSet.getString("status"),
								borrowResultSet.getDouble("return_fee")));
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return borrowTableRecordList;
	}
	
	// Fetch book category row count
	private int fetchBorrowCount() {
		try(Connection connection = dataSource.getConnection();
			Statement retrieveStatement = connection.createStatement();
			ResultSet countResultSet = retrieveStatement.executeQuery("SELECT COUNT(member_id) AS total_count FROM borrow")) {
			
			countResultSet.next();
			double totalCount = countResultSet.getDouble("total_count");
			int totalPageCount = (int) Math.ceil(totalCount / PAGE_SIZE);
			return totalPageCount;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * Shows the first page of the panel.
	 */
	public void initializePanel() {
		setCurrentPage(1);
	}
	
}
	
