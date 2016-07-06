package com.esri.android.nearbyplaces.mapplace;

import com.esri.android.nearbyplaces.map.MapContract;
import com.esri.android.nearbyplaces.map.MapPresenter;
import com.esri.android.nearbyplaces.places.PlacesContract;
import com.esri.android.nearbyplaces.places.PlacesPresenter;

/**
 * Created by sand8529 on 7/6/16.
 */
public interface MapPlaceContract {

  MapContract.Presenter getMapPresenter();

  PlacesContract.Presenter getPlacePresenter();

  void registerMapPresenter(MapContract.Presenter presenter);

  void registerPlacePresenter(PlacesContract.Presenter presenter);
}
