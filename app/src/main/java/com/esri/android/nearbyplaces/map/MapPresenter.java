package com.esri.android.nearbyplaces.map;

import android.support.annotation.NonNull;
import android.util.Log;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.LocationService;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.data.PlacesServiceApi;
import com.esri.android.nearbyplaces.mapplace.MapPlaceContract;
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
  private boolean initialPlacesFound = false;

  private final MapContract.View mMapView;
  private final MapPlaceContract mMapPlacePresenter;
  private LocationService mLocationService;
  private final static String GEOCODE_URL = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";

  public MapPresenter(@NonNull MapContract.View mapView, @NonNull MapPlaceContract mapPlaceContract){
    mMapView = checkNotNull(mapView, "map view cannot be null");
    mMapPlacePresenter = checkNotNull(mapPlaceContract);
    mMapView.setPresenter(this);
    mapPlaceContract.registerMapPresenter(this);
  }

  @Override public void findPlacesNearby() {
    if (mLocation !=null){
      GeocodeParameters parameters = new GeocodeParameters();
      parameters.setMaxResults(20);
      parameters.setPreferredSearchLocation(mLocation);
      mLocationService.getPlaces(parameters, new PlacesServiceApi.PlacesServiceCallback() {
        @Override public void onLoaded(Object places) {
          List<Place> data = (List) places;
          // Send to PlacePresenter for displaying in PlaceFragment
          mMapPlacePresenter.getPlacePresenter().setPlacesNearby(data);
          // Create graphics for displaying locations in map
          mMapView.showNearbyPlaces(data);
          initialPlacesFound = true;
        }
      });
    }
  }

  private void loadGeocodingService(){
    if (mLocationService == null){
      mLocationService = LocationService.getInstance();
      LocationService.configureService(GEOCODE_URL,
          new Runnable() {
            @Override public void run() {
              findPlacesNearby();
            }
          });
    }else{
      findPlacesNearby();
    }
  }


  @Override public void start() {
    LocationDisplay locationDisplay = mMapView.getLocationDisplay();
    locationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
      @Override public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
        if (locationChangedEvent.getLocation()!=null){
          mLocation = locationChangedEvent.getLocation().getPosition();
          Log.i(TAG,"Location changed to " + mLocation.getX() + ", " + mLocation.getY());
          if (!initialPlacesFound){
            // Initialize the geocoding service
            // after the current device location
            // has been set.
            loadGeocodingService();
          }
        }
      }
    });
  }
}
