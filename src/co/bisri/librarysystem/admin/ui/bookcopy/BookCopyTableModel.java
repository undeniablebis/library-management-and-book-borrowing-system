package co.bisri.librarysystem.admin.ui.bookcopy;

import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopyTableRecord;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for BookCategory Table in BooksCategoryManagementPanel.
 *
 * @author Rian Reyes
 */
public class BookCopyTableModel extends AbstractTableModel {

    // Ignore, this is to remove serializability-related warnings
    private static final long serialVersionUID = 1L;

    // Column names of this tablemodel
    private static final String[] COLUMN_NAMES = {"ISBN", "Copy", "Title", "Status", "Is Borrowed?"};

    // Back reference to owning panel
    protected BookCopyManagementPanel bookCopyManagementPanel;

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
        if (bookCopyManagementPanel.currentCache == null)
            return 0;

        return bookCopyManagementPanel.currentCache.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BookCopyTableRecord bookCopy = bookCopyManagementPanel.currentCache.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> bookCopy.isbn();
            case 1 -> bookCopy.copyNo();
            case 2 -> bookCopy.title();
            case 3 -> bookCopy.status();
            case 4 -> bookCopy.borrowStatus();
            default -> null;
        };

    }

}
