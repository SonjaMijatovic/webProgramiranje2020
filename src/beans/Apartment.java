package beans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


public class Apartment {

	public enum Type {
		ENTIRE_PLACE, ROOM
	}
	
	public enum ActiveStatus {
		ACTIVE, INACTIVE
	}

	private String id = UUID.randomUUID().toString();
	private Type type;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private ArrayList<Date> datesToRent;
	private ArrayList<Date> availabilityPerDates;
	private String hostUsername;
	private String checkinTime;
	private String checkoutTime;
	private int price;
	private ActiveStatus status;
	private ArrayList<String> images;
	private ArrayList<Amenity> amenities;

	private Collection<Review> reviews;
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

	private boolean deleted;

	public Apartment() {
		super();
	}

	public Apartment(String id, Type type, int numberOfRooms, int numberOfGuests, Location location, String hostUsername,
			String checkinTime, String checkoutTime, int price, ActiveStatus status, ArrayList<String> images,
			ArrayList<Amenity> amenities, Collection<Review> reviews, ArrayList<Reservation> reservations,
			boolean deleted) {
		super();
		this.id = id;
		this.type = type;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.location = location;
		this.hostUsername = hostUsername;
		this.checkinTime = checkinTime;
		this.checkoutTime = checkoutTime;
		this.price = price;
		this.status = status;
		this.images = images;
		this.amenities = amenities;
		this.reviews = reviews;
		this.reservations = reservations;
		this.deleted = deleted;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getHostUsername() {
		return hostUsername;
	}

	public void setHostUsername(String hostUsername) {
		this.hostUsername = hostUsername;
	}

	public String getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(String checkinTime) {
		this.checkinTime = checkinTime;
	}

	public String getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(String checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ActiveStatus getStatus() {
		return status;
	}

	public void setStatus(ActiveStatus status) {
		this.status = status;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public ArrayList<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(ArrayList<Amenity> amenities) {
		this.amenities = amenities;
	}

	public Collection<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public ArrayList<Date> getDatesToRent() {
		return datesToRent;
	}

	public void setDatesToRent(ArrayList<Date> datesToRent) {
		this.datesToRent = datesToRent;
	}

	public ArrayList<Date> getAvailabilityPerDates() {
		return availabilityPerDates;
	}

	public void setAvailabilityPerDates(ArrayList<Date> availabilityPerDates) {
		this.availabilityPerDates = availabilityPerDates;
	}

	public void addReservation(Reservation reservation) {
		reservations.add(reservation);
	}
	
	public void setCancelation(String id) {
		for(Reservation reservation : reservations) {
			if(reservation.getId().equals(id)) {
				reservation.setStatus(Reservation.Status.CANCELED);
			}
		}
	}
	
	public void setCompleted(String id) {
		for(Reservation reservation : reservations) {
			if(reservation.getId().equals(id)) {
				reservation.setStatus(Reservation.Status.COMPLETED);
			}
		}
	}
	
	public void setAccepted(String id) {
		for(Reservation reservation : reservations) {
			if(reservation.getId().equals(id)) {
				reservation.setStatus(Reservation.Status.ACCEPTED);
			}
		}
	}
	
	public void addReview(Review review) {
		reviews.add(review);
	}

}
