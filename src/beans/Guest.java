package beans;

import java.util.ArrayList;

import beans.Reservation.Status;


public class Guest extends User {

	private ArrayList<Apartment> rentedApartment;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();
	
	public Guest() {
		super();
	}
	
	public Guest(String username, String password) {
		super(username, username);
	}
	
	public Guest(String username, String password, String name, String lastName, String gender) {
		super(username, password, name, lastName, gender, Role.GUEST);
	}
	
	public Guest(User user) {
		super(user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getGender(), Role.GUEST);		
	}

	public ArrayList<Apartment> getRentedApartment() {
		return rentedApartment;
	}

	public void setRentedApartment(ArrayList<Apartment> rentedApartment) {
		this.rentedApartment = rentedApartment;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public void addReservationToGuest(Reservation reservation) {
		reservations.add(reservation);
		System.out.println("Reservation added to the guest's list");
	}
	
	public void cancelUserReservation(int id) {
		for(Reservation reservation : reservations) {
			if(reservation.getId() == id) {
				reservation.setStatus(Status.CANCELED);
			}
		}
	}
}
