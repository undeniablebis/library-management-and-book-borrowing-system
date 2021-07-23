package co.bisri.librarysystem;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import co.bisri.librarysystem.admin.ui.ManagementPanel;
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

        // Main Frame
        MainFrame mainFrame = new MainFrame();

        // Management Panels
        ManagementPanel[] managementPanels = new ManagementPanel[5];
        managementPanels[0] = new BookManagementPanel();
        managementPanels[1] = new MemberManagementPanel();
        managementPanels[2] = new BookCategoryManagementPanel();
        managementPanels[3] = new BookCopyManagementPanel();
        managementPanels[4] = new BorrowManagementPanel();

        // Set the datasource of all management panels
        for(ManagementPanel managementPanel : managementPanels)
            managementPanel.setDataSource(dataSource);

        // Wire all management panels to the main frame
        mainFrame.setManagementPanels(managementPanels);

        // Show the frame on the event dispatch thread.
        SwingUtilities.invokeLater(() -> {
            mainFrame.setVisible(true);
        });
    }

}
