package org.bubba.cookiecounter;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cookie implements Serializable
{
	private static final long serialVersionUID = 1245L;
	private String name;
	private BigDecimal cost;
	private int quantity;

	public Cookie()
	{
		this.name = "";
		this.cost = new BigDecimal("0.00");
		this.quantity = 0;
	}
	
	public Cookie(String name)
	{
		this.name = name;
		this.cost = new BigDecimal("0.00");
		this.quantity = 0;
	}
	
	public Cookie(String name, BigDecimal cost, int quantity)
	{
		super();
		this.name = name;
		this.cost = cost;
		this.quantity = quantity;
	}
	
	public StringBuffer toStringBuffer(int namelen, int quantitylen, int costlen, int saletotallen)
	{
		StringBuffer sb = new StringBuffer(100);
		String blanks = "                                                    ";

		int nameLen = getName().length();
		int costLen = getCost().toString().length();
		int quantityLen = ("" + getQuantity()).length();

		BigDecimal saleTotal = getTotal();
		int saleTotalLen = saleTotal.toString().length();
		
		sb.append(
			getName() 
			+ blanks.substring(0, namelen - nameLen)
			+ getQuantity() 
			+ blanks.substring(0, quantitylen - quantityLen)
			+ " * "
			+ getCost().toString() 
			+ blanks.substring(0, costlen - costLen)
			+ " = $"
			+ saleTotal 
			+ blanks.substring(0, saletotallen - saleTotalLen)
			+ "\n");
		
		return sb;
	}

//	public StringBuffer toStringBuffer()
//	{
//		StringBuffer sb = new StringBuffer(100);
//		String blanks = "                              ";
//
//		int nameLen = getName().length();
//		int costLen = getCost().toString().length();
//		int quantityLen = ("" + getQuantity()).length();
//
//		BigDecimal saleTotal = getTotal();
//		int saleTotalLen = saleTotal.toString().length();
//		
//		sb.append(
//			getName() 
//			+ blanks.substring(0, 20 - nameLen)
//			+ getQuantity() 
//			+ blanks.substring(0, 5 - quantityLen)
//			+ " * "
//			+ getCost().toString() 
//			+ blanks.substring(0, 8 - costLen)
//			+ " = $"
//			+ saleTotal 
//			+ blanks.substring(0, 8 - saleTotalLen)
//			+ "\n");
//		
//		return sb;
//	}
	public BigDecimal getTotal()
	{
		return getCost().multiply(new BigDecimal(getQuantity()+"")).setScale(2);
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public BigDecimal getCost()
	{
		return cost;
	}
	public void setCost(BigDecimal cost)
	{
		this.cost = cost;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
}