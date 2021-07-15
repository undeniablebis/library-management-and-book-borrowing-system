package co.bisri.librarysystems;

import javax.swing.SwingUtilities;

import co.bisri.librarysystem.admin.ui.MainFrame;
import co.bisri.librarysystem.admin.ui.book.BooksManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcategory.BooksCategoryManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcopy.BookCopyManagementPanel;
import co.bisri.librarysystem.admin.ui.borrow.BorrowManagementPanel;

public class LibraryManagementAndBookBorrowingSystem {

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		
		BooksManagementPanel booksManagementPanel = new BooksManagementPanel();
		BooksCategoryManagementPanel booksCategoryManagementPanel = new BooksCategoryManagementPanel();
		BookCopyManagementPanel bookCopyManagementPanel = new BookCopyManagementPanel();
		BorrowManagementPanel borrowManagementPanel = new BorrowManagementPanel();
		
		mainFrame.setBooksManagementPanel(booksManagementPanel);
		mainFrame.setBooksCategoryManagementPanel(booksCategoryManagementPanel);
		mainFrame.setBookCopyManagementPanel(bookCopyManagementPanel);
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
