package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import beans.Amenity;
import beans.Apartment;
import beans.Apartment.ActiveStatus;
import beans.Location;

public class ApartmentDao {

	private ArrayList<Apartment> apartments = new ArrayList<Apartment>();
	Path currentDir = Paths.get(".");
	String path=currentDir.toAbsolutePath().toString();
	String contextPath;
	
	LocationDao locationDao = null;
	AmenityDao amenityDao = null;

	public ApartmentDao(String contextPath) {
		super();
		read(contextPath);
	}
	
	public ApartmentDao(String contextPath, LocationDao locationDao, AmenityDao amenityDao) {
		super();
		this.locationDao = locationDao;
		this.amenityDao = amenityDao;
		this.contextPath = contextPath;
		read(contextPath);
	}

//	public void addApartment(Apartment a) {
//		apartments.add(a);
//	}

	public ArrayList<Apartment> getAllApartments() {
		return apartments;
	}

	public void removeApartmentById(String id) {
		for (Apartment apartment : apartments) {
			if (apartment.getId().equals(id)) {
				apartment.setDeleted(true);
				apartment.setStatus(ActiveStatus.INACTIVE);
				break;
			}
		}
	}

	public Apartment getActiveApartmentById(String id) {
		ArrayList<Apartment> activeApartments = getActiveApartments();

		for (Apartment apartment : activeApartments) {
			if (apartment.getId().equals(id)) {
				return apartment;
			}
		}
		return null;
	}

	public Apartment getApartmentById(String id) {
		for (Apartment apartment : apartments) {
			if (apartment.getId().equals(id)) {
				return apartment;
			}
		}
		return null;
	}

	public ArrayList<Apartment> getActiveApartments() {
		ArrayList<Apartment> activeApartments = new ArrayList<Apartment>();

		for (Apartment apartment : apartments) {
			if (apartment.getStatus() == ActiveStatus.ACTIVE) {
				activeApartments.add(apartment);
			}
		}
		return activeApartments;
	}

	public ArrayList<Apartment> getApartmentsByType(Apartment.Type type, boolean onlyActive) {
		ArrayList<Apartment> apartmentsToCheck = new ArrayList<Apartment>();
		if (onlyActive) {
			apartmentsToCheck = getActiveApartments();
		} else {
			apartmentsToCheck = apartments;
		}

		switch (type) {
		case Room:
			ArrayList<Apartment> rooms = new ArrayList<Apartment>();
			for (Apartment apartment : apartmentsToCheck) {
				if (apartment.getType().equals(Apartment.Type.Room)) {
					rooms.add(apartment);
				}
			}
			return rooms;
		case Apartment:
			ArrayList<Apartment> entirePlaces = new ArrayList<Apartment>();
			for (Apartment apartment : apartmentsToCheck) {
				if (apartment.getType().equals(Apartment.Type.Apartment)) {
					entirePlaces.add(apartment);
				}
			}
			return entirePlaces;
		default:
			return null;

		}
	}

	public ArrayList<Apartment> getApartmentsByNumberOfRooms(int numberOfRooms, boolean onlyActive) {
		ArrayList<Apartment> apartmentsToCheck = new ArrayList<Apartment>();
		if (onlyActive) {
			apartmentsToCheck = getActiveApartments();
		} else {
			apartmentsToCheck = apartments;
		}

		ArrayList<Apartment> list = new ArrayList<Apartment>();

		for (Apartment apartment : apartmentsToCheck) {
			if (apartment.getNumberOfRooms() == numberOfRooms) {
				list.add(apartment);
			}
		}
		return list;
	}

