package co.bisri.librarysystem.admin.ui.member;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for BookCategory Table in BooksCategoryManagementPanel.
 * 
 * @author Rian Reyes
 *
 */
public class MemberTableModel extends AbstractTableModel {

	// Ignore, this is to remove serializability-related warnings
	private static final long serialVersionUID = 1L;

	// Column names of this tablemodel
	private static final String[] COLUMN_NAMES = { "ID", "Name", "Address", "Contact Number", "Email Address",
			"Date registered" };

	// Current values stored by this tablemodel
	private List<Member> cache;

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return COLUMN_NAMES[columnIndex];
	}

	@Override
	public int getRowCount() {
		if (cache == null)
			return 0;

		return cache.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Member member = cache.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return member.id();
		
		case 1:
			return member.firstName() +" "+ member.lastName();

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

	/**
	 * Updates the cache with new data. Also calls the using table to re-render
	 * data.
	 * 
	 * @param newCache
	 */
	public void updateCache(List<Member> newCache) {
		cache = newCache;
		fireTableDataChanged();
	}

}
