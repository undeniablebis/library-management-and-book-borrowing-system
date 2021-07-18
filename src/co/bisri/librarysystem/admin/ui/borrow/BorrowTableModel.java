package co.bisri.librarysystem.admin.ui.borrow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import co.bisri.librarysystem.admin.ui.borrow.record.BorrowTableRecord;

/**
 * TableModel for BookCategory Table in BooksCategoryManagementPanel.
 * 
 * @author Rian Reyes
 *
 */
public class BorrowTableModel extends AbstractTableModel {
	
	// Ignore, this is to remove serializability-related warnings
	private static final long serialVersionUID = 1L;

	// Column names of this tablemodel
	private static final String[] COLUMN_NAMES = { "Member", "Borrowed On", "Target Return", "Returned", "Status", "Fee" };
	
	// Current values stored by this tablemodel
	private List<BorrowTableRecord> cache;
	
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
		BorrowTableRecord borrowTableRecord = cache.get(rowIndex);
		
		switch(columnIndex) {
		
		case 0:
			return borrowTableRecord.memberFirstName() + " " + borrowTableRecord.memberLastName();
		
		case 1:
			return borrowTableRecord.borrowedOn().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
			
		case 2:
			return borrowTableRecord.targetReturnDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
			
		case 3:
			return borrowTableRecord.returnedOn() == null ?
					"Not yet returned" :
						borrowTableRecord.returnedOn().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a"));
			
		case 4:
			return borrowTableRecord.status();
			
		case 5:
			return borrowTableRecord.returnFee();
		
		}
		
		return null;
	}
	
	public int getMemberIdAtRow(int rowIndex) {
		return cache.get(rowIndex).memberId();
	}
	
	public LocalDate getBorrowDateAtRow(int rowIndex) {
		return cache.get(rowIndex).borrowedOn();
	}
	
	/**
	 * Updates the cache with new data. Also calls the using table to re-render data.
	 * @param newCache
	 */
	public void updateCache(List<BorrowTableRecord> newCache) {
		cache = newCache;
		fireTableDataChanged();
	}

}
