package com.esri.android.nearbyplaces.places;

import com.esri.android.nearbyplaces.BasePresenter;
import com.esri.android.nearbyplaces.BaseView;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.List;


/**
 * Created by sand8529 on 6/16/16.
 */
public interface PlacesContract {

  interface View extends BaseView<Presenter> {


    void showNearbyPlaces(List<Place> places);

    void showProgressIndicator(boolean active);

    boolean isActive();


  }

  interface Presenter extends BasePresenter {

    void setPlacesNearby(List<Place> places);



  }
}
