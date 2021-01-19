package beans;

import java.util.ArrayList;
import java.util.Collection;


public class Apartment {

	public enum Type {
		ENTIRE_PLACE, ROOM
	}
	
	public enum ActiveStatus {
		ACTIVE, INACTIVE
	}

	private int id;
	private Type type;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private User host;
	private String checkinTime;
	private String checkoutTime;
	private int price;
	private ActiveStatus status;
	private ArrayList<String> images;
	private ArrayList<Amenity> amenities;

	private Collection<Review> reviews;
	private Collection<Reservation> reservations;

	private boolean deleted;

	public Apartment() {
		super();
	}

	public Apartment(int id, Type type, int numberOfRooms, int numberOfGuests, Location location, User host,
			String checkinTime, String checkoutTime, int price, ActiveStatus status, ArrayList<String> images,
			ArrayList<Amenity> amenities, Collection<Review> reviews, Collection<Reservation> reservations) {
		super();
		this.id = id;
		this.type = type;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.location = location;
		this.host = host;
		this.checkinTime = checkinTime;
		this.checkoutTime = checkoutTime;
		this.price = price;
		this.status = status;
		this.images = images;
		this.amenities = amenities;
		this.reviews = reviews;
		this.reservations = reservations;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
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

	public Collection<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
