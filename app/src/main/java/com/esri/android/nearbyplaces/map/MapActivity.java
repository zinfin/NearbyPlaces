package com.esri.android.nearbyplaces.map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.esri.android.nearbyplaces.PlaceListener;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.CategoryHelper;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.places.PlacesActivity;
import com.esri.android.nearbyplaces.util.ActivityUtils;

import java.util.List;

/**
 * Created by sand8529 on 7/27/16.
 */
public class MapActivity extends AppCompatActivity implements PlaceListener {

  private BottomSheetBehavior bottomSheetBehavior;
  private FrameLayout mBottomSheet;
  private CoordinatorLayout mMapLayout;
  private boolean mShowSnackbar = false;
  private MapPresenter mMapPresenter;
  private List placesFound;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map_layout);

    mMapLayout = (CoordinatorLayout) findViewById(R.id.map_coordinator_layout);

    // Set up the toolbar
    setUpToolbar();


    AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.map_appbar);

    // Sets the outline of the shadow to the background color
    // of the layout (transparent, in this case)
    appBarLayout.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

    //Set up behavior for the bottom sheet
    bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_card_view));

    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(View bottomSheet, int newState) {
        invalidateOptionsMenu();
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

    setUpFragments(savedInstanceState);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.map_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }
  private void setUpToolbar(){
    Toolbar toolbar = (Toolbar) findViewById(R.id.map_toolbar);
    setSupportActionBar(toolbar);

    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
           @Override public boolean onMenuItemClick(MenuItem item) {
             if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.list_view))){
               // Show the list of places
              showList();
             }
             return false;
           }
         });
  }

  private void showList(){
    Intent intent = new Intent(MapActivity.this, PlacesActivity.class);

    startActivity(intent);
  }
  /**
   * Set up fragments
   */
  private void setUpFragments(Bundle savedInstanceState){
    MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    if (mapFragment == null){
      mapFragment = MapFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), mapFragment, R.id.map_fragment_container, "map fragment");
    }
    mMapPresenter = new MapPresenter(mapFragment);

  }

  private void showSearchSnackbar(){
    // Show snackbar prompting user about
    // scanning for new locations
    Snackbar snackbar = Snackbar
        .make(mMapLayout, "Search for places?", Snackbar.LENGTH_LONG)
        .setAction("SEARCH", new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mMapPresenter.findPlacesNearby();
          }
        });

    snackbar.show();
  }

  @Override public void onPlacesFound(List<Place> places) {
    placesFound = places;
  }

  @Override public void onPlaceSearch() {

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
    //toggleAppBarLayout(true);

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

  @Override public void onMapScroll() {
    //Dismiss bottom sheet
    mShowSnackbar = true;
    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){ // show snackbar prompting for re-doing search
      showSearchSnackbar();
    }else{
      bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

  }

  /**
   * Set the menu options based on
   * the bottom sheet state
   *
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu){
    MenuItem listItem = menu.findItem(R.id.list_action);
    MenuItem routeItem = menu.findItem(R.id.route_action);
    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
      listItem.setVisible(true);
      routeItem.setVisible(false);
    }else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
      listItem.setVisible(false);
      routeItem.setVisible(true);
    }
    return super.onPrepareOptionsMenu(menu);
  }
}
