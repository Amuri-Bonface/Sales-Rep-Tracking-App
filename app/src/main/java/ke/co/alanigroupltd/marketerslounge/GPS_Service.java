package ke.co.alanigroupltd.marketerslounge;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Patterns;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GPS_Service extends Service {
    private static final String URL = "http://10.0.2.2/markets/marketers.php";
    private Double xx, yy;
    private String y, token;
    private String DataString;
    private LocationListener listener;
    private LocationManager locationManager;
    private String email = null;
    CountDownTimer myCountDownTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        //trying to get the primary registered email address

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(GPS_Service.this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                email = account.name;
            }
        }
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //getting location updates from the device
                DataString = DateFormat.getTimeInstance(DateFormat.LONG).format
                        (Calendar.getInstance().getTime());

               /* Intent i = new Intent("location_update");
                i.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude() + " " + email + DataString);
                sendBroadcast(i);*/

                //send data to server
                xx = location.getLongitude();
                yy = location.getLatitude();

                token = "1";
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("longitude", xx.toString());
                        hashMap.put("latitude", yy.toString());
                        hashMap.put("token", token);
                        hashMap.put("email", email);
                        hashMap.put("tarehe", DataString);
                        return hashMap;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(stringRequest1);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, listener);

        myCountDownTimer=new CountDownTimer(1000000, 20000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // initialize the two classes used for sending msgs with call records to server

            }

            @Override
            public void onFinish() {
            }
        }.start();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }


}
