package org.bubba.cookiecounter;

import java.io.Serializable;

public class Contact implements Serializable, Comparable<Contact>
{
	private static final long serialVersionUID = 143237L;

	private String name = "";
	private String address1= "";
	private String address2 = "";
	private String phoneNumber = "";
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int compareTo(Contact another) {
		return name.compareTo(another.getName());
	}
	
	@Override
	public String toString() {
		return getField(name)
				+ getField(address1)
				+ getField(address2)
				+ getField(phoneNumber) + "\n\n";
	}
	
	private String getField(String field)
	{
		if("".equals(field))
		{
			return "";
		}
		else
		{
			return field + "\n";
		}
	}
}
