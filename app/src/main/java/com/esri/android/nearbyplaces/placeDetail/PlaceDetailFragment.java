package com.esri.android.nearbyplaces.placeDetail;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.CategoryHelper;
import com.esri.android.nearbyplaces.data.Place;

/**
 * Created by sand8529 on 7/12/16.
 */
public class PlaceDetailFragment extends BottomSheetDialogFragment implements PlaceDetailContract.View {

  private PlaceDetailContract.Presenter mPresenter;

  private View mBottomSheet;

  private BottomSheetBehavior mBottomSheetBehavior;

  public PlaceDetailFragment(){}

  public static PlaceDetailFragment newInstance(){
    return new PlaceDetailFragment();
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState){
    BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
    mBottomSheet = View.inflate(getContext(), R.layout.place_card_view, null);
    dialog.setContentView(mBottomSheet);
    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) mBottomSheet.getParent()).getLayoutParams();
    CoordinatorLayout.Behavior behavior = params.getBehavior();
    mBottomSheetBehavior = (BottomSheetBehavior) behavior;

    if( behavior != null && behavior instanceof BottomSheetBehavior ) {
      ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
    }

    return dialog;
  }

  @Override public void showPlaceDetail(Place place) {
    TextView txtAddress = (TextView) mBottomSheet.findViewById(R.id.placeAddress);
    txtAddress.setText(place.getName() + " " + place.getAddress());
    TextView txtPhone  = (TextView) mBottomSheet.findViewById(R.id.placePhone) ;
    txtPhone.setText(place.getPhone());
    TextView txtUrl = (TextView) mBottomSheet.findViewById(R.id.placeUrl);
    txtUrl.setText(place.getURL());
    TextView txtType = (TextView) mBottomSheet.findViewById(R.id.placeType) ;
    txtType.setText(place.getType());

    // Assign the appropriate icon
    Drawable d =   CategoryHelper.getDrawableForPlace(place, getActivity()) ;
    ImageView icon = (ImageView) mBottomSheet.findViewById(R.id.TypeIcon);
    icon.setImageDrawable(d);

  }

  @Override public void dismissPlaceDetail() {
    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
  }

  @Override public void setPresenter(PlaceDetailContract.Presenter presenter) {
    mPresenter = presenter;
  }

  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
      if (newState == BottomSheetBehavior.STATE_HIDDEN) {
        dismiss();
      }

    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
    }
  };

}
