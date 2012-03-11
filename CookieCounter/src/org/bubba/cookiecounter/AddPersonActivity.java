package org.bubba.cookiecounter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddPersonActivity extends Activity
{

    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addperson);

        Button addPersonButton = (Button)findViewById(R.id.addpersonbutton);
        addPersonButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
        		ArrayList<CookiesSold> list = CookieCounterActivity.readGsFilex(v.getContext());
        		TextView tv = (TextView) findViewById(R.id.addpersonedittext);
        		
        		if(list == null || tv.getText() == null || tv.getText().length() == 0)
        		{
        			return;
        		}
        		
        		CookiesSold gscs = new CookiesSold();
        		gscs.setName(tv.getText().toString());
        		CookieDao cookieDao = new CookieDao();
        		gscs.setCookiesSoldList(cookieDao.readFile(v.getContext()));
        		
        		list.add(gscs);
        		tv.setText("");
        		
        		CookieCounterActivity.saveGsFilex(v.getContext(), list);
            }
        });

        Button cancelPersonButton = (Button)findViewById(R.id.cancelpersonbutton);
        cancelPersonButton.setOnClickListener(new View.OnClickListener() 
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
