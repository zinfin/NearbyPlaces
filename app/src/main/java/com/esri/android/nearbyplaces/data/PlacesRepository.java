package com.esri.android.nearbyplaces.data;

import android.support.annotation.NonNull;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.List;

/**
 * Main entry point for accessing places data.
 */
public interface PlacesRepository {
  interface LoadPlacesCallback{  // callback from server

    void onPlacesLoaded(List<Place> places);

    void onDataNotAvailable();
  }

  interface GetPlaceCallback{ //call back from local data repo

    void onPlacesLoaded(List<Place> places);

    void onDataNotAvailabe();
  }

  void getPlaces(@NonNull GeocodeParameters parameters, @NonNull LoadPlacesCallback callback);

}
