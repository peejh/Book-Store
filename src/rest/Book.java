package rest;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.Hub;

@Path("book")
public class Book {
	
	@GET
	@Path("/read/")
	@Produces("text/plain")
	public String getProductInfo(@QueryParam("id") int id) throws ClassNotFoundException, SQLException {
		return Hub.getInstance().exportBookJsonById(id);
	}
	
}