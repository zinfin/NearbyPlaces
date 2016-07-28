package com.esri.android.nearbyplaces.places;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.esri.android.nearbyplaces.PlaceListener;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.CategoryHelper;
import com.esri.android.nearbyplaces.data.Place;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by sand8529 on 6/16/16.
 */
public class PlacesFragment extends Fragment implements PlacesContract.View{

  private PlacesContract.Presenter mPresenter;

  private PlacesAdapter mPlaceAdapter;

  private RecyclerView mPlacesView;

  private PlaceListener mCallback;

  private OnItemClickListener mPlaceItemListener;

  private static final String TAG = PlacesFragment.class.getSimpleName();

  public PlacesFragment(){

  }
  public static  PlacesFragment newInstance(){
    return new PlacesFragment();

  }
  @Override
  public void onCreate(@NonNull Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    List<Place> placeList = new ArrayList<>();
    mPlaceItemListener = new OnItemClickListener() {
      @Override public void onItemClick(Place p ) {
        Log.i(TAG, "Place clicked " + p.toString());
        mCallback.showDetail(p);
      }
    };

    mPlaceAdapter = new PlacesAdapter(getContext(), R.id.placesContainer,placeList, mPlaceItemListener);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstance){

    mPlacesView= (RecyclerView) inflater.inflate(
        R.layout.places_fragment2, container, false);

    mPlacesView.setLayoutManager(new LinearLayoutManager(mPlacesView.getContext()));
    mPlacesView.setAdapter(mPlaceAdapter);

    return mPlacesView;
  }
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mCallback = (PlaceListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement PlacesListener");
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.start();

  }


  @Override public void showNearbyPlaces(List<Place> places) {
    mPlaceAdapter.setPlaces(places);
    mPlaceAdapter.notifyDataSetChanged();
    mCallback.onPlacesFound(places);
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

  public  class PlacesAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Place> mPlaces = Collections.emptyList();
    private  OnItemClickListener listener;
    public PlacesAdapter(Context context, int resource, List<Place> places, OnItemClickListener listener){
          this.listener = listener;
          mPlaces = places;
    }

    public void setPlaces(List<Place> places){
      checkNotNull(places);
      mPlaces = places;
      notifyDataSetChanged();
    }

    @Override public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      final View itemView = inflater.inflate(R.layout.place, parent, false);
      return new RecyclerViewHolder(itemView);
    }


    @Override public void onBindViewHolder(RecyclerViewHolder holder, int position) {
      Place place = mPlaces.get(position);
      holder.placeName.setText(place.getName());
      holder.address.setText(place.getAddress());
      Drawable drawable = assignIcon(position);
      holder.icon.setImageDrawable(drawable);
      holder.bind(place, listener);
    }

    @Override public int getItemCount() {
      return mPlaces.size();
    }

    private Drawable assignIcon(int position){
      Place p = mPlaces.get(position);
      return CategoryHelper.getDrawableForPlace(p, getActivity());
    }
  }


  public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView placeName;
    public TextView address;
    public ImageView icon;

    public RecyclerViewHolder(View itemView) {
      super(itemView);
      placeName = (TextView) itemView.findViewById(R.id.placeName);
      address = (TextView) itemView.findViewById(R.id.placeAddress);
      icon = (ImageView) itemView.findViewById(R.id.placeTypeIcon);
    }
    public void bind(final Place place, final OnItemClickListener listener){
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          listener.onItemClick(place);
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(Place place);
  }
}
