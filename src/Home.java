
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
	public static int currentPostID = 0;
	public static int maxPostID = 0;

	public static int cur_post_id; // declare current post id 
	String cur_user_id = Login.cur_user_id; // get current user id from Login class

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
				btnReply.setForeground(new Color(255, 255, 255));
				btnReply.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new Reply(parent);
					}
				});
				buttonPane.add(btnReply);
				getRootPane().setDefaultButton(btnReply);
			}

			// TODO: add like success & fail message 
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
					dispose();
				}
			});
			buttonPane.add(btnBookmark);

			// TODO: fix first tweet not showing error 
			// after pressing next button
			btnPrevious = new JButton("<-");
			btnPrevious.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					if (m <= 1) {
					} else {
						m--;
					}
					new Home(parent);
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

			// TODO: fix null tweet showing error 
			// after viewing the oldest tweet
			btnNext = new JButton("->");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					if (m >= maxPostID) {
						m++;
					} else {
					}
					new Home(parent);
				}
			});
			buttonPane.add(btnNext);

		}

		setSize(500, 750);
		setLocationRelativeTo(parent);
		var parentLocation = parent.getLocationOnScreen();
		setLocation(parentLocation.x + parent.getWidth() - getWidth(),
				parentLocation.y + parent.getHeight() / 2 - getHeight() / 2 + 50);

		seePostFromDatabase(m);

		setVisible(true);
	}

	/**
	 * See tweet from database.
	 */
	public Post post;
	public Comment comment;

	private void seePostFromDatabase(int n) {
		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			String s1 = "SELECT * FROM post ORDER BY pst_date DESC LIMIT " + n + ", 1";
			PreparedStatement preparedStatement = conn.prepareStatement(s1);

			ResultSet resultSet = null;
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int post_id = resultSet.getInt(1);
				currentPostID = resultSet.getInt(1);
				cur_post_id = post_id;
				String post_text = resultSet.getString(2);
				String post_image = resultSet.getString(3);
//				String post_video = resultSet.getString(4);
				int post_num_of_likes = resultSet.getInt(5);
				String post_user_id = resultSet.getString(6);
				String post_date = resultSet.getString(7);

//				// for debugging
//				System.out.println("post_user_id=" + post_user_id);
//				System.out.println("post_id=" + post_id);
//				System.out.println("post_date=" + post_date);
//				System.out.println("post_text=" + post_text);
//				System.out.println("post_image=" + post_image);
//				System.out.println("post_num_of_likes=" + post_num_of_likes);

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
			String s1 = "SELECT pstl_lkr_id FROM post_like WHERE pstl_lkr_id=\"" + cur_user_id + "\" AND pstl_pst_id=\""
					+ cur_post_id + "\"";

			rs = stmt.executeQuery(s1);

			if (rs.next()) {
				System.out.println("You already liked this post!");
			} else {
				++plid; // for pstl_id

				// import usr_id for corresponding pst_id from the post table
				String s2 = " SELECT DISTINCT pst_usr_id FROM post WHERE pst_id=\"" + cur_post_id + "\" ";
				rs = stmt.executeQuery(s2);
				String post_user_id = ""; // pst_usr_id

				// get the usr_id of the person who wrote the post
				while (rs.next()) {
					post_user_id = rs.getString(1);
				}

				// insert the information about the post and the information about the person
				// who likes the post into the post_like table
				String s3 = "INSERT INTO post_like VALUES(\"" + plid + "\", \"" + cur_user_id + "\", \"" + cur_post_id
						+ "\", \"" + post_user_id + "\")";
				pstm = conn.prepareStatement(s3);
				pstm.executeUpdate();

				// increase the number of likes for the post in the post table by one
				String s4 = "UPDATE post SET pst_nol = pst_nol + 1 WHERE pst_id = \"" + cur_post_id + "\"";
				stmt.executeUpdate(s4);
				
				System.out.println("You liked this post.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
