package com.esri.android.nearbyplaces.map;

import android.support.annotation.NonNull;
import android.util.Log;
import com.esri.android.nearbyplaces.places.PlaceFilterType;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by sand8529 on 6/24/16.
 */
public class MapPresenter implements MapContract.Presenter {

  private final static String TAG = MapPresenter.class.getSimpleName();
  private Point mLocation;

  private final MapContract.View mMapView;

  public MapPresenter(@NonNull MapContract.View mapView){
    mMapView = checkNotNull(mapView, "map view cannot be null");
    mMapView.setPresenter(this);
  }

  @Override public void findPlacesNearby() {

  }

  @Override public void filterPlacesNearby(PlaceFilterType filter) {

  }

  @Override public void setLocationDisplay(LocationDisplay locationDisplay) {
    locationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
      @Override public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
        if (dataSourceStatusChangedEvent.getSource().getMapLocation() != null){
          mLocation = dataSourceStatusChangedEvent.getSource().getMapLocation();
          Log.i(TAG, "Location point is " + mLocation.getX() + ", " + mLocation.getY());
        }
      }
    });
  }

  @Override public void start() {

  }
}
