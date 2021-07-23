package co.bisri.librarysystem.admin.ui.borrow.record;

public record MemberComboBoxItem(int id, String name) {

    @Override
    public String toString() {
        return name;
    }

}
