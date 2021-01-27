package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Admin;
import beans.Host;
import beans.Guest;
import beans.User;
import beans.User.Role;

public class UserDao {

	private HashMap<String, User> users = new HashMap<String, User>();
	public static Admin mainAdmin;
	private String contextPath;
	
	public UserDao(String path) {		
		contextPath = path;
		try {
			loadUsers();
		}catch(IOException e) {
			e.printStackTrace();
		}

		Admin admin = new Admin("admin","admin","Sonja","Brzak","zenski");
		users.put(admin.getUsername(), admin);
		mainAdmin = admin;		
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
	
	public void updateUser(User user) {
		users.put(user.getUsername(), user);
	}
	
	private void loadUsers() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		File adminFile = new File(this.contextPath + "data" + java.io.File.separator + "admins.json");
		File hostsFile = new File(this.contextPath + "data" + java.io.File.separator + "hosts.json");
		File guestsFile = new File(this.contextPath + "data" + java.io.File.separator + "guests.json");
				
		String json = ""; 
		String temp;
		
		if(adminFile.exists()) {
			try(BufferedReader br = new BufferedReader(new FileReader(adminFile))){
				while ((temp = br.readLine()) != null) {
					json += temp;
				}
			}	
			
			List<Admin> admins = mapper.readValue(json, new TypeReference<ArrayList<Admin>>() {});
			
			users.clear();
			for(Admin admin : admins) {
				users.put(admin.getUsername(), admin);
				System.out.println(admin.getUsername() + " is loaded");
			}
		}
		
		json = "";
		if(hostsFile.exists()) {
			try(BufferedReader br = new BufferedReader(new FileReader(hostsFile))) { 
				while ((temp = br.readLine()) != null) {
					json += temp;
				}
			}
			
			ArrayList<Host> hosts = mapper.readValue(json, new TypeReference<ArrayList<Host>>() {});
			
			for(Host host : hosts) {
				users.put(host.getUsername(), host);
				System.out.println(host.getUsername() + " is loaded");
			}
				
		}
		
		json = "";		
		if(guestsFile.exists()) {
			try(BufferedReader br = new BufferedReader(new FileReader(guestsFile))) { 
				while ((temp = br.readLine()) != null) {
					json += temp;
				}
			}
			
			ArrayList<Guest> guests = mapper.readValue(json, new TypeReference<ArrayList<Guest>>() {});
			
			for(Guest guest : guests) {
				users.put(guest.getUsername(), guest);
				System.out.println(guest.getUsername() + " is loaded");
			}
				
		}
	}
	
	public void saveUsers() {
		ObjectMapper mapper = new ObjectMapper();
		File hostsFile = new File(this.contextPath + "data" + java.io.File.separator + "hosts.json");
		File guestsFile = new File(this.contextPath + "data" + java.io.File.separator + "guests.json");
		
		ArrayList<Guest> guests = new ArrayList<Guest>();
		for (User user: users.values()) {
			if (user.getRole().equals(Role.GUEST)) {
				guests.add((Guest)user );
			}
		}
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(guestsFile, guests);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<Host> hosts = new ArrayList<Host>();		
		for (User user: users.values()) {
			if (user.getRole().equals(Role.HOST)) {
				hosts.add((Host)user);
			}
		}
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(hostsFile, hosts);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
