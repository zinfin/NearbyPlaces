package com.esri.android.nearbyplaces.places;

import android.support.annotation.NonNull;
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
  private final MapPlaceContract mMapPlacePresenter;

  public PlacesPresenter( @NonNull PlacesContract.View listView,
      @NonNull MapPlaceContract mapPlaceContract){
    mPlacesView = checkNotNull(listView);
    mMapPlacePresenter = checkNotNull(mapPlaceContract);
    mPlacesView.setPresenter(this);
    mapPlaceContract.registerPlacePresenter(this);
  }


  @Override public void start() {

  }


  @Override public void setPlacesNearby(List<Place> places) {
    mPlacesView.showNearbyPlaces(places);
  }
}
