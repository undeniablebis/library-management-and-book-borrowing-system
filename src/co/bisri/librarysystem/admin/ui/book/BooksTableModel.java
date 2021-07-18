package co.bisri.librarysystem.admin.ui.book;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for Books Table in BooksCategoryManagementPanel.
 * 
 * @author Rian Reyes
 *
 */
public class BooksTableModel extends AbstractTableModel {
	
	// Ignore, this is to remove serializability-related warnings
	private static final long serialVersionUID = 1L;

	// Column names of this tablemodel
	private static final String[] COLUMN_NAMES = { "ISBN", "Category Name", "Title", "Author", "Publisher", "Published On" };
	
	// Current values stored by this tablemodel
	private List<Books> cache;
	
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
		Books books = cache.get(rowIndex);
		
		switch(columnIndex) {
		
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
	
	/**
	 * Updates the cache with new data. Also calls the using table to re-render data.
	 * @param newCache
	 */
	public void updateCache(List<Books> newCache) {
		cache = newCache;
		fireTableDataChanged();
	}

}
