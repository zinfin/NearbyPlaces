package com.esri.android.nearbyplaces.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.places.PlacesContract;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

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

  private final static String TAG = MapFragment.class.getSimpleName();

  public MapFragment(){

  }

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


    mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);

    Basemap basemap = Basemap.createStreets();
    ArcGISMap map = new ArcGISMap(basemap);
    mMapView.setMap(map);

    // Add graphics overlay for map markers
    mGraphicOverlay  = new GraphicsOverlay();
    mMapView.getGraphicsOverlays().add(mGraphicOverlay);

  }


  @Override
  public void onResume(){
    super.onResume();
    mMapView.resume();
   /* if (!mLocationDisplay.isStarted()){
      mLocationDisplay.startAsync();
    }*/
    Log.i(TAG, "Map fragment onResume " + "and location display is " + mLocationDisplay.isStarted());
    mPresenter.start();
  }

  @Override
  public void onPause(){
    super.onPause();
    mMapView.pause();
   /* if (mLocationDisplay.isStarted()){
      mLocationDisplay.stop();
    }*/
    Log.i(TAG, "Map fragment onPaus " + "and location display is " + mLocationDisplay.isStarted());
  }

  @Override public void showNearbyPlaces(List<Place> places) {
    // Clear out any existing graphics
    mGraphicOverlay.getGraphics().clear();

    // Create a graphic for every place
    for (Place place : places){
      SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
      Point graphicPoint = place.getLocation();
      Graphic graphic = new Graphic(graphicPoint, symbol);
      mGraphicOverlay.getGraphics().add(graphic);
    }
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
