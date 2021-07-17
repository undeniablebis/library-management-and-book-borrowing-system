package co.bisri.librarysystem.admin.ui.bookcategory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

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
	protected BooksCategoryManagementPanel booksCategoryManagementPanel;

	// Content Panel for input fields
	private final JPanel jpnlContent;
	
	// Header label
	private JLabel jlblHeader;
	
	// Old category name (PK), only used when updating
	private String oldCategoryName;
	
	// Input fields
	private JTextField jtxtfldName;
	private JTextArea jtxtareaDescription;
	
	// SQL operation to perform when OK is clicked (insert or update)
	private FormOperation currentOperation;

	/**
	 * Create the dialog.
	 */
	public FormDialog() {
		// Reference for nested anonymous contexts
		FormDialog formDialog = this;
		
		/* Dialog properties */
		setTitle("Save Category");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		/* END OF Dialog properties */

		/* jpnlContent */
		jpnlContent = new JPanel();
		jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
		getContentPane().add(jpnlContent, BorderLayout.CENTER);
		GridBagLayout gbl_jpnlContent = new GridBagLayout();
		gbl_jpnlContent.columnWidths = new int[]{0, 0, 0};
		gbl_jpnlContent.rowHeights = new int[]{0, 0, 0, 0};
		gbl_jpnlContent.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_jpnlContent.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		jpnlContent.setLayout(gbl_jpnlContent);
		/* END OF jpnlContent */

		/* jlblHeader */
		jlblHeader = new JLabel("... Category");
		jlblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		jlblHeader.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		GridBagConstraints gbc_jlblHeader = new GridBagConstraints();
		gbc_jlblHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblHeader.gridwidth = 2;
		gbc_jlblHeader.insets = new Insets(0, 0, 5, 0);
		gbc_jlblHeader.gridx = 0;
		gbc_jlblHeader.gridy = 0;
		jpnlContent.add(jlblHeader, gbc_jlblHeader);
		/* END OF jlblHeader */

		/* jlblName */
		JLabel jlblName = new JLabel("Name:");
		jlblName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblName = new GridBagConstraints();
		gbc_jlblName.insets = new Insets(0, 0, 5, 5);
		gbc_jlblName.anchor = GridBagConstraints.EAST;
		gbc_jlblName.gridx = 0;
		gbc_jlblName.gridy = 1;
		jpnlContent.add(jlblName, gbc_jlblName);
		/* END OF jlblName */
		
		/* jtxtfldName */
		jtxtfldName = new JTextField();
		jtxtfldName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jtxtfldName = new GridBagConstraints();
		gbc_jtxtfldName.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldName.gridx = 1;
		gbc_jtxtfldName.gridy = 1;
		jpnlContent.add(jtxtfldName, gbc_jtxtfldName);
		jtxtfldName.setColumns(10);
		/* END OF jtxtfldName */

		/* jlblDescription */
		JLabel jlblDescription = new JLabel("Description:");
		jlblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		GridBagConstraints gbc_jlblDescription = new GridBagConstraints();
		gbc_jlblDescription.anchor = GridBagConstraints.NORTHEAST;
		gbc_jlblDescription.insets = new Insets(0, 0, 0, 5);
		gbc_jlblDescription.gridx = 0;
		gbc_jlblDescription.gridy = 2;
		jpnlContent.add(jlblDescription, gbc_jlblDescription);
		/* END OF jlblDescription */

		/* jscrlpnDescription */
		JScrollPane jscrlpnDescription = new JScrollPane();
		GridBagConstraints gbc_jscrlpnDescription = new GridBagConstraints();
		gbc_jscrlpnDescription.fill = GridBagConstraints.BOTH;
		gbc_jscrlpnDescription.gridx = 1;
		gbc_jscrlpnDescription.gridy = 2;
		jpnlContent.add(jscrlpnDescription, gbc_jscrlpnDescription);
		/* END OF jscrlpnDescription */

		/* jtxtareaDescription */
		jtxtareaDescription = new JTextArea();
		jscrlpnDescription.setViewportView(jtxtareaDescription);
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
						
						case INSERT:
							try(PreparedStatement bookCategoryInsertStatement = connection.prepareStatement(
									"INSERT INTO book_category(name, description) VALUES(?, ?)")) {
								bookCategoryInsertStatement.setString(1, bookCategory.name());
								bookCategoryInsertStatement.setString(2, bookCategory.description());
								bookCategoryInsertStatement.execute();
							}
							
							break;
							
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
						JOptionPane.showMessageDialog(
							booksCategoryManagementPanel,
							"Successfully saved category to database. Refreshing your panel.",
							"Success!",
							JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
						booksCategoryManagementPanel.initializePanel();
					} catch (InterruptedException | ExecutionException e) {
						JOptionPane.showMessageDialog(
							booksCategoryManagementPanel,
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
	}
	
	/**
	 * Prepares the form for inserting a new category.
	 */
	public void reset() {
		currentOperation = FormOperation.INSERT;
		
		jlblHeader.setText("Add Category");
		
		oldCategoryName = null;
		jtxtfldName.setText("");
		jtxtareaDescription.setText("");
	}
	
	/**
	 * Prepares the form for updating an existing category.
	 */
	public void reset(BookCategory bookCategory) {
		currentOperation = FormOperation.UPDATE;
		
		jlblHeader.setText("Modify Category");
		
		oldCategoryName = bookCategory.name();
		jtxtfldName.setText(bookCategory.name());
		jtxtareaDescription.setText(bookCategory.description());
	}

}
