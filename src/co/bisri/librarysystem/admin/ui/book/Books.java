package co.bisri.librarysystem.admin.ui.book;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record Books(String isbn, String categoryName, String title, String author, 
		LocalDate publishedOn, String publisher) {

	

}
