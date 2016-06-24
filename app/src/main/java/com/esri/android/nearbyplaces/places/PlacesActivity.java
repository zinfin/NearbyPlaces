package com.esri.android.nearbyplaces.places;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.esri.android.nearbyplaces.Injection;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.map.MapFragment;
import com.esri.android.nearbyplaces.map.MapPresenter;
import com.esri.android.nearbyplaces.util.ActivityUtils;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback  {

  private static final int PERMISSION_REQUEST_LOCATION = 0;
  private View mLayout;
  private PageAdapter mPageAdapter;
  private PlacesPresenter mPlacePresenter;
  private MapPresenter mMapPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    // Set up the toolbar.
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);



    final ViewPager viewPager = (ViewPager) findViewById(R.id.pager) ;
    if (viewPager != null){
      setUpViewPager(viewPager);
    }


    // Set up tabs in the main page, one for the list
    // of place the second tab for the map view
    TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    tabLayout.setupWithViewPager(viewPager);

    // request location permission
    requestLocationPermission();


  }
  /**
   * Configure tab layout
   */
  private void setUpViewPager(ViewPager viewPager){
    mPageAdapter = new PageAdapter(getSupportFragmentManager());
    PlacesFragment placesFragment = (PlacesFragment) getSupportFragmentManager().findFragmentById(R.id.placesContainer) ;

    if (placesFragment == null){
      // Create the fragment
      placesFragment = PlacesFragment.newInstance();
     /* ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), placesFragment, R.id.contentFrame);*/
    }
    mPageAdapter.addFragment(placesFragment, "LIST");

    MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);

    if (mapFragment == null){
      mapFragment = MapFragment.newInstance();
    }

    mPageAdapter.addFragment(mapFragment,"MAP");

    mPageAdapter.notifyDataSetChanged();
    viewPager.setAdapter(mPageAdapter);

    mPlacePresenter = new PlacesPresenter(Injection.providePlacesRepository(getApplicationContext()), placesFragment);
    mMapPresenter = new MapPresenter(mapFragment);
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
}
