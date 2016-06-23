package com.esri.android.nearbyplaces.data;

import android.support.annotation.NonNull;
import com.esri.android.nearbyplaces.data.source.PlacesDataSource;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class FakePlaceDataSource implements PlacesDataSource {

  private static FakePlaceDataSource INSTANCE;

  private static final Map<GeocodeParameters, Place> PLACES_SERVICE_DATA = new LinkedHashMap<>();

  private FakePlaceDataSource(){}

  public static FakePlaceDataSource getInstance(){
    if (INSTANCE == null){
      INSTANCE = new FakePlaceDataSource();
    }
    return INSTANCE;
  }

  @Override
  public void getPlaces(@NonNull LoadPlacesCallback callback) {
    callback.onPlacesLoaded(Lists.newArrayList(PLACES_SERVICE_DATA.values()));
  }

  @Override public void getPlace(@NonNull GeocodeParameters parameters,
      @NonNull GetPlaceCallback callback) {

  }
}
