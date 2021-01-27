package beans;

public class Review {

	private int id;
	private String guestId;
	private String apartmentId;
	private String text;
	private int rate;
	private boolean visible;

	public Review() {
		super();
	}

	public Review(int id, String guestId, String apartmentId, String text, int rate, boolean visible) {
		super();
		this.id = id;
		this.guestId = guestId;
		this.apartmentId = apartmentId;
		this.text = text;
		this.rate = rate;
		this.visible = visible;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public String getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}

