package com.esri.android.nearbyplaces.mapplace;

import android.support.annotation.NonNull;
import com.esri.android.nearbyplaces.map.MapContract;
import com.esri.android.nearbyplaces.map.MapPresenter;
import com.esri.android.nearbyplaces.places.PlacesContract;
import com.esri.android.nearbyplaces.places.PlacesPresenter;

/**
 * Created by sand8529 on 7/6/16.
 */
public class MapPlaceMediator implements MapPlaceContract {

  private PlacesContract.Presenter mPlacePresenter;
  private MapContract.Presenter mMapPresenter;

  public MapPlaceMediator(){}

  @Override public MapContract.Presenter getMapPresenter() {
    return mMapPresenter;
  }

  @Override public PlacesContract.Presenter getPlacePresenter() {
    return mPlacePresenter;
  }

  @Override public void registerMapPresenter(MapContract.Presenter presenter) {
    mMapPresenter = presenter;
  }

  @Override public void registerPlacePresenter(PlacesContract.Presenter presenter) {
    mPlacePresenter = presenter;
  }
}
