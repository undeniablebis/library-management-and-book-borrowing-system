package co.bisri.librarysystem.admin.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import co.bisri.librarysystem.admin.ui.book.BooksManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcategory.BooksCategoryManagementPanel;
import co.bisri.librarysystem.admin.ui.bookcopy.BookCopyManagementPanel;
import co.bisri.librarysystem.admin.ui.borrow.BorrowManagementPanel;
import co.bisri.librarysystem.admin.ui.member.MemberManagementPanel;

public class MainFrame extends JFrame {

	/*
	 * String constants for CardLayout traversal
	 */
	private static final String BOOK_MANAGEMENT_PANEL = "BOOK";
	private static final String BOOK_CATEGORY_MANAGEMENT_PANEL = "BOOK CATEGORY";
	private static final String BOOK_COPY_MANAGEMENT_PANEL = "BOOK COPY";
	private static final String BORROW_MANAGEMENT_PANEL = "BORROW";
	private static final String MEMBER_MANAGEMENT_PANEL = "MEMBER";

	/**
	 * Ignore for now, this is to avoid warnings.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Custom content pane for this Frame
	 */
	private JPanel jpnlContentPane;

	/**
	 * The main content panel, uses a CardLayout
	 */
	private JPanel jpnlMainContentPanel;

	/**
	 * Books Management Panel of this module.
	 */
	private BooksManagementPanel booksManagementPanel;

	/**
	 * Books Category Management Panel of this module.
	 */
	private BooksCategoryManagementPanel booksCategoryManagementPanel;

	/**
	 * Books Copy Management Panel of this module.
	 */
	private BookCopyManagementPanel bookCopyManagementPanel;

	/**
	 * Borrow Management Panel of this module.
	 */
	private BorrowManagementPanel borrowManagementPanel;

	/**
	 * Member Management Panel of this module.
	 */
	private MemberManagementPanel memberManagementPanel;

