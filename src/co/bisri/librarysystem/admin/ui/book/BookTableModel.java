package co.bisri.librarysystem.admin.ui.book;

import co.bisri.librarysystem.admin.ui.book.record.BookTableRecord;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for Books Table in BooksCategoryManagementPanel.
 *
 * @author Rian Reyes
 * @author Bismillah Constantino
 */
public class BookTableModel extends AbstractTableModel {

    // Ignore, this is to remove serializability-related warnings
    private static final long serialVersionUID = 1L;

    // Column names of this tablemodel
    private static final String[] COLUMN_NAMES = {"ISBN", "Category Name", "Title", "Author", "Publisher", "Published On"};

    // Back reference to the management panel
    protected BookManagementPanel bookManagementPanel;

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
        if (bookManagementPanel.currentCache == null)
            return 0;

        return bookManagementPanel.currentCache.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BookTableRecord books = bookManagementPanel.currentCache.get(rowIndex);

        switch (columnIndex) {

            case 0:
                return books.isbn();

            case 1:
                return books.categoryName();
            case 2:
                return books.title();

            case 3:
                return books.author();

            case 4:
                return books.publisher();

            case 5:
                return books.publishedOn();


        }

        return null;
    }

}
