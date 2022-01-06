package rest;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.Hub;

@Path("order")
public class OrderItem {

	@GET
	@Path("/read/")
	@Produces("text/plain")
	public String getOrderByNumber(@QueryParam("id") int id) throws ClassNotFoundException, SQLException {
		return Hub.getInstance().exportOrderJsonById(id);
	}
}
