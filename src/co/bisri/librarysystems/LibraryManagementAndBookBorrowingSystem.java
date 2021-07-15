package co.bisri.librarysystems;

import javax.swing.SwingUtilities;

import co.bisri.librarysystem.admin.ui.MainFrame;
import co.bisri.librarysystem.admin.ui.book.BooksManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcategory.BooksCategoryManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcopy.BookCopyManagementPanel;

public class LibraryManagementAndBookBorrowingSystem {

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		
		BooksManagementPanel booksManagementPanel = new BooksManagementPanel();
		BooksCategoryManagementPanel booksCategoryManagementPanel = new BooksCategoryManagementPanel();
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
