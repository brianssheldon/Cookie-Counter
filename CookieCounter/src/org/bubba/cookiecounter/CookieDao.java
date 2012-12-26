package org.bubba.cookiecounter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;

public class CookieDao implements Serializable
{
	private static final String LIST_OF_COOKIES = "listOfCookies";
	private static final long serialVersionUID = 197245L;
	private ArrayList<Cookie> list;

	public CookieDao()
	{
		list = new ArrayList<Cookie>();
	}
	
	public ArrayList<Cookie> readFile(Context context)
	{
		ArrayList<Cookie> list;
		try
		{
			FileInputStream fis = context.openFileInput(LIST_OF_COOKIES);
	    	ObjectInputStream in = new ObjectInputStream(fis);
	    	list = (ArrayList<Cookie>) in.readObject();
	    	in.close();
	    	fis.close();
		}
		catch (Exception e)
		{
			try
			{
				list = new ArrayList<Cookie>();
				list.add(new Cookie());
				
				FileOutputStream fos = context.openFileOutput(LIST_OF_COOKIES, Context.MODE_PRIVATE);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(list);
				out.close();
				fos.close();
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
				list = new ArrayList<Cookie>();
			}
		}
		return list;
	}
	
	public void writeFile(ArrayList<Cookie> list, Context context)
	{
		try
		{
			FileOutputStream fos = context.openFileOutput(LIST_OF_COOKIES, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
			fos.close();
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
	}
	
	public ArrayList<Cookie> getList()
	{
		return list;
	}

	public void setList(ArrayList<Cookie> list)
	{
		this.list = list;
	}
}