package com.esri.android.nearbyplaces.map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.esri.android.nearbyplaces.PlaceListener;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.CategoryHelper;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by sand8529 on 6/24/16.
 */
public class MapFragment extends Fragment implements  MapContract.View {

  private MapContract.Presenter mPresenter;

  private FrameLayout mMapLayout;

  private MapView mMapView;

  private LocationDisplay mLocationDisplay;

  private GraphicsOverlay mGraphicOverlay;


  private boolean initialLocationLoaded =false;

  private boolean mCenteringOnPlace = false;

  private Graphic mCenteredGraphic = null;

  private Place mCenteredPlace = null;

  private PlaceListener mCallback;

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

    mMapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
      @Override public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
        if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED){
          mPresenter.start();
          mMapView.removeDrawStatusChangedListener(this);
        }
      }
    });

    // Setup OnTouchListener to detect and act on long-press
    mMapView.setOnTouchListener(new MapTouchListener(getActivity().getApplicationContext(), mMapView));
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
        if (!mCenteringOnPlace){
          mCallback.onPlaceSearch();
          mPresenter.findPlacesNearby();
        }
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
  //  mPresenter.start();
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
    int d = CategoryHelper.getPinForPlace(p);
    return d;
  }
  private int getPinForCenterPlace(Place p){
    return CategoryHelper.getPinForCenterPlace(p);
  }

  @Override public MapView getMapView() {
    return mMapView;
  }

  @Override public LocationDisplay getLocationDisplay() {
    return mLocationDisplay;
  }

  /**
   * Center the selected place and change the pin
   * color to blue.
   * @param p
   */
  @Override public void centerOnPlace(Place p) {

    mCenteringOnPlace = true;
    mMapView.setViewpointCenterAsync(p.getLocation());
    // Change the pin icon
    if (mCenteredGraphic != null){
      BitmapDrawable oldPin = (BitmapDrawable) ContextCompat.getDrawable(this.getActivity(),getDrawableForPlace(mCenteredPlace)) ;
      mCenteredGraphic.setSymbol(new PictureMarkerSymbol(oldPin));
    }
    List<Graphic> graphics = mGraphicOverlay.getGraphics();
    for (Graphic g : graphics){
      if (g.getGeometry().equals(p.getLocation())){
        mCenteredGraphic = g;
        BitmapDrawable pin = (BitmapDrawable) ContextCompat.getDrawable(this.getActivity(),getPinForCenterPlace(p)) ;
        final PictureMarkerSymbol pinSymbol = new PictureMarkerSymbol(pin);
        g.setSymbol(pinSymbol);
        break;
      }
    }
    // Keep track of centered place since
    // it will be needed to reset
    // the graphic if another place
    // is centered.
    mCenteredPlace = p;
  //  mCenteringOnPlace = false;
  }

  /**
   *
   * @param presenter
   */
  @Override public void setPresenter(MapContract.Presenter presenter) {
    mPresenter = presenter;
  }

  /**
   * Given a map point, find the associated Place
   */
  private Place findPlaceForPoint(Point p){
    Place foundPlace = mPresenter.findPlaceForPoint(p);
    return foundPlace;
  }
  private class MapTouchListener extends DefaultMapViewOnTouchListener {
    /**
     * Instantiates a new DrawingMapViewOnTouchListener with the specified
     * context and MapView.
     *
     * @param context the application context from which to get the display
     *                metrics
     * @param mapView the MapView on which to control touch events
     */
    public MapTouchListener(Context context, MapView mapView) {
      super(context, mapView);
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
      Log.i(TAG, "Single tap event");
      android.graphics.Point screenPoint = new android.graphics.Point(
          (int) motionEvent.getX(),
          (int) motionEvent.getY());
      // identify graphics on the graphics overlay
      final ListenableFuture<List<Graphic>> identifyGraphic = mMapView
          .identifyGraphicsOverlayAsync(mGraphicOverlay, screenPoint, 10, 2);

      identifyGraphic.addDoneListener(new Runnable() {
        @Override
        public void run() {
          try {
            // get the list of graphics returned by identify
            List<Graphic> graphic = identifyGraphic.get();

            // get size of list in results
            int identifyResultSize = graphic.size();
            if (identifyResultSize > 0){
              Graphic foundGraphic = graphic.get(0);
              Place foundPlace = findPlaceForPoint((Point)foundGraphic.getGeometry());
              if (foundPlace != null){
                mCallback.showDetail(foundPlace);
              }
            }
          } catch (InterruptedException | ExecutionException ie) {
            ie.printStackTrace();
          }
        }

      });


      return true;
    }
  }

}
