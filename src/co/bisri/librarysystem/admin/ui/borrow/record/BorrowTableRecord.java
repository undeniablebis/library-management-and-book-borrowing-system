package co.bisri.librarysystem.admin.ui.borrow.record;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BorrowTableRecord(int memberId, String memberFirstName, String memberLastName, LocalDate borrowedOn,
                                LocalDate targetReturnDate, LocalDateTime returnedOn, String status, double returnFee) {

}
