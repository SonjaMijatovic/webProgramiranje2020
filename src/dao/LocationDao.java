package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import beans.Address;
import beans.Location;

public class LocationDao {
	private List<Location> locations = new ArrayList<>();

	Path currentDir = Paths.get(".");
	String path = currentDir.toAbsolutePath().toString();
	
	public LocationDao(String contextPath) {
		read(contextPath);
	}
	
	 public List<Location> getAll() {
	        return locations;
	    }
	 
	 public Collection<Location> findAll() {
			return locations;
		}
	 
	 public Location getById(String id) {
		 Location location = null;
			for (int i=0; i<locations.size();i++ ) {
				if (locations.get(i).getId().equals(id))
					location= locations.get(i);
			}
			return location;
		}
	 
	 public void read(String contextPath) {
			BufferedReader in = null;
			try {
				File file = new File(path + "/web2020/WebContent/data/locations.txt");
				in = new BufferedReader(new FileReader(file));
				String s;
				StringTokenizer st;
				while ((s = in.readLine()) != null) {
					s = s.trim();
					if (s.equals(""))
						continue;
					st = new StringTokenizer(s, ";");
					while (st.hasMoreTokens()) {
						String id= st.nextToken().trim();
						String latitude = st.nextToken().trim();
						String longitude = st.nextToken().trim();
						String streetAndNumber = st.nextToken().trim();
						String city = st.nextToken().trim();
						String postalCode = st.nextToken().trim();
						
						Address address = new Address(streetAndNumber, city, postalCode);
						locations.add(new Location(id, latitude, longitude, address));
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
		}
	 
	 public String save(Location location) {	    	
	    	String s = location.getId() + ";" +
	    			location.getLatitude() + ";" +
	    			location.getLongitude()+ ";" +
	    			location.getAddress().getStreetAndNumber() + ";" +
	    			location.getAddress().getCity() + ";" +
	    			location.getAddress().getPostalCode();
	    	
			BufferedWriter writer = null;
			try {
				File file = new File(path + "/web2020/WebContent/data/locations.txt");
				System.out.println("put:"+file.getAbsolutePath());
				writer = new BufferedWriter(new FileWriter(file, true));
				PrintWriter out = new PrintWriter(writer);
				out.println(s);
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (Exception e) {
					}
				}
			}
	        locations.add(location);
	        return location.getId();
	    }
	 
	 public String update(Location location, String contextPath) {
		 boolean match=false;
	    	try {
				File file = new File(path + "/web2020/WebContent/data/locations.txt");
	    		File tempFile = new File(contextPath + "locationsTemp.txt");
				BufferedReader in = new BufferedReader(new FileReader(file));
				BufferedWriter writer =null;
				writer = new BufferedWriter(new FileWriter(tempFile, true));
				
				String s = "", newS = "";
				StringTokenizer st;
				while ((s = in.readLine()) != null) {
					if (s.equals(""))
						continue;
					String[] tokens = s.split(",");
					String id= tokens[2];
					if (id.equals(location.getId())) {
						newS += location.getId() + ";" +
								location.getLatitude() + ";" +
								location.getLongitude() + ";" +
								location.getAddress().getStreetAndNumber() + ";" +
								location.getAddress().getCity()+ ";" +
								location.getAddress().getPostalCode() + "\r\n";
				    	
						match=true;
					} else {
						newS += s + "\r\n";
					}
				}
			in.close();
			PrintWriter out = new PrintWriter(writer);
			out.println(newS);
			out.close();
			Files.copy(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	        tempFile.delete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	    	if (match==true) {
		    	Location removeMe = null;
		    	for (Location l : locations) {
		    	    if (location.getId().equals(l.getId())) {
		    	    	removeMe=l;
		    	    }
		    	}
	    	    locations.remove(removeMe);
		    	locations.add(location);
		    	return location.getId();
	    	}
	    	else
	    		return null;   
	    }
}