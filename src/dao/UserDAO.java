package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

import beans.User;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {
	
	private Map<String, User> users = new HashMap<>();

	public UserDAO() {
		super();
	}

	public UserDAO(String contextPath) {
		loadUsers(contextPath);
	}
	
	private void loadUsers(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/users.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstname = st.nextToken().trim();
					String lastname = st.nextToken().trim();
					String gender = st.nextToken().trim();
					String role = st.nextToken().trim();
					users.put(username,
							new User(username, password, firstname, lastname, gender, User.Role.valueOf(role)));
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) { }
			}
		}
	}
}
