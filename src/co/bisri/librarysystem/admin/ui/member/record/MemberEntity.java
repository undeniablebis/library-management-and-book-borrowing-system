package co.bisri.librarysystem.admin.ui.member.record;

import java.time.LocalDateTime;

/**
 * Record class for Member
 *
 * @author Bismillah Constantino
 */
public record MemberEntity(int id, String firstName, String lastName, String addressLine1, String addressLine2,
                           String addressLine3, String contactNumber, String emailAddress, LocalDateTime registeredOn) {


}
