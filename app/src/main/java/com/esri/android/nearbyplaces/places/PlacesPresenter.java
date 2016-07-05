package com.esri.android.nearbyplaces.places;

import android.support.annotation.NonNull;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.data.PlacesRepository;
import com.esri.android.nearbyplaces.data.PlacesServiceApi;
import com.esri.android.nearbyplaces.util.EspressoIdlingResource;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesPresenter implements PlacesContract.Presenter {

  private final PlacesRepository mPlacesDataSource;

  private final PlacesContract.View mPlacesView;

  public PlacesPresenter(@NonNull PlacesRepository placesDataSource,
      @NonNull PlacesContract.View placesView){
    mPlacesDataSource = checkNotNull(placesDataSource,"Places data source cannot be null");
    mPlacesView = checkNotNull(placesView,"Places view cannot be null");

    mPlacesView.setPresenter(this);
  }


  @Override public void loadPlaces(final boolean showLoadingIndicator) {
    if (showLoadingIndicator){
      mPlacesView.showProgressIndicator(true);
    }
    // The network request might be handled in a different thread so make sure Espresso knows
    // that the app is busy until the response is handled.
    EspressoIdlingResource.increment(); // App is busy until further notice


    mPlacesDataSource.getPlaces(new PlacesRepository.LoadPlacesCallback() {
      @Override public void onPlacesLoaded(List<Place> places) {
        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()){
          EspressoIdlingResource.decrement();
        }
        mPlacesView.showProgressIndicator(false);
        mPlacesView.showPlaces(places);
      }

      @Override public void onDataNotAvailable() {
        //nothing for now
      }
    });

  /**  mPlacesDataSource.getPlaces(parameters, new PlacesRepository.LoadPlacesCallback() {
      @Override public void onPlacesLoaded(List<Place> objects) {
        List<Place> places = new ArrayList<Place>();
        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()){
          EspressoIdlingResource.decrement();
        }
        mPlacesView.showPlaces(places);
      }
    });*/
  }

  @Override public void setFiltering(PlaceFilterType filterType) {

  }

  @Override public void loadPlaceDetail(String placeName) {
    // Do some logic here to retrieve details of the place

    mPlacesView.showPlaceDetail(mPlacesDataSource.getPlaceDetail(placeName));
  }

  @Override public void start() {
    //loadPlaces(true );

  }
}
