package com.esri.android.nearbyplaces.data;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sand8529 on 6/22/16.
 */
public class PlacesRepositories {

  private PlacesRepositories(){
  }

  private static PlacesRepository repository = null;

  public synchronized static PlacesRepository getInMemoryRepoInstance(@NonNull PlacesServiceApi placesServiceApi){
    checkNotNull(placesServiceApi);
    if (repository == null){
      repository = new InMemoryPlacesRepository(placesServiceApi);
    }

    return repository;
  }

}
