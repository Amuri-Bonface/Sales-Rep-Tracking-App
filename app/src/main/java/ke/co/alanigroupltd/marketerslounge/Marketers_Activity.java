package ke.co.alanigroupltd.marketerslounge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Marketers_Activity extends FragmentActivity implements OnMapReadyCallback {
    private static final String URL = "http://10.0.2.2/markets/marketers.php";
    private Button b;
    private TextView t,tt;
    private LocationManager locationManager;
    private LocationListener listener;
    private GoogleMap mMap;
    private EditText address;
    private Double xx,yy;
    private String y,token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.marketers_activity);

            t = (TextView) findViewById(R.id.textView);
            b = (Button) findViewById(R.id.clear);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

          locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            listener = new LocationListener() {

          @Override
            public void onLocationChanged(Location location) {

              address=(EditText)findViewById(R.id.Address);
              t.append("\n "+location.getLongitude()+" "+location.getLatitude());
                  xx = location.getLongitude();
                   yy = location.getLatitude();
                    token="1";

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                y=currentDateTimeString;
                LatLng juja = new LatLng(yy, xx);
                CameraPosition position = CameraPosition.builder()
                      .target(juja)
                      .zoom(15)
                      .bearing(0)
                      .tilt(0)
                      .build();

              mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              mMap.addMarker(new MarkerOptions().position(juja).title("Alani Marketer"+y));
              mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
              //noinspection MissingPermission
             // mMap.setMyLocationEnabled(true);

              StringRequest stringRequest1 =new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {

                  }
              }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {

                  }
              }){

                  @Override
                  protected Map<String, String> getParams() throws AuthFailureError {

                      HashMap<String, String> hashMap = new HashMap<>();
                      hashMap.put("longitude", xx.toString());
                      hashMap.put("latitude", yy.toString());
                      hashMap.put("token", token);
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
                startActivity(i);
            }
        };

        configure_button();
    } catch (Exception e) {
        //  Toast.makeText(getActivity(), "Google Maps problem", Toast.LENGTH_SHORT).show();

    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                    configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
                //noinspection MissingPermission
                mMap.setMyLocationEnabled(true);

            }
        });
   }

       @Override
         public void onMapReady(GoogleMap googleMap) {
           mMap = googleMap;
            LatLng juja = new LatLng(-1.102997, 37.020587);
            CameraPosition position = CameraPosition.builder()
                    .target(juja)
                    .zoom(15f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();


            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),null);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.addMarker(new MarkerOptions().position(juja).title("Alani Marketer"));

           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                           ,10);
               }
               return;
           }

                  //noinspection MissingPermission
           locationManager.requestLocationUpdates("gps", 50000, 0, listener);
           mMap.setMyLocationEnabled(true);
       }

}


