package ke.co.alanigroupltd.marketerslounge;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Register extends Activity {

    //variable declaration
    private EditText fname;
    private EditText sname;
    private EditText phone;
    private EditText email;
    private Button db_clear;
    private static final String URL = "http://192.168.88.241/markets/marketers_register.php";
    private static final String URL2 = "http://10.0.2.2/markets/clear_nannie.php";
    private  String owner_email = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(Register.this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                owner_email = account.name;
            }
        }

           //initiatializa activity items
           fname=(EditText) findViewById(R.id.fname);
           sname=(EditText) findViewById(R.id.sname);
           phone=(EditText) findViewById(R.id.phone);
           email=(EditText) findViewById(R.id.email);
            db_clear=(Button)findViewById(R.id.clear_db);

            //navigation Bar
           BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
           navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            //button for clearing fields
            Button bb =(Button) findViewById(R.id.clear);
            bb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fname.setText("");
                    sname.setText("");
                    phone.setText("");
                    email.setText("");
                }
            });
            //Using command button to open a new Register Activity
            Button b=(Button)findViewById(R.id.db_update);
            b.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                //Begin of json string request
               StringRequest stringRequest1 =new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String found_user = jsonObject.getString("reply");
                            Toast.makeText(getApplicationContext(),found_user+"  ",Toast.LENGTH_LONG).show();
                            TextView repo=(TextView) findViewById(R.id.reply);
                            repo.setText(found_user);
                            db_clear.setVisibility(View.VISIBLE);
                            db_clear.setText("Click here to Remove completely");

                        }catch (Exception e)
                        {

                        }

                        }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("fname", fname.getText().toString());
                        hashMap.put("sname", sname.getText().toString());
                        hashMap.put("email", email.getText().toString());
                        hashMap.put("phone",phone.getText().toString());
                        hashMap.put("owner_email",owner_email);
                        return hashMap;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(stringRequest1);

                 }
        });
            ;
}

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent myIntent = new Intent(Register.this,Retrieving_cordinates.class);
                    startActivity(myIntent);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    Intent i = new Intent(Register.this,Register.class);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.navigation_personal:

                    return true;
                            }
            return false;
        }

    };

public void test(View view)
{
    StringRequest stringRequest1 =new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                String found_user = jsonObject.getString("reply");
                Toast.makeText(getApplicationContext(),found_user+"  ",Toast.LENGTH_LONG).show();
                TextView repo=(TextView) findViewById(R.id.reply);
                repo.setText(found_user);
                db_clear.setVisibility(View.GONE);

            }catch (Exception e)
            {

            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }){

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("owner_email",owner_email);
            return hashMap;
        }
    };

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    queue.add(stringRequest1);



}
}
