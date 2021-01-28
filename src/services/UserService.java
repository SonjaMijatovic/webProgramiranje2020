package services;

import java.util.ArrayList;
import java.util.List;

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

import beans.Host;
import beans.Guest;
import beans.User;
import dao.UserDao;

@Path("user")
public class UserService {

	@Context
	ServletContext ctx;
	
	public UserService() {
		super();
	}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("userDao") == null)
			ctx.setAttribute("userDao", new UserDao(ctx.getRealPath("/")));
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user, @Context HttpServletRequest req) {
		UserDao dao = (UserDao) ctx.getAttribute("userDao");
		if(dao == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		
		if (dao.doesUsernameAlreadyExists(user.getUsername())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Username already exists").build();
		}
		
		Guest newGuest = new Guest(user);
		dao.save(newGuest);
		
		req.getSession().setAttribute("user", newGuest);
		System.out.println("Logged in user: " + newGuest.getUsername());
		
		return Response.ok().build();	
	}
	
	@POST
	@Path("/login/{username}/{password}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(@PathParam("username") String username, @PathParam("password") String password, @Context HttpServletRequest req) {
		
		UserDao dao = (UserDao) ctx.getAttribute("userDao");
		if(dao == null)
			return Response.status(500).build();
		
		if(req.getSession().getAttribute("user") != null)
			req.getSession().invalidate();
		
		if(!dao.doesUserAlreadyExists(username, password)) 
			return Response.status(500).build();
					
		User user = dao.getUserByUsername(username);
		
		req.getSession().setAttribute("user", user);
		System.out.println("Logged in user: " + user);
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpServletRequest req) {
		
		req.getSession().invalidate();
		System.out.println("User is logged out!");
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/getUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getCurrentUser(@Context HttpServletRequest req) {

		if(req.getSession(false) == null) {
			System.out.println("get current user is not possible at the moment");
			return null;
		}
		
		User user = (User) req.getSession(false).getAttribute("user");
		if (user != null) {
			System.out.println("Current user: " + user.getUsername());
		}
		return user;
	}
	
	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers(){
		
		UserDao dao = (UserDao) ctx.getAttribute("userDao");
		ArrayList<User> users = null;
		if (dao != null) {
			users = dao.getAllUsers();
			System.out.println("System has " + users.size() + " users.");
		}		
		return users;	
	}
	
	@GET
	@Path("/search/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public User searchForUser(@PathParam("username") String username){
		
		UserDao dao = (UserDao) ctx.getAttribute("userDao");
		if(dao == null)
			return null;
		
		User user = dao.getUserByUsername(username);
		if(user == null) {
			System.out.println("User with this username doesn't exist.");
			 return null;
		}else {
			System.out.println("User if found.");
			return user;
		}
	}
	
	@GET
	@Path("/search/{username}/{role}/{gender}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> findUser(@PathParam("username") String username, @PathParam("role") User.Role role, @PathParam("gender") String gender) {
		
		UserDao dao = (UserDao) ctx.getAttribute("userDao");
		if(dao == null)
			return null;
		
		ArrayList<User> allUsers = dao.getAllUsers();
		
		ArrayList<User> usersMatchingGender = new ArrayList<User>();
		ArrayList<User> usersMatchingRole = new ArrayList<User>();
		ArrayList<User> foundUser = new ArrayList<User>();

		for(User user : allUsers) {
			if(user.getUsername().equals(username)) {
				foundUser.add(user);
			}
			if(user.getGender() != null) {
				if(user.getGender().equals(gender)) {
					usersMatchingGender.add(user);
				}
			}
			if(user.getRole() == role) {
				usersMatchingRole.add(user);
			}
		}
		
		foundUser.retainAll(usersMatchingGender);
		foundUser.retainAll(usersMatchingRole);
		
		
		if(foundUser.isEmpty()) {
			System.out.println("No user found with matching criteria!");
			return null;
		} else {
			System.out.println("Found user matching criteria.");
			return foundUser;
		}
	}
	
	
//	@PUT
//	@Path("edit/{username}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response editProfile(@Context HttpServletRequest request, @PathParam("username") String username, User user) {
//			UserDao userDao = (UserDao) ctx.getAttribute("userDao");
//			User updatedUser = userDao.update(user, ctx.getRealPath(""));
//			if (updatedUser != null) {
//				return Response.status(Response.Status.OK).entity(updatedUser).build();
//			}
//			return Response.status(Response.Status.FORBIDDEN).build();
//	}
	
	@PUT
	@Path("/edit/{username}/{password}/{name}/{lastname}/{gender}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("username") String username, @PathParam("password") String password,
								@PathParam("name") String name, @PathParam("lastname") String lastname, 
								@PathParam("gender") String gender) {
		
		UserDao dao = (UserDao) ctx.getAttribute("userDao");
		if(dao == null)
			return null;
		
		User user = dao.getUserByUsername(username);
		
		user.setPassword(password);
		user.setFirstname(name);
		user.setLastname(lastname);
		user.setGender(gender);
		
		User updatedUser = dao.update(user);

		if (updatedUser != null) {
			return Response.status(Response.Status.OK).entity(updatedUser).build();
		}
		return Response.status(Response.Status.FORBIDDEN).build();
	}
	
	@PUT
	@Path("/updateRole/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUserRole(@PathParam("username") String username) {
		
		UserDao dao = (UserDao) ctx.getAttribute("userDao");	
		
		if(dao == null)
			return Response.status(500).build();
		
		Host host = Host.Parse(dao.getUserByUsername(username));
		User updatedUser = dao.update(host);
		if (updatedUser != null) {
			return Response.status(Response.Status.OK).entity(updatedUser).build();
		}
		return Response.status(Response.Status.FORBIDDEN).build();
	}
}
