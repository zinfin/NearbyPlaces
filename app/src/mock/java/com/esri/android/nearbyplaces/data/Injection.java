package com.esri.android.nearbyplaces.data;

import android.content.Context;
import android.support.annotation.NonNull;
import com.esri.android.nearbyplaces.data.source.PlacesDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link PlacesDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {
  public static PlacesDataSource providePlacesDataSource(@NonNull Context context){
    checkNotNull(context);
    return FakePlaceDataSource.getInstance();
  }
}
