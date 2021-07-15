package co.bisri.librarysystem.admin.ui.borrow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class BorrowManagementPanel extends JPanel {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to
	 * remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The main table of this panel.
	 */
	private JTable jtblBorrow;

	/**
	 * Add Form Dialog of this panel.
	 */
	//protected AddDialog addDialog;
	//protected UpdateDialog updateDialog;

	/**
	 * Construct the panel.
	 */
	public BorrowManagementPanel() {
		BorrowManagementPanel thisPanel = this;
		// Set border to EmptyBorder for spacing
		setBorder(new EmptyBorder(10, 10, 10, 10));
		// Use BoxLayout to lay the internal 3 panels: Header, Table, Pagination Actions
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		/* jpnlHeader - Header Panel */
		JPanel jpnlHeader = new JPanel();
		jpnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
		jpnlHeader.setMinimumSize(new Dimension(10, 45));
		jpnlHeader.setMaximumSize(new Dimension(32767, 55));
		add(jpnlHeader);
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
		/* END OF jpnlHeader */

		/* jlblHeader - Header label */
		JLabel jlblHeader = new JLabel("Manage Borrow");
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
		JButton jbtnShowAddForm = new JButton("Add");
		jbtnShowAddForm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnShowAddForm.setBackground(Color.WHITE);
		jbtnShowAddForm.setFont(new Font("Roboto", Font.PLAIN, 12));
		/*
		jbtnShowAddForm.addActionListener(event -> {
			addDialog.resetForm();
			addDialog.setVisible(true);
		});
		*/
		jpnlButtonActions.add(jbtnShowAddForm);
		/* END OF jbtnShowAddForm */

		/* jbtnUpdate - button for updating account */
		JButton jbtnUpdate = new JButton("Update");
		jbtnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnUpdate.setBackground(Color.WHITE);
		/*jbtnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblBorrow.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel,
							"Please select a book first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				int databaseIdOfSelectedAccount = (int) jtblBorrow.getValueAt(selectedRowIndexOnTable, 0);

				updateDialog.initializeWithAccountId(databaseIdOfSelectedAccount);

				updateDialog.setVisible(true);
			}
		});*/
		jbtnUpdate.setFont(new Font("Roboto", Font.PLAIN, 12));
		jpnlButtonActions.add(jbtnUpdate);
		/* END OF jbtnUpdate */

		/* jbtnDelete - button for deleting account */
		JButton jbtnDelete = new JButton("Delete");
		jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnDelete.setBackground(Color.WHITE);
		jbtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblBorrow.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel,
							"Please select a book copy first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (JOptionPane.showConfirmDialog(thisPanel,
						"Are you sure you want to delete this book copy?") == JOptionPane.YES_OPTION) {
					// perform delete here
					int databaseIdOfSelectedAccount = (int) jtblBorrow.getValueAt(selectedRowIndexOnTable, 0);
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pnr_db",
							"pnr_app", "password123");
							PreparedStatement deleteStatement = connection
									.prepareStatement("DELETE FROM cashier WHERE id = ?")) {

						deleteStatement.setInt(1, databaseIdOfSelectedAccount);

						deleteStatement.execute();

						JOptionPane.showMessageDialog(thisPanel, "Successfully deleted a book copy.", "Success!",
								JOptionPane.INFORMATION_MESSAGE);

						//refreshTable();
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(thisPanel,
								"An error occured while fetching from the database.\n\nDetails: "
										+ e1.getMessage());
					}
				}
			}
		});

		jbtnDelete.setFont(new Font("Roboto", Font.PLAIN, 12));
		jpnlButtonActions.add(jbtnDelete);
		/* END OF jbtnDelete */

		/* jscrlpnAccounts - Scrollable Table Panel */
		JScrollPane jscrlpnAccounts = new JScrollPane();
		jscrlpnAccounts.setMinimumSize(new Dimension(500, 20));
		jscrlpnAccounts.setAlignmentX(0.0f);
		jscrlpnAccounts.setAlignmentY(0.0f);
		add(jscrlpnAccounts);
		/* END OF jscrlpnAccounts */

		/* jtblBorrow - Main Panel Table */
		jtblBorrow = new JTable();
		jtblBorrow.setRowHeight(22);
		jtblBorrow.setIntercellSpacing(new Dimension(4, 4));
		jscrlpnAccounts.setViewportView(jtblBorrow);
		
		/*
		// Create the add form dialog
		addDialog = new AddDialog();
		addDialog.accountsManagementPanel = this;

		updateDialog = new UpdateDialog();
		updateDialog.accountsManagementPanel = this;
	}
	*/
	
	/*
	public void refreshTable() {
		DefaultTableModel booksTableData = new DefaultTableModel();
		booksTableData.setColumnCount(3);
		booksTableData.setColumnIdentifiers(new String[] { "ID", "Name", "Assigned Station" });

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pnr_db", "pnr_app",
				"password123"); Statement retrieveStatement = connection.createStatement();) {
			retrieveStatement.execute(
					"SELECT cashier.id, cashier.first_name, cashier.last_name, station.name AS assigned_station FROM cashier LEFT JOIN station ON station.id = cashier.assigned_station_id");
			ResultSet retrievedAccountsData = retrieveStatement.getResultSet();

			while (retrievedAccountsData.next()) {
				int id = retrievedAccountsData.getInt("id");
				String firstName = retrievedAccountsData.getString("first_name");
				String lastName = retrievedAccountsData.getString("last_name");
				String assignedStation = retrievedAccountsData.getString("assigned_station");
				booksTableData.addRow(new Object[] { id, firstName + " " + lastName, assignedStation });
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e);
		}
		jtblBorrow.setModel(booksTableData);
		
		*/
	}
	

}