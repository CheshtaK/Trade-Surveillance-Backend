import java.util.ArrayList;
import java.util.Random;

public class RandomPriceGenerator {
	
	public static class Order{
		private boolean type; //true = buy, false = sell
		private double price;
		private int quantity;
		
		
		public boolean isType() {
			return type;
		}
		public void setType(boolean type) {
			this.type = type;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public Order(boolean type, double price, int quantity) {
			super();
			this.type = type;
			this.price = price;
			this.quantity = quantity;
		}
		
		
		
	}
	
	public static ArrayList<Order> generatePrices(int numOfOrders, double initialPrice, int maxQty){
		Random ran = new Random();
		
		int qty = ran.nextInt(maxQty);
		ArrayList<Order> orderList = new ArrayList<>(numOfOrders);
		boolean randomType = Math.random() < 0.5 ? true:false; //probability of either type is 1/2
		
		orderList.add(0, new Order(randomType, initialPrice, qty));
		for(int i = 1; i < numOfOrders; i++) {
			randomType = Math.random() < 0.5 ? true:false;
			Order prevOrder = orderList.get(i-1);
			double currPrice;
			if(prevOrder.type) {
				// previous order was buy, so price can increase
				currPrice = prevOrder.price + (prevOrder.quantity / (prevOrder.quantity + 1.0));
			}
			else {
				currPrice = prevOrder.price - (prevOrder.quantity / (prevOrder.quantity + 1.0));
			}
			qty = ran.nextInt(maxQty);
			orderList.add(i, new Order(randomType, currPrice, qty));
		}
		return orderList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Order> test = new ArrayList<>();
		test = generatePrices(50, 100, 100000);
		char orderType;
		for(Order o: test) {
			if(o.type)
				orderType = 'B';
			else
				orderType = 'S';
			System.out.println(orderType + "\t" + o.quantity + "\t" + o.price);
		}
	}

}
