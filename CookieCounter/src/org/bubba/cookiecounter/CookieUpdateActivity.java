package org.bubba.cookiecounter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CookieUpdateActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		populateScreen();
	}

	private void populateScreen()
	{
		setContentView(R.layout.cookieupdate);

		Intent sender = getIntent();
		final int id = sender.getExtras().getInt("id");
		
		final ArrayList<CookiesSold> list = CookieCounterActivity.readGsFilex(this);
		
		if(list == null || id < 0 || id > list.size())
		{
			return;
		}
		
		CookiesSold gscs = list.get(id);
		
		TextView titleView = (TextView) findViewById(R.id.cookiesfortextview);
		String gsName = gscs.getName();
		final String gsNameFinal = gsName;
		titleView.setText("Cookies for  " + gsName);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.mylayoutxxyz);
		if (ll == null)
		{
			return;
		}
		
		Button emailButton = (Button) findViewById(R.id.cookieUpdateEmailButton);
        emailButton.setOnClickListener(new View.OnClickListener() 
        {
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

		ArrayList<Cookie> gsList = gscs.getCookiesSoldList();
		int i = 0;
		int totalQuantity = 0;
		BigDecimal totalCost = BigDecimal.ZERO;
		
		for (Iterator<Cookie> iterator = gsList.iterator(); iterator.hasNext();)
		{
			final Cookie cookie = (Cookie) iterator.next();
			LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    
			RelativeLayout rl = (RelativeLayout) linflater.inflate(R.layout.cookielistupdaterow, null);
			
			TextView tvDesc = (TextView) rl.findViewById(R.id.cookierowdesc);
			tvDesc.setText(cookie.getName());
			
	        tvDesc.setLongClickable(true);
	        tvDesc.setOnLongClickListener(new OnLongClickListener()
			{
				public boolean onLongClick(View v)
				{
					final View vv = v;
					
			        new AlertDialog.Builder(v.getContext())
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("Delete Item?")
			        .setMessage("Do you want to delete\n\n" + cookie.getName() + "?")
			        .setPositiveButton("Delete", new DialogInterface.OnClickListener() 
			        {
			            public void onClick(DialogInterface dialog, int which)
			            {	// they have clicked on the description so remove this cookie
			            	removeCookieFromGsList(list, gsNameFinal, cookie.getName(), vv.getContext());
			            	populateScreen();
			            }
			        })
			        .setNegativeButton("cancel", null)
			        .show();
					return true;
				}
			});

			TextView tvPrice = (TextView) rl.findViewById(R.id.cookierowprice);
			tvPrice.setText("" + cookie.getCost().setScale(2)); 

			TextView tvQuantity = (TextView) rl.findViewById(R.id.cookierowquantity);
			int quantity = cookie.getQuantity();
			totalQuantity += quantity;
			totalCost = totalCost.add(cookie.getTotal());
			
			tvQuantity.setText("" + quantity);
			UpdateCookieLocator ucl = new UpdateCookieLocator(id, i, gsList);
			tvQuantity.setTag(ucl);

			TextView tvTotal = (TextView) rl.findViewById(R.id.cookierowtotalcost);
			tvTotal.setText(cookie.getCost().multiply(new BigDecimal(cookie.getQuantity())).setScale(2).toString()); 

			Button plusSign = (Button) rl.findViewById(R.id.cookierowplus);
			plusSign.setOnClickListener(new View.OnClickListener() 
	        {public void onClick(View v){(new UpdateCookieTotals()).updateRow(v, 1);}});
			
			Button plusMinus = (Button) rl.findViewById(R.id.cookierowminus);
			plusMinus.setOnClickListener(new View.OnClickListener()
			{public void onClick(View v){(new UpdateCookieTotals()).updateRow(v, -1);}});
			
			ll.addView(rl);
//			getLineDivider(ll);
			i += 1;
		}
		
		writeTotalLine(id, ll, i, totalQuantity, totalCost);
	}
	
	private void removeCookieFromGsList(
			ArrayList<CookiesSold> list,
			String gsNameFinal, String cookieName, Context context)
	{
		ArrayList<CookiesSold> newList = new ArrayList<CookiesSold>();
		CookiesSold cookiesSold;
		
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			cookiesSold = (CookiesSold) iter.next();
			if(gsNameFinal.equals(cookiesSold.getName()))
			{
				ArrayList<Cookie> cookiesSoldList = cookiesSold.getCookiesSoldList();
				ArrayList<Cookie> newCookiesSoldList = new ArrayList<Cookie>();
				Cookie cookie;
				
				for (Iterator iter2 = cookiesSoldList.iterator(); iter2.hasNext();)
				{
					cookie = (Cookie) iter2.next();
					if(!cookie.getName().equals(cookieName))
					{
						newCookiesSoldList.add(cookie);						
					}
				}
				cookiesSold.setCookiesSoldList(newCookiesSoldList);
			}
			newList.add(cookiesSold);
		}
		CookieCounterActivity.saveGsFilex(context, newList);
	}
	
	void writeTotalLine(int id, LinearLayout ll, int i, int totalQuantity, BigDecimal totalCost)
	{
		LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
		RelativeLayout rl = (RelativeLayout) linflater.inflate(R.layout.cookietotalrow, null);

		TextView tvDesc = (TextView) rl.findViewById(R.id.cookierowdesc);
		tvDesc.setText("Total");

		TextView tvPrice = (TextView) rl.findViewById(R.id.cookierowprice);
		tvPrice.setText("    ");//3.50"); 

		TextView tvQuantity = (TextView) rl.findViewById(R.id.cookierowquantity);
		tvQuantity.setText("" + totalQuantity);

		TextView tvTotal = (TextView) rl.findViewById(R.id.cookierowtotalcost);
		tvTotal.setText(totalCost.setScale(2).toString()); 
		
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
    		if(i == 1) bd = bd.add(row.getCost());
    		if(i == -1) bd = bd.subtract(row.getCost());
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

    	int namelen = 20;
    	int quantitylen = 5;
    	int costlen = 8; 
    	int saletotallen = 8;
    	
    	for (Iterator<Cookie> iter2 = gscs.getCookiesSoldList().iterator(); iter2.hasNext();)
		{
			Cookie gsc = iter2.next();
			namelen = CookieUtil.whichIsLarger(gsc.getName().length(), namelen);
			quantitylen = CookieUtil.whichIsLarger(gsc.getQuantity(), quantitylen);
			costlen = CookieUtil.whichIsLarger(("" + gsc.getCost()).toString().length(), costlen);
			saletotallen = CookieUtil.whichIsLarger(gsc.getTotal().toString().length(), namelen);
		}
    	
    	for (Iterator<Cookie> iter2 = gscs.getCookiesSoldList().iterator(); iter2.hasNext();)
		{
			Cookie gsc = iter2.next();
			sb.append(gsc.toStringBuffer(namelen, quantitylen, costlen, saletotallen));
			BigDecimal saleTotal = gsc.getTotal();
			personTotal = personTotal.add(saleTotal);
		}
    	sb.append("    total = " + personTotal.toString());
		
		return sb.toString();
	}
}
