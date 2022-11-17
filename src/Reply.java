
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Reply extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	
	private JTextArea taReplyText;
	
	private JButton btnCancel;
	private JButton btnReply;

	String cur_user_id = Login.curUserId; // get current user id from Login class
	int cur_post_id = Home.curPostId; // get current post id from Home class

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Reply dialogReply = new Reply(null);

			Comment comment = dialogReply.comment;

			if (comment != null) {
				System.out.println("Reply successful!");
			} else {
				System.out.println("Reply canceled.");
			}

			dialogReply.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialogReply.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the Reply dialog.
	 */
	public Reply(JFrame parent) {
		super(parent);
		setTitle("Reply");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setModal(true);
		contentPanel.setLayout(null);

		{
			taReplyText = new JTextArea();
			taReplyText.setLineWrap(true);
			taReplyText.setWrapStyleWord(true);
			taReplyText.setBounds(74, 223, 337, 188);
			contentPanel.add(taReplyText);
		}
		{
			JLabel lbReplyMessage = new JLabel("Tweet your reply");
			lbReplyMessage.setBounds(74, 195, 134, 16);
			contentPanel.add(lbReplyMessage);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			{
				btnReply = new JButton("Reply");
				btnReply.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						replyTweet();
					}
				});
				buttonPane.add(btnReply);
				getRootPane().setDefaultButton(btnReply);
			}
			
			{
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(btnCancel);
			}
		}

		setSize(500, 750);
		setLocationRelativeTo(parent);
		var parentLocation = parent.getLocationOnScreen();
		setLocation(parentLocation.x + parent.getWidth() - getWidth(),
				parentLocation.y + parent.getHeight() / 2 - getHeight() / 2 + 50);
		setVisible(true);
	}

	/**
	 * Reply tweet.
	 */
	User user = null;
	Post post = null;

	private void replyTweet() {
		int comment_id = 0; // cmt_id
		comment_id++;

		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();

			// solution for duplicate entry error!!!
			ResultSet rs = null;
			String s0 = "SELECT DISTINCT MAX(cmt_id) from comment";
			rs = stmt.executeQuery(s0);
			if (rs.next()) {
				comment_id = rs.getInt(1) + 1;
//	                System.out.printf("cmt_id = %d\n", comment_id); // for debugging
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String comment_text = taReplyText.getText(); // cmt_txt
		String comment_image = ""; // cmt_img
		String comment_video = ""; // cmt_vid
		int comment_num_of_likes = 0; // cmt_nol
		int comment_post_id = cur_post_id;

//	        LoginForm.user_id = user_id;
		System.out.println(cur_user_id); // for debugging
		String comment_user_id = cur_user_id; // cmt_usr_id

		if (comment_text.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please write something.", "Reply Failed", JOptionPane.ERROR_MESSAGE);
			return;
		}

		comment = addReplyToDB(comment_id, comment_text, comment_image, comment_video, comment_num_of_likes,
				comment_post_id, comment_user_id);
		if (comment != null) {
			JOptionPane.showMessageDialog(this, "New reply uploaded.", "Reply Success",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Failed to reply tweet.", "Reply Failed", JOptionPane.ERROR_MESSAGE);
			dispose();
		}
	}

	/**
	 * Add comment to database.
	 */
	public Comment comment;

	private Comment addReplyToDB(int comment_id, String comment_text, String comment_image,
			String comment_video, int comment_num_of_likes, int comment_post_id, String comment_user_id) {
		Comment comment = null;

		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO comment(cmt_id, cmt_txt, cmt_img, cmt_vid, cmt_nol, cmt_pst_id, cmt_usr_id)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, comment_id);
			preparedStatement.setString(2, comment_text);
			preparedStatement.setString(3, comment_image);
			preparedStatement.setString(4, comment_video);
			preparedStatement.setInt(5, comment_num_of_likes);
			preparedStatement.setInt(6, comment_post_id);
			preparedStatement.setString(7, comment_user_id);

			// insert row into the comment table
			int addedRows = preparedStatement.executeUpdate();
			if (addedRows > 0) {
				comment = new Comment();
				comment.comment_id = comment_id;
				comment.comment_text = comment_text;
				comment.comment_image = comment_image;
				comment.comment_video = comment_video;
				comment.comment_num_of_likes = comment_num_of_likes;
				comment.comment_user_id = comment_user_id;
			}

			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return comment;
	}
}
