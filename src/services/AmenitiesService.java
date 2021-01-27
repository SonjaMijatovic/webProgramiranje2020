package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

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
import beans.Amenity;
import dao.ApartmentDao;
import dao.AmenityDao;

@Path("amenities")
public class AmenitiesService {

	@Context
	ServletContext ctx;
	
	public AmenitiesService() {
		super();
	}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("amenityDao") == null) 
			ctx.setAttribute("amenityDao", new AmenityDao(ctx.getRealPath("/")));
	}
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Amenity> getAll(){
		
		AmenityDao amenityDao = (AmenityDao) ctx.getAttribute("amenityDao");
		
		return amenityDao.getAllAmenities();
	}
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAmenity(Amenity newAmenity) {
		
		AmenityDao amenityDao = (AmenityDao) ctx.getAttribute("amenityDao");
		
		if(amenityDao == null)
			return Response.status(500).build();
		
		ArrayList<Amenity> amenities = amenityDao.getAllAmenities();
		for(Amenity amenity : amenities) {
			if(amenity.getId() == newAmenity.getId()) {
				return Response.status(500).build();
			}
		}
		
		amenityDao.addAmenity(newAmenity);
		amenityDao.saveApartmentAmenities();
		System.out.println("Amenity added!");
		
		return Response.ok().build();
	}
	
	@PUT
	@Path("/remove/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeAmenityById(@PathParam("id") int id) {
		
		AmenityDao amenityDao = (AmenityDao) ctx.getAttribute("amenityDao");
		
		if(amenityDao == null) {
			return Response.status(500).build();
		}
		amenityDao.removeAmenityFromApartment(id);	
		amenityDao.saveApartmentAmenities();
		return Response.ok().build();
	}
	
//	@PUT
//	@Path("/edit/{id}/{newName}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response editAmenityById(@PathParam("id") int id, @PathParam("newName") String newName) {
//		
//		AmenityDao amenityDao = (AmenityDao) ctx.getAttribute("amenityDao");
//		
//		if(amenityDao == null) {
//			return Response.status(500).build();
//		}
//		
//		ArrayList<Amenity> amenities = amenityDao.getAllAmenities();
//		
//		for(Amenity amenity : amenities) {
//			if(id == amenity.getId()) {
//				amenity.setName(newName);
//				break;
//			}
//		}
//		amenityDao.saveApartmentAmenities();
//		return Response.ok().build();
//	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Amenity getamenityById(@PathParam("id") int id) {
		
		AmenityDao amenityDao = (AmenityDao) ctx.getAttribute("amenityDao");
		
		if(amenityDao == null)
			return null;
		
		return amenityDao.getApartmentAmenityById(id);
	}
	
	@GET
	@Path("/apartment/{apartmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Amenity> getSadrzajJednog(@PathParam("apartmentId") String apartmentId){

		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");
		
		if(apartmentDao == null) 
			return null;
		
		Apartment apartment = apartmentDao.getApartmentById(apartmentId);
		return apartment.getAmenities();
	}
}
