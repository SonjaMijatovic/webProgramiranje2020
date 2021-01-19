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

import beans.Reservation;


public class ReservationDao {

	ArrayList<Reservation> reservations = new ArrayList<Reservation>();
	private String ctxPath;
	private File reservationsFile;
	
	public  ReservationDao(String ctx) {
		super();
		ctxPath = ctx;
		try {
			loadReservations();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reservationsFile = new File(this.ctxPath + "data"+ java.io.File.separator +"reservations.json");
	}
	
	public void addReservations(Reservation reservation) {
		reservations.add(reservation);
	}
	
	public ArrayList<Reservation> getAllReservations(){
		return reservations;
	}
	
	public void loadReservations() throws FileNotFoundException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String json = ""; 
		String temp;
		try(BufferedReader br = new BufferedReader(new FileReader(reservationsFile))){
			while ((temp = br.readLine()) != null) {
				json += temp;
			}
		}
		
		List<Reservation> list =  mapper.readValue(json, new TypeReference<ArrayList<Reservation>>() {});
		
		this.reservations.clear();
		for(Reservation reservation : list) {
			this.reservations.add(reservation);
		}
	}
	
	public void saveReservations() {		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(reservationsFile, this.reservations);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
