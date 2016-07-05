package com.esri.android.nearbyplaces.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.places.PlacesContract;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

/**
 * Created by sand8529 on 6/24/16.
 */
public class MapFragment extends Fragment implements  MapContract.View {

  private MapContract.Presenter mPresenter;

  private FrameLayout mMapLayout;

  private MapView mMapView;

  private LocationDisplay mLocationDisplay;

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


    mPresenter.setLocationDisplay(mLocationDisplay);
    mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
    mLocationDisplay.startAsync();

    Basemap basemap = Basemap.createStreets();
    ArcGISMap map = new ArcGISMap(basemap);
    mMapView.setMap(map);

  }

  @Override public void showAllPlacesOnMap(List<Place> placeList) {

  }

  @Override public void showPlaceDetail(Place place) {

  }

  @Override public MapView getMapView() {
    return mMapView;
  }

  @Override public void setPresenter(MapContract.Presenter presenter) {
    mPresenter = presenter;

  }
  @Override
  public void onResume(){
    super.onResume();
    mMapView.resume();
    mLocationDisplay.startAsync();
  }

  @Override
  public void onPause(){
    mMapView.pause();
    mLocationDisplay.stop();
    super.onPause();
  }
}
