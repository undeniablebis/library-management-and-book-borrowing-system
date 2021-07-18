package co.bisri.librarysystem.admin.ui.borrow.record;

public record BookCopyComboBoxItem(String isbn, int copyNo, String title) {

	@Override
	public String toString() {
		return isbn + " " + title;
	}
	
}
