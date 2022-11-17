
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class Bookmarks extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JLabel lbPostUserID;
	private JLabel lbPostID;
	private JLabel lbPostDate;
	private JLabel lbPostText;
	private JLabel lbPostImage;

	private JButton btnReply;
	private JButton btnLike;
	private JButton btnBookmark;
	private JButton btnPrevious;
	private JButton btnClose;
	private JButton btnNext;

	String cur_user_id = Login.curUserId; // get current user id from Login class
	int cur_post_id = Home.curPostId; // get current post id from Home class

	public static int m = 0;
	public static int currentBookmarkID = 0;
	public static int maxBookmarkID = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Bookmarks dialog = new Bookmarks(null);

			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Bookmarks(JFrame parent) {
		super(parent);
		setTitle("Bookmarks");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setModal(true);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lbPostUserID = new JLabel("User ID");
			lbPostUserID.setBounds(6, 10, 138, 16);
			contentPanel.add(lbPostUserID);
		}
		{
			lbPostID = new JLabel("Post ID");
			lbPostID.setHorizontalAlignment(SwingConstants.CENTER);
			lbPostID.setBounds(223, 10, 61, 16);
			contentPanel.add(lbPostID);
		}
		{
			lbPostDate = new JLabel("Date");
			lbPostDate.setHorizontalAlignment(SwingConstants.RIGHT);
			lbPostDate.setBounds(356, 10, 138, 16);
			contentPanel.add(lbPostDate);
		}
		{
			lbPostText = new JLabel("Text");
			lbPostText.setVerticalAlignment(SwingConstants.TOP);
			lbPostText.setBounds(6, 89, 488, 104);
			contentPanel.add(lbPostText);
		}

		lbPostImage = new JLabel("Image");
		lbPostImage.setHorizontalAlignment(SwingConstants.CENTER);
		lbPostImage.setBounds(6, 205, 488, 143);
		contentPanel.add(lbPostImage);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnPrevious = new JButton("<-");
				btnPrevious.setForeground(new Color(0, 0, 0));
				btnPrevious.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						if (m <= 1) {
						} else {
							m--;
						}
						new Bookmarks(parent);
					}
				});
				{
					btnReply = new JButton("Reply");
					btnReply.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
						}
					});
					buttonPane.add(btnReply);
				}
				{
					btnLike = new JButton("Like");
					btnLike.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
						}
					});
					buttonPane.add(btnLike);
				}
				{
					btnBookmark = new JButton("Bmk");
					btnBookmark.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
						}
					});
					buttonPane.add(btnBookmark);
				}
				buttonPane.add(btnPrevious);
			}
			{
				btnClose = new JButton("X");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(btnClose);
			}

			btnNext = new JButton("->");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					if (m >= maxBookmarkID) {
						m++;
					} else {
					}
					new Bookmarks(parent);
				}
			});
			buttonPane.add(btnNext);
		}

		setSize(500, 750);
		setLocationRelativeTo(parent);
		var parentLocation = parent.getLocationOnScreen();
		setLocation(parentLocation.x + parent.getWidth() - getWidth(),
				parentLocation.y + parent.getHeight() / 2 - getHeight() / 2 + 50);

		seeBookmarkFromDB(m);

		setVisible(true);
	}

	/**
	 * See bookmark from database.
	 */
	public Bookmark bookmark;

	private void seeBookmarkFromDB(int n) {
		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		String cur_user_id = Login.curUserId;
		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			String s1 = "SELECT * FROM bookmark WHERE bmk_usr_id LIKE \"" + cur_user_id
					+ "\" ORDER BY bmk_id DESC LIMIT " + n + ", 1";

			PreparedStatement preparedStatement = conn.prepareStatement(s1);

			ResultSet resultSet = null;
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int bookmark_id = resultSet.getInt(1);
				currentBookmarkID = bookmark_id;
				int bookmark_post_id = resultSet.getInt(2);
				String bookmark_post_text = resultSet.getString(3);
				String bookmark_post_image = resultSet.getString(4);
//				String bookmark_post_video = resultSet.getString(5);
				int bookmark_post_num_of_likes = resultSet.getInt(6);
				System.out.println(bookmark_post_num_of_likes);
				String bookmark_post_user_id = resultSet.getString(7);
//				String bookmark_user_id = resultSet.getString(8);
				String bookmark_post_date = resultSet.getString(9);

				lbPostUserID.setText(bookmark_post_user_id);
				lbPostID.setText(String.valueOf(bookmark_post_id));
				lbPostDate.setText(bookmark_post_date);
				lbPostText.setText(bookmark_post_text);
				lbPostImage.setText(bookmark_post_image);
//                lbPostVideo.setText(bookmark_post_video);
				btnLike.setText(String.valueOf(bookmark_post_num_of_likes));
			}

			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
