package beans;

public class Amenity {

	public enum Type {
		BASIC, FACILITIES, DINING, FAMILY, OUTDOOR
	}

	private int id;
	private String name;
	private Type type;

	private boolean deleted = false;

	public Amenity() {
		super();
	}

	public Amenity(int id, String name, Type type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
