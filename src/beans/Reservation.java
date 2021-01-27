package beans;

import java.util.UUID;

public class Reservation {

	public enum Status {
		CREATED, REJECTED, CANCELED, ACCEPTED, COMPLETED;
	}

	private String id = UUID.randomUUID().toString();
	private String apartmentId;
	private int nights = 1;
	private int price;
	private String message;
	private int guestId;
	private Status status;

	private String confirmation;

	public Reservation() {
		super();
	}

	public Reservation(String id, String apartmentId, int nights, int price, String message, int guestId, Status status,
			String confirmation) {
		super();
		this.id = id;
		this.apartmentId = apartmentId;
		this.nights = nights;
		this.price = price;
		this.message = message;
		this.guestId = guestId;
		this.status = status;
		this.confirmation = confirmation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}

	public int getNights() {
		return nights;
	}

	public void setNights(int nights) {
		this.nights = nights;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getGuestId() {
		return guestId;
	}

	public void setGuestId(int guestId) {
		this.guestId = guestId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

}
