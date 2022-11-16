
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.Font;

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

	String cur_user_id = Login.cur_user_id; // get current user id from Login class

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
	 * Create the frame.
	 */
	public Dashboard() {
		System.out.println("this is a test.");
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
		lbWelcomeUser.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lbWelcomeUser.setHorizontalAlignment(SwingConstants.CENTER);
		lbWelcomeUser.setBounds(6, 6, 488, 46);
		contentPanel.add(lbWelcomeUser);
		contentPanel.setSize(500, 811);

		boolean hasRegisteredUsers = connectToDatabase();

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

		btnProfile = new JButton("Profile");
		btnProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Profile(Dashboard.this);
			}
		});
		btnProfile.setBounds(-1, 63, 117, 29);
		buttonPanel.add(btnProfile);

		btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Home(Dashboard.this);
			}
		});
		btnHome.setBounds(-1, 183, 117, 29);
		buttonPanel.add(btnHome);

		btnTweet = new JButton("Tweet");
		btnTweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Tweet(Dashboard.this);
				new Post();
			}
		});
		btnTweet.setBounds(-1, 311, 117, 29);
		buttonPanel.add(btnTweet);

		btnBookmarks = new JButton("Bookmarks");
		btnBookmarks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Bookmarks(Dashboard.this);
			}
		});
		btnBookmarks.setBounds(-1, 453, 117, 29);
		buttonPanel.add(btnBookmarks);

		btnMessages = new JButton("Messages");
		btnMessages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Messages(Dashboard.this);
			}
		});
		btnMessages.setBounds(-1, 585, 117, 29);
		buttonPanel.add(btnMessages);

		// TODO: fix dashboard not showing error
		// after logout -> sign up -> cancel -> login
		btnLogout = new JButton("Logout");
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
		btnLogout.setBounds(-1, 716, 117, 29);
		buttonPanel.add(btnLogout);
		setSize(650, 850);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Connect to Database.
	 */
	private boolean connectToDatabase() {
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
