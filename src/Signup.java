
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Signup extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JLabel lbWelcomeMsg;

	private JTextField tfID;
	private JPasswordField pfPwd;
	private JPasswordField pfCnfPwd;
	private JTextField tfName;
	private JTextField tfEmail;
	private JTextField tfPhone;
	private JTextField tfBirthdate;

	private JButton btnSignup;
	private JButton btnCancel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Signup dialogSignup = new Signup(null);

			User user = dialogSignup.user;

			if (user != null) {
				System.out.println("Sign up success!");
				System.out.println("ID: " + user.id);
				System.out.println("Name: " + user.name);
				System.out.println("Email: " + user.email);
				System.out.println("Phone: " + user.phone);
			} else {
				System.out.println("Sign up canceled.");
			}

			dialogSignup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the Signup dialog.
	 */
	public Signup(JFrame parent) {
		super(parent);
		setTitle("Sign up");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setModal(true);
		contentPanel.setLayout(null);

		{
			lbWelcomeMsg = new JLabel("New to Twitter?");
			lbWelcomeMsg.setHorizontalAlignment(SwingConstants.CENTER);
			lbWelcomeMsg.setBounds(6, 26, 488, 33);
			contentPanel.add(lbWelcomeMsg);
		}
		{
			JLabel lbID = new JLabel("ID");
			lbID.setBounds(6, 223, 203, 33);
			contentPanel.add(lbID);
		}
		{
			JLabel lbPwd = new JLabel("Password");
			lbPwd.setBounds(6, 286, 203, 33);
			contentPanel.add(lbPwd);
		}
		{
			JLabel lbCnfPwd = new JLabel("Confirm Password");
			lbCnfPwd.setBounds(6, 351, 203, 33);
			contentPanel.add(lbCnfPwd);
		}
		{
			JLabel lbName = new JLabel("Name");
			lbName.setBounds(6, 427, 203, 33);
			contentPanel.add(lbName);
		}
		{
			JLabel lbEmail = new JLabel("Email");
			lbEmail.setBounds(6, 488, 203, 33);
			contentPanel.add(lbEmail);
		}
		{
			JLabel lbPhone = new JLabel("Phone");
			lbPhone.setBounds(6, 562, 203, 33);
			contentPanel.add(lbPhone);
		}
		{
			JLabel lbBirthdate = new JLabel("Birthdate");
			lbBirthdate.setBounds(6, 634, 203, 33);
			contentPanel.add(lbBirthdate);
		}
		{
			tfID = new JTextField();
			tfID.setBounds(264, 223, 230, 33);
			contentPanel.add(tfID);
			tfID.setColumns(10);
		}
		{
			pfPwd = new JPasswordField();
			pfPwd.setBounds(264, 289, 230, 30);
			contentPanel.add(pfPwd);
		}
		{
			pfCnfPwd = new JPasswordField();
			pfCnfPwd.setBounds(264, 351, 230, 33);
			contentPanel.add(pfCnfPwd);
		}
		{
			tfName = new JTextField();
			tfName.setBounds(264, 427, 230, 33);
			contentPanel.add(tfName);
			tfName.setColumns(10);
		}
		{
			tfEmail = new JTextField();
			tfEmail.setBounds(264, 488, 230, 33);
			contentPanel.add(tfEmail);
			tfEmail.setColumns(10);
		}
		{
			tfPhone = new JTextField();
			tfPhone.setBounds(264, 565, 230, 30);
			contentPanel.add(tfPhone);
			tfPhone.setColumns(10);
		}
		{
			tfBirthdate = new JTextField();
			tfBirthdate.setBounds(264, 634, 230, 33);
			contentPanel.add(tfBirthdate);
			tfBirthdate.setColumns(10);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnSignup = new JButton("Sign up");
				btnSignup.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						signupUser();
						dispose();
						new Login(parent);
					}
				});
				buttonPane.add(btnSignup);
				getRootPane().setDefaultButton(btnSignup);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						new Login(parent);
					}
				});
				buttonPane.add(btnCancel);
			}
		}

		setSize(500, 850);
		setLocationRelativeTo(parent);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Sign up user.
	 */
	private void signupUser() {
		String id = tfID.getText(); // usr_id
		String pwd = String.valueOf(pfPwd.getPassword()); // usr_pwd
		String confirmPwd = String.valueOf(pfCnfPwd.getPassword()); // usr_pwd
		String name = tfName.getText(); // usr_name
		String email = tfEmail.getText(); // usr_email
		String phone = tfPhone.getText(); // usr_phone
		String birthdate = tfBirthdate.getText(); // usr_birthdate

		if (id.isEmpty() || pwd.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()
				|| birthdate.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter all fields.", "Sign up Failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!pwd.equals(confirmPwd)) {
			JOptionPane.showMessageDialog(this, "Confirm password does not match.", "Sign up Failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		user = addUserToDB(id, pwd, name, email, phone, birthdate);
		if (user != null) {
			JOptionPane.showMessageDialog(this, "Registered a new user.", "Sign up Success",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Failed to register new user.", "Sign up Failed",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Add user to database.
	 */
	public User user;

	private User addUserToDB(String id, String pwd, String name, String email, String phone, String birthdate) {
		User user = null;

		final String DB_URL = "jdbc:mysql://localhost/twitter_3rd";
		final String USERNAME = "root";
		final String PASSWORD = "msNjs0330";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			// connected to database successfully...

			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO user(usr_id, usr_pwd, usr_name, usr_email, usr_phone, usr_birthdate)"
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, pwd);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, email);
			preparedStatement.setString(5, phone);
			preparedStatement.setString(6, birthdate);

			// insert row into the user table
			int addedRows = preparedStatement.executeUpdate();
			if (addedRows > 0) {
				user = new User();
				user.id = id;
				user.pwd = pwd;
				user.name = name;
				user.email = email;
				user.phone = phone;
				user.birthdate = birthdate;
			}

			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}
}
