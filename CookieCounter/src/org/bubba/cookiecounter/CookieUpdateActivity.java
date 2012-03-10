package org.bubba.cookiecounter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CookieUpdateActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cookieupdate);

		Intent sender = getIntent();
//		String name = sender.getExtras().getString("name");
		final int id = sender.getExtras().getInt("id");
		
		ArrayList<CookiesSold> list = CookieCounterActivity.readGsFilex(this);
		
		if(list == null || id < 0 || id > list.size())
		{
			return;
		}
		
		CookiesSold gscs = list.get(id);
		
		TextView titleView = (TextView) findViewById(R.id.cookiesfortextview);
		titleView.setText("Cookies for  " + gscs.getName());
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.mylayoutxxyz);
		if (ll == null)
		{
			return;
		}
		
		Button emailButton = (Button) findViewById(R.id.cookieUpdateEmailButton);
        emailButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
		        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		        emailIntent.setType("plain/text"); 
		        
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Cookie List as of "
						+ (new Date()).toString()); 
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "List of Cookies\n"
						+ getCookieListEmailString(id));
		        startActivity(emailIntent); 
            }
        });
		// ----------------
		
//		getLineDivider(ll);
		ArrayList<Cookie> gsList = gscs.getCookiesSoldList();
		int i = 0;
		int totalQuantity = 0;
		
		for (Iterator<Cookie> iterator = gsList.iterator(); iterator.hasNext();)
		{
			Cookie cookie = (Cookie) iterator.next();
			LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
			RelativeLayout rl = (RelativeLayout) linflater.inflate(R.layout.cookieupdaterow, null);
			
			TextView tvDesc = (TextView) rl.findViewById(R.id.cookierowdesc);
			tvDesc.setText(cookie.getName());

			TextView tvPrice = (TextView) rl.findViewById(R.id.cookierowprice);
			tvPrice.setText("" + cookie.getCost().setScale(2)); 

			TextView tvQuantity = (TextView) rl.findViewById(R.id.cookierowquantity);
			int quantity = cookie.getQuantity();
			totalQuantity += quantity;
			tvQuantity.setText("" + quantity);
			UpdateCookieLocator ucl = new UpdateCookieLocator(id, i, gsList);
			tvQuantity.setTag(ucl);

			TextView tvTotal = (TextView) rl.findViewById(R.id.cookierowtotalcost);
			tvTotal.setText(cookie.getCost().multiply(new BigDecimal(cookie.getQuantity())).setScale(2).toString()); 

			Button plusSign = (Button) rl.findViewById(R.id.cookierowplus);
			plusSign.setOnClickListener(new View.OnClickListener() 
	        {@Override public void onClick(View v){(new UpdateCookieTotals()).updateRow(v, 1);}});
			
			Button plusMinus = (Button) rl.findViewById(R.id.cookierowminus);
			plusMinus.setOnClickListener(new View.OnClickListener()
			{@Override public void onClick(View v){(new UpdateCookieTotals()).updateRow(v, -1);}});
			
			ll.addView(rl);
//			getLineDivider(ll);
			i += 1;
		}
		
		writeTotalLine(id, ll, i, totalQuantity);
	}

	void writeTotalLine(int id, LinearLayout ll, int i, int totalQuantity)
	{
		LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
		RelativeLayout rl = (RelativeLayout) linflater.inflate(R.layout.cookietotalrow, null);

		TextView tvDesc = (TextView) rl.findViewById(R.id.cookierowdesc);
		tvDesc.setText("Total");

		TextView tvPrice = (TextView) rl.findViewById(R.id.cookierowprice);
		tvPrice.setText("3.50"); 

		TextView tvQuantity = (TextView) rl.findViewById(R.id.cookierowquantity);
		tvQuantity.setText("" + totalQuantity);

		TextView tvTotal = (TextView) rl.findViewById(R.id.cookierowtotalcost);
		tvTotal.setText((new BigDecimal("3.50")).multiply(new BigDecimal(totalQuantity)).setScale(2).toString()); 
		
		ll.addView(rl);
	}

	void getLineDivider(LinearLayout ll)
	{
		View view = new View(this);
		view.setBackgroundColor(0xFFFFFFFF);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 2);
		view.setLayoutParams(params);
		ll.addView(view);
	}
	
	public class UpdateCookieTotals
	{
		public void updateRow(View v, int i)
		{
        	RelativeLayout rl = (RelativeLayout) v.getParent();
	    	TextView tvQuantity = (TextView) rl.findViewById(R.id.cookierowquantity);
	    	int q = (new Integer(tvQuantity.getText().toString())).intValue() + i;
			tvQuantity.setText("" + q);
			
			TextView tvPrice = (TextView) rl.findViewById(R.id.cookierowprice);
	    	BigDecimal p = new BigDecimal(tvPrice.getText().toString());
	
			TextView tvTotal = (TextView) rl.findViewById(R.id.cookierowtotalcost);
			tvTotal.setText(p.multiply(new BigDecimal(q)).setScale(2).toString());
			
			UpdateCookieLocator ucl = (UpdateCookieLocator) tvQuantity.getTag();
			
			ArrayList<CookiesSold> list = CookieCounterActivity.readGsFilex(v.getContext());
			CookiesSold gscs = list.get(ucl.personRow);
			Cookie row = gscs.getCookiesSoldList().get(ucl.getCookieRow());
			row.setQuantity(q);
    		CookieCounterActivity.saveGsFilex(v.getContext(), list);
    		
    		LinearLayout ll = (LinearLayout)v.getParent().getParent();
    		
    		RelativeLayout rlTotalRow = (RelativeLayout)ll.findViewById(R.id.rlcookietotalrow);
    		TextView tvTotalQuantity = (TextView) rlTotalRow.findViewById(R.id.cookierowquantity);
    		TextView tvTotalTotal = (TextView) rlTotalRow.findViewById(R.id.cookierowtotalcost);
	    	
    		tvTotalQuantity.setText("" + (Integer.parseInt(tvTotalQuantity.getText().toString()) + i));
    		
    		BigDecimal bd = new BigDecimal(tvTotalTotal.getText().toString());
    		if(i == 1) bd = bd.add(new BigDecimal("3.50"));
    		if(i == -1) bd = bd.subtract(new BigDecimal("3.50"));
    		tvTotalTotal.setText(bd.toString());
		}
	}
	
	public class UpdateCookieLocator
	{
		private int personRow = -1;
		private int cookieRow = -1;
		private ArrayList<Cookie> gsList;
		
		public UpdateCookieLocator(int personRow, int cookieRow, ArrayList<Cookie> gsList)
		{
			super();
			this.personRow = personRow;
			this.cookieRow = cookieRow;
			this.gsList = gsList;
		}
		
		public int getPersonRow()
		{
			return personRow;
		}
		public void setPersonRow(int personRow)
		{
			this.personRow = personRow;
		}
		public int getCookieRow()
		{
			return cookieRow;
		}
		public void setCookieRow(int cookieRow)
		{
			this.cookieRow = cookieRow;
		}
		public ArrayList<Cookie> getGsList()
		{
			return gsList;
		}
		public void setGsList(ArrayList<Cookie> gsList)
		{
			this.gsList = gsList;
		} 
	}

	protected String getCookieListEmailString(int id)
	{
		StringBuffer sb = new StringBuffer(100);
		
		ArrayList<CookiesSold> arrayList = CookieCounterActivity.readGsFilex(this);
		
    	CookiesSold gscs = arrayList.get(id); 
    			//(CookiesSold) iter.next();
    	
    	BigDecimal personTotal = new BigDecimal("0.00").setScale(2);
    	sb.append(gscs.getName() + "\n");
    	
    	for (Iterator<Cookie> iter2 = gscs.getCookiesSoldList().iterator(); iter2.hasNext();)
		{
			Cookie gsc = iter2.next();
			sb.append(gsc.toStringBuffer());
			BigDecimal saleTotal = gsc.getTotal();
			personTotal = personTotal.add(saleTotal);
		}
    	sb.append("    total = " + personTotal.toString());
		
		return sb.toString();
	}
}
