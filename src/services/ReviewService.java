package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Apartment;
import beans.Host;
import beans.Guest;
import beans.Review;
import beans.User;
import beans.Reservation;
import beans.Reservation.Status;
import dao.ApartmentDao;
import dao.ReviewDao;
import dao.UserDao;

@Path("review")
public class ReviewService {

	@Context
	ServletContext ctx;
	
    public static final String reviewDao = "reviewDao";

	
	public ReviewService() {
		super();
	}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute(reviewDao) == null) {
			ctx.setAttribute(reviewDao, new ReviewDao(ctx.getRealPath("/")));
		}
	}
	
	@POST
	@Path("/add/{apartmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addReview(Review review, @PathParam("apartmentId") String apartmentId) {
		
		ReviewDao reviewDao = (ReviewDao) ctx.getAttribute("reviewDao");
		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");
		UserDao userDao = (UserDao) ctx.getAttribute("userDao");
		
		//add review in review context
		reviewDao.addReview(review);
		
		//add review to the apartment it belongs
		Apartment apartment = apartmentDao.getApartmentById(apartmentId);
		apartment.addReview(review);
		
		//add review to the apartment at host
		String hostUsername = apartment.getHostUsername();
		User user = userDao.getUserByUsername(hostUsername);
		System.out.println("Username of apartment host where review belongs is: " + hostUsername);
		
		Host host = (Host) user;
		(host.getApartmentById(apartmentId)).addReview(review);
			
		reviewDao.saveReviews();
		apartmentDao.saveApartments();
		userDao.saveUsers();
		
		System.out.println("Review created!");
		return Response.ok().build();
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Review> getAllReviews(){
		
		ReviewDao reviewDao = (ReviewDao) ctx.getAttribute("reviewDao");
		if(reviewDao == null){
			System.out.println("No reviews!");
			return null;
		}
		return reviewDao.getAllReviews();		
	}
	
	@GET
	@Path("/reviews/{apartmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Review> getApartmentReviews(@PathParam("apartmentId") String apartmentId){
		
		ReviewDao reviewDao = (ReviewDao) ctx.getAttribute("reviewDao");
		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");
				
		if(reviewDao == null) {
			System.out.println("No reviews.");
			return null;
		}
		
		ArrayList<Review> reviewList = new ArrayList<Review>();
		
		Apartment selectedApartment = null;
		for(Apartment apartment : apartmentDao.getAllApartments()) {
			if (apartment.getId().equals(apartmentId)) {
				selectedApartment = apartment;
			}
		}
		
		if (selectedApartment.getReviews() != null) {
			for(Review review : selectedApartment.getReviews()) {
				if(review.isVisible()) {
					reviewList.add(review);
				}
			}
		}
		
		System.out.println("Reviews found!");
		return reviewList;
	}
	
//	@GET
//	@Path("/getKomentareMogApartmana/{idApartmana}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public ArrayList<Komentar> getKomentareMogApartmana(@PathParam("idApartmana") String idAp, @Context HttpServletRequest rq){
//		
//		Domacin d = (Domacin) rq.getSession().getAttribute("korisnik");
//		Apartman a = d.getApartmanPoId(idAp);
//		
//		ArrayList<Komentar> lista = new ArrayList<Komentar>();
//		
//		for(Komentar k : a.getKomentari()){
//			lista.add(k);
//		}
//		
//		return lista;
//	}
	
	@PUT
	@Path("/changeVisibility/{reviewId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeVisibility(@PathParam("reviewId") int reviewId, @Context HttpServletRequest rq) {
		
		ReviewDao reviewDao = (ReviewDao) ctx.getAttribute("reviewDao");
		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");
		
		ArrayList<Review> allReviews = reviewDao.getAllReviews();
		
		// change review on context
		for(Review review : allReviews) {
			if(review.getId() == reviewId) {
				review.setVisible(!review.isVisible());
			}
		}
		
		// change in apartment for user on context
		Host host = (Host) rq.getSession().getAttribute("user");
		
		ArrayList<Apartment> hostApartments =  host.getApartments();
		ArrayList<Review> reviewsForAllHostApartments = new ArrayList<Review>();
		
		for(Apartment apartment : hostApartments) {
			reviewsForAllHostApartments.addAll(apartment.getReviews());
		}
		
		for(Review review : reviewsForAllHostApartments) {
			if(review.getId() == reviewId) {
				review.setVisible(!review.isVisible());
			}
		}
		
		// change in specific appartment on context where it's placed
		ArrayList<Apartment> allApartments = apartmentDao.getAllApartments();
		ArrayList<Review> reviewList = new ArrayList<Review>();
		
		for(Apartment apartment : allApartments) {
			reviewList.addAll(apartment.getReviews());
		}
		
		for(Review review : reviewList) {
			if(review.getId() == reviewId) {
				review.setVisible(!review.isVisible());
			}
		}
		
		reviewDao.saveReviews();
		apartmentDao.saveApartments();
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/apartmentsEligibleForReview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> getApartmentsEligibleForReview(@Context HttpServletRequest rq) {
		
		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");
		
		Guest guest = (Guest) rq.getSession().getAttribute("user");
		
		ArrayList<Reservation> guestReservations = guest.getReservations();
		ArrayList<Reservation> reservationsEligibleForReview = new ArrayList<Reservation>();
		
		for(Reservation reservation : guestReservations) {
			if(reservation.getStatus() == Status.REJECTED || reservation.getStatus() == Status.COMPLETED) {
				reservationsEligibleForReview.add(reservation);
			}
		}
		
		ArrayList<Apartment> activeApartments = apartmentDao.getActiveApartments();
		ArrayList<Apartment> apartmentsEligibleForReview = new ArrayList<Apartment>();
		ArrayList<String> apartmentIdsEligibleForReview = new  ArrayList<String>();
		
		for(Reservation reservation : reservationsEligibleForReview) {
			apartmentIdsEligibleForReview.add(reservation.getApartmentId());
		}
		
		for(Apartment apartment : activeApartments) {
			for(String id : apartmentIdsEligibleForReview) {
				if(id.equals(apartment.getId()) && !reservationsEligibleForReview.contains(apartment)) {
					apartmentsEligibleForReview.add(apartment);
				}
			}
		}
		
		System.out.println("------------------------------ : " + apartmentsEligibleForReview);
		return apartmentsEligibleForReview;
	}
}
