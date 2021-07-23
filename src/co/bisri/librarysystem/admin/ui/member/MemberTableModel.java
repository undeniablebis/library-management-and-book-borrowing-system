package co.bisri.librarysystem.admin.ui.member;

import co.bisri.librarysystem.admin.ui.member.record.MemberTableRecord;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;

/**
 * TableModel for BookCategory Table in BooksCategoryManagementPanel.
 *
 * @author Rian Reyes
 */
public class MemberTableModel extends AbstractTableModel {

    // Ignore, this is to remove serializability-related warnings
    private static final long serialVersionUID = 1L;

    // Column names of this tablemodel
    private static final String[] COLUMN_NAMES = {"ID", "Name", "Address", "Contact Number", "Email Address",
            "Date registered"};

    // Back reference to the management panel
    protected MemberManagementPanel memberManagementPanel;

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public int getRowCount() {
        if (memberManagementPanel.currentCache == null)
            return 0;

        return memberManagementPanel.currentCache.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MemberTableRecord member = memberManagementPanel.currentCache.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return member.id();

            case 1:
                return member.firstName() + " " + member.lastName();

            case 2:
                return member.addressLine1() + " " + member.addressLine2() + " " + member.addressLine3();

            case 3:
                return member.contactNumber();

            case 4:
                return member.emailAddress();

            case 5:
                return member.registeredOn().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a"));

        }

        return null;
    }

}
