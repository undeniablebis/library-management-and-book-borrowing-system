package co.bisri.librarysystem.admin.ui.borrow.record;

public record BookCopy(String isbn, int copyNo, String title, double currentWorth) {

	@Override
	public String toString() {
		return isbn + " " + title + " Copy" + copyNo;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof BookCopy))
			return false;
		
		BookCopy otherBookCopy = (BookCopy) other;
		return isbn.contentEquals(otherBookCopy.isbn()) &&
				copyNo == otherBookCopy.copyNo;
	}
	
}
