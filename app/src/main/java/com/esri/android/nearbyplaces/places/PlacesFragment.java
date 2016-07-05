package com.esri.android.nearbyplaces.places;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.Place;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesFragment extends Fragment implements PlacesContract.View{

  private PlacesContract.Presenter mPresenter;

  private PlacesAdapter mPlaceAdapter;

  private LinearLayout mPlacesView;

  public PlacesFragment(){

  }
  public static  PlacesFragment newInstance(){
    return new PlacesFragment();

  }
  @Override
  public void onCreate(@NonNull Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    List<Place> placeList = new ArrayList<>();
    mPlaceAdapter = new PlacesAdapter(getContext(), R.id.placesContainer,placeList);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstance){

    View root = inflater.inflate(R.layout.places_fragment, container, false);

    // Places list
    ListView placesList = (ListView) root.findViewById(R.id.places_list);
    placesList.setAdapter(mPlaceAdapter);

    mPlacesView = (LinearLayout) root.findViewById(R.id.placesLinearLayout);

    // Set up progress indicator
    final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
        (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
    swipeRefreshLayout.setColorSchemeColors(
        ContextCompat.getColor(getActivity(), R.color.colorPrimary),
        ContextCompat.getColor(getActivity(), R.color.colorAccent),
        ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
    );
    // Set the scrolling view in the custom SwipeRefreshLayout.
    swipeRefreshLayout.setScrollUpChild(placesList);

    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        mPresenter.loadPlaces(true );
      }
    });

    return root;
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.start();
  }

  @Override public void showPlaceDetail(Place place) {
    Intent detailIntent = new Intent();
  }

  PlaceItemListener mItemListener = new PlaceItemListener() {
    @Override public void onPlaceClick(Place clickedPlace) {
      // Show place detail with map
    }
  };

  @Override public void showPlaces(List<Place> places) {
    mPlaceAdapter.setPlaces(places);
    mPlaceAdapter.notifyDataSetChanged();
    mPlacesView.setVisibility(View.VISIBLE);
  }

  @Override public void showProgressIndicator(final boolean active) {
    if (getView() == null){
      return;
    }

    final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

    // Make sure setRefreshing() is called after the layout is done with everything else.
    srl.post(new Runnable() {
      @Override
      public void run() {
        srl.setRefreshing(active);
      }
    });
  }

  @Override public boolean isActive() {
    return false;
  }

  @Override public void setPresenter(PlacesContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  public static class PlacesAdapter extends ArrayAdapter<Place> {

    private List<Place> mPlaces;
    private PlaceItemListener mPlaceItemListener;
    public PlacesAdapter(Context context, int resource, List<Place> objects){
      super(context, resource, objects);
      mPlaces = objects;
     // mPlaceItemListener = placeListener;
    }

    public void setPlaces(List<Place> places){
      checkNotNull(places);
      mPlaces = places;
    }

    @Override public int getCount() {
      return mPlaces.size();
    }

    @Override public Place getItem(int position) {
      return mPlaces.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      View rowView = convertView;
      if (rowView == null) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        rowView = inflater.inflate(R.layout.place, parent, false);
      }
      final Place place= (Place) getItem(position);

      TextView titleTV = (TextView) rowView.findViewById(R.id.placeName);
      titleTV.setText(place.getName());

    /*  rowView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mPlaceItemListener.onPlaceClick(place);
        }
      });*/

      return rowView;
    }
  }
  public interface PlaceItemListener{
    void onPlaceClick(Place clickedPlace);
  }
}
