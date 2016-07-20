package com.esri.android.nearbyplaces.placeDetail;

import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.places.PlacesContract;

/**
 * Created by sand8529 on 7/19/16.
 */
public class PlaceDetailPresenter implements PlaceDetailContract.Presenter {

  private PlaceDetailContract.View mView;

  public PlaceDetailPresenter(PlaceDetailContract.View placeDetailView){
    mView = placeDetailView;
  }

  @Override public void setPlaceDetail(Place place) {
    mView.showPlaceDetail(place);
  }

  @Override public void dismissPlaceDetail() {
    mView.dismissPlaceDetail();
  }

  @Override public void start() {

  }
}
