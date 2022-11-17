
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Login extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	
	private JLabel lbWelcomeUser;
	private JLabel lbID;
	private JLabel lbPwd;
	
	private JTextField tfID;
	private JPasswordField pfPwd;
	
	private JButton btnLogin;
	private JButton btnSignup;
	private JButton btnQuit;

	public static String curUserId; // declare current user id 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Login dialogLogin = new Login(null);

			User user = dialogLogin.user;

			if (user != null) {
				System.out.println("Login success!");
				System.out.println("ID: " + user.id);
				System.out.println("Name: " + user.name);
				System.out.println("Email: " + user.email);
				System.out.println("Phone: " + user.phone);
			} else {
				System.out.println("Login canceled.");
			}

			dialogLogin.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialogLogin.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Login(JFrame parent) {
		super(parent);
		setTitle("Login");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setModal(true);
		contentPanel.setLayout(null);

		{
			lbWelcomeUser = new JLabel("Welcome Again!");
			lbWelcomeUser.setHorizontalAlignment(SwingConstants.CENTER);
			lbWelcomeUser.setBounds(6, 23, 488, 26);
			contentPanel.add(lbWelcomeUser);
		}

		lbID = new JLabel("ID");
		lbID.setBounds(6, 92, 488, 26);
		contentPanel.add(lbID);

		tfID = new JTextField();
		tfID.setBounds(6, 141, 488, 26);
		contentPanel.add(tfID);
		tfID.setColumns(10);

		lbPwd = new JLabel("Password");
		lbPwd.setBounds(6, 204, 488, 26);
		contentPanel.add(lbPwd);

		pfPwd = new JPasswordField();
		pfPwd.setBounds(6, 254, 488, 26);
		contentPanel.add(pfPwd);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			btnSignup = new JButton("Sign up");
			btnSignup.setBackground(new Color(0, 252, 255));
			btnSignup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					new Signup(parent);
				}
			});
			{
				btnLogin = new JButton("Login");
				btnLogin.setForeground(new Color(255, 255, 255));
				btnLogin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String id = tfID.getText();
						String pwd = String.valueOf(pfPwd.getPassword());

						user = authenticateUser(id, pwd);

						// save user id for other functions!!!
						if (user != null) {
							JOptionPane.showMessageDialog(Login.this, "Hello! " + user.id, "Login Success",
									JOptionPane.INFORMATION_MESSAGE);
							curUserId = user.id;
							System.out.println("current user = " + curUserId); // for debugging
							setVisible(true);

							dispose();
						} else {
							JOptionPane.showMessageDialog(Login.this, "ID or Password Invalid.", "Login Failed",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				buttonPane.add(btnLogin);
				getRootPane().setDefaultButton(btnLogin);
			}
			buttonPane.add(btnSignup);
			{
				btnQuit = new JButton("Quit");
				btnQuit.setBackground(new Color(255, 38, 0));
				btnQuit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				buttonPane.add(btnQuit);
			}
		}

		setSize(500, 850);
		setLocationRelativeTo(parent);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Authenticate the user.
	 */
	public User user;

	private User authenticateUser(String id, String pwd) {
		User user = null;

		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM user WHERE usr_id = ? AND usr_pwd = ?";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, pwd);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				user = new User();
				user.id = resultSet.getString("usr_id");
				user.pwd = resultSet.getString("usr_pwd");
				user.name = resultSet.getString("usr_name");
				user.email = resultSet.getString("usr_email");
				user.phone = resultSet.getString("usr_phone");
				user.birthdate = resultSet.getString("usr_birthdate");
			}

			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return user;
	}
}
