package co.bisri.librarysystem.admin.ui.bookcopy;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import co.bisri.librarysystem.admin.ui.bookcopy.record.BookCopyTableRecord;

/**
 * TableModel for BookCategory Table in BooksCategoryManagementPanel.
 * 
 * @author Rian Reyes
 *
 */
public class BookCopyTableModel extends AbstractTableModel {
	
	// Ignore, this is to remove serializability-related warnings
	private static final long serialVersionUID = 1L;

	// Column names of this tablemodel
	private static final String[] COLUMN_NAMES = { "ISBN", "Copy", "Title", "Status", "Is Borrowed?" };
	
	// Current values stored by this tablemodel
	private List<BookCopyTableRecord> cache;
	
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
		if(cache == null)
			return 0;
		
		return cache.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		BookCopyTableRecord bookCopy = cache.get(rowIndex);
		
		switch(columnIndex) {
		
		case 0:
			return bookCopy.isbn();
		
		case 1:
			return bookCopy.copyNo();
			
		case 2:
			return bookCopy.title();
			
		case 3:
			return bookCopy.status();
			
		case 4:
			return bookCopy.borrowStatus();
		
		}
		
		return null;
	}
	
	public String getBookIsbnAtRow(int rowIndex) {
		return cache.get(rowIndex).isbn();
	}
	
	public int getCopyNoAtRow(int rowIndex) {
		return cache.get(rowIndex).copyNo();
	}

	public String getBookTitleAtRow(int rowIndex) {
		return cache.get(rowIndex).title();
	}
	
	/**
	 * Updates the cache with new data. Also calls the using table to re-render data.
	 * @param newCache
	 */
	public void updateCache(List<BookCopyTableRecord> newCache) {
		cache = newCache;
		fireTableDataChanged();
	}

}
