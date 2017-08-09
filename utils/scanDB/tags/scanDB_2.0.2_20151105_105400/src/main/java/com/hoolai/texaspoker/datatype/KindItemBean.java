/**
 * 
 */
package com.hoolai.texaspoker.datatype;


/**
 * @author Cedric(TaoShuang)
 * @create 2012-3-31
 */
public class KindItemBean {
	public static final KindItemBean NULL = new KindItemBean(LimitsAndConsts.UNKNOWN_ID, 0) {
		public void setAmount(int amount) {};
		public void setKindID(int kindID) {};
		public synchronized boolean changeAmount(int delta) {return false;};
	};
	
	private int kindID;
	private int amount;
	/**
	 * @param kindID
	 * @param amount
	 */
	public KindItemBean(int kindID, int amount) {
		this.kindID = kindID;
		this.amount = amount;
	}
	
	public KindItemBean(String codedValue) {
		String[] data = codedValue.split("\\,");
		kindID = Integer.parseInt(data[0]);
		amount = Integer.parseInt(data[1]);
	}
	
	public KindItemBean() {
		
	}
	
	/**
	 * @return the kindID
	 */
	public int getKindID() {
		return kindID;
	}
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	public synchronized boolean changeAmount(int delta) {
		boolean result = false;
		if (delta < 0) {
			if (this.amount < -delta) {
				result = false;
			} else {
				this.amount += delta;
				result = true;
			}
		} else {
			this.amount += delta;
			result = true;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "[" + kindID + ":" + amount + "]"; 
	}

	/**
	 * @param kindID the kindID to set
	 */
	public void setKindID(int kindID) {
		this.kindID = kindID;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
