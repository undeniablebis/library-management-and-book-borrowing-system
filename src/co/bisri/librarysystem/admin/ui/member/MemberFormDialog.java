package co.bisri.librarysystem.admin.ui.member;

import co.bisri.librarysystem.admin.ui.FormOperation;
import co.bisri.librarysystem.admin.ui.member.record.MemberEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

/**
 * Form Dialog for adding and updating members
 *
 * @author Rian Reyes
 * @author Bismillah Constantino
 */
public class MemberFormDialog extends JDialog {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Back reference to owning panel
    protected MemberManagementPanel memberManagementPanel;

    // Content Panel for input fields
    private final JPanel jpnlContent;

    // Header label
    private final JLabel jlblHeader;

    // Old Member
    private MemberEntity oldMember;

    // SQL operation to perform when OK is clicked (insert or update)
    private FormOperation currentOperation;
    private final JTextField jtxtfldFirstName;
    private final JTextField jtxtfldLastName;
    private final JTextField jtxtfldAddressLine1;
    private final JTextField jtxtfldAddressLine2;
    private final JTextField jtxtfldAddressLine3;
    private final JTextField jtxtfldContactNumber;
    private final JTextField jtxtfldEmailAddress;
    private final JTextField jtxtfldDateRegistered;

    /**
     * Create the dialog.
     */
    public MemberFormDialog() {

        // Reference for nested anonymous contexts
        MemberFormDialog formDialog = this;

        /* Dialog properties */
        setTitle("Save Member");
        setBounds(100, 100, 600, 270);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        /* END OF Dialog properties */

        /* jpnlContent */
        jpnlContent = new JPanel();
        jpnlContent.setBorder(new EmptyBorder(10, 10, 5, 10));
        getContentPane().add(jpnlContent, BorderLayout.CENTER);
        GridBagLayout gbl_jpnlContent = new GridBagLayout();
        gbl_jpnlContent.columnWidths = new int[]{0, 115, 0, 39, 0};
        gbl_jpnlContent.rowHeights = new int[]{0, 0, 0, 27, 0, 0};
        gbl_jpnlContent.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_jpnlContent.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        jpnlContent.setLayout(gbl_jpnlContent);
        /* END OF jpnlContent */

        /* jlblHeader */
        jlblHeader = new JLabel("Member");
        jlblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        jlblHeader.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        GridBagConstraints gbc_jlblHeader = new GridBagConstraints();
        gbc_jlblHeader.anchor = GridBagConstraints.WEST;
        gbc_jlblHeader.gridwidth = 4;
        gbc_jlblHeader.insets = new Insets(0, 0, 5, 0);
        gbc_jlblHeader.gridx = 0;
        gbc_jlblHeader.gridy = 0;
        jpnlContent.add(jlblHeader, gbc_jlblHeader);
        /* END OF jlblHeader */

        /* jlblFirstName */
        JLabel jlblFirstName = new JLabel("First name");
        jlblFirstName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblFirstName = new GridBagConstraints();
        gbc_jlblFirstName.anchor = GridBagConstraints.EAST;
        gbc_jlblFirstName.insets = new Insets(0, 0, 5, 5);
        gbc_jlblFirstName.gridx = 0;
        gbc_jlblFirstName.gridy = 1;
        jpnlContent.add(jlblFirstName, gbc_jlblFirstName);
        /* END OF jlblFirstName */

        /* jtxtfldFirstName */
        jtxtfldFirstName = new JTextField();
        jtxtfldFirstName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldFirstName.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldFirstName = new GridBagConstraints();
        gbc_jtxtfldFirstName.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldFirstName.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldFirstName.gridx = 1;
        gbc_jtxtfldFirstName.gridy = 1;
        jpnlContent.add(jtxtfldFirstName, gbc_jtxtfldFirstName);
        jtxtfldFirstName.setColumns(10);
        /* END OF jtxtfldFirstName */

        /* jlblLastName */
        JLabel jlblLastName = new JLabel("Last name");
        jlblLastName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblLastName = new GridBagConstraints();
        gbc_jlblLastName.anchor = GridBagConstraints.EAST;
        gbc_jlblLastName.insets = new Insets(0, 0, 5, 5);
        gbc_jlblLastName.gridx = 2;
        gbc_jlblLastName.gridy = 1;
        jpnlContent.add(jlblLastName, gbc_jlblLastName);
        /* END OF jlblLastName */

        /* jtxtfldLastName */
        jtxtfldLastName = new JTextField();
        jtxtfldLastName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldLastName.setMargin(new Insets(5, 5, 5, 5));
        jtxtfldLastName.setEnabled(true);
        jtxtfldLastName.setEditable(true);
        jtxtfldLastName.setText("");
        GridBagConstraints gbc_jtxtfldLastName = new GridBagConstraints();
        gbc_jtxtfldLastName.insets = new Insets(0, 0, 5, 0);
        gbc_jtxtfldLastName.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldLastName.gridx = 3;
        gbc_jtxtfldLastName.gridy = 1;
        jpnlContent.add(jtxtfldLastName, gbc_jtxtfldLastName);
        /* END OF jtxtfldLastName */

        /* jlblAddressLine1 */
        JLabel jlblAddressLine1 = new JLabel("Street and House no.");
        jlblAddressLine1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblAddressLine1 = new GridBagConstraints();
        gbc_jlblAddressLine1.anchor = GridBagConstraints.EAST;
        gbc_jlblAddressLine1.insets = new Insets(0, 0, 5, 5);
        gbc_jlblAddressLine1.gridx = 0;
        gbc_jlblAddressLine1.gridy = 2;
        jpnlContent.add(jlblAddressLine1, gbc_jlblAddressLine1);
        /* END OF jlblAddressLine1 */

        /* jtxtfldAddressLine1 */
        jtxtfldAddressLine1 = new JTextField();
        jtxtfldAddressLine1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldAddressLine1.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldAddressLine1 = new GridBagConstraints();
        gbc_jtxtfldAddressLine1.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldAddressLine1.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldAddressLine1.gridx = 1;
        gbc_jtxtfldAddressLine1.gridy = 2;
        jpnlContent.add(jtxtfldAddressLine1, gbc_jtxtfldAddressLine1);
        /* END OF jtxtfldAddressLine1 */

        /* jlblAddressLine2 */
        JLabel jlblAddressLine2 = new JLabel("Brgy. Zone and District");
        jlblAddressLine2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblAddressLine2 = new GridBagConstraints();
        gbc_jlblAddressLine2.anchor = GridBagConstraints.EAST;
        gbc_jlblAddressLine2.insets = new Insets(0, 0, 5, 5);
        gbc_jlblAddressLine2.gridx = 2;
        gbc_jlblAddressLine2.gridy = 2;
        jpnlContent.add(jlblAddressLine2, gbc_jlblAddressLine2);
        /* END OF jlblAddressLine2 */

        /* jtxtfldAddressLine2 */
        jtxtfldAddressLine2 = new JTextField();
        jtxtfldAddressLine2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldAddressLine2.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldAddressLine2 = new GridBagConstraints();
        gbc_jtxtfldAddressLine2.insets = new Insets(0, 0, 5, 0);
        gbc_jtxtfldAddressLine2.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldAddressLine2.gridx = 3;
        gbc_jtxtfldAddressLine2.gridy = 2;
        jpnlContent.add(jtxtfldAddressLine2, gbc_jtxtfldAddressLine2);
        /* END OF jtxtfldAddressLine2 */

        /* jlblAddressLine3 */
        JLabel jlblAddressLine3 = new JLabel("City/Province");
        jlblAddressLine3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblAddressLine3 = new GridBagConstraints();
        gbc_jlblAddressLine3.anchor = GridBagConstraints.EAST;
        gbc_jlblAddressLine3.insets = new Insets(0, 0, 5, 5);
        gbc_jlblAddressLine3.gridx = 0;
        gbc_jlblAddressLine3.gridy = 3;
        jpnlContent.add(jlblAddressLine3, gbc_jlblAddressLine3);
        /* END OF jlblAddressLine3 */

        /* jtxtfldAddressLine3 */
        jtxtfldAddressLine3 = new JTextField();
        jtxtfldAddressLine3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldAddressLine3.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldAddressLine3 = new GridBagConstraints();
        gbc_jtxtfldAddressLine3.insets = new Insets(0, 0, 5, 5);
        gbc_jtxtfldAddressLine3.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldAddressLine3.gridx = 1;
        gbc_jtxtfldAddressLine3.gridy = 3;
        jpnlContent.add(jtxtfldAddressLine3, gbc_jtxtfldAddressLine3);
        /* END OF jtxtfldAddressLine3 */

        /* jlblContactNumber */
        JLabel jlblContactNumber = new JLabel("Contact number");
        jlblContactNumber.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblContactNumber = new GridBagConstraints();
        gbc_jlblContactNumber.anchor = GridBagConstraints.EAST;
        gbc_jlblContactNumber.insets = new Insets(0, 0, 5, 5);
        gbc_jlblContactNumber.gridx = 2;
        gbc_jlblContactNumber.gridy = 3;
        jpnlContent.add(jlblContactNumber, gbc_jlblContactNumber);
        /* END OF jlblContactNumber */

        /* jtxtfldContactNumber */
        jtxtfldContactNumber = new JTextField();
        jtxtfldContactNumber.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldContactNumber.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldContactNumber = new GridBagConstraints();
        gbc_jtxtfldContactNumber.insets = new Insets(0, 0, 5, 0);
        gbc_jtxtfldContactNumber.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldContactNumber.gridx = 3;
        gbc_jtxtfldContactNumber.gridy = 3;
        jtxtfldContactNumber.setText("+63");
        jpnlContent.add(jtxtfldContactNumber, gbc_jtxtfldContactNumber);
        /* END OF jtxtfldContactNumber */

        /* jlblEmailAddress */
        JLabel jlblEmailAddress = new JLabel("Email Address");
        jlblEmailAddress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblEmailAddress = new GridBagConstraints();
        gbc_jlblEmailAddress.anchor = GridBagConstraints.EAST;
        gbc_jlblEmailAddress.insets = new Insets(0, 0, 0, 5);
        gbc_jlblEmailAddress.gridx = 0;
        gbc_jlblEmailAddress.gridy = 4;
        jpnlContent.add(jlblEmailAddress, gbc_jlblEmailAddress);
        /* END OF jlblEmailAddress */

        /* jtxtfldEmailAddress */
        jtxtfldEmailAddress = new JTextField();
        jtxtfldEmailAddress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldEmailAddress.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldEmailAddress = new GridBagConstraints();
        gbc_jtxtfldEmailAddress.insets = new Insets(0, 0, 0, 5);
        gbc_jtxtfldEmailAddress.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldEmailAddress.gridx = 1;
        gbc_jtxtfldEmailAddress.gridy = 4;
        jpnlContent.add(jtxtfldEmailAddress, gbc_jtxtfldEmailAddress);
        /* END OF jtxtfldEmailAddress */

        /* jlblDateRegistered */
        JLabel jlblDateRegistered = new JLabel("Date registered");
        jlblDateRegistered.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_jlblDateRegistered = new GridBagConstraints();
        gbc_jlblDateRegistered.anchor = GridBagConstraints.EAST;
        gbc_jlblDateRegistered.insets = new Insets(0, 0, 0, 5);
        gbc_jlblDateRegistered.gridx = 2;
        gbc_jlblDateRegistered.gridy = 4;
        jpnlContent.add(jlblDateRegistered, gbc_jlblDateRegistered);
        /* END OF jlblDateRegistered */

        /* jtxtfldDateRegistered */
        jtxtfldDateRegistered = new JTextField();
        jtxtfldDateRegistered.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtxtfldDateRegistered.setMargin(new Insets(5, 5, 5, 5));
        GridBagConstraints gbc_jtxtfldDateRegistered = new GridBagConstraints();
        gbc_jtxtfldDateRegistered.fill = GridBagConstraints.HORIZONTAL;
        gbc_jtxtfldDateRegistered.gridx = 3;
        gbc_jtxtfldDateRegistered.gridy = 4;
        jtxtfldDateRegistered.setEditable(false);
        jpnlContent.add(jtxtfldDateRegistered, gbc_jtxtfldDateRegistered);
        /* END OF jtxtfldDateRegistered */

        /* jpnlButtonActions */
        JPanel jpnlButtonActions = new JPanel();
        jpnlButtonActions.setBorder(new EmptyBorder(0, 10, 5, 10));
        jpnlButtonActions.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(jpnlButtonActions, BorderLayout.SOUTH);
        /* END OF jpnlButtonActions */

        /* jbtnOk */
        JButton jbtnOk = new JButton("OK");
        jbtnOk.setActionCommand("OK");
        jbtnOk.addActionListener((event) -> {

            // FIRST NAME
            String firstName = jtxtfldFirstName.getText();
            if (firstName.contentEquals("") || firstName.length() > 64) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for first name. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 64 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // LAST NAME
            String lastName = jtxtfldLastName.getText();
            if (lastName.contentEquals("") || lastName.length() > 64) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for last name. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 64 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ADDRESS LINE 1
            String addressLine1 = jtxtfldAddressLine1.getText();
            if (addressLine1.contentEquals("") || addressLine1.length() > 128) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for Address. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 128 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ADDRESS LINE 2
            String addressLine2 = jtxtfldAddressLine2.getText();
            if (addressLine2.contentEquals("") || addressLine2.length() > 128) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for Address. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 128 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ADDRESS LINE 3
            String addressLine3 = jtxtfldAddressLine3.getText();
            if (addressLine3.contentEquals("") || addressLine3.length() > 128) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for Address. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- Up to 128 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // CONTACT NUMBER
            String contactNumber = jtxtfldContactNumber.getText();
            if (contactNumber.contentEquals("") || contactNumber.length() != 13) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for Contact number. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- 13 characters",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // EMAIL ADDRESS
            String emailAddress = jtxtfldEmailAddress.getText();
            if (emailAddress.contentEquals("") || !emailAddress.contains("@") || contactNumber.length() > 128) {
                JOptionPane.showMessageDialog(formDialog,
                        "Invalid value for Email Address. Please check that it matches the ff. criteria:\n"
                                + "- Not empty or blank\n" + "- must contain '@' ",
                        "Invalid input.", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // REGISTERED ON
            LocalDateTime registeredOn = oldMember == null ? LocalDateTime.now() : oldMember.registeredOn();

            // Create a new Member record
            MemberEntity member = new MemberEntity(oldMember == null ? 0 : oldMember.id(), firstName, lastName,
                    addressLine1, addressLine2, addressLine3, contactNumber, emailAddress, registeredOn);

            // Save it to database with a SwingWorker Thread in background
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try (Connection connection = memberManagementPanel.getConnection()) {
                        switch (currentOperation) {

                            // If form is in insert mode, perform an SQL INSERT
                            case INSERT:
                                try (PreparedStatement memberInsertStatement = connection.prepareStatement(
                                        "INSERT INTO member(first_name, last_name, address_line_1, address_line_2, address_line_3, contact_number, email_address, registered_on) VALUES(?, ?, ?, ?, ?, ?, ?, ?)")) {
                                    memberInsertStatement.setString(1, member.firstName());
                                    memberInsertStatement.setString(2, member.lastName());
                                    memberInsertStatement.setString(3, member.addressLine1());
                                    memberInsertStatement.setString(4, member.addressLine2());
                                    memberInsertStatement.setString(5, member.addressLine3());
                                    memberInsertStatement.setString(6, member.contactNumber());
                                    memberInsertStatement.setString(7, member.emailAddress());
                                    memberInsertStatement.setString(8, member.registeredOn().toString());
                                    memberInsertStatement.execute();
                                }

                                break;

                            // Else, perform an SQL UPDATE with the old name
                            case UPDATE:
                                try (PreparedStatement memberUpdateStatement = connection.prepareStatement(
                                        "UPDATE member SET first_name = ?, last_name = ?, address_line_1 = ?, address_line_2 = ?, address_line_3 = ?, "
                                                + "contact_number = ?, email_address = ? WHERE id = ?")) {
                                    memberUpdateStatement.setString(1, member.firstName());
                                    memberUpdateStatement.setString(2, member.lastName());
                                    memberUpdateStatement.setString(3, member.addressLine1());
                                    memberUpdateStatement.setString(4, member.addressLine2());
                                    memberUpdateStatement.setString(5, member.addressLine3());
                                    memberUpdateStatement.setString(6, member.contactNumber());
                                    memberUpdateStatement.setString(7, member.emailAddress());
                                    memberUpdateStatement.setInt(8, member.id());
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
                        memberManagementPanel.refreshPage();
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
        jbtnOk.setBackground(Color.WHITE);
        jbtnOk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jpnlButtonActions.add(jbtnOk);
        getRootPane().setDefaultButton(jbtnOk);
        /* END OF jbtnOk */

        /* jbtnCancel */
        JButton jbtnCancel = new JButton("Cancel");
        jbtnCancel.setActionCommand("Cancel");
        jbtnCancel.addActionListener((event) -> {
            setVisible(false);
        });
        jbtnCancel.setBackground(Color.WHITE);
        jbtnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jpnlButtonActions.add(jbtnCancel);
        /* END OF jbtnCancel */
    }

    /**
     * Prepares the form for inserting a new member.
     */
    public void initialize() {
        // Change mode to insert
        currentOperation = FormOperation.INSERT;

        // Change the header
        jlblHeader.setText("Add Member");

        // Reset the fields and input components
        oldMember = null;
        jtxtfldFirstName.setText("");
        jtxtfldLastName.setText("");
        jtxtfldAddressLine1.setText("");
        jtxtfldAddressLine2.setText("");
        jtxtfldAddressLine3.setText("");
        jtxtfldContactNumber.setText("+63");
        jtxtfldEmailAddress.setText("");
        jtxtfldDateRegistered.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")));
    }

    /**
     * Prepares the form for updating an existing member.
     */
    public void initialize(MemberEntity member) {
        // Change mode to insert
        currentOperation = FormOperation.UPDATE;

        // Change the header
        jlblHeader.setText("Modify Member");

        // Reset the fields and input components
        oldMember = member;
        jtxtfldFirstName.setText(member.firstName());
        jtxtfldLastName.setText(member.lastName());
        jtxtfldAddressLine1.setText(member.addressLine1());
        jtxtfldAddressLine2.setText(member.addressLine2());
        jtxtfldAddressLine3.setText(member.addressLine3());
        jtxtfldContactNumber.setText(member.contactNumber());
        jtxtfldEmailAddress.setText(member.emailAddress());
        jtxtfldDateRegistered.setText(member.registeredOn().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")));
    }
}