	public MainFrame() {
		setForeground(SystemColor.activeCaptionText);
		/* Frame Properties */
		setTitle("Library Management Administrator");
		setBounds(100, 100, 770, 492);
		setMinimumSize(new Dimension(770, 492));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* END OF Frame Properties */

		/* jpnlContentPane - custom content pane for this frame */
		jpnlContentPane = new JPanel();
		jpnlContentPane.setBorder(null);
		setContentPane(jpnlContentPane);
		jpnlContentPane.setLayout(new BoxLayout(jpnlContentPane, BoxLayout.X_AXIS));
		/* END OF jpnlContentPane */

		/*
		 * jpnlSidebar - sidebar panel shown on the left, uses BoxLayout to lay its
		 * components
		 */
		JPanel jpnlSidebar = new JPanel();
		jpnlSidebar.setBorder(null);
		jpnlSidebar.setBackground(SystemColor.desktop);
		jpnlSidebar.setMaximumSize(new Dimension(225, 32767));
		jpnlSidebar.setMinimumSize(new Dimension(225, 10));
		jpnlSidebar.setLayout(new BoxLayout(jpnlSidebar, BoxLayout.Y_AXIS));
		jpnlContentPane.add(jpnlSidebar);
		/* END OF jpnlSidebar */

		/*
		 * jpnlMainContentPanel - the main content panel where management panels are
		 * displayed
		 */
		jpnlMainContentPanel = new JPanel();
		jpnlMainContentPanel.setMaximumSize(new Dimension(32767, 32767));
		jpnlMainContentPanel.setLayout(new CardLayout());
		// Default show an empty panel
		jpnlMainContentPanel.add(new JPanel());
		jpnlContentPane.add(jpnlMainContentPanel);
		/* END OF jpnlMainContentPanel */

		/* jlblSidebarHeader - header label in the sidebar */
		JLabel jlblMiniHeader = new JLabel("<html>Polytechnic University of <br>the Philippines</html>");
		jlblMiniHeader.setAlignmentY(0.0f);
		jlblMiniHeader.setForeground(Color.WHITE);
		jlblMiniHeader.setFont(new Font("Roboto Light", Font.PLAIN, 11));
		jlblMiniHeader.setBorder(new EmptyBorder(20, 20, 0, 20));
		jpnlSidebar.add(jlblMiniHeader);

		JLabel jlblSidebarHeader = new JLabel("<html>Library Management<br>and Book Borrowing System</html>");
		jlblSidebarHeader.setAlignmentY(0.0f);
		jlblSidebarHeader.setBorder(new EmptyBorder(0, 20, 0, 20));
		jlblSidebarHeader.setForeground(Color.WHITE);
		jlblSidebarHeader.setFont(new Font("Roboto", Font.PLAIN, 20));
		jpnlSidebar.add(jlblSidebarHeader);
		/* END OF jlblSidebarHeader */

		// Spacing for the sidebar before the buttons
		jpnlSidebar.add(Box.createRigidArea(new Dimension(0, 50)));

		/* jbtnBookCategory -- Button for Book Category panel */
		JButton jbtnBookCategory = new JButton("Book Category");

		jbtnBookCategory.addActionListener((event) -> {
			CardLayout mainContentPanelLayout = (CardLayout) jpnlMainContentPanel.getLayout();
			mainContentPanelLayout.show(jpnlMainContentPanel, BOOK_CATEGORY_MANAGEMENT_PANEL);
			booksCategoryManagementPanel.initializePanel();
		});

		jbtnBookCategory.setAlignmentY(0.0f);
		jbtnBookCategory.setHorizontalAlignment(SwingConstants.LEFT);
		jbtnBookCategory.setMinimumSize(new Dimension(200, 40));
		jbtnBookCategory.setFont(new Font("Roboto", Font.PLAIN, 13));
		jbtnBookCategory.setForeground(Color.WHITE);
		jbtnBookCategory.setBackground(Color.BLACK);
		jbtnBookCategory.setBorder(new EmptyBorder(0, 20, 0, 20));
		jbtnBookCategory.setFocusPainted(false);
		jbtnBookCategory.setBorderPainted(false);
		jbtnBookCategory.setMaximumSize(new Dimension(32767, 40));
		jpnlSidebar.add(jbtnBookCategory);
		/* END OF jbtnBookCategory */

		/* jbtnBook -- Button for Book */
		JButton jbtnBook = new JButton("Book");

		jbtnBook.addActionListener((event) -> {
			CardLayout mainContentPanelLayout = (CardLayout) jpnlMainContentPanel.getLayout();
			mainContentPanelLayout.show(jpnlMainContentPanel, BOOK_MANAGEMENT_PANEL);
		});

		jbtnBook.setAlignmentY(0.0f);
		jbtnBook.setMinimumSize(new Dimension(200, 40));
		jbtnBook.setMaximumSize(new Dimension(32767, 40));
		jbtnBook.setHorizontalAlignment(SwingConstants.LEFT);
		jbtnBook.setForeground(Color.WHITE);
		jbtnBook.setFont(new Font("Roboto", Font.PLAIN, 13));
		jbtnBook.setFocusPainted(false);
		jbtnBook.setBorderPainted(false);
		jbtnBook.setBorder(new EmptyBorder(0, 20, 0, 20));
		jbtnBook.setBackground(Color.BLACK);
		jpnlSidebar.add(jbtnBook);
		/* END OF jbtnBook */

		/* jbtnBookCopy */
		JButton jbtnBookCopy = new JButton("Book Copy");

		jbtnBookCopy.addActionListener((event) -> {
			CardLayout mainContentPanelLayout = (CardLayout) jpnlMainContentPanel.getLayout();
			mainContentPanelLayout.show(jpnlMainContentPanel, BOOK_COPY_MANAGEMENT_PANEL);
		});

		jbtnBookCopy.setAlignmentY(0.0f);
		jbtnBookCopy.setMinimumSize(new Dimension(200, 40));
		jbtnBookCopy.setMaximumSize(new Dimension(32767, 40));
		jbtnBookCopy.setHorizontalAlignment(SwingConstants.LEFT);
		jbtnBookCopy.setForeground(Color.WHITE);
		jbtnBookCopy.setFont(new Font("Roboto", Font.PLAIN, 13));
		jbtnBookCopy.setFocusPainted(false);
		jbtnBookCopy.setBorderPainted(false);
		jbtnBookCopy.setBorder(new EmptyBorder(0, 20, 0, 20));
		jbtnBookCopy.setBackground(Color.BLACK);
		jpnlSidebar.add(jbtnBookCopy);
		/* END OF jbtnBookCopy */

		/* jbtnMember */
		JButton jbtnMember = new JButton("Member");
		jbtnMember.addActionListener((event) -> {
			CardLayout mainContentPanelLayout = (CardLayout) jpnlMainContentPanel.getLayout();
			mainContentPanelLayout.show(jpnlMainContentPanel, MEMBER_MANAGEMENT_PANEL);
			memberManagementPanel.initializePanel();
		});
		jbtnMember.setMinimumSize(new Dimension(200, 35));
		jbtnMember.setMaximumSize(new Dimension(32767, 35));
		jbtnMember.setHorizontalAlignment(SwingConstants.LEFT);
		jbtnMember.setForeground(Color.WHITE);
		jbtnMember.setFont(new Font("Roboto", Font.PLAIN, 13));
		jbtnMember.setFocusPainted(false);
		jbtnMember.setBorderPainted(false);
		jbtnMember.setBorder(new EmptyBorder(0, 20, 0, 0));
		jbtnMember.setBackground(Color.BLACK);
		jpnlSidebar.add(jbtnMember);
		/* END OF jbtnMember */

		/* jbtnBorrow */
		JButton jbtnBorrow = new JButton("Borrow");

		jbtnBorrow.setMinimumSize(new Dimension(200, 35));
		jbtnBorrow.setMaximumSize(new Dimension(32767, 35));
		jbtnBorrow.setHorizontalAlignment(SwingConstants.LEFT);
		jbtnBorrow.setForeground(Color.WHITE);
		jbtnBorrow.setFont(new Font("Roboto", Font.PLAIN, 13));
		jbtnBorrow.setFocusPainted(false);
		jbtnBorrow.setBorderPainted(false);
		jbtnBorrow.setBorder(new EmptyBorder(0, 20, 0, 0));
		jbtnBorrow.setBackground(Color.BLACK);
		jpnlSidebar.add(jbtnBorrow);
		/* END OF jbtnBorrow */

		/* jbtnBorrowItem */
		JButton jbtnBorrowItem = new JButton("Borrow Item");

		jbtnBorrowItem.setMinimumSize(new Dimension(200, 35));
		jbtnBorrowItem.setMaximumSize(new Dimension(32767, 35));
		jbtnBorrowItem.setHorizontalAlignment(SwingConstants.LEFT);
		jbtnBorrowItem.setForeground(Color.WHITE);
		jbtnBorrowItem.setFont(new Font("Roboto", Font.PLAIN, 13));
		jbtnBorrowItem.setFocusPainted(false);
		jbtnBorrowItem.setBorderPainted(false);
		jbtnBorrowItem.setBorder(new EmptyBorder(0, 20, 0, 0));
		jbtnBorrowItem.setBackground(Color.BLACK);
		jpnlSidebar.add(jbtnBorrowItem);
		/* END OF jbtnBorrowItem */
	}

