package com.esri.android.nearbyplaces.map;

import android.support.annotation.NonNull;
import android.util.Log;
import com.esri.android.nearbyplaces.data.LocationService;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.data.PlacesServiceApi;
import com.esri.android.nearbyplaces.places.PlaceFilterType;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.LocatorAttribute;
import com.esri.arcgisruntime.tasks.geocode.LocatorInfo;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by sand8529 on 6/24/16.
 */
public class MapPresenter implements MapContract.Presenter {

  private final static String TAG = MapPresenter.class.getSimpleName();
  private Point mLocation;

  private final MapContract.View mMapView;
  private LocationService mLocationService;

  public MapPresenter(@NonNull MapContract.View mapView){
    mMapView = checkNotNull(mapView, "map view cannot be null");
    mMapView.setPresenter(this);
  }

  @Override public void findPlacesNearby() {

    SpatialReference spatialReference = mMapView.getMapView().getSpatialReference();
    if (mLocation !=null){
      GeocodeParameters parameters = new GeocodeParameters();
      parameters.setMaxResults(20);
   //   parameters.setOutputSpatialReference(spatialReference);
     // parameters.setSearchArea(mMapView.getMapView().getCurrentViewpoint(Viewpoint.Type.BOUNDING_GEOMETRY).getTargetGeometry());
      parameters.setPreferredSearchLocation(mLocation);
      mLocationService.getPlaces(parameters, new PlacesServiceApi.PlacesServiceCallback() {
        @Override public void onLoaded(Object places) {
          List<Place> data = (List) places;
        }
      });
    }
  }

  private void loadGeocodingService(){
    if (mLocationService == null){
      mLocationService = LocationService.getInstance();
      LocationService.configureService("http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer",
          new Runnable() {
            @Override public void run() {
              findPlacesNearby();
            }
          });
    }else{
      findPlacesNearby();
    }
  }
  @Override public void filterPlacesNearby(PlaceFilterType filter) {

  }

  @Override public void setLocationDisplay(LocationDisplay locationDisplay) {

    locationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
      @Override public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
        if (locationChangedEvent.getSource().getMapLocation() != null){
          mLocation = locationChangedEvent.getSource().getLocation().getPosition();

          Log.i(TAG, "Location point is " + mLocation.getX() + ", " + mLocation.getY());
          loadGeocodingService();
        }
      }
    });

  }

  @Override public void start() {

  }
}
