package co.bisri.librarysystem.admin.ui.member;

import java.time.LocalDateTime;

/**
 * 
 * Record class for Member
 * @author Bismillah Constantino
 * 
 * */
public record Member(int id, String firstName, String lastName, String addressLine1, String addressLine2, 
		String addressLine3, String contactNumber, String emailAddress, LocalDateTime registeredOn) {



}
