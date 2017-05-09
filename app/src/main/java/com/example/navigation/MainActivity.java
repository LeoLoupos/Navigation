package com.example.navigation;

import android.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED;

public class MainActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks  , GoogleApiClient.OnConnectionFailedListener , LocationListener,NavigationView.OnNavigationItemSelectedListener {
    /*
    public Bundle b ;
    public String personName ;
    public String personGivenName ;
    public String personFamilyName ;
    public String personEmail ;
    public String personId ;
    public String personPhoto ;
*/
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .build();

        /*
        b = new Bundle();
        b = this.getIntent().getExtras();
        String personName = b.getString("personName");
        String personGivenName = b.getString("personGivenName");
        String personFamilyName = b.getString("personFamilyName");
        String personEmail = b.getString("personEmail");
        String personId = b.getString("personId");
        Toast.makeText(getApplicationContext(),personEmail,Toast.LENGTH_LONG);*/

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.requestTransparentRegion(vpPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }


    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

    }

    protected void onStop() {
        // Disconnecting the client invalidates it.
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);

        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            //mGoogleApiClient.disconnect();
            //Toast.makeText(this, "disconnected", Toast.LENGTH_SHORT).show();
        }

        super.onStop();
    }

    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("DEBUG", "current location: eRror ");

            return;
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        Toast.makeText(getApplicationContext(), "loc:" , Toast.LENGTH_LONG).show();

        if (mCurrentLocation != null) {
            // Print current location if not null
            Toast.makeText(getApplicationContext(), "loc:" + mCurrentLocation.getAltitude(), Toast.LENGTH_LONG).show();

            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();
    }



        @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

        /*
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();

    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;
        Bundle args = new Bundle();
        public MyPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);

        }
        /*
        public MyPagerAdapter(FragmentManager fragmentManager,String personName ,
                              String personGivenName ,
                              String personFamilyName ,
                              String personEmail ,
                              String personId ) {
            super(fragmentManager);
            args.putString("personName",personName);
            args.putString("personGivenName",personGivenName);
            args.putString("personFamilyName",personFamilyName);
            args.putString("personEmail",personEmail);
            args.putString("personId",personId);

        }*/

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragmen
                    return  MapFrag.newInstance();
                    //return ListFrag.newInstance(args.getString("personName"),args.getString("personGivenName"),args.getString("personName"),args.getString("personName"),args.getString("personName"));//ListFrag.newInstance(0);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                //return ListFrag.newInstance(args.getString("personName"),args.getString("personGivenName"),args.getString("personName"),args.getString("personName"),args.getString("personName"));
                return MapFrag.newInstance();//SupportMapFragment.newInstance(); //MapFrag.newInstance();
                //FirstFragment.newInstance(0, "List");
                // case 2: // Fragment # 1 - This will show SecondFragment
                // return SupportMapFragment.newInstance();
                default:
                    return null;
            }


            //vpPager.getCurrentItem();

            //vpPager.setCurrentItem(2)

        }
    }
        public static class ListFrag extends Fragment {


            private String title;
            private int page;

            private ImageView profile_pic = null;
            private TextView tv = null;
            private Button logoutButton = null;
            //private Profile profile = null;
            String personName ;
            String personGivenName ;
            String personFamilyName ;
            String personEmail ;
            String personId ;
            public static ListFrag newInstance(){
                ListFrag fragmentFirst = new ListFrag();
                return  fragmentFirst;
            }

            // newInstance constructor for creating fragment with arguments
            /*
            public static ListFrag newInstance(String personName ,
                    String personGivenName ,
                    String personFamilyName ,
                    String personEmail ,
                    String personId
                ) {
                ListFrag fragmentFirst = new ListFrag();
                Bundle args = new Bundle();
                args.putString("personName",personName);
                args.putString("personGivenName",personGivenName);
                args.putString("personFamilyName",personFamilyName);
                args.putString("personEmail",personEmail);
                args.putString("personId",personId);
                fragmentFirst.setArguments(args);
                return fragmentFirst;
            }*/

            @Override
            public View onCreateView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.list_frag, container, false);


                TextView tvLabel = (TextView) view.findViewById(R.id.textView3);
                tvLabel.setText(personName + " " + personEmail );

                return view;

            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

            }
        }

        public static class MapFrag extends Fragment implements OnMapReadyCallback {

        MapView gMapView ;
        GoogleMap gMap = null;

        public static MapFrag newInstance(){
            MapFrag fragmentFirst = new MapFrag();

            return  fragmentFirst;
        }


            @Override
            public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);


            }

            @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.map_frag,container,false);
            gMapView = (MapView) view.findViewById(R.id.gViewmap);
            gMapView.getMapAsync(this);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onMapReady(GoogleMap map){
            gMap = map;
            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.39,-124.83),20));

        }
    }







}


