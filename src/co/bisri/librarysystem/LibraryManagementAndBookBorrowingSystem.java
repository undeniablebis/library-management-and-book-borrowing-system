package co.bisri.librarysystem;

import javax.swing.SwingUtilities;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import co.bisri.librarysystem.admin.ui.MainFrame;
import co.bisri.librarysystem.admin.ui.book.BooksManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcategory.BooksCategoryManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcopy.BookCopyManagementPanel;

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
		
		BooksCategoryManagementPanel booksCategoryManagementPanel = new BooksCategoryManagementPanel();
		booksCategoryManagementPanel.setDataSource(dataSource);
		
		BookCopyManagementPanel bookCopyManagementPanel = new BookCopyManagementPanel();
		
		mainFrame.setBooksManagementPanel(booksManagementPanel);
		mainFrame.setBooksCategoryManagementPanel(booksCategoryManagementPanel);
		mainFrame.setBookCopyManagementPanel(bookCopyManagementPanel);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Show the entry frame.
				mainFrame.setVisible(true);
			}
		});
	}

}
