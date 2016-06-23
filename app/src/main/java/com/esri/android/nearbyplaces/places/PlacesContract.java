package com.esri.android.nearbyplaces.places;

import com.esri.android.nearbyplaces.BasePresenter;
import com.esri.android.nearbyplaces.BaseView;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.List;


/**
 * Created by sand8529 on 6/16/16.
 */
public interface PlacesContract {

  interface View extends BaseView<Presenter> {

    void showPlaceDetail(String placeName);

    void showPlaces(List<Place> places);

    void showProgressIndicator(boolean active);

    boolean isActive();
  }

  interface Presenter extends BasePresenter {

    void loadPlaces(boolean showIndicator, GeocodeParameters parameters);

    void setFiltering(PlaceFilterType filterType);

    void loadPlaceDetail(String placeName);
  }
}
