package com.esri.android.nearbyplaces.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load places from the a data source.
 */
public class InMemoryPlacesRepository implements PlacesRepository {

  private final PlacesServiceApi mPlacesServiceApi;

  public InMemoryPlacesRepository(@NonNull PlacesServiceApi placesServiceApi){
    mPlacesServiceApi = placesServiceApi;
  }

  @VisibleForTesting
  List<Place> mCachedPlaces;

  @Override public void getPlaces(@NonNull final LoadPlacesCallback callback) {

    checkNotNull(callback);
    if (mCachedPlaces == null){
      mPlacesServiceApi.getPlaces( new GeocodeParameters(), new PlacesServiceApi.PlacesServiceCallback<List<Place>>() {
        @Override public void onLoaded(List<Place> places) {
          mCachedPlaces = ImmutableList.copyOf(places);
          callback.onPlacesLoaded(mCachedPlaces);
        }
      });
    }else{
      callback.onPlacesLoaded(mCachedPlaces);
    }
  }

  @Override public Place getPlaceDetail(String placeName) {
    Place foundPlace = null;
    for (Place p: mCachedPlaces){
      if (p.getName().equalsIgnoreCase(placeName)){
        foundPlace = p;
        break;
      }
    }
    return foundPlace;
  }

}
