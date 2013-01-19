package org.bubba.cookiecounter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class CookieUtil
{
	public static ArrayList<Cookie> getCookieList()
	{
		ArrayList<Cookie> list = new ArrayList<Cookie>();
		
		list.add(new Cookie("Do-si-dos", new BigDecimal("3.50"), 0));
		list.add(new Cookie("Samoas", new BigDecimal("3.50"), 0));
		list.add(new Cookie("Savannah Smiles", new BigDecimal("3.50"), 0));
		list.add(new Cookie("Tagalongs", new BigDecimal("3.50"), 0));
		list.add(new Cookie("Thin Mints", new BigDecimal("3.50"), 0));
		list.add(new Cookie("Trefoils", new BigDecimal("3.50"), 0));
		
		return list;
	}

	public static String getCookieTotalsForEmail(ArrayList<CookiesSold> arrayList)
	{
		StringBuffer sb = new StringBuffer(100);
		sb.append("\n\nCookie Totals\n\n");
		BigDecimal grandTotal = new BigDecimal("0.00").setScale(2);
		int grandTotalQuantity = 0;
		ArrayList<Cookie> gtList = new ArrayList<Cookie>();

    	int namelen = 20;
    	int quantitylen = 5;
    	int saletotallen = 8;
    	
		for (Iterator<CookiesSold> iter = arrayList.iterator(); iter.hasNext();)
		{
        	CookiesSold gscs = (CookiesSold) iter.next();
        	
        	for (Iterator<Cookie> iter2 = gscs.getCookiesSoldList().iterator(); iter2.hasNext();)
			{
				Cookie gsc = iter2.next();
				boolean found = false;
				
				for(Iterator<Cookie> iter3 = gtList.iterator(); iter3.hasNext();)
				{
					Cookie gtCookie = iter3.next();
					if(gtCookie.getName().equals(gsc.getName()))
					{
						gtCookie.setQuantity(gtCookie.getQuantity() + gsc.getQuantity());
						found = true;
						break;
					}
				}
				
				if(!found)
				{
					Cookie newCookie = new Cookie();
					newCookie.setName(gsc.getName());
					newCookie.setQuantity(gsc.getQuantity());
					newCookie.setCost(gsc.getCost());
					gtList.add(newCookie);
				}
			}
		}
		

		for(Iterator<Cookie> iter3 = gtList.iterator(); iter3.hasNext();)
		{
			Cookie gtCookie = iter3.next();

	    	namelen = whichIsLarger(gtCookie.getName().length(), namelen);
	    	quantitylen = whichIsLarger(("" + gtCookie.getQuantity()).length(), quantitylen);
	    	saletotallen = whichIsLarger(gtCookie.getTotal().toString().length(), saletotallen);
		}
		for(Iterator<Cookie> iter3 = gtList.iterator(); iter3.hasNext();)
		{
			Cookie gtCookie = iter3.next();
			sb.append(padWithSpaces(gtCookie.getName(), namelen) + " ");
			sb.append(padWithSpaces("" + gtCookie.getQuantity(), quantitylen) + " ");
			sb.append(padWithSpaces("" + gtCookie.getTotal(), saletotallen) + " ");
			sb.append("\n");
			
			grandTotal = grandTotal.add(gtCookie.getTotal());
			grandTotalQuantity += gtCookie.getQuantity();
		}
		
		sb.append("\n");
		sb.append(padWithSpaces("Total", 15) + " ");
		sb.append(padWithSpaces("" + grandTotalQuantity, 3) + " ");
		sb.append(padWithSpaces(grandTotal.toString(), 6) + " ");
		sb.append("\n");
		
		return sb.toString();
	}

	public static int whichIsLarger(int length, int namelen)
	{
		if(length > namelen) return length;
		return namelen;
	}

	private static String padWithSpaces(String name, int i)
	{
		return (name + "                                         ").substring(0, i);
	}
}