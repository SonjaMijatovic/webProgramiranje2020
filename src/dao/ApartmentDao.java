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

import beans.Apartment;
import beans.Apartment.ActiveStatus;

public class ApartmentDao {
	
	private ArrayList<Apartment> apartments = new ArrayList<Apartment>();
	private String ctxPath;
	private File apartmentsFile;
	
	public ApartmentDao(String ctx) {
		super();
		ctxPath = ctx;
		try {
			loadApartments();
		} catch (IOException e) {
			e.printStackTrace();
		}
		apartmentsFile = new File(this.ctxPath + "data"+ java.io.File.separator +"apartments.json");
	}
	
	public void addApartment(Apartment a) {
		apartments.add(a);
	}
	
	public ArrayList<Apartment> getAllApartments() {
		return apartments;
	}
	
	public void removeApartmentById(String id) {
		for(Apartment apartment : apartments) {
			if(apartment.getId().equals(id)) {
//				apartment.setRemoved(true);
				apartment.setStatus(ActiveStatus.INACTIVE);
				break;
			}
		}
	}
	
	public Apartment getActiveApartmentById(String id) {
		ArrayList<Apartment> activeApartments = getActiveApartments();
		
		for(Apartment apartment : activeApartments) {
			if(apartment.getId().equals(id)) {
				return apartment;
			}
		}
		return null;
	}
	
	public Apartment getApartmentById(String id) {
		for(Apartment apartment : apartments) {
			if(apartment.getId().equals(id)) {
				return apartment;
			}
		}
		return null;
	}
	
	public ArrayList<Apartment> getActiveApartments() {
		ArrayList<Apartment> activeApartments = new ArrayList<Apartment>();
		
		for(Apartment apartment : apartments) {
			if(apartment.getStatus() == ActiveStatus.ACTIVE) {
				activeApartments.add(apartment);
			}
		}
		return activeApartments;
	}
	
	public ArrayList<Apartment> getApartmentsByType(Apartment.Type type, boolean onlyActive){
		ArrayList<Apartment> apartmentsToCheck = new ArrayList<Apartment>();
		if (onlyActive) {
			apartmentsToCheck = getActiveApartments();
		} else {
			apartmentsToCheck = apartments;
		}
		
		
		switch(type) {
			case ROOM:
				ArrayList<Apartment> rooms = new ArrayList<Apartment>();
				for(Apartment apartment : apartmentsToCheck) {
					if(apartment.getType().equals(Apartment.Type.ROOM)) {
						rooms.add(apartment);
					}
				}
				return rooms;
			case ENTIRE_PLACE:
				ArrayList<Apartment> entirePlaces = new ArrayList<Apartment>();
				for(Apartment apartment : apartmentsToCheck) {
					if(apartment.getType().equals(Apartment.Type.ENTIRE_PLACE)) {
						entirePlaces.add(apartment);
					}
				}
				return entirePlaces;
			default:
				return null;
			
		}
	}
	
	public ArrayList<Apartment> getApartmentsByNumberOfRooms(int numberOfRooms, boolean onlyActive){
		ArrayList<Apartment> apartmentsToCheck = new ArrayList<Apartment>();
		if (onlyActive) {
			apartmentsToCheck = getActiveApartments();
		} else {
			apartmentsToCheck = apartments;
		}
		
		ArrayList<Apartment> list = new ArrayList<Apartment>();
		
		for(Apartment apartment : apartmentsToCheck) {
			if(apartment.getNumberOfRooms() == numberOfRooms) {
				list.add(apartment);
			}
		}
		return list;
	}
	
	public ArrayList<Apartment> getApartmentsByNumberOfGuests(int numberOfGuests, boolean onlyActive){
		ArrayList<Apartment> apartmentsToCheck = new ArrayList<Apartment>();
		if (onlyActive) {
			apartmentsToCheck = getActiveApartments();
		} else {
			apartmentsToCheck = apartments;
		}

		ArrayList<Apartment> list = new ArrayList<Apartment>();
		
		for(Apartment apartment : apartmentsToCheck) {
			if(apartment.getNumberOfGuests() == numberOfGuests) {
				list.add(apartment);
			}
		}
		return list;
	}
	
	public void loadApartments() throws FileNotFoundException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = ""; 
		String temp;
		try(BufferedReader br = new BufferedReader(new FileReader(apartmentsFile))){
			while ((temp = br.readLine()) != null) {
				json += temp;
			}
		}
		List<Apartment> list = mapper.readValue(json, new TypeReference<ArrayList<Apartment>>() {});
		
		this.apartments.clear();
		for(Apartment apartment: list) {
			this.apartments.add(apartment);
		}
	}
	
	public void saveApartments() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(apartmentsFile, this.apartments);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
