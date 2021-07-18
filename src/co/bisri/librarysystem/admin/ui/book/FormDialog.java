package co.bisri.librarysystem.admin.ui.book;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import co.bisri.librarysystem.admin.ui.util.FormOperation;

public class FormDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected BooksManagementPanel booksManagementPanel;

	// Content Panel for input fields
	private final JPanel jpnlContent;
	// Book ISBN (PK), only used when updating
		private String oldBookIsbn;
		private FormOperation currentOperation;
	// Header label
	private JLabel jlblHeader;
	private JTextField jtxtfldIsbn;
	private JTextField jtxtfldCategoryName;
	private JTextField jtxtfldTitle;
	private JTextField jtxtfldAuthor;
	private JTextField jtxtfldPublisher;
	private JTextField jtxtfldPublishedOn;

	/**
	 * Create the dialog.
	 */
	public FormDialog() {
		// Reference for nested anonymous contexts
		FormDialog formDialog = this;

		/* Dialog properties */
		setTitle("Save Book");
		setBounds(100, 100, 559, 233);
		getContentPane().setLayout(new BorderLayout());
		/* END OF Dialog properties */

		/* jpnlContent */
		jpnlContent = new JPanel();
		jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
		getContentPane().add(jpnlContent, BorderLayout.CENTER);
		GridBagLayout gbl_jpnlContent = new GridBagLayout();
		gbl_jpnlContent.columnWidths = new int[] { 58, 163, 65, 39, 0 };
		gbl_jpnlContent.rowHeights = new int[] { 0, 0, 0, 27, 0, 0 };
		gbl_jpnlContent.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_jpnlContent.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		jpnlContent.setLayout(gbl_jpnlContent);
		/* END OF jpnlContent */

		/* jlblHeader */
		jlblHeader = new JLabel("Book");
		jlblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		jlblHeader.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		GridBagConstraints gbc_jlblHeader = new GridBagConstraints();
		gbc_jlblHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblHeader.gridwidth = 4;
		gbc_jlblHeader.insets = new Insets(0, 0, 5, 0);
		gbc_jlblHeader.gridx = 0;
		gbc_jlblHeader.gridy = 0;
		jpnlContent.add(jlblHeader, gbc_jlblHeader);
		{
			JLabel jlblIsbn = new JLabel("ISBN");
			jlblIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblIsbn = new GridBagConstraints();
			gbc_jlblIsbn.anchor = GridBagConstraints.EAST;
			gbc_jlblIsbn.insets = new Insets(0, 0, 5, 5);
			gbc_jlblIsbn.gridx = 0;
			gbc_jlblIsbn.gridy = 1;
			jpnlContent.add(jlblIsbn, gbc_jlblIsbn);
		}
		{
			jtxtfldIsbn = new JTextField();
			jtxtfldIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldIsbn = new GridBagConstraints();
			gbc_jtxtfldIsbn.insets = new Insets(0, 0, 5, 5);
			gbc_jtxtfldIsbn.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldIsbn.gridx = 1;
			gbc_jtxtfldIsbn.gridy = 1;
			jpnlContent.add(jtxtfldIsbn, gbc_jtxtfldIsbn);
			jtxtfldIsbn.setColumns(10);
		}
		{
			JLabel jlblTitle = new JLabel("Title");
			jlblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblTitle = new GridBagConstraints();
			gbc_jlblTitle.anchor = GridBagConstraints.EAST;
			gbc_jlblTitle.insets = new Insets(0, 0, 5, 5);
			gbc_jlblTitle.gridx = 2;
			gbc_jlblTitle.gridy = 1;
			jpnlContent.add(jlblTitle, gbc_jlblTitle);
		}
		{
			jtxtfldTitle = new JTextField();
			jtxtfldTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldTitle = new GridBagConstraints();
			gbc_jtxtfldTitle.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldTitle.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldTitle.gridx = 3;
			gbc_jtxtfldTitle.gridy = 1;
			jpnlContent.add(jtxtfldTitle, gbc_jtxtfldTitle);
			jtxtfldTitle.setColumns(10);
		}
		{
			JLabel jlblCategoryName = new JLabel("Category Name");
			jlblCategoryName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblCategoryName = new GridBagConstraints();
			gbc_jlblCategoryName.anchor = GridBagConstraints.EAST;
			gbc_jlblCategoryName.insets = new Insets(0, 0, 5, 5);
			gbc_jlblCategoryName.gridx = 0;
			gbc_jlblCategoryName.gridy = 2;
			jpnlContent.add(jlblCategoryName, gbc_jlblCategoryName);
		}
		{
			jtxtfldCategoryName = new JTextField();
			jtxtfldCategoryName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldCategoryName = new GridBagConstraints();
			gbc_jtxtfldCategoryName.insets = new Insets(0, 0, 5, 5);
			gbc_jtxtfldCategoryName.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldCategoryName.gridx = 1;
			gbc_jtxtfldCategoryName.gridy = 2;
			jpnlContent.add(jtxtfldCategoryName, gbc_jtxtfldCategoryName);
			jtxtfldCategoryName.setColumns(10);
		}
		{
			JLabel jlblAuthor = new JLabel("Author");
			jlblAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblAuthor = new GridBagConstraints();
			gbc_jlblAuthor.anchor = GridBagConstraints.EAST;
			gbc_jlblAuthor.insets = new Insets(0, 0, 5, 5);
			gbc_jlblAuthor.gridx = 2;
			gbc_jlblAuthor.gridy = 2;
			jpnlContent.add(jlblAuthor, gbc_jlblAuthor);
		}
		{
			jtxtfldAuthor = new JTextField();
			jtxtfldAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldAuthor = new GridBagConstraints();
			gbc_jtxtfldAuthor.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldAuthor.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldAuthor.gridx = 3;
			gbc_jtxtfldAuthor.gridy = 2;
			jpnlContent.add(jtxtfldAuthor, gbc_jtxtfldAuthor);
			jtxtfldAuthor.setColumns(10);
		}
		{
			JLabel jlblPublisher = new JLabel("Publisher");
			jlblPublisher.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblPublisher = new GridBagConstraints();
			gbc_jlblPublisher.anchor = GridBagConstraints.EAST;
			gbc_jlblPublisher.insets = new Insets(0, 0, 5, 5);
			gbc_jlblPublisher.gridx = 0;
			gbc_jlblPublisher.gridy = 3;
			jpnlContent.add(jlblPublisher, gbc_jlblPublisher);
		}
		{
			jtxtfldPublisher = new JTextField();
			jtxtfldPublisher.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldPublisher = new GridBagConstraints();
			gbc_jtxtfldPublisher.insets = new Insets(0, 0, 5, 5);
			gbc_jtxtfldPublisher.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldPublisher.gridx = 1;
			gbc_jtxtfldPublisher.gridy = 3;
			jpnlContent.add(jtxtfldPublisher, gbc_jtxtfldPublisher);
			jtxtfldPublisher.setColumns(10);
		}
		{
			JLabel jlblPublishedOn = new JLabel("Published On");
			jlblPublishedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblPublishedOn = new GridBagConstraints();
			gbc_jlblPublishedOn.anchor = GridBagConstraints.EAST;
			gbc_jlblPublishedOn.insets = new Insets(0, 0, 5, 5);
			gbc_jlblPublishedOn.gridx = 2;
			gbc_jlblPublishedOn.gridy = 3;
			jpnlContent.add(jlblPublishedOn, gbc_jlblPublishedOn);
		}
		{
			jtxtfldPublishedOn = new JTextField();
			jtxtfldPublishedOn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldPublishedOn = new GridBagConstraints();
			gbc_jtxtfldPublishedOn.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldPublishedOn.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldPublishedOn.gridx = 3;
			gbc_jtxtfldPublishedOn.gridy = 3;
			jpnlContent.add(jtxtfldPublishedOn, gbc_jtxtfldPublishedOn);
			jtxtfldPublishedOn.setColumns(10);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton jbtnOk = new JButton("OK");
				jbtnOk.setActionCommand("OK");
				jbtnOk.addActionListener((event) -> {

					// Fetch data
					String isbn = jtxtfldIsbn.getText();
					String title = jtxtfldTitle.getText();
					String categoryName = jtxtfldCategoryName.getText();
					String author = jtxtfldAuthor.getText();
					String publisher = jtxtfldPublisher.getText();
					LocalDate publishedOn = null;

					/* Validation Layer */

					// Check ISBN
					if (isbn.contentEquals("") || isbn.length() != 13) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for ISBN. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- must be 13 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}

					// Check Title
					if (title.contentEquals("") || title.length() > 64) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for title. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 64 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}

					// Check Category name
					if (categoryName.contentEquals("") || categoryName.length() > 64) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for category name. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 64 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}

					// Check Author
					if (author.contentEquals("") || author.length() > 128) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for Author. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 128 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}

					// Check Publisher
					if (publisher.contentEquals("") || publisher.length() > 128) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for publisher. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 128 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}

					// Check Published On

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
					Books books = new Books(isbn, categoryName, title, author, publishedOn, publisher);
					
					// Save it to database with a SwingWorker Thread in background
					new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							try(Connection connection = booksManagementPanel.dataSource.getConnection()) {
								switch(currentOperation) {
								
								// If form is in insert mode, perform an SQL INSERT
								case INSERT:
									try(PreparedStatement booksInsertStatement = connection.prepareStatement(
											"INSERT INTO book(isbn, category_name, title, author, published_on, publisher) VALUES(?,?,?,?,?,?)")) {
										booksInsertStatement.setString(1, books.isbn());
										booksInsertStatement.setString(2, books.categoryName());
										booksInsertStatement.setString(3, books.title());
										booksInsertStatement.setString(4, books.author());
										booksInsertStatement.setString(5, books.publishedOn().toString());
										booksInsertStatement.setString(6, books.publisher());
										booksInsertStatement.execute();
									}
									
									break;
								
								// Else, perform an SQL UPDATE with the old name
								case UPDATE:
									try(PreparedStatement booksUpdateStatement = connection.prepareStatement(
											"UPDATE book SET isbn = ?, category_name = ?, title = ?, author = ?, published_on = ?, "
											+ "publisher = ? WHERE isbn = ?")) {
										booksUpdateStatement.setString(1, books.isbn());
										booksUpdateStatement.setString(2, books.categoryName());
										booksUpdateStatement.setString(3, books.title());
										booksUpdateStatement.setString(4, books.author());
										booksUpdateStatement.setString(5, books.publishedOn().toString());
										booksUpdateStatement.setString(6, books.publisher());
										booksUpdateStatement.setString(7, oldBookIsbn);
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
									booksManagementPanel,
									"Successfully saved book to database. Refreshing your panel.",
									"Success!",
									JOptionPane.INFORMATION_MESSAGE);
								setVisible(false);
								booksManagementPanel.setCurrentPage(booksManagementPanel.getCurrentPage());
							} catch (InterruptedException | ExecutionException e) {
								e.printStackTrace();
								// If an error occured, show dialog and inform user.
								JOptionPane.showMessageDialog(
									booksManagementPanel,
									"An error occured while trying to save to database.\n\nError: " + e.getMessage(),
									"Database access error!",
									JOptionPane.ERROR_MESSAGE);
							}
						}
					}.execute();
				

				});
				buttonPane.add(jbtnOk);
				getRootPane().setDefaultButton(jbtnOk);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	/**
	 * Prepares the form for inserting a new member.
	 */
	public void reset() {
		currentOperation = FormOperation.INSERT;

		jlblHeader.setText("Add Member");

		oldBookIsbn = null;
		jtxtfldIsbn.setText("");
		jtxtfldCategoryName.setText("");
		jtxtfldAuthor.setText("");
		jtxtfldPublisher.setText("");
		jtxtfldTitle.setText("");
		jtxtfldPublishedOn.setText("");
	}
	
	/**
	 * Prepares the form for updating an existing member.
	 */
	public void reset(Books books) {
		currentOperation = FormOperation.UPDATE;

		jlblHeader.setText("Modify Member");

		oldBookIsbn = books.isbn();
		jtxtfldIsbn.setText(books.isbn());
		jtxtfldCategoryName.setText(books.categoryName());
		jtxtfldAuthor.setText(books.author());
		jtxtfldPublisher.setText(books.publisher());
		jtxtfldTitle.setText(books.title());
		jtxtfldPublishedOn.setText(books.publishedOn().toString());
	
	}

}
