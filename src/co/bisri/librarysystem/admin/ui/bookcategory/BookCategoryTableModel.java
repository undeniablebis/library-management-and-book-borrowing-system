package co.bisri.librarysystem.admin.ui.bookcategory;

import co.bisri.librarysystem.admin.ui.bookcategory.record.BookCategoryTableRecord;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for BookCategory Table in BookCategoryManagementPanel.
 *
 * @author Rian Reyes
 */
public class BookCategoryTableModel extends AbstractTableModel {

    // Ignore, this is to remove serializability-related warnings
    private static final long serialVersionUID = 1L;

    // Column names of this tablemodel
    private static final String[] COLUMN_NAMES = {"Name", "Description"};

    // Back reference to the management panel
    protected BookCategoryManagementPanel bookCategoryManagementPanel;

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
        if (bookCategoryManagementPanel.currentCache == null)
            return 0;

        return bookCategoryManagementPanel.currentCache.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BookCategoryTableRecord bookCategory = bookCategoryManagementPanel.currentCache.get(rowIndex);

        switch (columnIndex) {

            case 0:
                return bookCategory.name();

            case 1:
                return bookCategory.description();

        }

        return null;
    }

}
