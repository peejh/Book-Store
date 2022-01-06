package bean;

import java.sql.Timestamp;

import model.OrderStatus;

/***
 * Some RULES:
 * 		ONLY CART orders can be checked out
 *		CART orders with NULL user_ids are not stored and only persists on session
 *		On checkout, user is forced to login and asked to combine session cart to 
 *			his/her stored cart (if any)
 *			this is when a CART order is associated to a user_id
 *			this means user_id doesn't have to be updated when a CART order is
 *			updated to ORDERED
 */

public class OrdersBean {

	private int id;						// randomly generated
	private int userId;					// who owns the Order
	private Timestamp orderTimestamp;	// updated only on checkout
	private int addrId;
	private OrderStatus status;			// one of {CART, ORDERED, PROCESSED, SHIPPED, DENIED}
	
	public OrdersBean(int id, int userId, Timestamp orderTimestamp, int addrId, OrderStatus status) {
		super();
		this.id = id;
		this.userId = userId;
		this.orderTimestamp = orderTimestamp;
		this.addrId = addrId;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getOrderTimestamp() {
		return orderTimestamp;
	}
	public void setOrderTimestamp(Timestamp orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}
	public int getAddrId() {
		return addrId;
	}
	public void setAddrId(int addrId) {
		this.addrId = addrId;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
}
