package ke.co.alanigroupltd.marketerslounge;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static ke.co.alanigroupltd.marketerslounge.R.id.map;

public class Retrieving_cordinates1 extends AppCompatActivity implements OnMapReadyCallback {
    private static final String URL = "http://192.168.88.70/markets/marketers_retrieve.php";
    private static final String URL2 = "http://192.168.88.70/markets/distributor_retrieve.php";
    private static final String URL_task = "http://192.168.88.70/markets/allocate_task.php";
    private static final String URL_task_cleared = "http://192.168.88.70/markets/submit_task.php";
    private static final String URL_login = "http://192.168.88.70/markets/login_retrieve.php";

    private TextView t, tt;
    private LocationManager locationManager;
    private LocationListener listener;
    private GoogleMap mMap;
    private EditText address;
    private Double xx, yy;
    private String y, token;
    private Marker currentLocationMarker;
    private String pphone;
    private String ffname;
    private String tarehe;
    private String activate;
    private  TextView ar;

    private String ssname;
    private String owner_email = null;
    private TextView mTextField;
    private CountDownTimer myCountDownTimer;

    private TextView allocateName;
    private EditText taskname;
    private EditText goal;
    private EditText exp;
    private Button submit;

    private TextView distributorName;
    private EditText initial_state;
    private EditText action_taken;
    private EditText final_verdict;
    private EditText expiry1;
    private Button submit1;

    private String spinner_user_selected;
    private EditText mail;
    private EditText password;
    private EditText user_level;
    private Button loginbtn;

