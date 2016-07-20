package com.esri.android.nearbyplaces.map;

import android.support.annotation.NonNull;
import android.util.Log;
import com.esri.android.nearbyplaces.data.LocationService;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.data.PlacesServiceApi;
import com.esri.android.nearbyplaces.mapplace.MapPlaceContract;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by sand8529 on 6/24/16.
 */
public class MapPresenter implements MapContract.Presenter {

  private final static String TAG = MapPresenter.class.getSimpleName();
  private Point mLocation;

  private final MapContract.View mMapView;
  private final MapPlaceContract mMapPlacePresenter;
  private LocationService mLocationService;
  private final static int MAX_RESULT_COUNT = 10;
  private final static String GEOCODE_URL = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";

  public MapPresenter(@NonNull MapContract.View mapView, @NonNull MapPlaceContract mapPlaceContract){
    mMapView = checkNotNull(mapView, "map view cannot be null");
    mMapPlacePresenter = checkNotNull(mapPlaceContract);
    mMapView.setPresenter(this);
    mapPlaceContract.registerMapPresenter(this);
  }

  /**
   * Use the location service to geocode places of interest
   * based on the map's visible area extent.
   */
  @Override public void findPlacesNearby() {
    Point g =  mMapView.getMapView().getVisibleArea().getExtent().getCenter();

    if ( g !=null ){
      GeocodeParameters parameters = new GeocodeParameters();
      parameters.setMaxResults(MAX_RESULT_COUNT);
      // Use envelope
      //g.getExtent();
    //  Geometry searchArea = GeometryEngine.project(g, SpatialReference.create(4326))
      // parameters.setSearchArea(g.getExtent());
      parameters.setPreferredSearchLocation(g);
      mLocationService.getPlacesFromService(parameters, new PlacesServiceApi.PlacesServiceCallback() {
        @Override public void onLoaded(Object places) {
          List<Place> data = (List) places;
          // Send to PlacePresenter for displaying in PlaceFragment.
          // This isn't the ideal way to communicate among presenters.
          // A better solution is to use the observable pattern.
          // The presenters would register themselves as observers on
          // a observable service (LocationService), but since the map
          // fragment and the place fragment are essentially displaying
          // the same data in different ways, I'm allowing access
          // to a mediator object that brokers communication between
          // the presenters.
          mMapPlacePresenter.getPlacePresenter().setPlacesNearby(data);
          // Create graphics for displaying locations in map
          mMapView.showNearbyPlaces(data);
        }
      });
    }
  }

  @Override public void centerOnPlace(Place p) {
    mMapView.centerOnPlace(p);
  }

  @Override public Place findPlaceForPoint(Point p) {
    Log.i(TAG, "Point X/Y " + p.getX() + " " + p.getY());
    Place foundPlace = null;
    List<Place> foundPlaces =mLocationService.getPlacesFromRepo();
    for (Place place : foundPlaces){
      Log.i(TAG, "Place location " + place.getLocation().getX() + " " + place.getLocation().getY());
      if ((p).equals(place.getLocation())){
        foundPlace = place;
        break;
      }
    }
    return foundPlace;
  }

  /**
   * Provision the geocoding service and wait
   * for it to be loaded before searching for
   * nearby locations of interest.
   */
//  private void loadGeocodingService(){
//    if (mLocationService == null){
//      mLocationService = LocationService.getInstance();
//      LocationService.configureService(GEOCODE_URL,
//          new Runnable() {
//            @Override public void run() {
//              findPlacesNearby();
//            }
//          });
//    }else{
//      findPlacesNearby();
//    }
//  }

  /**
   * The entry point for this class starts
   * by loading the gecoding service.
   */
  @Override public void start() {
    mLocationService = LocationService.getInstance();
    LocationService.configureService(GEOCODE_URL,
        new Runnable() {
          @Override public void run() {
            findPlacesNearby();
          }
        });
  }
}
