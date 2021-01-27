package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import beans.User;
import dao.ApartmentDao;
import dao.UserDao;

@Path("apartment")
public class ApartmentService {
	
	@Context
	ServletContext ctx;
	
	public ApartmentService() {
		super();
	}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("apartmentDao") == null)
			ctx.setAttribute("apartmentDao", new ApartmentDao(ctx.getRealPath("/")));
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> getAllApartments(){
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");
		return dao.getAllApartments();
	}
	
	@GET
	@Path("/active/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Apartment getActiveApartmentById(@PathParam("id") String id) {
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");	
		if(dao == null) 
			return null;
		
		return dao.getActiveApartmentById(id);
	}
	
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Apartment getAnyApartmentById(@PathParam("id") String id) {
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");		
		if(dao == null) 
			return null;
		
		return dao.getApartmentById(id);
	}
	
	@GET
	@Path("/activeApartments")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> getActiveApartments() {
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");			
		return dao.getActiveApartments();
	}
	
	@GET
	@Path("/hostsApartmants")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> getHostsApartmants(@Context HttpServletRequest rq) {
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");			
		
		Host host = (Host) rq.getSession().getAttribute("user");
				
		ArrayList<Apartment> allApartmants = dao.getAllApartments();
		ArrayList<Apartment> list = new ArrayList<Apartment>();
		
		for(Apartment apartment : allApartmants) {
			if(apartment.getHostUsername().equals(host.getUsername())) {
				list.add(apartment);
			}
		}
		return list;
	}
	
	@GET
	@Path("/search/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> searchByType(@PathParam("type") Apartment.Type type){
		 
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");					
		if(dao == null)
			return null;
		
		ArrayList<Apartment> list = dao.getApartmentsByType(type, true);
		
		if(list.isEmpty()) {
			System.out.println("There are no active apartments of the requested type!");
			return null;
		} else {
			return list;
		}
	}
	
	@GET
	@Path("/search/rooms/{numberOfRooms}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> searchByNumberOfRooms(@PathParam("numberOfRooms") int numberOfRooms){
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");							
		if(dao == null) 
			return null;
		
		ArrayList<Apartment> list = dao.getApartmentsByNumberOfRooms(numberOfRooms, true);
		
		if(list.isEmpty()) {
			System.out.println("There are no active apartments of the requested number of rooms!");
			return null;
		}else {
			return list;
		}
	}
	
	@GET
	@Path("/search/guests/{numberOfGuests}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> searchByNumberOfGuests(@PathParam("numberOfGuests") int numberOfGuests){
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");							
		if(dao == null) 
			return null;
		
		ArrayList<Apartment> list = dao.getApartmentsByNumberOfGuests(numberOfGuests, true);
		
		if(list.isEmpty()) {
			System.out.println("There are no active apartments of the requested number of guest!");
			return null;
		}else {
			return list;
		}
	}
	
	@GET
	@Path("/search/{type}/{numberOfRooms}/{numberOfGuests}/{onlyActive}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> search(@PathParam("type") Apartment.Type type,
			@PathParam("numberOfRooms") int numberOfRooms, @PathParam("numberOfGuests") int numberOfGuests,
			@PathParam("onlyActive") boolean onlyActive){
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");							
		
		ArrayList<Apartment> apartmentsByType = dao.getApartmentsByType(type, onlyActive);
		
		ArrayList<Apartment> apartmentsByRooms =  dao.getApartmentsByNumberOfRooms(numberOfRooms, onlyActive);
		
		ArrayList<Apartment> apartmentsByGuests = dao.getApartmentsByNumberOfGuests(numberOfGuests, onlyActive);
		
		apartmentsByType.retainAll(apartmentsByRooms);
		apartmentsByType.retainAll(apartmentsByGuests);
		
		if(apartmentsByType.isEmpty()) {
			System.out.println("There are no active apartments matching all criteria!");
			return null;
		}else {
			return apartmentsByType;
		}
	}

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addApartment(Apartment apartment, @Context HttpServletRequest rq) {
		
		System.out.println("Apartment to add: " + apartment);
		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");							
		UserDao userDao = (UserDao) ctx.getAttribute("userDao");							
		
		Host host = (Host) rq.getSession().getAttribute("user");
		apartment.setHostUsername(host.getUsername());
		
		host.addApartment(apartment);
	
		apartmentDao.addApartment(apartment);
		
		apartmentDao.saveApartments();
		userDao.saveUsers();
		
		return Response.ok().build();	
	}
	
	
	@PUT
	@Path("/remove/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeApartment(@PathParam("id") String id) {
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");									
		if(dao == null)
			return Response.status(500).build();
		
		dao.removeApartmentById(id);
		System.out.println("Apartment is successfully removed.");
	
		dao.saveApartments();
		return Response.ok().build();
	}
	
	@PUT
	@Path("/edit/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editApartment(Apartment apartment, @PathParam("id") String id) {
		
		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");									
		UserDao userDao = (UserDao) ctx.getAttribute("userDao");							
		
		if(apartmentDao == null)
			return Response.status(500).build();
		
		ArrayList<Apartment> allApartments = apartmentDao.getAllApartments();
		
		for(Apartment app : allApartments) {
			if(app.getId().equals(id)) {
				app.setStatus(apartment.getStatus());
				app.setNumberOfRooms(apartment.getNumberOfRooms());
				app.setNumberOfGuests(apartment.getNumberOfGuests());
				app.setPrice(apartment.getPrice());
				app.setAmenities(apartment.getAmenities());
				app.setLocation(apartment.getLocation());
				System.out.println("Edited apartment: " + app.toString());
				break;
			}
		}
		
		
		String apartmentHost = apartment.getHostUsername();
		Host host = null;
		for(User user : userDao.getAllUsers()) {
			if(user.getUsername().equals(apartmentHost)) {
				host = (Host) user;
			}
		}		
		
		host.updateApartment(apartment, id);
		userDao.saveUsers();
		apartmentDao.saveApartments();
		return Response.ok().build();
	}
	
	@GET
	@Path("/apartmentHost/{apartmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getapartmentHost(@PathParam("apartmentId") String apartmentId) {
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");									
		ArrayList<Apartment> allApartments = dao.getAllApartments();
		
		String hostUsername = null;
		
		for(Apartment apartment : allApartments) {
			if(apartment.getId().equals(apartmentId)) {
				hostUsername = apartment.getHostUsername();
				break;
			}
		}
		
		return hostUsername;
	}
	
	@GET
	@Path("/filter/status/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> filterByStatus(@PathParam("status") Apartment.ActiveStatus status) {
		
		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");									
		
		ArrayList<Apartment> allApartments = dao.getAllApartments();
		ArrayList<Apartment> list = new ArrayList<Apartment>();
		
		for(Apartment apartment : allApartments) {
			if(apartment.getStatus().equals(status)) {
				list.add(apartment);
			}
		}
		return list;
	}
	
	@GET
	@Path("/filter/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> filterByType(@PathParam("type") Apartment.Type type){

		ApartmentDao dao = (ApartmentDao) ctx.getAttribute("apartmentDao");									
		
		ArrayList<Apartment> allApartments = dao.getAllApartments();
		ArrayList<Apartment> list = new ArrayList<Apartment>();
		
		for(Apartment apartment : allApartments) {
			if(apartment.getType().equals(type)) {
				list.add(apartment);
			}
		}
		return list;
	}
}
