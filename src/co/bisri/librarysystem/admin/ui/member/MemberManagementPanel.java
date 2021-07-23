package co.bisri.librarysystem.admin.ui.member;

import co.bisri.librarysystem.admin.ui.ManagementPanel;
import co.bisri.librarysystem.admin.ui.member.record.MemberEntity;
import co.bisri.librarysystem.admin.ui.member.record.MemberTableRecord;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing members.
 *
 * @author Bismillah Constantino
 * @author Rian Reyes
 */
public class MemberManagementPanel extends ManagementPanel {

    // Serial version
    private static final long serialVersionUID = 1L;

    // Table Model
    private final MemberTableModel memberTableModel;

    // Current table cache
    protected List<MemberTableRecord> currentCache;

    // Form Dialog
    protected MemberFormDialog memberFormDialog;

    public MemberManagementPanel() {
        super();

        // Set header label
        jlblHeader.setText("Manage Members");

        /* jbtnShowAddForm */
        JButton jbtnAdd = new JButton("Add");
        jbtnAdd.addActionListener((event) -> {
            // Reset and show the form dialog
            memberFormDialog.initialize();
            memberFormDialog.setVisible(true);
        });
        jbtnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnAdd.setBackground(Color.WHITE);
        jbtnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnAdd);
        /* END OF jbtnShowAddForm */

