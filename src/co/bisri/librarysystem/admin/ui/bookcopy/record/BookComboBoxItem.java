package co.bisri.librarysystem.admin.ui.bookcopy.record;

public record BookComboBoxItem(String isbn, String title) {
	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof BookComboBoxItem))
			return false;
		
		BookComboBoxItem otherItem = (BookComboBoxItem) other;
		return isbn.equals(otherItem.isbn);
	}
	
	@Override
	public String toString() {
		return isbn + " " + title;
	}

}
