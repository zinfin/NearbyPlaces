package com.esri.android.nearbyplaces.placeDetail;

import com.esri.android.nearbyplaces.BasePresenter;
import com.esri.android.nearbyplaces.BaseView;
import com.esri.android.nearbyplaces.data.Place;

/**
 * Created by sand8529 on 7/13/16.
 */
public interface PlaceDetailContract {

  interface View extends BaseView<Presenter> {
    void showPlaceDetail(Place place);

    void dismissPlaceDetail();
  }

  interface Presenter extends BasePresenter {

    void setPlaceDetail(Place place);

    void dismissPlaceDetail();

  }
}