        /* jbtnUpdate */
        JButton jbtnUpdate = new JButton("Update");
        jbtnUpdate.addActionListener((event) -> {
            // Get current selected row
            int selectedRow = jtblMainTable.getSelectedRow();

            // If no row is selected, don't proceed
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a record first before clicking the update button.",
                        "Select first!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Retrieve the PK from the cache
            int id = currentCache.get(selectedRow).id();
            MemberEntity member = null;

            // Retrieve the corresponding record from the database
            try (Connection connection = getConnection();
                 PreparedStatement retrieveMemberStatement = connection.prepareStatement(
                         "SELECT id, first_name, last_name, address_line_1, address_line_2, address_line_3, " +
                                 "contact_number, email_address, registered_on FROM member WHERE id = ?")) {

                // Bind the PK
                retrieveMemberStatement.setInt(1, id);

                // Fetch the result set, extract entity
                try (ResultSet memberResultSet = retrieveMemberStatement.executeQuery()) {
                    if (memberResultSet.next()) {
                        // If a record was found, parse into a book category object
                        member = new MemberEntity(
                                memberResultSet.getInt("id"),
                                memberResultSet.getString("first_name"),
                                memberResultSet.getString("last_name"),
                                memberResultSet.getString("address_line_1"),
                                memberResultSet.getString("address_line_2"),
                                memberResultSet.getString("address_line_3"),
                                memberResultSet.getString("contact_number"),
                                memberResultSet.getString("email_address"),
                                LocalDateTime.parse(memberResultSet.getString("registered_on"),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } else {
                        // If no records were found, show an error
                        JOptionPane.showMessageDialog(
                                this,
                                "There was no corresponding member found. Refresh your panel.",
                                "Retrieve error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (SQLException e1) {
                // If an error occurred, show message then exit
                JOptionPane.showMessageDialog(
                        this,
                        "An error occured while retrieving member from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                        "Database connectivity error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Reset and show the form dialog
            memberFormDialog.initialize(member);
            memberFormDialog.setVisible(true);
        });
        jbtnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnUpdate.setBackground(Color.WHITE);
        jbtnUpdate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnUpdate);
        /* END OF jbtnUpdate */

        /* jbtnDelete */
        JButton jbtnDelete = new JButton("Delete");
        jbtnDelete.addActionListener(e -> {
            // Get current selected row
            int selectedRow = jtblMainTable.getSelectedRow();

            // If no row is selected, don't proceed
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a record first before clicking the delete button.",
                        "Select first!",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Retrieve the PK from the cache
            int id = currentCache.get(selectedRow).id();

            // Confirm the user first
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove member with id: " + id + "?",
                    "Remove category",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                // Delete the corresponding record from the database
                try (Connection connection = getConnection();
                     PreparedStatement deleteMemberStatement = connection.prepareStatement(
                             "DELETE FROM member WHERE id = ?")) {

                    // Bind the PK
                    deleteMemberStatement.setInt(1, id);
                    // Execute the delete statement
                    deleteMemberStatement.execute();
                } catch (SQLException e1) {
                    // If an error occurred, show message then exit
                    JOptionPane.showMessageDialog(
                            this,
                            "An error occurred while removing member from the database.\n\nMessage: " + e1.getLocalizedMessage(),
                            "Database connectivity error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Show success message
                JOptionPane.showMessageDialog(
                        this,
                        "Successfully removed member with id: " + id,
                        "Remove success",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refresh the panel
                refreshPage();
            }
        });
        jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbtnDelete.setBackground(Color.WHITE);
        jbtnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jpnlButtonActions.add(jbtnDelete);
        /* END OF jbtnDelete */

        /* memberTableModel */
        memberTableModel = new MemberTableModel();
        memberTableModel.memberManagementPanel = this;
        jtblMainTable.setModel(memberTableModel);
        /* END OF memberTableModel */

        /* formDialog */
        memberFormDialog = new MemberFormDialog();
        memberFormDialog.memberManagementPanel = this;
        /* END OF formDialog */

        /* currentCache */
        currentCache = new ArrayList<>();
        /* END OF currentCache */
    }

    @Override
    public void setPage(int page) {
        // Fetch member count
        try (Connection connection = getConnection();
             Statement retrieveCountStatement = connection.createStatement();
             ResultSet countResultSet = retrieveCountStatement.executeQuery("SELECT COUNT(id) AS record_count FROM member")) {

            // Traverse the result set once
            countResultSet.next();

            // Calculate the page count based on how many records are in the database, by page size
            int totalRecordCount = countResultSet.getInt("record_count");
            totalPageCount = (int) Math.ceil((double) totalRecordCount / PAGE_SIZE);
        } catch (SQLException e) {
            // If an error occurred, show message then exit
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while interacting with the database.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database connectivity error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the given page value is valid
        if(totalPageCount == 0) {
            return;
        } else if (page < 1 || page > totalPageCount) {
            // Display error message
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid page to display.",
                    "Data paging error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Set the current page
        currentPage = page;

        // Fetch members
        currentCache.clear();
        try (Connection connection = getConnection();
             Statement retrieveMembersStatement = connection.createStatement();
             ResultSet memberResultSet = retrieveMembersStatement.executeQuery(
                     "SELECT id, first_name, last_name, address_line_1, address_line_2, address_line_3, " +
                             "contact_number, email_address, registered_on FROM member " +
                             "LIMIT " + (currentPage - 1) * PAGE_SIZE + ", " + PAGE_SIZE)) {

            // Parse each row into a MemberTableRecord
            while (memberResultSet.next()) {
                currentCache.add(
                        new MemberTableRecord(
                                memberResultSet.getInt("id"),
                                memberResultSet.getString("first_name"),
                                memberResultSet.getString("last_name"),
                                memberResultSet.getString("address_line_1"),
                                memberResultSet.getString("address_line_2"),
                                memberResultSet.getString("address_line_3"),
                                memberResultSet.getString("contact_number"),
                                memberResultSet.getString("email_address"),
                                LocalDateTime.parse(memberResultSet.getString("registered_on"),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        ));
            }
        } catch (SQLException e) {
            // If an error occurred, show message then exit
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while interacting with the database.\n\nMessage: " + e.getLocalizedMessage(),
                    "Database connectivity error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tell the tablemodel that the data has changed
        memberTableModel.fireTableDataChanged();

        // Tell the page button panel that the page has changed
        pageButtonPanel.setTotalPageCount(totalPageCount);
        pageButtonPanel.setCurrentPage(currentPage);
    }

}