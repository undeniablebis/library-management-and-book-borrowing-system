package co.bisri.librarysystem.admin.ui.bookcopy.record;

import java.time.LocalDate;

public record BookCopy(String isbn, int copyNo, LocalDate dateAcquired, String status, double currentWorth, String borrowStatus) {

}
