package co.bisri.librarysystem.admin.ui.borrow.record;

public record BookCopyEntity(String isbn, int copyNo, String title, double currentWorth) {

    @Override
    public String toString() {
        return isbn + " " + title + " Copy" + copyNo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof BookCopyEntity))
            return false;

        BookCopyEntity otherBookCopyEntity = (BookCopyEntity) other;
        return isbn.contentEquals(otherBookCopyEntity.isbn()) &&
                copyNo == otherBookCopyEntity.copyNo;
    }

}
