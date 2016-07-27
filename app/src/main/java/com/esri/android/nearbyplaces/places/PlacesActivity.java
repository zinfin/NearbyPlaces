package com.esri.android.nearbyplaces.places;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.esri.android.nearbyplaces.PlaceListener;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.CategoryHelper;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.map.MapFragment;
import com.esri.android.nearbyplaces.map.MapPresenter;
import com.esri.android.nearbyplaces.mapplace.MapPlaceMediator;
import com.esri.android.nearbyplaces.util.ActivityUtils;

import java.util.List;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback, PlaceListener{

  private static final String TAG = PlacesActivity.class.getSimpleName();
  private static final int PERMISSION_REQUEST_LOCATION = 0;
  private CoordinatorLayout mLayout;
  private PlacesPresenter mPlacePresenter;
  private MapPresenter mMapPresenter;
  private ProgressBar mProgressBar;
  private BottomSheetBehavior bottomSheetBehavior;
  private FrameLayout mBottomSheet;
  private boolean mShowSnackbar = false;
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    mLayout = (CoordinatorLayout) findViewById(R.id.main_layout);

    // Set up the toolbar.
    setUpToolbar();

    setUpFragments(savedInstanceState);

    // request location permission
    requestLocationPermission();

    // Get the progress bar
    mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

    //Set up behavior for the bottom sheet
    bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_card_view));

    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(View bottomSheet, int newState) {
        if (newState == BottomSheetBehavior.STATE_COLLAPSED && mShowSnackbar) {
          showSearchSnackbar();
          mShowSnackbar = false;
        }
      }

      @Override
      public void onSlide(View bottomSheet, float slideOffset) {
      }
    });

    mBottomSheet = (FrameLayout) findViewById(R.id.bottom_card_view);
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
          showMap(item);
         }else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.list_view))){
           // Hide the map, show the list
          showList(item);
         }
         return false;
       }
     });
   }

  private void showMap(MenuItem item){
    findViewById(R.id.recycleView).setVisibility(View.INVISIBLE);
    findViewById(R.id.map).setVisibility(View.VISIBLE);

    Log.i(TAG, "Show map");
    // Change the menu
    item.setIcon(getDrawable(R.drawable.ic_list_white_24px));
    item.setTitle(R.string.list_view);

    // Lock the toolbar
    AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
    appBarLayout.setExpanded(true,true);

  }
  private void showList(MenuItem item){
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    findViewById(R.id.map).setVisibility(View.INVISIBLE);
    findViewById(R.id.recycleView).setVisibility(View.VISIBLE);

    item.setIcon(getDrawable(android.R.drawable.ic_menu_mapmode));
    item.setTitle(R.string.map_view);
    Log.i(TAG, "Show list");
  }
  /**
   * Set up fragments
   */
  private void setUpFragments(Bundle savedInstanceState){

    MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    if (mapFragment == null){
      mapFragment = MapFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), mapFragment, R.id.fragment_container, "map fragment");
    }

    PlacesFragment placesFragment = (PlacesFragment) getSupportFragmentManager().findFragmentById(R.id.recycleView) ;

    if (placesFragment == null){
      // Create the fragment
      placesFragment = PlacesFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), placesFragment, R.id.fragment_container, "list fragment");
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
   * @param place
   */
  @Override public void showDetail(Place place) {
    // Get the menu item and show the map
    //invalidateOptionsMenu();

    // Change the background of the app bar layout
    // and add icons for closing detail and
    // requesting routing
    toggleAppBarLayout(true);

    TextView txtName = (TextView) mBottomSheet.findViewById(R.id.placeName);
    txtName.setText(place.getName());
    TextView txtAddress = (TextView) mBottomSheet.findViewById(R.id.placeAddress) ;
    txtAddress.setText(place.getAddress());
    TextView txtPhone  = (TextView) mBottomSheet.findViewById(R.id.placePhone) ;
    txtPhone.setText(place.getPhone());
    TextView txtUrl = (TextView) mBottomSheet.findViewById(R.id.placeUrl);
    txtUrl.setText(place.getURL());
    TextView txtType = (TextView) mBottomSheet.findViewById(R.id.placeType) ;
    txtType.setText(place.getType());

    // Assign the appropriate icon
    Drawable d =   CategoryHelper.getDrawableForPlace(place, this) ;
    ImageView icon = (ImageView) findViewById(R.id.TypeIcon);
    icon.setImageDrawable(d);
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    // Center map on selected place
    mMapPresenter.centerOnPlace(place);
    mShowSnackbar = false;
  }

  private void toggleAppBarLayout(boolean show){
    if (show){
      AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
      appBarLayout.setMinimumHeight(100);
      appBarLayout.setBackground(ResourcesCompat.getDrawable(this.getResources(), R.drawable.gradient,null));
      LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.app_bar_layout, null);
      appBarLayout.addView(linearLayout);
    }else{
      mLayout.removeView(findViewById(R.id.appbar_linear_layout));
    }
  }
  @Override public void onMapScroll() {
    //Dismiss bottom sheet
    mShowSnackbar = true;
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
  }

  private void showSearchSnackbar(){
    // Show snackbar prompting user about
    // scanning for new locations
    Snackbar snackbar = Snackbar
        .make(mLayout, "Search for places?", Snackbar.LENGTH_LONG)
        .setAction("SEARCH", new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mMapPresenter.findPlacesNearby();
          }
        });

    snackbar.show();
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu){
    showMap(menu.findItem(R.id.map_action));
    return true;
  }
}
