package co.bisri.librarysystem.admin.ui.book;

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

import co.bisri.librarysystem.admin.ui.book.record.Books;
import co.bisri.librarysystem.admin.ui.util.PageButtonPanel;

public class BooksManagementPanel extends JPanel {

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
	private JTable jtblBooks;
	
	/**
	 * Table Model of jtblBooksCategory
	 */
	private BooksTableModel booksTableModel;
	
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
	public BooksManagementPanel() {
		BooksManagementPanel booksManagementPanel = this;
		
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
		JLabel jlblHeader = new JLabel("Manage Book");
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
			int selectedRow = jtblBooks.getSelectedRow();
			
			// If no row is selected, don't proceed
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(
						booksManagementPanel,
						"Please select a book first before clicking the update button.",
						"Select first!",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Else, fetch the respective Member from the database
			String selectedIsbn = (String) booksTableModel.getValueAt(selectedRow, 0);
			Books books = null;
			try(Connection connection = dataSource.getConnection();
				PreparedStatement retrieveStatement = connection.prepareStatement("SELECT isbn, category_name, title, author, "
						+ "published_on, publisher FROM book WHERE isbn = ?")) {
				
				// Bind the name retrieved from table
				retrieveStatement.setString(1, selectedIsbn);
				
				try(ResultSet booksResultSet = retrieveStatement.executeQuery()) {
					// If a record was found, retrieve
					if(booksResultSet.next())
						books = new Books(
							booksResultSet.getString("isbn"),
							booksResultSet.getString("category_name"),
							booksResultSet.getString("title"),
							booksResultSet.getString("author"),
							LocalDate.parse(booksResultSet.getString("published_on"), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
							booksResultSet.getString("publisher"));
							
					// If no record was found, show message dialog
					else {
						JOptionPane.showMessageDialog(
							booksManagementPanel,
							"No corresponding member was found.",
							"Select first!",
							JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			} catch(SQLException e) {
				// If an exception occured, show dialog and inform user
				JOptionPane.showMessageDialog(
					booksManagementPanel,
					"An error occured while trying to fetch book from the database. Error: " + e.getMessage(),
					"Error!",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Prepare the dialog for updating entry
			formDialog.reset(books);
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
			int selectedRow = jtblBooks.getSelectedRow();
			
			// If no row is selected, don't proceed
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(
						booksManagementPanel,
						"Please select a book first before clicking the delete button.",
						"Select first!",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// Else, confirm the user, then proceed if user agrees
			String selectedIsbn = (String) booksTableModel.getValueAt(selectedRow, 0);
			String bookTitle = (String) booksTableModel.getValueAt(selectedRow, 2);
			if(JOptionPane.showConfirmDialog(
					booksManagementPanel,
					"Are you sure you want to delete book named: " + bookTitle + "?",
					"Confirmation",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				
				// Use a SwingWorker thread to perform delete
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						try(Connection connection = dataSource.getConnection();
							PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM member WHERE id = ?")) {
							
							// Bind the name retrieved from table
							deleteStatement.setString(1, selectedIsbn);
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
									booksManagementPanel,
									"Successfully deleted member.",
									"Success!",
									JOptionPane.INFORMATION_MESSAGE);
							// Update the management panel
							setCurrentPage(currentPage);
						} catch (InterruptedException | ExecutionException e) {
							// If an error occured, show dialog and inform user.
							JOptionPane.showMessageDialog(
								booksManagementPanel,
								"An error occured while trying to delete book.\n\nError: " + e.getMessage(),
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

		/* jtblBooksCategory - Main Panel Table */
		jtblBooks = new JTable();
		jtblBooks.setMaximumSize(new Dimension(32767, 32767));
		jtblBooks.setRowHeight(22);
		jtblBooks.setIntercellSpacing(new Dimension(4, 4));
		jscrlpnAccounts.setViewportView(jtblBooks);
		// Table Model
		booksTableModel = new BooksTableModel();
		jtblBooks.setModel(booksTableModel);
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
		formDialog.booksManagementPanel = this;
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
		// Fetch the total page count from database
		totalPageCount = fetchMemberCount();
		
		// Use a SwingWorker to fetch row count and calculate page count
		new SwingWorker<Integer, Void>() {
			@Override
			protected Integer doInBackground() throws Exception {
				return fetchMemberCount();
			}
			@Override
			protected void done() {
				try {
					pageButtonPanel.setTotalPageCount(get());
					pageButtonPanel.setCurrentPage(newPage);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}.execute();
		
		// Use a SwingWorker to fetch all book categories on current page
		new SwingWorker<List<Books>, Void>() {
			@Override
			protected List<Books> doInBackground() throws Exception {
				return fetchBooks(currentPage);
			}
			@Override
			protected void done() {
				try {
					booksTableModel.updateCache(get());
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
	private List<Books> fetchBooks(int page) {
		int offset = (page - 1) * PAGE_SIZE;
		
		List<Books> BooksList = new ArrayList<>();
		
		try(Connection connection = dataSource.getConnection();
			Statement retrieveStatement = connection.createStatement();
			ResultSet booksResultSet = retrieveStatement.executeQuery("SELECT isbn, category_name, "
					+ "title, author, published_on, publisher FROM book LIMIT " + PAGE_SIZE + " OFFSET " + offset)) {
			
			while(booksResultSet.next())
				BooksList.add(
						new Books(
								booksResultSet.getString("isbn"),
								booksResultSet.getString("category_name"),
								booksResultSet.getString("title"),
								booksResultSet.getString("author"),
								LocalDate.parse(booksResultSet.getString("published_on")),
								booksResultSet.getString("publisher")));
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return BooksList;
	}
	
	// Fetch book category row count
	private int fetchMemberCount() {
		try(Connection connection = dataSource.getConnection();
			Statement retrieveStatement = connection.createStatement();
			ResultSet countResultSet = retrieveStatement.executeQuery("SELECT COUNT(isbn) AS total_count FROM book")) {
			
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