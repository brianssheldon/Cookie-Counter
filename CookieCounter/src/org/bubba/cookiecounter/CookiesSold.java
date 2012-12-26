package org.bubba.cookiecounter;

import java.io.Serializable;
import java.util.ArrayList;

public class CookiesSold implements Serializable
{
	private static final long serialVersionUID = 123L;
	private String name;
	private ArrayList<Cookie> cookiesSoldList;
	
	public CookiesSold()
	{
		cookiesSoldList = CookieUtil.getCookieList();
		name = "Jane Doe";
	}

	public CookiesSold(String name, ArrayList<Cookie> cookiesSoldList)
	{
		super();
		this.name = name;
		this.cookiesSoldList = cookiesSoldList;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<Cookie> getCookiesSoldList()
	{
		return cookiesSoldList;
	}
	public void setCookiesSoldList(ArrayList<Cookie> cookiesSoldList)
	{
		this.cookiesSoldList = cookiesSoldList;
	}
}