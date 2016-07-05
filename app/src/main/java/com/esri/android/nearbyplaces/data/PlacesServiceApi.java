package com.esri.android.nearbyplaces.data;

import android.support.annotation.NonNull;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.List;

/**
 * Main entry point for accessing places data.
 * <p>
 * For simplicity, only getPlaces() and getPlace() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new place is saved, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 *
 * Defines an interface to the service API that is used by this application. All data request should
 * be piped through this interface.
 */

public interface PlacesServiceApi {

  interface PlacesServiceCallback<List>{  // callback from server

   void onLoaded(List places);

  }


  void getPlaces(@NonNull GeocodeParameters parameters, @NonNull PlacesServiceCallback callback);
  Place getPlaceDetail(String placeName);


}
