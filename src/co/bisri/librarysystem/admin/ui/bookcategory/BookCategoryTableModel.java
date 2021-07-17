package co.bisri.librarysystem.admin.ui.bookcategory;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for BookCategory Table in BooksCategoryManagementPanel.
 * 
 * @author Rian Reyes
 *
 */
public class BookCategoryTableModel extends AbstractTableModel {
	
	// Ignore, this is to remove serializability-related warnings
	private static final long serialVersionUID = 1L;

	// Column names of this tablemodel
	private static final String[] COLUMN_NAMES = { "Name", "Description" };
	
	// Current values stored by this tablemodel
	private List<BookCategory> cache;
	
	@Override
	public int getColumnCount() {
		return 2;
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
		BookCategory bookCategory = cache.get(rowIndex);
		
		switch(columnIndex) {
		
		case 0:
			return bookCategory.name();
		
		case 1:
			return bookCategory.description();
		
		}
		
		return null;
	}
	
	/**
	 * Updates the cache with new data. Also calls the using table to re-render data.
	 * @param newCache
	 */
	public void updateCache(List<BookCategory> newCache) {
		cache = newCache;
		fireTableDataChanged();
	}

}