	public ArrayList<Apartment> getApartmentsByNumberOfGuests(int numberOfGuests, boolean onlyActive) {
		ArrayList<Apartment> apartmentsToCheck = new ArrayList<Apartment>();
		if (onlyActive) {
			apartmentsToCheck = getActiveApartments();
		} else {
			apartmentsToCheck = apartments;
		}

		ArrayList<Apartment> list = new ArrayList<Apartment>();

		for (Apartment apartment : apartmentsToCheck) {
			if (apartment.getNumberOfGuests() == numberOfGuests) {
				list.add(apartment);
			}
		}
		return list;
	}
//
//	public void loadApartments() throws FileNotFoundException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		File apartmentsFile = new File(this.ctxPath + "data" + java.io.File.separator + "apartments.json");
//		String json = "";
//		String temp;
//		try (BufferedReader br = new BufferedReader(new FileReader(apartmentsFile))) {
//			while ((temp = br.readLine()) != null) {
//				json += temp;
//			}
//		}
//		List<Apartment> list = mapper.readValue(json, new TypeReference<ArrayList<Apartment>>() {});
//
//		this.apartments.clear();
//		for (Apartment apartment : list) {
//			this.apartments.add(apartment);
//		}
//	}

//	public void saveApartments() {
//		ObjectMapper mapper = new ObjectMapper();
//		File apartmentsFile = new File(this.ctxPath + "data" + java.io.File.separator + "apartments.json");
//		try {
//			mapper.writerWithDefaultPrettyPrinter().writeValue(apartmentsFile, this.apartments);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	 public void read(String contextPath) {
			BufferedReader in = null;
			try {
				File file = new File(path + "/web2020/WebContent/data/apartments.txt");
				in = new BufferedReader(new FileReader(file));
				String s;
				StringTokenizer st;
				this.apartments.clear();

				while ((s = in.readLine()) != null) {
					s = s.trim();
					if (s.equals(""))
						continue;
					st = new StringTokenizer(s, ";");
					
					while (st.hasMoreTokens()) {
						String id= st.nextToken();
						System.out.println("ID apartmana " + id);
						Apartment.Type type = Apartment.Type.valueOf(st.nextToken());
						int numberOfRooms = Integer.parseInt(st.nextToken());
						int numberOfGuests = Integer.parseInt(st.nextToken());
						Location location = locationDao.getById(st.nextToken());
						long to = Long.parseLong(st.nextToken());
						long from = Long.parseLong(st.nextToken());
						String hostUsername = st.nextToken();
						String checkinTime = st.nextToken();
						String checkoutTime = st.nextToken();
						int price = Integer.parseInt( st.nextToken());
						Apartment.ActiveStatus status = Apartment.ActiveStatus.valueOf(st.nextToken());
//						String image = st.nextToken();
//						ArrayList<Amenity> amenities = amenityDao.findAllByApartmentId(contextPath, id);
						
						this.apartments.add(new Apartment(id, type, numberOfRooms, numberOfGuests, location,
								to, from, hostUsername, checkinTime, checkoutTime, price, status, null));
						
						System.out.println(id);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) { }
				}
			}
		}
	 
	 public Apartment save(Apartment apartment) {
		 	apartment.setId(UUID.randomUUID().toString());
	    	apartment.setStatus(Apartment.ActiveStatus.INACTIVE);
	    	
	    	String s = apartment.getId() + ";" +
	    			apartment.getType()+ ";" +
	    			apartment.getNumberOfRooms()+ ";" +
	    			apartment.getNumberOfGuests()+ ";" +
	    			apartment.getLocation().getId().toString()+ ";" +
	    			apartment.getTo()+ ";" +
	    			apartment.getFrom()+ ";" +
	    			apartment.getHostUsername()+ ";" +
	    			apartment.getCheckinTime()+ ";" +
	    			apartment.getCheckoutTime()+ ";" +
	    			apartment.getPrice()+ ";" +
	    			apartment.getStatus()+ ";" +
	    			apartment.getImage();   			
	    	
			BufferedWriter writer = null;
			try {
				File file = new File(path + "/web2020/WebContent/data/apartments.txt");
				System.out.println("file for apartments: "+file.getAbsolutePath());
				
				writer = new BufferedWriter(new FileWriter(file, true));
				PrintWriter out = new PrintWriter(writer);
				out.println(s);
				out.close();
//				for (Amenity amenity : apartment.getAmenities()) {
//					String line2 = apartment.getId() + "," + amenity.getId();
//					System.out.println(line2);
//
//					File file2 = new File(path + "/web-2020/WebRest/apartment-amenities.txt");
//					BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2, true));
//					PrintWriter out2 = new PrintWriter(writer2);
//					out2.println(line2);
//					out2.close();
//				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (Exception e) {
					}
				}
			}
	        apartments.add(apartment);
			return apartment;
	    }
	 
	 public Apartment update(Apartment apartment) {
		 boolean match=false;
	    	try {
				File file = new File(path + "/web2020/WebContent/data/apartments.txt");
				File tempFile = new File(path + "/web2020/WebContent/data/apartmentsTemp.txt");
				BufferedReader in = new BufferedReader(new FileReader(file));
				BufferedWriter writer =null;
				writer = new BufferedWriter(new FileWriter(tempFile, true));
				
				String s = "", newS = "";
				StringTokenizer st;
				while ((s = in.readLine()) != null) {
					if (s.equals(""))
						continue;
					String[] tokens = s.split(",");
					String id= tokens[0];
					if (id.equals(apartment.getId())) {
						newS += apartment.getId() + ";" +
				    			apartment.getType()+ ";" +
				    			apartment.getNumberOfRooms()+ ";" +
				    			apartment.getNumberOfGuests()+ ";" +
				    			apartment.getLocation().getId().toString()+ ";" +
				    			apartment.getTo()+ ";" +
				    			apartment.getFrom()+ ";" +
				    			apartment.getHostUsername()+ ";" +
				    			apartment.getCheckinTime()+ ";" +
				    			apartment.getCheckoutTime()+ ";" +
				    			apartment.getPrice()+ ";" +
				    			apartment.getStatus()+ ";" +
				    			apartment.getImage(); 
						match=true;
					} else {
						newS += s + "\r\n";
					}
				}
			in.close();
			PrintWriter out = new PrintWriter(writer);
			out.println(newS);
			out.close();
			Files.copy(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	        tempFile.delete();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	    	if (match == true) {
		    	Apartment removeMe = null;
		    	for (Apartment l : apartments) {
		    	    if (apartment.getId().equals(l.getId())) {
		    	    	removeMe = l;
		    	    }
		    	}
		    	apartments.remove(removeMe);
		    	apartments.add(apartment);
//		    	amenityDao.updateApartmentAmenities(contextPath, apartment);
				read(contextPath);
		    	return apartment;
	    	}
	    	else
	    		return null;
	        
	    }
}
