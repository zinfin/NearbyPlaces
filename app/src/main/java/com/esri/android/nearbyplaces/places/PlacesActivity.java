package com.esri.android.nearbyplaces.places;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.esri.android.nearbyplaces.PlaceListener;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.filter.FilterDialogFragment;
import com.esri.android.nearbyplaces.map.MapActivity;
import com.esri.android.nearbyplaces.util.ActivityUtils;

import java.util.List;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback, PlaceListener{

  private static final String TAG = PlacesActivity.class.getSimpleName();
  private static final int PERMISSION_REQUEST_LOCATION = 0;
  private CoordinatorLayout mMainLayout;
  private PlacesPresenter mPlacePresenter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    mMainLayout = (CoordinatorLayout) findViewById(R.id.list_coordinator_layout);

    // Set up the toolbar.
    setUpToolbar();

    setUpFragments(savedInstanceState);

    // request location permission
    requestLocationPermission();

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
     Toolbar toolbar = (Toolbar) findViewById(R.id.placeList_toolbar);
     setSupportActionBar(toolbar);
     toolbar.setTitle("");
     final ActionBar ab = getSupportActionBar();
     ab.setDisplayHomeAsUpEnabled(true);

     toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
       @Override public boolean onMenuItemClick(MenuItem item) {
         if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.map_view))){
           // Hide the list, show the map
          showMap(item);
         }
         if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.filter))){
           FilterDialogFragment dialogFragment = new FilterDialogFragment();
           dialogFragment.show(getFragmentManager(),"dialog_fragment");

         }
         return false;
       }
     });
   }

  private void showMap(MenuItem item){
    Intent intent = new Intent(PlacesActivity.this, MapActivity.class);
    startActivity(intent);
  }

  /**
   * Set up fragments
   */
  private void setUpFragments(Bundle savedInstanceState){

    PlacesFragment placesFragment = (PlacesFragment) getSupportFragmentManager().findFragmentById(R.id.recycleView) ;

    if (placesFragment == null){
      // Create the fragment
      placesFragment = PlacesFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), placesFragment, R.id.list_fragment_container, "list fragment");
    }

    mPlacePresenter = new PlacesPresenter (placesFragment);

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
      Snackbar.make(mMainLayout, "Location access is required to search for places nearby.", Snackbar.LENGTH_INDEFINITE)
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
        Snackbar.make(mMainLayout, "Location permission request was denied.", Snackbar.LENGTH_SHORT).show();
      }
    }
  }


  @Override public void onPlacesFound(List<Place> places) {

  }

  @Override public void onPlaceSearch() {

  }

  @Override public void showDetail(Place place) {

  }

  @Override public void onMapScroll() {

  }
}
