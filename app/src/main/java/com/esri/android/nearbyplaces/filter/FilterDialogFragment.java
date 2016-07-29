package com.esri.android.nearbyplaces.filter;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.data.CategoryKeeper;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sand8529 on 7/28/16.
 */
public class FilterDialogFragment extends DialogFragment implements FilterContract.View {
  private FilterContract.Presenter mPresenter;
  private FilterItemAdapter mFilterItemAdapter;


  public FilterDialogFragment(){}

  public static FilterDialogFragment newInstance(){
    return new FilterDialogFragment();
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    getDialog().setTitle(R.string.filter_dialog);
    View view = inflater.inflate(R.layout.place_filter, container,false);
    ListView listView = (ListView) view.findViewById(R.id.filterView);
    CategoryKeeper keeper = CategoryKeeper.getInstance();
    ArrayList<FilterItem> localCategories = keeper.getCategories();

    for (FilterItem fi : localCategories){
      Log.i("ADATPER",fi.getTitle() + " " + fi.getSelected());
    }
    mFilterItemAdapter = new FilterItemAdapter(getActivity(), localCategories);
    listView.setAdapter(mFilterItemAdapter);

    // Listen for Cancel/Apply
    Button cancel = (Button) view.findViewById(R.id.btnCancel);
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    Button apply = (Button) view.findViewById(R.id.btnApply);
    apply.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
    return view;
  }

  @Override
  public void onCreate(Bundle savedBundleState){
    super.onCreate(savedBundleState);
    setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
  }

  @Override public List<FilterItem> getFilteredCategories() {
    return null;
  }

  @Override public void setPresenter(FilterContract.Presenter presenter) {
    mPresenter = presenter;
  }

  public class FilterItemAdapter extends ArrayAdapter<FilterItem>{
    public FilterItemAdapter(Context context, ArrayList<FilterItem> items){
      super(context,0, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // Get the data item for this position
      FilterItem item = getItem(position);
      // Check if an existing view is being reused, otherwise inflate the view
      if (convertView == null) {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_view, parent, false);
      }
      // Lookup view for data population
      TextView category = (TextView) convertView.findViewById(R.id.categoryName);
      final Button btn = (Button) convertView.findViewById(R.id.categoryBtn);
      if (!item.getSelected()){
        btn.setAlpha(0.5f);
      }else{
        btn.setAlpha(1.0f);
      }
      // Populate the data into the template view using the data object
      category.setText(item.getTitle());
      btn.setBackground(ResourcesCompat.getDrawable(getResources(),item.getIconId(),null));
      final int myPosition = position;
      // Attach listener that toggles selected state of category
      convertView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          AlphaAnimation lighten = new AlphaAnimation(1.0f, 0.5f);
          lighten.setDuration(1000);
          lighten.setFillAfter(true);
          AlphaAnimation darken = new AlphaAnimation(0.5f, 1.0f);
          darken.setDuration(1000);
          darken.setFillAfter(true);
          FilterItem clickedItem = getItem(myPosition);
          if (clickedItem.getSelected()){
            clickedItem.setSelected(false);
            btn.startAnimation(lighten);
          }else{
            clickedItem.setSelected(true);
            btn.startAnimation(darken);
          }
        }
      });

      // Return the completed view to render on screen
      return convertView;
    }
  }

}
