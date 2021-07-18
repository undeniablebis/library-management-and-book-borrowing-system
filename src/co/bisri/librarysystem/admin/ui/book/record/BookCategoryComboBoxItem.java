package co.bisri.librarysystem.admin.ui.book.record;

public record BookCategoryComboBoxItem(String name) {
	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof BookCategoryComboBoxItem))
			return false;
		
		BookCategoryComboBoxItem otherItem = (BookCategoryComboBoxItem) other;
		
		if((otherItem.name == null && this.name != null) || (otherItem.name != null && this.name == null))
			return false;
		
		return name.contentEquals(otherItem.name);
	}

	@Override
	public String toString() {
		return name;
	}
	
}
