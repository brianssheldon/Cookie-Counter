package org.bubba.cookiecounter;

import java.math.BigDecimal;
import java.util.ArrayList;

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
}