package co.bisri.librarysystem.admin.ui.bookcopy;

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

import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopy;
import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopyTableRecord;
import co.bisri.librarysystem.admin.ui.util.PageButtonPanel;

public class BookCopyManagementPanel extends JPanel {

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
	private JTable jtblBookCopies;
	
	/**
	 * Table Model of jtblBookCopies
	 */
	private BookCopyTableModel bookCopyTableModel;
	
	/**
	 * Paginating page button panel.
	 */
	private PageButtonPanel pageButtonPanel;

	/**
	 * Form Dialog of this panel, for adding or updating categories
	 */
	protected FormDialog formDialog;
	
	// Total number of pages available in book category table based on internal size
	private int totalPageCount;
	// Current rendered page
	private int currentPage;

	/**
	 * Construct the panel.
	 */
	public BookCopyManagementPanel() {
		BookCopyManagementPanel bookCopyManagementPanel = this;
		
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
		JLabel jlblHeader = new JLabel("Manage Book Copies");
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
		JButton jbtnUpdate = new JButton("Update");
		jbtnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnUpdate.setBackground(Color.WHITE);
		jbtnUpdate.setFont(new Font("Roboto", Font.PLAIN, 12));
		jbtnUpdate.addActionListener((event) -> {
			// Get current selected row
			int selectedRow = jtblBookCopies.getSelectedRow();
			
			// If no row is selected, don't proceed
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
						"Please select a record first before clicking the update button.",
						"Select first!",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Else, fetch the respective bookcategory from the database
			String selectedIsbn = bookCopyTableModel.getBookIsbnAtRow(selectedRow);
			int selectedCopyNo = bookCopyTableModel.getCopyNoAtRow(selectedRow);
			
			BookCopy bookCopy = null;
			try(Connection connection = dataSource.getConnection();
				PreparedStatement retrieveStatement = connection.prepareStatement("SELECT date_acquired, status, current_worth, borrow_status FROM book_copy WHERE isbn = ? AND copy_no = ?")) {
				
				// Bind the PK
				retrieveStatement.setString(1, selectedIsbn);
				retrieveStatement.setInt(2, selectedCopyNo);
				
				try(ResultSet bookCopyResultSet = retrieveStatement.executeQuery()) {
					// If a record was found, retrieve
					if(bookCopyResultSet.next())
						bookCopy = new BookCopy(
							selectedIsbn,
							selectedCopyNo,
							LocalDate.parse(bookCopyResultSet.getString("date_acquired")),
							bookCopyResultSet.getString("status"),
							bookCopyResultSet.getDouble("current_worth"),
							bookCopyResultSet.getString("borrow_status"));
					// If no record was found, show message dialog
					else {
						JOptionPane.showMessageDialog(
							bookCopyManagementPanel,
							"No corresponding record was found.",
							"Select first!",
							JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			} catch(SQLException e) {
				// If an exception occured, show dialog and inform user
				JOptionPane.showMessageDialog(
					bookCopyManagementPanel,
					"An error occured while trying to fetch book copy from the database. Error: " + e.getMessage(),
					"Error!",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Prepare the dialog for updating entry
			formDialog.reset(bookCopy);
			formDialog.setVisible(true);
		});
		jpnlButtonActions.add(jbtnUpdate);
		/* END OF jbtnUpdate */

		/* jbtnDelete - button for deleting account */
		JButton jbtnDelete = new JButton("Delete");
		jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnDelete.setBackground(Color.WHITE);
		jbtnDelete.setFont(new Font("Roboto", Font.PLAIN, 12));
		jbtnDelete.addActionListener((event) -> {
			// Get current selected row
			int selectedRow = jtblBookCopies.getSelectedRow();
			
			// If no row is selected, don't proceed
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(
						bookCopyManagementPanel,
						"Please select a record first before clicking the delete button.",
						"Select first!",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Else, confirm the user, then proceed if user agrees
			String selectedIsbn = bookCopyTableModel.getBookIsbnAtRow(selectedRow);
			String selectedTitle = bookCopyTableModel.getBookTitleAtRow(selectedRow);
			int selectedCopyNo = bookCopyTableModel.getCopyNoAtRow(selectedRow);
			
			if(JOptionPane.showConfirmDialog(
					bookCopyManagementPanel,
					"Are you sure you want to delete book copy no. " + selectedCopyNo + " of " + selectedTitle + "?",
					"Confirmation",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				
				// Use a SwingWorker thread to perform delete
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						try(Connection connection = dataSource.getConnection();
							PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM book_copy WHERE isbn = ? AND copy_no = ?")) {
							
							// Bind the name retrieved from table
							deleteStatement.setString(1, selectedIsbn);
							deleteStatement.setInt(2, selectedCopyNo);
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
									bookCopyManagementPanel,
									"Successfully deleted copy.",
									"Success!",
									JOptionPane.INFORMATION_MESSAGE);
							// Update the management panel
							setCurrentPage(currentPage);
						} catch (InterruptedException | ExecutionException e) {
							// If an error occured, show dialog and inform user.
							JOptionPane.showMessageDialog(
								bookCopyManagementPanel,
								"An error occured while trying to delete copy.\n\nError: " + e.getMessage(),
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

		/* jtblBookCopies - Main Panel Table */
		jtblBookCopies = new JTable();
		jtblBookCopies.setMaximumSize(new Dimension(32767, 32767));
		jtblBookCopies.setRowHeight(22);
		jtblBookCopies.setIntercellSpacing(new Dimension(4, 4));
		jscrlpnAccounts.setViewportView(jtblBookCopies);
		// Table Model
		bookCopyTableModel = new BookCopyTableModel();
		jtblBookCopies.setModel(bookCopyTableModel);
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
		formDialog.bookCopyManagementPanel = this;
		/* END OF formDialog */
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
				return fetchBookCopyCount();
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
		
		// Use a SwingWorker to fetch all book copies on current page
		new SwingWorker<List<BookCopyTableRecord>, Void>() {
			@Override
			protected List<BookCopyTableRecord> doInBackground() throws Exception {
				return fetchBookCopies(currentPage);
			}
			@Override
			protected void done() {
				try {
					bookCopyTableModel.updateCache(get());
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
	
	// Fetch all book copies based on given page
	private List<BookCopyTableRecord> fetchBookCopies(int page) {
		int offset = (page - 1) * PAGE_SIZE;
		
		List<BookCopyTableRecord> bookCopyList = new ArrayList<>();
		
		try(Connection connection = dataSource.getConnection();
			Statement retrieveStatement = connection.createStatement();
			ResultSet bookCopyResultSet = retrieveStatement.executeQuery("SELECT bc.isbn, bc.copy_no, bk.title, bc.status, bc.borrow_status FROM book_copy bc INNER JOIN book bk ON bk.isbn = bc.isbn LIMIT " + PAGE_SIZE + " OFFSET " + offset)) {
			
			while(bookCopyResultSet.next())
				bookCopyList.add(
						new BookCopyTableRecord(
								bookCopyResultSet.getString("isbn"),
								bookCopyResultSet.getInt("copy_no"),
								bookCopyResultSet.getString("title"),
								bookCopyResultSet.getString("status"),
								bookCopyResultSet.getString("borrow_status")));
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return bookCopyList;
	}
	
	// Fetch book copy row count
	private int fetchBookCopyCount() {
		try(Connection connection = dataSource.getConnection();
			Statement retrieveStatement = connection.createStatement();
			ResultSet countResultSet = retrieveStatement.executeQuery("SELECT COUNT(isbn) AS total_count FROM book_copy")) {
			
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
	
