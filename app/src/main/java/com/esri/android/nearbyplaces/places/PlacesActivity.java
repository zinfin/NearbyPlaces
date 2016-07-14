package com.esri.android.nearbyplaces.places;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.ProgressBar;
import com.esri.android.nearbyplaces.NearbyPlaces;
import com.esri.android.nearbyplaces.PlaceListener;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.map.MapFragment;
import com.esri.android.nearbyplaces.map.MapPresenter;
import com.esri.android.nearbyplaces.mapplace.MapPlaceMediator;
import com.esri.android.nearbyplaces.placeDetail.PlaceDetailActivity;
import com.esri.android.nearbyplaces.util.ActivityUtils;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback, PlaceListener{

  private static final String TAG = PlacesActivity.class.getSimpleName();
  private static final int PERMISSION_REQUEST_LOCATION = 0;
  private View mLayout;
  private PlacesPresenter mPlacePresenter;
  private MapPresenter mMapPresenter;
  private ProgressBar mProgressBar;
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    // Set up the toolbar.
    setUpToolbar();

    setUpFragments(savedInstanceState);

    // request location permission
    requestLocationPermission();

    // Get the progress bar
    mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Set up toolbar
   */
   private void setUpToolbar(){
     Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     setSupportActionBar(toolbar);
     toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
       @Override public boolean onMenuItemClick(MenuItem item) {
         if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.map_view))){
           // Hide the list, show the map
           findViewById(R.id.recycleView).setVisibility(View.INVISIBLE);
           findViewById(R.id.map).setVisibility(View.VISIBLE);
           Log.i(TAG, "Show map");
           // Change the menu
           item.setIcon(getDrawable(R.drawable.ic_list_black_24dp));
           item.setTitle(R.string.list_view);
         }else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.list_view))){
           // Hide the map, show the list
           findViewById(R.id.map).setVisibility(View.INVISIBLE);
           findViewById(R.id.recycleView).setVisibility(View.VISIBLE);

           item.setIcon(getDrawable(android.R.drawable.ic_menu_mapmode));
           item.setTitle(R.string.map_view);
           Log.i(TAG, "Show list");
         }
         return false;
       }
     });
   }


  /**
   * Set up fragments
   */
  private void setUpFragments(Bundle savedInstanceState){




    MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    if (mapFragment == null){
      mapFragment = MapFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), mapFragment, R.id.fragment_container);
    }

    PlacesFragment placesFragment = (PlacesFragment) getSupportFragmentManager().findFragmentById(R.id.recycleView) ;

    if (placesFragment == null){
      // Create the fragment
      placesFragment = PlacesFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), placesFragment, R.id.fragment_container);
    }

    MapPlaceMediator mapPlacePresenter = new MapPlaceMediator();
    mPlacePresenter = new PlacesPresenter( placesFragment, mapPlacePresenter);
    mMapPresenter = new MapPresenter(mapFragment, mapPlacePresenter);
  }

  /**
   * Requests the {@link Manifest.permission#ACCESS_FINE_LOCATION}
   * permission. If an additional rationale should be displayed, the user has
   * to launch the request from a SnackBar that includes additional
   * information.
   */

  private void requestLocationPermission() {
    // Permission has not been granted and must be requested.
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

      // Provide an additional rationale to the user if the permission was
      // not granted
      // and the user would benefit from additional context for the use of
      // the permission.
      // Display a SnackBar with a button to request the missing
      // permission.
      Snackbar.make(mLayout, "Location access is required to search for places nearby.", Snackbar.LENGTH_INDEFINITE)
          .setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Request the permission
              ActivityCompat.requestPermissions(PlacesActivity.this,
                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                  PERMISSION_REQUEST_LOCATION);
            }
          }).show();

    } else {
      // Request the permission. The result will be received in
      // onRequestPermissionResult().
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
          PERMISSION_REQUEST_LOCATION);
    }
  }
  /**
   * Once the app has prompted for permission to access location, the response
   * from the user is handled here. If permission exists to access location
   * check if GPS is available and device is not in airplane mode.
   *
   * @param requestCode
   *            int: The request code passed into requestPermissions
   * @param permissions
   *            String: The requested permission(s).
   * @param grantResults
   *            int: The grant results for the permission(s). This will be
   *            either PERMISSION_GRANTED or PERMISSION_DENIED
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    if (requestCode == PERMISSION_REQUEST_LOCATION) {
      if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // We're good to go

      } else {
        // Permission request was denied.
        Snackbar.make(mLayout, "Location permission request was denied.", Snackbar.LENGTH_SHORT).show();
      }
    }
  }
  private void hideProgressBar(){
    mProgressBar.setVisibility(View.INVISIBLE);
  }

  private void showProgressBar(){mProgressBar.setVisibility(View.VISIBLE);}

  @Override public void onPlacesFound(List<Place> places) {
    hideProgressBar();
  }

  @Override public void onPlaceSearch() {
    showProgressBar();
  }

  /**
   * Broadcast an intent to show the
   * map/detail view
   * @param place
   */
  @Override public void showDetail(Place place) {
    Intent intent = new Intent(this, PlaceDetailActivity.class);
    intent.putExtra("PLACE_NAME",place.getName());
    startActivity(intent);
  }
}
