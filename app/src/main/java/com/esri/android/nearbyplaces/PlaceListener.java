package com.esri.android.nearbyplaces;

import com.esri.android.nearbyplaces.data.Place;

import java.util.List;

/**
 * Created by sand8529 on 7/12/16.
 */
public interface PlaceListener {
  void onPlacesFound(List<Place> places);
  void onPlaceSearch();
  void showDetail(Place place);
  void onMapScroll();
}
