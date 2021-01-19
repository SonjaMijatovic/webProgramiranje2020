package beans;

public class Admin extends User {

	public Admin() {
		super();
	}
	
	public Admin(String username, String password) {
		super(username, password);
	}
	
	public Admin(String username, String password, String name, String lastname, String gender) {
		super(username, password, name, lastname, gender, Role.ADMIN);
	}
}
