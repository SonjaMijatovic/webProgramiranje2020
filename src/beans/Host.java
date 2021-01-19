package beans;

import java.util.ArrayList;

public class Host extends User {

	private ArrayList<Apartment> apartments = new ArrayList<Apartment>();
	
	public Host() {
		super();
	}
	
	public Host(User user) {
		super();
	}
	
	public Host(String username, String password) {
		super(username, password);
	}
	
	public Host(String username, String password, String name, String lastname, String gender) {
		super(username, password, name, lastname, gender, Role.HOST);
	}

	public ArrayList<Apartment> getApartments() {
		return apartments; 			
	}
	
	
	public void setApartments(ArrayList<Apartment> apartments) {
		this.apartments = apartments;
	}
	
	public static Host Parse(User user) {
		Host host = new Host(user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getGender());
		return host;
	}

	public void addApartment(Apartment apartment) {
		apartments.add(apartment);
	}
	
	public Apartment getApartmentById(int id) {
		Apartment selectedApartment = null;
		
		for(Apartment apartment : apartments) {
			if(apartment.getId() == id) {
				selectedApartment = apartment;
				break;
			}
		}
		return selectedApartment;
	}
	
	public void updateApartment(Apartment newApartment, int appartmentId) {
		for(Apartment appartment : apartments) {
			if(appartment.getId() == appartmentId) {
				apartments.remove(appartment);
				apartments.add(newApartment);
				break;
			}
		}
	}
	
}
