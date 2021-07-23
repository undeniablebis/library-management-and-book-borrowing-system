package co.bisri.librarysystem.admin.ui.book.record;

import java.time.LocalDate;


public record BookEntity(String isbn, String categoryName, String title, String author, LocalDate publishedOn,
                         String publisher) {


}