    @Override
    public void onPause(){
        super.onPause();
        myCountDownTimer.cancel();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.marketers_retrieve);
            //getting device email
            Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(Retrieving_cordinates1.this).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {
                    owner_email = account.name;
                }
            }

             // Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                //startService(i);

            //navigation
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            //Finding the map on the Activity
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map);
            mapFragment.getMapAsync(Retrieving_cordinates1.this);

            Button b=(Button)findViewById(R.id.button3);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*String url3 = "https://www.4shared.com/s/fKA_ujYoCca";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url3));
                    startActivity(i);*/


                }
            });
            runtime_permissions();
            retrieving_multiple();
            retrieving_multiple_distributor();
            login();
           // gettingCordinates();
            }
        catch(Exception e){
                //  Toast.makeText(getActivity(), "Google Maps problem", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public void onMapReady (GoogleMap googleMap){

            mMap = googleMap;
            retrieving_multiple();
            retrieving_multiple_distributor();
               }


        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent myIntent = new Intent(Retrieving_cordinates1.this, Retrieving_cordinates.class);
                        startActivity(myIntent);
                        myCountDownTimer.cancel();
                        finish();
                        return true;
                    case R.id.navigation_dashboard:
                        Intent i = new Intent(Retrieving_cordinates1.this, Register.class);
                        startActivity(i);
                        myCountDownTimer.cancel();
                        finish();
                        return true;
                    case R.id.navigation_personal:
                        return true;
                    case R.id.mytasks:
                        return true;
                   }
                return false;
           }

        };

        public void gettingCordinates() {
            try{

            mTextField = (TextView) findViewById(R.id.mTextField);
            myCountDownTimer = new CountDownTimer(1000000, 20000) {

                public void onTick(long millisUntilFinished) {

                    Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
                    Account[] accounts = AccountManager.get(Retrieving_cordinates1.this).getAccounts();
                    for (Account account : accounts) {
                        if (gmailPattern.matcher(account.name).matches()) {
                            owner_email = account.name;
                        }
                    }


                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject = null;
                            try {


                                jsonObject = new JSONObject(response);
                                String lat = jsonObject.getString("latitude");
                                String lon = jsonObject.getString("longitude");
                                pphone = jsonObject.getString("phone");
                                ffname = jsonObject.getString("fname");
                                ssname = jsonObject.getString("sname");
                                tarehe = jsonObject.getString("tarehe");
                                activate= jsonObject.getString("activate");
                                tt=(TextView)findViewById(R.id.textView2);
                                tt.setText(activate);

                                //convert string latitude to double
                                Double latitude1 = Double.parseDouble(lat);
                                Double longitude1 = Double.parseDouble(lon);

                                LatLng buda = new LatLng(latitude1, longitude1);
                                CameraPosition position = CameraPosition.builder()
                                        .target(buda)
                                        .zoom(15)
                                        .bearing(0)
                                        .tilt(0)
                                        .build();
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                mMap.setTrafficEnabled(true);
                                mMap.setBuildingsEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(true);

                                //lets add updated marker
                                mMap.addMarker(new MarkerOptions().position(buda).title(ffname + " " + ssname + ": " + pphone + " " + tarehe));
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("owner_email", owner_email);
                            return hashMap;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(stringRequest1);

                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    mTextField.setText("done!");
                  // gettingCordinates();
                }
            }.start();
        }catch(Exception e){}
        }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},100);
            return true;
        }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                runtime_permissions();

            } else {
                runtime_permissions();
            }
        }
    }
    public void retrieving_multiple()
    {

        JsonArrayRequest req = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    String jsonResponse = "";

                    Marker[] allMarkers = new Marker[response.length()];

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject person = (JSONObject) response.get(i);

                        String lat = person.getString("latitude");
                        String lon = person.getString("longitude");
                        pphone = person.getString("phone");
                        ffname = person.getString("fname");
                        ssname = person.getString("sname");
                        tarehe = person.getString("tarehe");
                        activate= person.getString("activate");
                        tt=(TextView)findViewById(R.id.textView2);
                        tt.setText(activate);

                        //convert string latitude to double
                        Double latitude1 = Double.parseDouble(lat);
                        Double longitude1 = Double.parseDouble(lon);

                        LatLng buda = new LatLng(latitude1, longitude1);
                        CameraPosition position = CameraPosition.builder()
                                .target(buda)
                                .zoom(10)
                                .bearing(0)
                                .tilt(0)
                                .build();
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        mMap.setTrafficEnabled(true);
                        mMap.setBuildingsEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);

                        //lets add updated marker
                        allMarkers[i]=mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.m1)).position(buda).title(ffname +" "+ ssname));
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                    }

                } catch (JSONException e)
                {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(req);
    }

    public void retrieving_multiple_distributor()
    {

        JsonArrayRequest req = new JsonArrayRequest(URL2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    String jsonResponse = "";

                    Marker[] allMarkers = new Marker[response.length()];

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject person = (JSONObject) response.get(i);

                        String lat = person.getString("latitude");
                        String lon = person.getString("longitude");
                        pphone = person.getString("phone");
                        ffname = person.getString("fname");
                        ssname = person.getString("sname");


                        //convert string latitude to double
                        Double latitude1 = Double.parseDouble(lat);
                        Double longitude1 = Double.parseDouble(lon);

                        LatLng buda = new LatLng(latitude1, longitude1);
                        CameraPosition position = CameraPosition.builder()
                                .target(buda)
                                .zoom(10)
                                .bearing(0)
                                .tilt(0)
                                .build();
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        mMap.setTrafficEnabled(true);
                        mMap.setBuildingsEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);

                        //lets add updated marker
                        allMarkers[i]=mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.d1)).position(buda).title(ffname +" "+  ssname ));
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                    }
                   // infowindow_allocate();
                  // infowindow_complete();
                    tasklist();

                } catch (JSONException e)
                {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(req);

    }
    public void infowindow_allocate()
    {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Retrieving_cordinates1.this);
                View view=getLayoutInflater().inflate(R.layout.task_allocated,null);

                //initialize views or items in allocate task
                allocateName=(TextView) view.findViewById(R.id.allocatename);
                taskname=(EditText)view.findViewById(R.id.taskname);
                goal=(EditText)view.findViewById(R.id.goals);
                exp=(EditText)view.findViewById(R.id.expiry);
                submit=(Button)view.findViewById(R.id.submit);

                //create dialog interface
                mBuilder.setView(view);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(500, 600);

                //get items to update in task allocated
                String info=marker.getTitle();
                allocateName.setText(info);
                TextView allocateTo=(TextView)view.findViewById(R.id.allocated);
                allocateTo.setText("Task Allocated To:");


                //button to submit task allocated
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequestTask = new StringRequest(Request.Method.POST, URL_task, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String repo = jsonObject.getString("reply");
                                    Toast.makeText(getApplicationContext(),repo+"  ",Toast.LENGTH_LONG).show();
                                }catch (Exception e)
                                {

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("supervisor_email", owner_email);
                                hashMap.put("marketer_name",allocateName.getText().toString());
                                hashMap.put("task_name",taskname.getText().toString());
                                hashMap.put("set_goal",goal.getText().toString());
                                hashMap.put("expiry_date",exp.getText().toString());
                                return hashMap;
                            }
                        };

                        RequestQueue task = Volley.newRequestQueue(getApplicationContext());
                        task.add(stringRequestTask);
                    }
                });
            }
        });
    }
    public void tasklist()
    {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
            Intent myintent = new Intent(Retrieving_cordinates1.this,MainActivity.class);
                finish();
                startActivity(myintent);

            }});
    }
    public void infowindow_complete()
    {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Retrieving_cordinates1.this);
                View view=getLayoutInflater().inflate(R.layout.task_complete,null);

                //initialize views or items in allocate task
                distributorName=(TextView) view.findViewById(R.id.distributor_name);
                taskname=(EditText)view.findViewById(R.id.taskname);
                initial_state=(EditText)view.findViewById(R.id.initstate);
                action_taken=(EditText)view.findViewById(R.id.actiontaken);
                final_verdict=(EditText)view.findViewById(R.id.final_verdict);
                exp=(EditText)view.findViewById(R.id.expiry);
                submit1=(Button)view.findViewById(R.id.submit_complete);

                //create dialog interface
                mBuilder.setView(view);
                AlertDialog dialog=mBuilder.create();
                Window window = dialog.getWindow();
                window.setLayout(500, 600);
                dialog.show();

                //get items to update in task allocated
                String info=marker.getTitle();
                distributorName.setText(info);
                TextView allocateTo=(TextView)view.findViewById(R.id.allocated);
                allocateTo.setText("Outlet Name:");


                //button to submit task allocated
                submit1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequestTask_cleared = new StringRequest(Request.Method.POST, URL_task_cleared, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String repo = jsonObject.getString("reply");
                                    Toast.makeText(getApplicationContext(),repo+"  ",Toast.LENGTH_LONG).show();
                                }catch (Exception e)
                                {

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("marketer_email", owner_email);
                                hashMap.put("distributor_Name",distributorName.getText().toString());
                                hashMap.put("initial_state",initial_state.getText().toString());
                                hashMap.put("action_taken",action_taken.getText().toString());
                                hashMap.put("final_verdict",final_verdict.getText().toString());
                                hashMap.put("expiry_date",exp.getText().toString());
                                hashMap.put("task_name",taskname.getText().toString());
                                return hashMap;
                            }
                        };

                        RequestQueue task_cleared = Volley.newRequestQueue(getApplicationContext());
                        task_cleared.add(stringRequestTask_cleared);
                    }
                });
            }
        });
    }


    public void login(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Retrieving_cordinates1.this);
        View view=getLayoutInflater().inflate(R.layout.login,null);

        mail=(EditText)view.findViewById(R.id.email) ;
        password=(EditText)view.findViewById(R.id.pass) ;
        loginbtn=(Button)view.findViewById(R.id.btnlogin);
        user_level=(EditText)view.findViewById(R.id.userlevel);
        TextView label=(TextView)view.findViewById(R.id.label) ;

        Spinner user_type=(Spinner) view.findViewById(R.id.user_type);

        String[] items = new String[]{"Administrator", "Personnel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Retrieving_cordinates1.this, android.R.layout.simple_spinner_dropdown_item, items);
        user_type.setAdapter(adapter);

        mBuilder.setView(view);
        AlertDialog dialog=mBuilder.create();
        dialog.show();

        //lets sen onItemSelected for the listener
        user_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        spinner_user_selected=parent.getItemAtPosition(0).toString();
                        user_level.setText(spinner_user_selected);
                        password.setText("");
                        password.setVisibility(View.VISIBLE);
                        user_level.setVisibility(View.GONE);
                        break;
                    case 1:
                        spinner_user_selected=parent.getItemAtPosition(1).toString();
                        user_level.setText(spinner_user_selected);
                        password.setVisibility(View.GONE);
                        password.setText("0");
                        user_level.setVisibility(View.GONE);
                        break;
                }
                //button to submit task allocated
                loginbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequestTask_cleared = new StringRequest(Request.Method.POST, URL_login, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);


                                    if (jsonObject.getString("success") != null) {
                                        String success = jsonObject.getString("success");
                                        Toast.makeText(getApplicationContext(),success+"  ",Toast.LENGTH_LONG).show();
                                    }
                                    if (jsonObject.getString("reply") != null)
                                    {
                                        String repo = jsonObject.getString("reply");
                                        Toast.makeText(getApplicationContext(),repo+"  ",Toast.LENGTH_LONG).show();
                                    }

                                }catch (Exception e)
                                {

                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                             HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("user_type", user_level.getText().toString());
                                hashMap.put("email",mail.getText().toString());
                                hashMap.put("password",password.getText().toString());
                                return hashMap;
                            }
                        };

                        RequestQueue task_cleared = Volley.newRequestQueue(getApplicationContext());
                        task_cleared.add(stringRequestTask_cleared);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
    }

}



