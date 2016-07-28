package com.esri.android.nearbyplaces.places;

import android.support.annotation.NonNull;
import com.esri.android.nearbyplaces.data.LocationService;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.map.MapContract;
import com.esri.android.nearbyplaces.mapplace.MapPlaceContract;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesPresenter implements PlacesContract.Presenter {


  private final PlacesContract.View mPlacesView;

  public PlacesPresenter( @NonNull PlacesContract.View listView){
    mPlacesView = checkNotNull(listView);
    mPlacesView.setPresenter(this);
  }


  @Override public void start() {
    LocationService locationService = LocationService.getInstance();
    List<Place> places = locationService.getPlacesFromRepo();
    setPlacesNearby(places);
  }

  /**
   * Delegates the display of places to the view
   * @param places List<Place> items
   */
  @Override public void setPlacesNearby(List<Place> places) {
    mPlacesView.showNearbyPlaces(places);
  }
}
