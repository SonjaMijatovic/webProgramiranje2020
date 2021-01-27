package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Amenity;

public class AmenityDao {

	private ArrayList<Amenity> amenities = new ArrayList<Amenity>();
	private String ctxPath;
	
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
//				amenity.setUklonjen(true);
//				amenities.remove(amenity);
//				TODO which approach
				break;
			}
		}
	}
	
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
}
