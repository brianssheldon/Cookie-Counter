package org.bubba.cookiecounter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditListActivity extends Activity
{
	CookieDao cookieDao = new CookieDao();
	
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cookielistupdate);
        
        ArrayList<Cookie> list = cookieDao.readFile(this);
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.updateCookieListList);
        
        for (Iterator<Cookie> iterator = list.iterator(); iterator.hasNext();)
		{
			Cookie cookie = (Cookie) iterator.next();
			populateRow(ll, cookie);
		}
        populateRow(ll, new Cookie());
        
        makeSaveButtonListener();
        makeExitButtonListener();
    }

	void populateRow(LinearLayout ll, Cookie cookie)
	{
		LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout rl = (RelativeLayout) linflater.inflate(R.layout.cookieupdaterow, null);
		
		TextView tvDesc = (TextView) rl.findViewById(R.id.cookieupdaterowdesc);
		String name = cookie.getName();
		if(name == null) name = "";
		tvDesc.setText(name);

		TextView tvPrice = (TextView) rl.findViewById(R.id.cookieupdaterowprice);
		BigDecimal cost = cookie.getCost();
		if(cost == null) cost = new BigDecimal("0.00");
		tvPrice.setText("" + cost.setScale(2)); 
		// -----------
		ll.addView(rl);
	}

	void makeSaveButtonListener()
	{
		Button saveButton = (Button)findViewById(R.id.savecookielist);
        saveButton.setOnClickListener(new View.OnClickListener() 
        {
        	@Override
            public void onClick(View v)
            {
        		CookieDao cookieDao = new CookieDao();
                ArrayList<Cookie> list = new ArrayList<Cookie>();
                LinearLayout ll = (LinearLayout) findViewById(R.id.updateCookieListList);
                int x = ll.getChildCount();
                
                for (int i = 0; i < x; i++)
				{
                	RelativeLayout rl = (RelativeLayout) ll.getChildAt(i);
					EditText descView = (EditText)rl.getChildAt(0);
					String name = descView.getText().toString();
					if(name == null || "".equals(name))continue;
					
					EditText priceView = (EditText)rl.getChildAt(1);
					String price = priceView.getText().toString();
					if(price == null || "".equals(price)) continue;
					
					Cookie cookie = new Cookie();
					cookie.setName(name);
					cookie.setCost(new BigDecimal(price));
					list.add(cookie);
				}
                
                cookieDao.writeFile(list, v.getContext());
                
        		ll.removeAllViews();	// remove all views
                
                for (Iterator<Cookie> iterator = list.iterator(); iterator.hasNext();)
        		{
        			Cookie cookie = (Cookie) iterator.next();
        			populateRow(ll, cookie);
        		}
                populateRow(ll, new Cookie());
            }
        });
	}

	private void makeExitButtonListener()
	{
		Button exitButton = (Button)findViewById(R.id.exitCookieList);
        exitButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
		    	Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
	}
}
