package com.esri.android.nearbyplaces.filter;

import com.esri.android.nearbyplaces.BaseView;
import com.esri.android.nearbyplaces.BasePresenter;

import java.util.List;

/**
 * Created by sand8529 on 7/28/16.
 */
public interface FilterContract {
  interface View extends BaseView<Presenter> {

    List<FilterItem> getFilteredCategories();
  }
  interface Presenter extends BasePresenter {
    void applyFilterCategories(List<FilterItem> filterItems);
  }
}
