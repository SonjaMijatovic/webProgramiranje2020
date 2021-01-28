package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Admin;
import beans.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserDao {

	private HashMap<String, User> users = new HashMap<String, User>();
	public static Admin mainAdmin;
	
	Path currentDir = Paths.get("");
	String path = currentDir.toAbsolutePath().toString();
	
	public UserDao(String path) {		
		read();

//		Admin admin = new Admin("admin","admin","Sonja","Brzak","female");
//		Admin admin2 = new Admin("admin2","admin2","Sinisa","Brzak","male");

//		save(admin);
//		save(admin2);
//		users.put(admin.getUsername(), admin);
//		mainAdmin = admin;		
	}
	

    public void read () {
		BufferedReader in = null;
		try {
			File file = new File(path + "/web2020/WebContent/data/users.txt");
			System.out.println("reading from file path" + file.getAbsolutePath());
			in = new BufferedReader(new FileReader(file));
			String s;
			StringTokenizer st;
			while ((s = in.readLine()) != null) {
				s = s.trim();
				if (s.equals(""))
					continue;
				st = new StringTokenizer(s, ";");
				while (st.hasMoreTokens()) {
					String username = st.nextToken();
					String password = st.nextToken();
					String firstname = st.nextToken();
					String lastname = st.nextToken();
					String gender = st.nextToken();
					String role = st.nextToken();
					System.out.println("user role " + User.Role.valueOf(role));
					users.put(username, new User(username, password, firstname, lastname, gender,  User.Role.valueOf(role)));
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
	
	public void addUser(User user) {
		users.put(user.getUsername(), user);
		System.out.println(user.getUsername() + " added to the user list");
	}
	
	public boolean doesUsernameAlreadyExists(String username) {
		for (User user : getAllUsers()) {
			System.out.println(user.getUsername());
		}
		return users.containsKey(username);
	}
	
	public boolean doesUserAlreadyExists(String username, String password) {
		for(User user : users.values()) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
	
	public User getUserByUsername(String username) {
		return users.get(username);	
	}
	
	public ArrayList<User> getAllUsers(){
		ArrayList<User> list = new ArrayList<User>();
		for(User user : users.values()) {
			list.add(user);
		}
		return list;
	}

	public User save(User user) {
		String line = user.getUsername() + ";"
				+ user.getPassword() + ";"
				+ user.getFirstname() + ";"
				+ user.getLastname() + ";"
				+ user.getGender() + ";"
				+ user.getRole();
		
		System.out.println(line);
		BufferedWriter writer = null;
		try {
			File file = new File(path + "/web2020/WebContent/data/users.txt");
			System.out.println("file path" + file.getAbsolutePath());
			writer = new BufferedWriter(new FileWriter(file, true));
			PrintWriter out = new PrintWriter(writer);
			out.println(line);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					return null;
				}
			}
		}
		users.put(user.getUsername(), user);
		return user;
	}
	
    public User update(User user) {
		boolean match=false;
    	try {
		    File file = new File(path + "/web2020/WebContent/data/users.txt");
		    File tempFile = new File(path + "/web2020/WebContent/data/usersTemp.txt");
			BufferedReader in = new BufferedReader(new FileReader(file));
			BufferedWriter writer =null;
			writer = new BufferedWriter(new FileWriter(tempFile, true));
			
			String s = "", newS = "";
			StringTokenizer st;
			while ((s = in.readLine()) != null) {
				if (s.equals(""))
					continue;
				String[] tokens = s.split(";");
				String username= tokens[0];
				if (user.getUsername().equals(username)) {
					newS += user.getUsername() + ";" +
			    			user.getPassword() + ";" +
			    			user.getFirstname() + ";" +
			    			user.getLastname() + ";" +
			    			user.getGender() + ";" +
			    			user.getRole() + "\r\n";
					match=true;
				} else {
					newS += s + "\r\n";
				}
			}
		in.close();
		PrintWriter out = new PrintWriter(writer);
		out.println(newS);
		System.out.println("novi string:" + newS);
		out.close();
		Files.copy(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        tempFile.delete();
        
	} catch (Exception ex) {
		ex.printStackTrace();
	}
    	if (match==true) {
	    	User userToRemove = null;
	    	for (User u : users.values()) {
	    	    if (user.getUsername().equals(u.getUsername())) {
	    	    	userToRemove = u;
	    	    }
	    	}
    	    users.remove(userToRemove);
	    	users.put(user.getUsername(), user);
	    	return user;
    	}
    	else
    		return null;
    }
}
