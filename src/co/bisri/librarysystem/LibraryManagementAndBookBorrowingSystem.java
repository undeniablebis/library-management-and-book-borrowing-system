package co.bisri.librarysystem;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import co.bisri.librarysystem.admin.ui.MainFrame;
import co.bisri.librarysystem.admin.ui.book.BookManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcategory.BookCategoryManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcopy.BookCopyManagementPanel;
import co.bisri.librarysystem.admin.ui.borrow.BorrowManagementPanel;
import co.bisri.librarysystem.admin.ui.member.MemberManagementPanel;

public class LibraryManagementAndBookBorrowingSystem {

    public static void main(String[] args) {

        // MySQL DataSource object
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setDatabaseName("library_db");
        dataSource.setUser("im_library_project");
        dataSource.setPassword("library123");
        // END: MySQL DataSource object

        MainFrame mainFrame = new MainFrame();

        BookManagementPanel booksManagementPanel = new BookManagementPanel();
        booksManagementPanel.setDataSource(dataSource);

        MemberManagementPanel memberManagementPanel = new MemberManagementPanel();
        memberManagementPanel.setDataSource(dataSource);

        BookCategoryManagementPanel bookCategoryManagementPanel = new BookCategoryManagementPanel();
        bookCategoryManagementPanel.setDataSource(dataSource);

        BookCopyManagementPanel bookCopyManagementPanel = new BookCopyManagementPanel();
        bookCopyManagementPanel.setDataSource(dataSource);

        BorrowManagementPanel borrowManagementPanel = new BorrowManagementPanel();
        borrowManagementPanel.setDataSource(dataSource);

        mainFrame.setBooksManagementPanel(booksManagementPanel);
        mainFrame.setBooksCategoryManagementPanel(bookCategoryManagementPanel);
        mainFrame.setBookCopyManagementPanel(bookCopyManagementPanel);
        mainFrame.setMemberManagementPanel(memberManagementPanel);
        mainFrame.setBorrowManagementPanel(borrowManagementPanel);
        
        // Set nimbus look and feel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Show the entry frame.
                mainFrame.setVisible(true);
            }
        });
    }

}
