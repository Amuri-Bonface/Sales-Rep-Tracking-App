package ke.co.alanigroupltd.marketerslounge;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static ke.co.alanigroupltd.marketerslounge.R.id.map;
public class Map_dialog extends AppCompatActivity implements GoogleMap.OnMarkerDragListener,OnMapReadyCallback {
    public GoogleMap mMap;
    public TextView posi;
    public LatLng newLocation;
    public Double distrlat;
    public Double distrlon;
    public   Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_dialog);



        //Search Button
        Button bsearch = (Button) findViewById(R.id.Bsearch);
        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
try {
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(map);
    mapFragment.getMapAsync(Map_dialog.this);

    marker = null;


    EditText location_tf = (EditText) findViewById(R.id.TFaddress);
    String location = location_tf.getText().toString();
    List<Address> addressList = null;
    if (location != null || !location.equals("")) {
        Geocoder geocoder = new Geocoder(Map_dialog.this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }


        Address address = addressList.get(0);
        Double lat = address.getLatitude();
        Double lon = address.getLongitude();
        LatLng latLng = new LatLng(lat, lon);
        CameraPosition position = CameraPosition.builder()
                .target(latLng)
                .zoom(18)
                .bearing(0)
                .tilt(0)
                .build();


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.d1)).position(latLng).title("Distributor Location").draggable(true));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));


    }
}catch (Exception ex){}
            }
        });

        //submit button
        Button submit=(Button)findViewById(R.id.btnsubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              alert();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(Map_dialog.this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(Map_dialog.this);
        mMap.setOnMarkerDragListener(this);
        //new ma

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
       newLocation=marker.getPosition();
        distrlat=newLocation.latitude;
        distrlon=newLocation.longitude;
          }

public void alert(){
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    Intent myIntent=new Intent();
                    myIntent.putExtra("latdist", distrlat.toString());
                    myIntent.putExtra("londist", distrlon.toString());
                    setResult(100, myIntent);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    AlertDialog.Builder builder = new AlertDialog.Builder(Map_dialog.this);
    builder.setMessage("Is the Location Correct?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
}
}