	public void setBooksManagementPanel(BooksManagementPanel booksManagementPanel) {
		this.booksManagementPanel = booksManagementPanel;
		jpnlMainContentPanel.add(booksManagementPanel, BOOK_MANAGEMENT_PANEL);
	}

	public void setBooksCategoryManagementPanel(BooksCategoryManagementPanel booksCategoryManagementPanel) {
		this.booksCategoryManagementPanel = booksCategoryManagementPanel;
		jpnlMainContentPanel.add(booksCategoryManagementPanel, BOOK_CATEGORY_MANAGEMENT_PANEL);
	}

	public void setBookCopyManagementPanel(BookCopyManagementPanel bookCopyManagementPanel) {
		this.bookCopyManagementPanel = bookCopyManagementPanel;
		jpnlMainContentPanel.add(bookCopyManagementPanel, BOOK_COPY_MANAGEMENT_PANEL);
	}

	public void setBorrowManagementPanel(BorrowManagementPanel borrowManagementPanel) {
		this.borrowManagementPanel = borrowManagementPanel;
		jpnlMainContentPanel.add(borrowManagementPanel, BORROW_MANAGEMENT_PANEL);
	}

	public void setMemberManagementPanel(MemberManagementPanel memberManagementPanel) {
		this.memberManagementPanel = memberManagementPanel;
		jpnlMainContentPanel.add(memberManagementPanel, MEMBER_MANAGEMENT_PANEL);
	}

}
