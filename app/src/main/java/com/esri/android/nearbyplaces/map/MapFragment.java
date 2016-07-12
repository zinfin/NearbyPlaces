package com.esri.android.nearbyplaces.map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.esri.android.nearbyplaces.NearbyPlaces;
import com.esri.android.nearbyplaces.PlaceListener;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.CategoryHelper;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;

import java.util.List;

/**
 * Created by sand8529 on 6/24/16.
 */
public class MapFragment extends Fragment implements  MapContract.View {

  private MapContract.Presenter mPresenter;

  private FrameLayout mMapLayout;

  private MapView mMapView;

  private LocationDisplay mLocationDisplay;

  private GraphicsOverlay mGraphicOverlay;

  private PlaceListener mCallback;

  private boolean initialLocationLoaded =false;

  private final static String TAG = MapFragment.class.getSimpleName();

  public MapFragment(){}

  public static MapFragment newInstance(){
    return new MapFragment();
  }

  @Override
  public void onCreate(@NonNull Bundle savedInstance){
    super.onCreate(savedInstance);
  }

  @Override
  @Nullable
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
      Bundle savedInstance){

    View root = layoutInflater.inflate(R.layout.map_fragment, container,false);
    setUpMapView(root);
    return root;
  }

  /**
   * Add the map to the view and set up location display
   * @param root View
   */
  private void setUpMapView(View root){

    mMapView = (MapView) root.findViewById(R.id.map);
    mLocationDisplay = mMapView.getLocationDisplay();
    mLocationDisplay.startAsync();


    mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);

    Basemap basemap = Basemap.createStreets();
    ArcGISMap map = new ArcGISMap(basemap);
    mMapView.setMap(map);

    // Add graphics overlay for map markers
    mGraphicOverlay  = new GraphicsOverlay();
    mMapView.getGraphicsOverlays().add(mGraphicOverlay);


  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mCallback = (PlaceListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement PlacesListener");
    }

  }
  /**
   * Attach the viewpoint change listener
   * so that POIs get updated as map's
   * visible area is changed.
   */
  private void setNavigationCompletedListener(){
    mMapView.addNavigationCompletedListener(new NavigationCompletedListener() {
      @Override public void navigationCompleted(NavigationCompletedEvent navigationCompletedEvent) {
        mCallback.onPlaceSearch();
        mPresenter.findPlacesNearby();
      }
    });
  }

  @Override
  public void onResume(){
    super.onResume();
    mMapView.resume();
   if (!mLocationDisplay.isStarted()){
      mLocationDisplay.startAsync();
    }
    Log.i(TAG, "Map fragment onResume " + "and location display is " + mLocationDisplay.isStarted());
    mPresenter.start();
  }

  @Override
  public void onPause(){
    super.onPause();
    mMapView.pause();
   if (mLocationDisplay.isStarted()){
      mLocationDisplay.stop();
    }
    Log.i(TAG, "Map fragment onPause " + "and location display is " + mLocationDisplay.isStarted());
  }

  /**
   * If any places are found,
   * add them to the map as graphics.
   * @param places List of Place items
   */
  @Override public void showNearbyPlaces(List<Place> places) {
    if (!initialLocationLoaded){
      setNavigationCompletedListener();
    }
    initialLocationLoaded = true;
    if (places.isEmpty()){
      Toast.makeText(getContext(),getString(R.string.no_places_found),Toast.LENGTH_SHORT).show();
      return;
    }
    // Clear out any existing graphics
    mGraphicOverlay.getGraphics().clear();

    // Create a graphic for every place
    for (Place place : places){
      BitmapDrawable pin = (BitmapDrawable) ContextCompat.getDrawable(this.getActivity(),getDrawableForPlace(place)) ;
      final PictureMarkerSymbol pinSymbol = new PictureMarkerSymbol(pin);
      Point graphicPoint = place.getLocation();
      Graphic graphic = new Graphic(graphicPoint, pinSymbol);
      mGraphicOverlay.getGraphics().add(graphic);
    }
  }


  /**
   * Assign appropriate drawable given place type
   * @param p - Place
   * @return - Drawable
   */
  private int getDrawableForPlace(Place p){
    String category = CategoryHelper.getCategoryForFoodType(p.getType());

    Integer d = null;
    switch (category){
      case "Pizza":
        d =  R.drawable.pizza_pin;
        break;
      case "Hotel":
        d =  R.drawable.hotel_pin;
        break;
      case "Food":
        d = R.drawable.restaurant_pin;
        break;
      case "Bar or Pub":
        d =  R.drawable.bar_pin;
        break;
      case "Bookstore":
        d =  R.drawable.empty_pin;
        break;
      case "Coffee Shop":
        d = R.drawable.cafe_pin;
        break;
      default:
        d = R.drawable.empty_pin;
    }
    return d;
  }

  @Override public MapView getMapView() {
    return mMapView;
  }

  @Override public LocationDisplay getLocationDisplay() {
    return mLocationDisplay;
  }

  @Override public void setPresenter(MapContract.Presenter presenter) {
    mPresenter = presenter;
  }


}
