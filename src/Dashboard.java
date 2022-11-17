
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;

public class Dashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JLabel lbWelcomeUser;

	private JButton btnProfile;
	private JButton btnHome;
	private JButton btnTweet;
	private JButton btnBookmarks;
	private JButton btnMessages;
	private JButton btnLogout;

	String cur_user_id = Login.curUserId; // get current user id from Login class

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Dashboard();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the Dashboard frame.
	 */
	public Dashboard() {
		setTitle("Dashboard");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel contentPanel = new JPanel();
		contentPanel.setBounds(135, 5, 500, 811);
		contentPane.add(contentPanel);
		contentPanel.setLayout(null);
		
		lbWelcomeUser = new JLabel("Hello! ");
				
		ImageIcon twitterImageIcon = new ImageIcon(Dashboard.class.getResource("/images/twitter.png"));
		Image twitterImage = twitterImageIcon.getImage(); // transform it 
		Image newTwitterImage = twitterImage.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		twitterImageIcon = new ImageIcon(newTwitterImage); // transform it back
		lbWelcomeUser.setIcon(twitterImageIcon);
		lbWelcomeUser.setBorder(null);
		
		lbWelcomeUser.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lbWelcomeUser.setHorizontalAlignment(SwingConstants.CENTER);
		lbWelcomeUser.setBounds(6, 6, 488, 46);
		contentPanel.add(lbWelcomeUser);
		
		// TODO new feature: get resolution from each computer and set size accordingly
		contentPanel.setSize(500, 811);

		boolean hasRegisteredUsers = connectToDB();

		// show login dialog
		if (hasRegisteredUsers) {
			Login dialogLogin = new Login(this);
			User user = dialogLogin.user;

			if (user != null) {
				lbWelcomeUser.setText("Hello! " + user.name);
				setLocationRelativeTo(null);
				setVisible(true);
			} else {
				dispose();
			}
		}

		// show sign up dialog
		else {
			Signup signup = new Signup(this);
			User user = signup.user;
			if (user != null) {
				lbWelcomeUser.setText("Hello! " + user.name);
				setLocationRelativeTo(null);
				setVisible(true);
			} else {
				dispose();
			}
		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(6, 5, 115, 811);
		contentPane.add(buttonPanel);
		buttonPanel.setLayout(null);

		btnProfile = new JButton();
		ImageIcon profileImageIcon = new ImageIcon(Dashboard.class.getResource("/images/profile.png"));
		Image profileImage = profileImageIcon.getImage(); // transform it 
		Image newProfileImage = profileImage.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		profileImageIcon = new ImageIcon(newProfileImage); // transform it back
		btnProfile.setIcon(profileImageIcon);
		btnProfile.setMargin(new Insets(0, 0, 0, 0));
		btnProfile.setBorder(null);
		btnProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Profile(Dashboard.this);
			}
		});
		btnProfile.setBounds(22, 37, 65, 65);
		buttonPanel.add(btnProfile);

		btnHome = new JButton();
		ImageIcon homeImageIcon = new ImageIcon(Dashboard.class.getResource("/images/home.png"));
		Image homeImage = homeImageIcon.getImage(); // transform it 
		Image newHomeImage = homeImage.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		homeImageIcon = new ImageIcon(newHomeImage); // transform it back
		btnHome.setIcon(homeImageIcon);
		btnHome.setMargin(new Insets(0, 0, 0, 0));
		btnHome.setBorder(null);
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Home(Dashboard.this);
			}
		});
		btnHome.setBounds(22, 170, 65, 65);
		buttonPanel.add(btnHome);
		
		btnTweet = new JButton();
		ImageIcon tweetImageIcon = new ImageIcon(Dashboard.class.getResource("/images/write.png"));
		Image tweetImage = tweetImageIcon.getImage(); // transform it 
		Image newTweetImage = tweetImage.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		tweetImageIcon = new ImageIcon(newTweetImage); // transform it back
		btnTweet.setIcon(tweetImageIcon);
		btnTweet.setMargin(new Insets(0, 0, 0, 0));
		btnTweet.setBorder(null);
		btnTweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tweet(Dashboard.this);
				new Post();
			}
		});
		btnTweet.setBounds(22, 300, 65, 65);
		buttonPanel.add(btnTweet);

		btnBookmarks = new JButton();
		ImageIcon bmkImageIcon = new ImageIcon(Dashboard.class.getResource("/images/bookmark.png"));
		Image bmkImage = bmkImageIcon.getImage(); // transform it 
		Image newBmkImage = bmkImage.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		bmkImageIcon = new ImageIcon(newBmkImage); // transform it back
		btnBookmarks.setIcon(bmkImageIcon);
		btnBookmarks.setMargin(new Insets(0, 0, 0, 0));
		btnBookmarks.setBorder(null);
		btnBookmarks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Bookmarks(Dashboard.this);
			}
		});
		btnBookmarks.setBounds(22, 440, 65, 65);
		buttonPanel.add(btnBookmarks);

		btnMessages = new JButton();
		ImageIcon msgImageIcon = new ImageIcon(Dashboard.class.getResource("/images/message.png"));
		Image msgImage = msgImageIcon.getImage(); // transform it 
		Image newMsgImage = msgImage.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		msgImageIcon = new ImageIcon(newMsgImage); // transform it back
		btnMessages.setIcon(msgImageIcon);
		btnMessages.setMargin(new Insets(0, 0, 0, 0));
		btnMessages.setBorder(null);
		btnMessages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Messages(Dashboard.this);
			}
		});
		btnMessages.setBounds(22, 571, 65, 65);
		buttonPanel.add(btnMessages);

		// TODO: fix dashboard not showing error after logout -> sign up -> cancel -> login
		btnLogout = new JButton();
		ImageIcon logoutImageIcon = new ImageIcon(Dashboard.class.getResource("/images/logout.png"));
		Image logoutImage = logoutImageIcon.getImage(); // transform it 
		Image newLogoutImage = logoutImage.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		logoutImageIcon = new ImageIcon(newLogoutImage); // transform it back
		btnLogout.setIcon(logoutImageIcon);
		btnLogout.setMargin(new Insets(0, 0, 0, 0));
		btnLogout.setBorder(null);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// logout success
				dispose();

				Login subLogin = new Login(Dashboard.this);
				User user = subLogin.user;

				// new login success
				if (user != null) {
					lbWelcomeUser.setText("Hello! " + user.name);
					setLocationRelativeTo(null);
					setVisible(true);
				}
				// new login failed
				else {
					dispose();
				}
			}
		});
		btnLogout.setBounds(22, 707, 65, 65);
		buttonPanel.add(btnLogout);
		setSize(650, 850);
		setResizable(false);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Connect to Database.
	 */
	private boolean connectToDB() {
		boolean hasRegisteredUsers = false;

		final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			// first, connect to MYSQL server and create the database if not created
			Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);

			Statement stmt = conn.createStatement();
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS twitter_3rd");

			stmt.close();
			conn.close();

			// second, connect to the database and create the table if user table does not
			// exist
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS user (" + "usr_id VARCHAR(15) NOT NULL,"
					+ "usr_pwd VARCHAR(15) NOT NULL," + "usr_name VARCHAR(45) NOT NULL,"
					+ "usr_email VARCHAR(45) NOT NULL," + "usr_phone VARCHAR(30) NOT NULL,"
					+ "usr_birthdate DATETIME NOT NULL," + "PRIMARY KEY (usr_id))" + "ENGINE = InnoDB";
			stmt.executeUpdate(sql);

			// check if we have users in the user table
			stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM user");

			if (resultSet.next()) {
				int numUsers = resultSet.getInt(1);
				if (numUsers > 0) {
					hasRegisteredUsers = true;
				}
			}

			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hasRegisteredUsers;
	}
}
