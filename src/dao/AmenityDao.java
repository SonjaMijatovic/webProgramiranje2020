package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Address;
import beans.Amenity;
import beans.Location;

public class AmenityDao {

	private ArrayList<Amenity> amenities = new ArrayList<Amenity>();
	private String ctxPath;
	Path currentDir = Paths.get(".");
	String path = currentDir.toAbsolutePath().toString();
	
	public AmenityDao(String ctx) {
		super();
		ctxPath = ctx;
		try {
			loadAmenities();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addAmenity(Amenity amenity) {
		this.amenities.add(amenity);
	}
	
	public ArrayList<Amenity> getAllAmenities(){
		return amenities;
	}
	
	public void removeAmenityFromApartment(int id) {
		for(Amenity amenity : amenities) {
			if(amenity.getId() == id) {
				amenity.setDeleted(true);
				break;
			}
		}
	}
	
	// TODO treba da radi sa ID-jem aprtmana, izmeni u apartmentDao kad je ovde reseno
	public Amenity getApartmentAmenityById(int id) {
		for(Amenity amenity : amenities) {
			if(amenity.getId() == id) {
				return amenity;
			}
		}
		return null;
	}
	
	public void loadAmenities() throws FileNotFoundException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		File amenitiesFile = new File(this.ctxPath + "data"+ java.io.File.separator +"amenities.json");
		String json = ""; 
		String temp;
		try(BufferedReader br = new BufferedReader(new FileReader(amenitiesFile))){
			while ((temp = br.readLine()) != null) {
				json += temp;
			}
		}
		
		List<Amenity> amenityList = mapper.readValue(json, 
			    new TypeReference<ArrayList<Amenity>>() {});
		
		this.amenities.clear();
		for(Amenity amenity : amenityList) {
			this.amenities.add(amenity);
		}
	}
	
	public void saveApartmentAmenities() {
		ObjectMapper mapper = new ObjectMapper();
		File amenitiesFile = new File(this.ctxPath + "data"+ java.io.File.separator +"amenities.json");

		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(amenitiesFile, this.amenities);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 public ArrayList<Amenity> getAmenitiesByApartmentId(String wantedApartmentId) {
		 	ArrayList<Amenity> apartmentAmenities = new ArrayList<>();
			BufferedReader in = null;
			try {
				File file = new File(path + "/web2020/WebContent/data/apartment-amenities.txt");
				in = new BufferedReader(new FileReader(file));
				String s;
				StringTokenizer st;
				while ((s = in.readLine()) != null) {
					s = s.trim();
					if (s.equals(""))
						continue;
					st = new StringTokenizer(s, ";");
					while (st.hasMoreTokens()) {
						String apartmentId = st.nextToken().trim();
						int amenityId = Integer.parseInt(st.nextToken());
						String amenityName = st.nextToken().trim();
						String amenityType = st.nextToken().trim();
						
						if (wantedApartmentId.equals(apartmentId)) {
							apartmentAmenities.add(new Amenity(amenityId, amenityName, Amenity.Type.valueOf(amenityType)));
						}
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
			return apartmentAmenities;
		}
}
