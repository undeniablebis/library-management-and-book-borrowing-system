package co.bisri.librarysystem.admin.ui.member;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
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



	protected MemberManagementPanel memberManagementPanel;

	// Content Panel for input fields
	private final JPanel jpnlContent;

	// Header label
	private JLabel jlblHeader;

	// Member ID (PK), only used when updating
	private int oldMemberId;

	// SQL operation to perform when OK is clicked (insert or update)
	private FormOperation currentOperation;
	private JTextField jtxtfldFirstName;
	private JTextField jtxtfldLastName;
	private JTextField jtxtfldAddressLine1;
	private JTextField jtxtfldAddressLine2;
	private JTextField jtxtfldAddressLine3;
	private JTextField jtxtfldContactNumber;
	private JTextField jtxtfldEmailAddress;
	private JTextField jtxtfldDateRegistered;

	/**
	 * Create the dialog.
	 */
	public FormDialog() {

		// Reference for nested anonymous contexts
		FormDialog formDialog = this;

		/* Dialog properties */
		setTitle("Save Member");
		setBounds(100, 100, 559, 233);
		getContentPane().setLayout(new BorderLayout());
		/* END OF Dialog properties */

		/* jpnlContent */
		jpnlContent = new JPanel();
		jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
		getContentPane().add(jpnlContent, BorderLayout.CENTER);
		GridBagLayout gbl_jpnlContent = new GridBagLayout();
		gbl_jpnlContent.columnWidths = new int[] { 0, 115, 0, 39, 0 };
		gbl_jpnlContent.rowHeights = new int[] { 0, 0, 0, 27, 0, 0 };
		gbl_jpnlContent.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_jpnlContent.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		jpnlContent.setLayout(gbl_jpnlContent);
		/* END OF jpnlContent */

		/* jlblHeader */
		jlblHeader = new JLabel("Member");
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
			JLabel jlblFirstName = new JLabel("First name");
			jlblFirstName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblFirstName = new GridBagConstraints();
			gbc_jlblFirstName.anchor = GridBagConstraints.EAST;
			gbc_jlblFirstName.insets = new Insets(0, 0, 5, 5);
			gbc_jlblFirstName.gridx = 0;
			gbc_jlblFirstName.gridy = 1;
			jpnlContent.add(jlblFirstName, gbc_jlblFirstName);
		}
		{
			jtxtfldFirstName = new JTextField();
			jtxtfldFirstName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldFirstName = new GridBagConstraints();
			gbc_jtxtfldFirstName.insets = new Insets(0, 0, 5, 5);
			gbc_jtxtfldFirstName.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldFirstName.gridx = 1;
			gbc_jtxtfldFirstName.gridy = 1;
			jpnlContent.add(jtxtfldFirstName, gbc_jtxtfldFirstName);
			jtxtfldFirstName.setColumns(10);
		}
		{
			JLabel jlblLastName = new JLabel("Last name");
			jlblLastName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblLastName = new GridBagConstraints();
			gbc_jlblLastName.anchor = GridBagConstraints.EAST;
			gbc_jlblLastName.insets = new Insets(0, 0, 5, 5);
			gbc_jlblLastName.gridx = 2;
			gbc_jlblLastName.gridy = 1;
			jpnlContent.add(jlblLastName, gbc_jlblLastName);
		}
		{
			jtxtfldLastName = new JTextField();
			jtxtfldLastName.setHorizontalAlignment(SwingConstants.LEADING);
			jtxtfldLastName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			jtxtfldLastName.setEnabled(true);
			jtxtfldLastName.setEditable(true);
			jtxtfldLastName.setText("");
			GridBagConstraints gbc_jtxtfldLastName = new GridBagConstraints();
			gbc_jtxtfldLastName.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldLastName.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldLastName.gridx = 3;
			gbc_jtxtfldLastName.gridy = 1;
			jpnlContent.add(jtxtfldLastName, gbc_jtxtfldLastName);
			jtxtfldLastName.setColumns(10);
		}
		{
			JLabel jlblAddressLine1 = new JLabel("Street and House no.");
			jlblAddressLine1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblAddressLine1 = new GridBagConstraints();
			gbc_jlblAddressLine1.anchor = GridBagConstraints.NORTHEAST;
			gbc_jlblAddressLine1.insets = new Insets(0, 0, 5, 5);
			gbc_jlblAddressLine1.gridx = 0;
			gbc_jlblAddressLine1.gridy = 2;
			jpnlContent.add(jlblAddressLine1, gbc_jlblAddressLine1);
		}
		{
			jtxtfldAddressLine1 = new JTextField();
			jtxtfldAddressLine1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldAddressLine1 = new GridBagConstraints();
			gbc_jtxtfldAddressLine1.insets = new Insets(0, 0, 5, 5);
			gbc_jtxtfldAddressLine1.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldAddressLine1.gridx = 1;
			gbc_jtxtfldAddressLine1.gridy = 2;
			jpnlContent.add(jtxtfldAddressLine1, gbc_jtxtfldAddressLine1);
			jtxtfldAddressLine1.setColumns(10);
		}
		{
			JLabel jlblAddressLine2 = new JLabel("Brgy. Zone and District");
			jlblAddressLine2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblAddressLine2 = new GridBagConstraints();
			gbc_jlblAddressLine2.anchor = GridBagConstraints.EAST;
			gbc_jlblAddressLine2.insets = new Insets(0, 0, 5, 5);
			gbc_jlblAddressLine2.gridx = 2;
			gbc_jlblAddressLine2.gridy = 2;
			jpnlContent.add(jlblAddressLine2, gbc_jlblAddressLine2);
		}
		{
			jtxtfldAddressLine2 = new JTextField();
			jtxtfldAddressLine2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldAddressLine2 = new GridBagConstraints();
			gbc_jtxtfldAddressLine2.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldAddressLine2.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldAddressLine2.gridx = 3;
			gbc_jtxtfldAddressLine2.gridy = 2;
			jpnlContent.add(jtxtfldAddressLine2, gbc_jtxtfldAddressLine2);
			jtxtfldAddressLine2.setColumns(10);
		}
		{
			JLabel jlblAddressLine3 = new JLabel("City/Province");
			jlblAddressLine3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblAddressLine3 = new GridBagConstraints();
			gbc_jlblAddressLine3.anchor = GridBagConstraints.EAST;
			gbc_jlblAddressLine3.insets = new Insets(0, 0, 5, 5);
			gbc_jlblAddressLine3.gridx = 0;
			gbc_jlblAddressLine3.gridy = 3;
			jpnlContent.add(jlblAddressLine3, gbc_jlblAddressLine3);
		}
		{
			jtxtfldAddressLine3 = new JTextField();
			jtxtfldAddressLine3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldAddressLine3 = new GridBagConstraints();
			gbc_jtxtfldAddressLine3.insets = new Insets(0, 0, 5, 5);
			gbc_jtxtfldAddressLine3.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldAddressLine3.gridx = 1;
			gbc_jtxtfldAddressLine3.gridy = 3;
			jpnlContent.add(jtxtfldAddressLine3, gbc_jtxtfldAddressLine3);
			jtxtfldAddressLine3.setColumns(10);
		}
		{
			JLabel jlblContactNumber = new JLabel("Contact number");
			jlblContactNumber.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblContactNumber = new GridBagConstraints();
			gbc_jlblContactNumber.anchor = GridBagConstraints.EAST;
			gbc_jlblContactNumber.insets = new Insets(0, 0, 5, 5);
			gbc_jlblContactNumber.gridx = 2;
			gbc_jlblContactNumber.gridy = 3;
			jpnlContent.add(jlblContactNumber, gbc_jlblContactNumber);
		}
		{
			jtxtfldContactNumber = new JTextField();
			jtxtfldContactNumber.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldContactNumber = new GridBagConstraints();
			gbc_jtxtfldContactNumber.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldContactNumber.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldContactNumber.gridx = 3;
			gbc_jtxtfldContactNumber.gridy = 3;
			jtxtfldContactNumber.setText("+63");
			jpnlContent.add(jtxtfldContactNumber, gbc_jtxtfldContactNumber);
			jtxtfldContactNumber.setColumns(10);
		}
		{
			JLabel jlblEmailAddress = new JLabel("Email Address");
			jlblEmailAddress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblEmailAddress = new GridBagConstraints();
			gbc_jlblEmailAddress.anchor = GridBagConstraints.EAST;
			gbc_jlblEmailAddress.insets = new Insets(0, 0, 0, 5);
			gbc_jlblEmailAddress.gridx = 0;
			gbc_jlblEmailAddress.gridy = 4;
			jpnlContent.add(jlblEmailAddress, gbc_jlblEmailAddress);
		}
		{
			jtxtfldEmailAddress = new JTextField();
			jtxtfldEmailAddress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldEmailAddress = new GridBagConstraints();
			gbc_jtxtfldEmailAddress.insets = new Insets(0, 0, 0, 5);
			gbc_jtxtfldEmailAddress.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldEmailAddress.gridx = 1;
			gbc_jtxtfldEmailAddress.gridy = 4;
			jpnlContent.add(jtxtfldEmailAddress, gbc_jtxtfldEmailAddress);
			jtxtfldEmailAddress.setColumns(10);
		}
		{
			JLabel jlblDateRegistered = new JLabel("Date registered");
			jlblDateRegistered.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jlblDateRegistered = new GridBagConstraints();
			gbc_jlblDateRegistered.anchor = GridBagConstraints.EAST;
			gbc_jlblDateRegistered.insets = new Insets(0, 0, 0, 5);
			gbc_jlblDateRegistered.gridx = 2;
			gbc_jlblDateRegistered.gridy = 4;
			jpnlContent.add(jlblDateRegistered, gbc_jlblDateRegistered);
		}
		{
		
			jtxtfldDateRegistered = new JTextField();
			jtxtfldDateRegistered.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			GridBagConstraints gbc_jtxtfldDateRegistered = new GridBagConstraints();
			gbc_jtxtfldDateRegistered.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldDateRegistered.gridx = 3;
			gbc_jtxtfldDateRegistered.gridy = 4;
			jtxtfldDateRegistered.setEditable(false);
			jpnlContent.add(jtxtfldDateRegistered, gbc_jtxtfldDateRegistered);
			jtxtfldDateRegistered.setColumns(10);
		}
		/* END OF jlblHeader */

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton jbtnOk = new JButton("OK");
				jbtnOk.setActionCommand("OK");
				jbtnOk.addActionListener((event) -> {

					// Fetch data from input fields
					int id = 0;
					String firstName = jtxtfldFirstName.getText();
					String lastName = jtxtfldLastName.getText();
					String addressLine1 = jtxtfldAddressLine1.getText();
					String addressLine2 = jtxtfldAddressLine2.getText();
					String addressLine3 = jtxtfldAddressLine3.getText();
					String contactNumber = jtxtfldContactNumber.getText();
					String emailAddress = jtxtfldEmailAddress.getText();
					String dateRegistered = jtxtfldDateRegistered.getText();
					
					

					/* Validation Layer */

					// Check first name
					if (firstName.contentEquals("") || firstName.length() > 64) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for first name. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 64 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Check last name
					if (lastName.contentEquals("") || lastName.length() > 64) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for last name. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 64 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Check Address Line 1
					if (addressLine1.contentEquals("") || addressLine1.length() > 128) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for Address. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 128 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Check Address Line 2
					if (addressLine2.contentEquals("") || addressLine2.length() > 128) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for Address. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 128 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Check Address Line 3
					if (addressLine3.contentEquals("") || addressLine3.length() > 128) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for Address. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 128 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Check Contact number
					if (contactNumber.contentEquals("") || contactNumber.length() != 13) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for Contact number. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- 13 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Check Contact number
					if (contactNumber.contentEquals("") || contactNumber.length() > 128) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for Contact number. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- Up to 13 characters",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Check Email
					if (emailAddress.contentEquals("") || !emailAddress.contains("@") ||contactNumber.length() > 128) {
						JOptionPane.showMessageDialog(formDialog,
								"Invalid value for Email Address. Please check that it matches the ff. criteria:\n"
										+ "- Not empty or blank\n" + "- must contain '@' ",
								"Invalid input.", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Create a new Member record
					Member member = new Member(id, firstName, lastName, addressLine1, addressLine2, addressLine3, contactNumber, emailAddress, LocalDateTime.parse(dateRegistered));
					
					// Save it to database with a SwingWorker Thread in background
					new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							try(Connection connection = memberManagementPanel.dataSource.getConnection()) {
								switch(currentOperation) {
								
								// If form is in insert mode, perform an SQL INSERT
								case INSERT:
									try(PreparedStatement memberInsertStatement = connection.prepareStatement(
											"INSERT INTO member(id, first_name, last_name, address_line_1, address_line_2, address_line_3, contact_number, email_address, registered_on) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
										memberInsertStatement.setInt(1, member.id());
										memberInsertStatement.setString(2, member.firstName());
										memberInsertStatement.setString(3, member.lastName());
										memberInsertStatement.setString(4, member.addressLine1());
										memberInsertStatement.setString(5, member.addressLine2());
										memberInsertStatement.setString(6, member.addressLine3());
										memberInsertStatement.setString(7, member.contactNumber());
										memberInsertStatement.setString(8, member.emailAddress());
										memberInsertStatement.setString(9, member.registeredOn().toString());
										memberInsertStatement.execute();
									}
									
									break;
								
								// Else, perform an SQL UPDATE with the old name
								case UPDATE:
									try(PreparedStatement memberUpdateStatement = connection.prepareStatement(
											"UPDATE member SET first_name = ?, last_name = ?, address_line_1 = ?, address_line_2 = ?, address_line_3 = ?, "
											+ "contact_number = ?, email_address = ? WHERE id = ?")) {
										memberUpdateStatement.setString(1, member.firstName());
										memberUpdateStatement.setString(2, member.lastName());
										memberUpdateStatement.setString(3, member.addressLine1());
										memberUpdateStatement.setString(4, member.addressLine2());
										memberUpdateStatement.setString(5, member.addressLine3());
										memberUpdateStatement.setString(6, member.contactNumber());
										memberUpdateStatement.setString(7, member.emailAddress());
										memberUpdateStatement.setInt(8, oldMemberId);
										memberUpdateStatement.execute();
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
									memberManagementPanel,
									"Successfully saved member to database. Refreshing your panel.",
									"Success!",
									JOptionPane.INFORMATION_MESSAGE);
								setVisible(false);
								memberManagementPanel.setCurrentPage(memberManagementPanel.getCurrentPage());
							} catch (InterruptedException | ExecutionException e) {
								// If an error occured, show dialog and inform user.
								JOptionPane.showMessageDialog(
									memberManagementPanel,
									"An error occured while trying to save to database.\n\nError: " + e.getMessage(),
									"Database access error!",
									JOptionPane.ERROR_MESSAGE);
							}
						}
					}.execute();
					/* END OF Validation Layer */

				});
				buttonPane.add(jbtnOk);
				getRootPane().setDefaultButton(jbtnOk);
			}
			{
				JButton jbtnCancel = new JButton("Cancel");
				jbtnCancel.setActionCommand("Cancel");
				buttonPane.add(jbtnCancel);
			}
		}
	}

	/**
	 * Prepares the form for inserting a new member.
	 */
	public void reset() {
		currentOperation = FormOperation.INSERT;

		jlblHeader.setText("Add Member");

		oldMemberId = 0;
		jtxtfldFirstName.setText("");
		jtxtfldLastName.setText("");
		jtxtfldAddressLine1.setText("");
		jtxtfldAddressLine2.setText("");
		jtxtfldAddressLine3.setText("");
		jtxtfldContactNumber.setText("+63");
		jtxtfldEmailAddress.setText("");
		jtxtfldDateRegistered.setText("" + LocalDateTime.now());
	}

	/**
	 * Prepares the form for updating an existing member.
	 */
	public void reset(Member member) {
		currentOperation = FormOperation.UPDATE;

		jlblHeader.setText("Modify Member");

		oldMemberId = member.id();
		jtxtfldFirstName.setText(member.firstName());
		jtxtfldLastName.setText(member.lastName());
		jtxtfldAddressLine1.setText(member.addressLine1());
		jtxtfldAddressLine2.setText(member.addressLine2());
		jtxtfldAddressLine3.setText(member.addressLine3());
		jtxtfldContactNumber.setText(member.contactNumber());
		jtxtfldEmailAddress.setText(member.emailAddress());
		jtxtfldDateRegistered.setText(member.registeredOn().toString());
	
	}
}
