package com.esri.android.nearbyplaces.data;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of the Notes Service API that adds a latency simulating network.
 */

public class PlacesServiceApiImpl implements  PlacesServiceApi{

  private static final int SERVICE_LATENCY_IN_MILLIS = 2000;
  private static final ArrayMap<String,Place> PLACES_SERVICE_DATA = PlacesServiceApiEndpoint.loadPersistedPlaces();

  @Override public void getPlacesFromService(final @NonNull GeocodeParameters parameters, final @NonNull PlacesServiceCallback callback) {

    checkNotNull(callback);
    // Simulate network by delaying the execution.
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        List<Place> places = new ArrayList<>(PLACES_SERVICE_DATA.values());
        callback.onLoaded(places);
      }
    }, SERVICE_LATENCY_IN_MILLIS);
  }

  @Override public Place getPlaceDetail(String placeName) {
    return null;
  }

  @Override public List<Place> getPlacesFromRepo() {
    return null;
  }

}
