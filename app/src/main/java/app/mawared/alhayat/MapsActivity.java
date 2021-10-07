package app.mawared.alhayat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageView marker;
    private TextView address;
    private TextView set_address;
    String selectedShippingType = null;
    double lat=0;
    double lng = 0;
    Spinner  map_spinner;
    EditText rec_phone, rec_name,address_name;

    LatLng preLoc;
    int id;
    String preAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        marker = findViewById(R.id.marker);
        address = findViewById(R.id.address);
       map_spinner = findViewById(R.id.map_spinner);
        rec_phone = findViewById(R.id.editText2);
        rec_name = findViewById(R.id.editText3);
        address_name = findViewById(R.id.address_name);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            double preLat = extras.getDouble("lat",0);
            double preLng = extras.getDouble("lng",0);
            preAddress = extras.getString("address",null);
            if (preLat!=0&&preLng!=0)
            {
                preLoc = new LatLng(preLat,preLng);
            }
            //The key argument here must match that used in the other activity
        }

        String[] types = new String[]{"شخصي", "مسجد", "مستلم اخر"};
        String[] types_e = new String[]{"personal", "mosque", "other"};

        ArrayAdapter<String> typesadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        map_spinner.setAdapter(typesadapter);

        map_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedShippingType = types[position];
                if (position == 0) {
                    rec_phone.setVisibility(View.GONE);
                    rec_name.setVisibility(View.GONE);
                } else {
                    rec_phone.setVisibility(View.VISIBLE);
                    rec_name.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        findViewById(R.id.btn_add_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (map_spinner.getSelectedItemPosition() != 0) {
                        if (rec_name.getText().toString().isEmpty()) {
                            Toast.makeText(MapsActivity.this, "ادخل اسم المستلم", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (rec_phone.getText().toString().isEmpty()) {
                            Toast.makeText(MapsActivity.this, "ادخل رقم الجوال للمستلم", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (address_name.getText().toString().isEmpty()){
                        Toast.makeText(MapsActivity.this, "ادخل اسم العنوان", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    //  Toast.makeText(getContext(), types_e[map_spinner.getSelectedItemPosition()], Toast.LENGTH_SHORT).show();

                Paper.book().write("latlng",new LatLng(lat,lng));
                Paper.book().write("address",new AddressModel(lat,lng,rec_phone.getText().toString(),address.getText().toString(),rec_name.getText().toString(),types_e[map_spinner.getSelectedItemPosition()],false));

            //  double lat, double lng, String mobile, String address, String username,String type, boolean isAdded)

                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                MapsActivity.this.finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (preLoc==null)
        checkLocPermission();
        else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(preLoc,11));
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.getUiSettings().setZoomGesturesEnabled(false);

        }
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Location temp = new Location(LocationManager.GPS_PROVIDER);
                temp.setLatitude(mMap.getCameraPosition().target.latitude);
                temp.setLongitude(mMap.getCameraPosition().target.longitude);

                lat = temp.getLatitude();
                lng = temp.getLongitude();



                try {
                    if (preAddress==null)
                    address.setText(getAddress(temp));
                    else
                        address.setText(preAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location temp = new Location(LocationManager.GPS_PROVIDER);
                temp.setLatitude(mMap.getCameraPosition().target.latitude);
                temp.setLongitude(mMap.getCameraPosition().target.longitude);

                try {
                    getAddress(temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    String getAddress(Location loc) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        if (addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            System.out.println("address is " + address);

            String city = addresses.get(0).getLocality();
            System.out.println("city is " + city);
            String state = addresses.get(0).getAdminArea();
            System.out.println("state is " + state);

            String country = addresses.get(0).getCountryName();
            System.out.println("country is " + country);

            String postalCode = addresses.get(0).getPostalCode();
            System.out.println("postalCode is " + postalCode);

            String knownName = addresses.get(0).getFeatureName();
            System.out.println("knownName is " + knownName);
            return address;
        }


        return "undefined";

    }

    void checkLocPermission() {
        Dexter.withContext(MapsActivity.this)
                .withPermissions(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (mMap != null) {
                                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    return;
                                }
                                mMap.setMyLocationEnabled(true);
                            }
                            SmartLocation.with(MapsActivity.this).location()
                                    //.oneFix()
                                    .start(new OnLocationUpdatedListener() {
                                        @Override
                                        public void onLocationUpdated(Location location) {

                                            try {
                                                if (mMap!=null){
                                                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                                                    //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
                                                }
                                                System.out.println(getAddress(location));
                                            } catch (NullPointerException | IOException e){
                                                e.printStackTrace();

                                            }

                                        }
                                    });
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(MapsActivity.this, "قم بالسماح للتطبيق للوصول الى موقعك من خلال الاعدادات", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();


                    }
                })
                .onSameThread()
                .check();
    }
}