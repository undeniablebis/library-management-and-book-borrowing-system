package co.bisri.librarysystem;

import javax.swing.SwingUtilities;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import co.bisri.librarysystem.admin.ui.MainFrame;
import co.bisri.librarysystem.admin.ui.book.BooksManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcategory.BooksCategoryManagementPanel;
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
		
		BooksManagementPanel booksManagementPanel = new BooksManagementPanel();
		booksManagementPanel.setDataSource(dataSource);
		
		MemberManagementPanel memberManagementPanel = new MemberManagementPanel();
		memberManagementPanel.setDataSource(dataSource);
		
		BooksCategoryManagementPanel booksCategoryManagementPanel = new BooksCategoryManagementPanel();
		booksCategoryManagementPanel.setDataSource(dataSource);
		
		BookCopyManagementPanel bookCopyManagementPanel = new BookCopyManagementPanel();
		bookCopyManagementPanel.setDataSource(dataSource);
		
		BorrowManagementPanel borrowManagementPanel = new BorrowManagementPanel();
		borrowManagementPanel.setDataSource(dataSource);
		
		mainFrame.setBooksManagementPanel(booksManagementPanel);
		mainFrame.setBooksCategoryManagementPanel(booksCategoryManagementPanel);
		mainFrame.setBookCopyManagementPanel(bookCopyManagementPanel);
		mainFrame.setMemberManagementPanel(memberManagementPanel);
		mainFrame.setBorrowManagementPanel(borrowManagementPanel);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Show the entry frame.
				mainFrame.setVisible(true);
			}
		});
	}

}
