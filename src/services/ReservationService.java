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

import beans.*;
import beans.Reservation.Status;
import beans.User.Role;
import dao.ApartmentDao;
import dao.UserDao;
import dao.ReservationDao;

@Path("reservation")
public class ReservationService {

	@Context
	ServletContext ctx;

	public ReservationService() {
		super();
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("reservationDao") == null)
			ctx.setAttribute("reservationDao", new ReservationDao(ctx.getRealPath("/")));
	}

	@POST
	@Path("/add/{apartmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addReservation(Reservation reservation, @PathParam("apartmentId") String apartmentId,
			@Context HttpServletRequest rq) {

		ReservationDao reservationDao = (ReservationDao) ctx.getAttribute("rezervreservationDaoacijaDAO");
		ApartmentDao apartmentDao = (ApartmentDao) ctx.getAttribute("apartmentDao");
		UserDao userDao = (UserDao) ctx.getAttribute("userDao");

		if (reservationDao == null) {
			return Response.status(500).build();
		}

		if (apartmentDao == null) {
			System.out.println("Apartmants dao is null");
			return Response.status(500).build();
		}

		if (userDao == null) {
			System.out.println("UserDao is null");
			return Response.status(500).build();
		}

		reservationDao.addReservations(reservation);

		Guest guest = (Guest) rq.getSession().getAttribute("user");
		guest.addReservationToGuest(reservation);

		ArrayList<Apartment> apartments = apartmentDao.getAllApartments();
		ArrayList<User> users = userDao.getAllUsers();

		Apartment selectedapartment = apartmentDao.getApartmentById(apartmentId);

		String apartmentHost = selectedapartment.getHostUsername();
		Host host = null;
		for (User user : users) {
			if (user.getUsername().equals(apartmentHost)) {
				host = (Host) user;
			}
		}

		Apartment usersApartment = host.getApartmentById(apartmentId);
		System.out.println(usersApartment);
		usersApartment.addReservation(reservation);

		for (Apartment apartment : apartments) {
			if (apartment.getId().equals(apartmentId)) {
				System.out.println("Apartment where reservation is added " + apartmentId);
				apartment.addReservation(reservation);
				System.out.println("Reservation is added to th apartment");
				break;
			}
		}

		userDao.saveUsers();
		apartmentDao.saveApartments();
		reservationDao.saveReservations();

		System.out.println("Reservation created!");
		return Response.ok().build();
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> getAllReservations() {

		ReservationDao reservationDao = (ReservationDao) ctx.getAttribute("reservationDao");
		if (reservationDao == null)
			return null;

		ArrayList<Reservation> lista = reservationDao.getAllReservations();
		return lista;
	}

	@GET
	@Path("/hostsReservations")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> getHostsReservations(@Context HttpServletRequest rq) {

		Host host = (Host) rq.getSession().getAttribute("user");

		ArrayList<Apartment> hostsApartments = host.getApartments();
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		ArrayList<Reservation> returnList = new ArrayList<Reservation>();

		for (Apartment apartment : hostsApartments) {
			if (apartment.getReservations() != null) {
				reservations = apartment.getReservations();
				returnList.addAll(reservations);
				System.out.println("Povratna lista: " + returnList);
			} else {
				System.out.println("No reservations!");
				continue;
			}
		}
		System.out.println("Hosts reservations: " + returnList);
		return returnList;
	}

	@PUT
	@Path("/completeReservation/{reservationId}/{apartmentId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response completeReservation(@PathParam("reservationId") String reservationId,
			@PathParam("apartmentId") String apartmentId, @Context HttpServletRequest rq) {

		ReservationDao reservationDao = (ReservationDao) ctx.getAttribute("rezervationDao");
		UserDao userDao = (UserDao) ctx.getAttribute("userDao");

		// izmena na kontekstu rezervacija
		ArrayList<Reservation> reservations = reservationDao.getAllReservations();

		for (Reservation reservation : reservations) {
			if (reservation.getId().equals(reservationId)) {
				reservation.setStatus(Status.COMPLETED);
				break;
			}
		}

		// izmena kod gosta kome pripada rezervacija
		ArrayList<User> users = userDao.getAllUsers();
		ArrayList<Guest> guests = new ArrayList<Guest>();

		Guest guest = null;
		for (User user : users) {
			if (user.getRole().equals(Role.GUEST)) {
				guest = (Guest) user;
				guests.add(guest);
			}
		}
		System.out.println("Guests list: " + guests);

		for (Guest tempGuest : guests) {
			for (Reservation tempReservation : tempGuest.getReservations()) {
				if (tempReservation.getId().equals(reservationId)) {
					tempReservation.setStatus(Status.COMPLETED);
					System.out.println("Reservation status changed to: " + tempReservation.getStatus());
					break;
				}
			}
		}

		// izmena kod domacina, u njegovom apartmanu(sa sesije ga uzimamo)
		Host host = (Host) rq.getSession().getAttribute("user");

		Apartment hostsApartment = host.getApartmentById(apartmentId);

		ArrayList<Reservation> hostsReservations = hostsApartment.getReservations();

		for (Reservation tempResrvation : hostsReservations) {
			if (tempResrvation.getId().equals(reservationId)) {
				tempResrvation.setStatus(Status.COMPLETED);
			}
		}
		reservationDao.saveReservations();
		userDao.saveUsers();

		return Response.ok().build();
	}
}
