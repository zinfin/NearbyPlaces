package com.esri.android.nearbyplaces.placeDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.Place;

/**
 * Created by sand8529 on 7/12/16.
 */
public class PlaceDetailFragment extends Fragment implements PlaceDetailContract.View {

  private PlaceDetailContract.Presenter mPresenter;

  private CardView mCardView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstance){
    mCardView = (CardView) inflater.inflate(R.layout.place_card_view, container,false);
    return mCardView;
  }

  @Override public void showPlaceDetail(Place place) {
    TextView txtPlaceName = (TextView) mCardView.findViewById(R.id.placeNameAndAddress);
    txtPlaceName.setText(place.getName() + " " + place.getAddress());

    TextView txtPhone = (TextView) mCardView.findViewById(R.id.placePhone) ;
    txtPhone.setText(place.getPhone());

    TextView txtUrl = (TextView) mCardView.findViewById(R.id.placeUrl);
    txtUrl.setText(place.getURL());


  }

  @Override public void setPresenter(PlaceDetailContract.Presenter presenter) {
    mPresenter = presenter;
  }
}
