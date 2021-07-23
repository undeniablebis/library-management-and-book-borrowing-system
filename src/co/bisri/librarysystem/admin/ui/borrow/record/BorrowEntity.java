package co.bisri.librarysystem.admin.ui.borrow.record;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BorrowEntity(int memberId, String memberName, LocalDate borrowedOn, LocalDate targetReturnDate,
                           LocalDateTime returnedOn, String status, double returnFee, List<BookCopyEntity> itemList) {

}
