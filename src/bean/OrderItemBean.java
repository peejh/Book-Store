package bean;

public class OrderItemBean {

	private int orderId;
	private int bookId;
	private int quantity;
	
	public OrderItemBean(int orderId, int bookId, int quantity) {
		super();
		this.orderId = orderId;
		this.bookId = bookId;
		this.quantity = quantity;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setId(int orderId) {
		this.orderId = orderId;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
