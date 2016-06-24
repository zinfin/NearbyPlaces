package com.esri.android.nearbyplaces.map;

import com.esri.android.nearbyplaces.BasePresenter;
import com.esri.android.nearbyplaces.BaseView;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.places.PlaceFilterType;
import com.esri.android.nearbyplaces.places.PlacesContract;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

/**
 * Created by sand8529 on 6/24/16.
 */
public interface MapContract {

  interface View extends BaseView<Presenter>{

    void showAllPlacesOnMap(List<Place> placeList);

    void showPlaceDetail(Place place);

    MapView getMapView();

  }

  interface Presenter extends BasePresenter{

    void findPlacesNearby();

    void filterPlacesNearby(PlaceFilterType filter);

    void setLocationDisplay(LocationDisplay locationDisplay);
  }
}
