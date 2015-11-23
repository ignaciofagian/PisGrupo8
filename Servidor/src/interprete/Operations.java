package interprete;

public class Operations {
	private double buyOpertions;
	private double sellOperations;
	
	public Operations() {
		this.buyOpertions = 0;
		this.sellOperations = 0;	
	}
	
	public double getBuyOperations() {
		return buyOpertions;
	}
	
	public double getSellOperations() {
		return sellOperations;
	}

	public void setBuyOperations(double buyOpertions) {
		this.buyOpertions = buyOpertions;
	}
	
	public void setSellOperations(double sellOperations) {
		this.sellOperations = sellOperations;
	}

}
