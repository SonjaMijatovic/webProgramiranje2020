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

import beans.Review;

public class ReviewDao {

	private ArrayList<Review> reviews = new ArrayList<Review>();
	private String ctxPath;
	private File reviewFile;
	
	public ReviewDao(String ctx) {
		super();
		ctxPath = ctx;
		try { 
			loadReviews();
		}catch(IOException e) {
			e.printStackTrace();
		}
		reviewFile = new File(this.ctxPath + "data"+ java.io.File.separator +"reviews.json");
	}
	
	public void addReview(Review review) {
		reviews.add(review);
	}
	
	public ArrayList<Review> getAllReviews(){
		return reviews;
	}
	
	public void loadReviews() throws FileNotFoundException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();

		String json = ""; 
		String temp;
		try(BufferedReader br = new BufferedReader(new FileReader(reviewFile))){
			while ((temp = br.readLine()) != null) {
				json += temp;
			}
		}
		
		List<Review> reviewList =  mapper.readValue(json, 
			    new TypeReference<ArrayList<Review>>() {});
		
		this.reviews.clear();
		// TODO add all elements at once
		for(Review review : reviewList) {
			this.reviews.add(review);
		}
	}
	
	public void saveReviews() { 
		ObjectMapper mapper = new ObjectMapper();		
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(reviewFile, this.reviews);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
