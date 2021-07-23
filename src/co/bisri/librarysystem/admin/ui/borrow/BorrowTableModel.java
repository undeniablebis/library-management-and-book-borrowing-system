package co.bisri.librarysystem.admin.ui.borrow;

import co.bisri.librarysystem.admin.ui.borrow.record.BorrowTableRecord;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;

/**
 * TableModel for BookCategory Table in BooksCategoryManagementPanel.
 *
 * @author Rian Reyes
 */
public class BorrowTableModel extends AbstractTableModel {

    // Ignore, this is to remove serializability-related warnings
    private static final long serialVersionUID = 1L;

    // Column names of this tablemodel
    private static final String[] COLUMN_NAMES = {"Member", "Borrowed On", "Target Return", "Returned", "Status", "Fee"};

    // Back reference to owning panel
    protected BorrowManagementPanel borrowManagementPanel;

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
        if (borrowManagementPanel.currentCache == null)
            return 0;

        return borrowManagementPanel.currentCache.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BorrowTableRecord borrowTableRecord = borrowManagementPanel.currentCache.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> borrowTableRecord.memberFirstName() + " " + borrowTableRecord.memberLastName();
            case 1 -> borrowTableRecord.borrowedOn().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            case 2 -> borrowTableRecord.targetReturnDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            case 3 -> borrowTableRecord.returnedOn() == null ?
                    "Not yet returned" :
                    borrowTableRecord.returnedOn().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a"));
            case 4 -> borrowTableRecord.status();
            case 5 -> borrowTableRecord.returnFee();
            default -> null;
        };

    }

}
