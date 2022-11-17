
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Home extends JDialog {

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
	private JButton btnNext;
	private JButton btnClose;
	private JButton btnPrevious;

	public static int m = 0;
	public static int curPostId = 0;
	public static int maxPostId = 0;
	String curUserId = Login.curUserId; // get current user id from Login class

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Home dialogHome = new Home(null);

			dialogHome.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialogHome.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Home(JFrame parent) {
		super(parent);
		setTitle("Home");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setModal(true);
		contentPanel.setLayout(null);

		{
			lbPostUserID = new JLabel("User ID");
			lbPostUserID.setHorizontalAlignment(SwingConstants.LEFT);
			lbPostUserID.setBounds(6, 46, 130, 16);
			contentPanel.add(lbPostUserID);
		}

		{
			lbPostID = new JLabel("Post ID");
			lbPostID.setHorizontalAlignment(SwingConstants.CENTER);
			lbPostID.setBounds(220, 46, 61, 16);
			contentPanel.add(lbPostID);
		}

		{
			lbPostDate = new JLabel("Date");
			lbPostDate.setHorizontalAlignment(SwingConstants.RIGHT);
			lbPostDate.setBounds(308, 46, 186, 16);
			contentPanel.add(lbPostDate);
		}

		lbPostText = new JLabel("Text");
		lbPostText.setVerticalAlignment(SwingConstants.TOP);
		lbPostText.setBounds(6, 144, 488, 116);
		contentPanel.add(lbPostText);

		lbPostImage = new JLabel("Image");
		lbPostImage.setHorizontalAlignment(SwingConstants.CENTER);
		lbPostImage.setBounds(70, 272, 354, 257);
		contentPanel.add(lbPostImage);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnReply = new JButton("Reply");
				btnReply.setForeground(new Color(0, 0, 255));
				btnReply.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new Reply(parent);
					}
				});
				buttonPane.add(btnReply);
				getRootPane().setDefaultButton(btnReply);
			}

			btnLike = new JButton("Like");
			btnLike.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					likeTweet();
					dispose();
					new Home(parent);
				}
			});
			buttonPane.add(btnLike);

			btnBookmark = new JButton("Bmk");
			btnBookmark.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					bookmarkTweet();
					dispose();
					new Home(parent);
				}
			});
			buttonPane.add(btnBookmark);

			seeTweetFromDB(m);

			// TODO: fix first tweet not showing error after pressing next button
			btnPrevious = new JButton("<-");
			btnPrevious.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					System.out.printf("cur_post_id= %d, max_post_id= %d\n ", curPostId + 1, maxPostId);
					if (curPostId  <= 0) {
						m = maxPostId;
						curPostId = maxPostId;

					} else {
						curPostId--;
						m--;
					}
					Home home = new Home(parent);
					System.out.println(home);
				}
			});
			buttonPane.add(btnPrevious);

			{
				btnClose = new JButton("X");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(btnClose);
			}

			// TODO: fix null tweet showing error after viewing the oldest tweet
			btnNext = new JButton("->");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					System.out.printf("cur_post_id= %d, max_post_id= %d\n ", curPostId - 1, maxPostId);
					if (curPostId - 1 > maxPostId) {
						curPostId = 1;
						m = 1;

					} else {
						m++;
						maxPostId++;
					}
					Home home = new Home(parent);
					System.out.println(home);
				}
			});
			buttonPane.add(btnNext);

		}

		setSize(500, 750);
		setLocationRelativeTo(parent);
		var parentLocation = parent.getLocationOnScreen();
		setLocation(parentLocation.x + parent.getWidth() - getWidth(),
				parentLocation.y + parent.getHeight() / 2 - getHeight() / 2 + 50);

		setVisible(true);
	}

	/**
	 * See tweet from database.
	 */
	public Post post;
	public Comment comment;

	private void seeTweetFromDB(int n) {
		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			ResultSet rs = null;

			String s0 = "SELECT DISTINCT MAX(pst_id) from post";
			rs = stmt.executeQuery(s0);
			if (rs.next()) {
				maxPostId = rs.getInt(1);
//				System.out.printf("max_post_id = %d\n", maxPostId);
			}

			String s1 = "SELECT * FROM post ORDER BY pst_date DESC LIMIT " + n + ", 1";
			PreparedStatement preparedStatement = conn.prepareStatement(s1);

			ResultSet resultSet = null;
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int post_id = resultSet.getInt(1); // pst_id
				curPostId = post_id; // pst_id
				String post_text = resultSet.getString(2); // pst_txt
				String post_image = resultSet.getString(3); // pst_img
//				String post_video = resultSet.getString(4); // pst_vid
				int post_num_of_likes = resultSet.getInt(5); // pst_nol
				String post_user_id = resultSet.getString(6); // pst_usr_id
				String post_date = resultSet.getString(7); // pst_date;

				lbPostUserID.setText(post_user_id);
				lbPostID.setText(String.valueOf(post_id));
				lbPostDate.setText(post_date);
				lbPostText.setText(post_text);
				lbPostImage.setText(post_image);
//                lbPostVideo.setText(post_video);
				btnLike.setText(String.valueOf(post_num_of_likes));
			}

			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Like tweet.
	 */
	private void likeTweet() {
		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			PreparedStatement pstm = null;

			int plid = -1;

			Statement stmt = conn.createStatement();
			ResultSet rs = null;

			// solution for duplicate entry error!!!
			String s1 = "SELECT pstl_lkr_id FROM post_like WHERE pstl_lkr_id=\"" + curUserId + "\" AND pstl_pst_id=\""
					+ curPostId + "\"";

			rs = stmt.executeQuery(s1);

			if (rs.next()) {
				System.out.println("You already liked this post!");
				JOptionPane.showMessageDialog(this, "You already liked this post!", "Like Failed",
						JOptionPane.ERROR_MESSAGE);
			} else {
				++plid; // for pstl_id

				// import usr_id for corresponding pst_id from the post table
				String s2 = " SELECT DISTINCT pst_usr_id FROM post WHERE pst_id=\"" + curPostId + "\" ";
				rs = stmt.executeQuery(s2);
				String postUserId = ""; // pst_usr_id

				// get the usr_id of the person who wrote the post
				while (rs.next()) {
					postUserId = rs.getString(1);
				}

				// insert the information about the post and the information about the person
				// who likes the post into the post_like table
				String s3 = "INSERT INTO post_like VALUES(\"" + plid + "\", \"" + curUserId + "\", \"" + curPostId
						+ "\", \"" + postUserId + "\")";
				pstm = conn.prepareStatement(s3);
				pstm.executeUpdate();

				// increase the number of likes for the post in the post table by one
				String s4 = "UPDATE post SET pst_nol = pst_nol + 1 WHERE pst_id = \"" + curPostId + "\"";
				stmt.executeUpdate(s4);

				System.out.println("You liked this post.");
				JOptionPane.showMessageDialog(this, "One like added.", "Like Success", JOptionPane.INFORMATION_MESSAGE);

				stmt.close();
				conn.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bookmark tweet.
	 */
	public User user;
	public Bookmark bookmark;

	private void bookmarkTweet() {
		int bookmark_id = 0; // pst_id
		bookmark_id++;

		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			ResultSet rs = null;

			// solution for duplicate entry error!!!
			String s0 = "SELECT DISTINCT MAX(bmk_id) from bookmark";
			rs = stmt.executeQuery(s0);
			if (rs.next()) {
				bookmark_id = rs.getInt(1) + 1;
//				System.out.printf("bmk_id = %d\n", bookmark_id); // for debugging
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String curUserId = Login.curUserId;

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			ResultSet rs = null;

			// solution for duplicate entry error!!!
			String s1 = "SELECT * from post WHERE pst_id= \"" + curPostId + "\"";
			rs = stmt.executeQuery(s1);

			if (rs.next()) {
				int bookmark_post_id = rs.getInt(1);
				String bookmark_post_text = rs.getString(2);
				String bookmark_post_image = rs.getString(3);
				String bookmark_post_video = rs.getString(4);
				int bookmark_post_nol = rs.getInt(5);
				String bookmark_post_user_id = rs.getString(6);
				String bookmark_user_id = curUserId;
				String bookmark_post_date = rs.getString(7);

				bookmark = addBookmarkToDB(bookmark_id, bookmark_post_id, bookmark_post_text, bookmark_post_image,
						bookmark_post_video, bookmark_post_nol, bookmark_post_user_id, bookmark_user_id,
						bookmark_post_date);
			}

			if (bookmark != null) {
				System.out.println("Bookmark success.");
				JOptionPane.showMessageDialog(this, "Bookmark ID = " + bookmark_id, "Bookmark Success",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				System.out.println("Bookmark failed.");
				JOptionPane.showMessageDialog(this, "Try again!", "Bookmark Failed", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Add bookmark to database.
	 */
	private Bookmark addBookmarkToDB(int bookmark_id, int bookmark_post_id, String bookmark_post_text,
			String bookmark_post_image, String bookmark_post_video, int bookmark_post_num_of_likes,
			String bookmark_post_user_id, String bookmark_user_id, String bookmark_post_date) {
		Bookmark bookmark = null;

		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
//			System.out.println("cur_post_id = " + curPostId);
			String sql = "INSERT INTO bookmark (bmk_pst_id, bmk_pst_txt, bmk_pst_img, bmk_pst_vid, bmk_pst_nol, bmk_pst_usr_id, bmk_usr_id, bmk_pst_date)"
					+ "SELECT ?, ?, ?, ?, ?, ?, \"" + curUserId + "\" , ?" + "FROM post WHERE pst_id= \""
					+ curPostId + "\"";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, bookmark_post_id);
			preparedStatement.setString(2, bookmark_post_text);
			preparedStatement.setString(3, bookmark_post_image);
			preparedStatement.setString(4, bookmark_post_video);
			preparedStatement.setInt(5, bookmark_post_num_of_likes);
			preparedStatement.setString(6, bookmark_post_user_id);
			preparedStatement.setString(7, bookmark_post_date);

			// insert row into the bookmark table
			int addedRows = preparedStatement.executeUpdate();
			if (addedRows > 0) {
				bookmark = new Bookmark();
				bookmark.bookmark_post_id = bookmark_post_id;
				bookmark.bookmark_post_text = bookmark_post_text;
				bookmark.bookmark_post_image = bookmark_post_image;
				bookmark.bookmark_post_video = bookmark_post_video;
				bookmark.bookmark_post_num_of_likes = bookmark_post_num_of_likes;
				bookmark.bookmark_post_user_id = bookmark_post_user_id;
				bookmark.bookmark_user_id = curUserId;
				bookmark.bookmark_post_date = bookmark_post_date;
			}

			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookmark;
	}
}
