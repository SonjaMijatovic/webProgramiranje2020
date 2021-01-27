package beans;

import java.util.ArrayList;
import java.util.UUID;
import java.sql.Date;

public class Reservation {

	public enum Status {
		CREATED, REJECTED, CANCELED, ACCEPTED, COMPLETED;
	}

	private String id = UUID.randomUUID().toString();
	private String apartmentId;
	private long startDate;
	private int nights;
	private double totalPrice;
	private String message;
	private String guestId;
	private Status status;
	private ArrayList<Date> reservedDates;

	public Reservation() {
		super();
	}

	public Reservation(String id, String apartmentId, long startDate, int nights, double totalPrice, String message,
			String guestId, Status status) {
		super();
		this.id = id;
		this.apartmentId = apartmentId;
		this.startDate = startDate;
		this.nights = nights;
		this.totalPrice = totalPrice;
		this.message = message;
		this.guestId = guestId;
		this.status = status;
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

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public int getNights() {
		return nights;
	}

	public void setNights(int nights) {
		this.nights = nights;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ArrayList<Date> getReservedDates() {
		return reservedDates;
	}

	public void setReservedDates(ArrayList<Date> datumiRezervacije) {
		this.reservedDates = datumiRezervacije;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", apartmentId=" + apartmentId + ", startDate=" + startDate + ", nights="
				+ nights + ", totalPrice=" + totalPrice + ", message=" + message + ", guestId=" + guestId + ", status="
				+ status + ", datumiRezervacije=" + reservedDates + "]";
	}
}
