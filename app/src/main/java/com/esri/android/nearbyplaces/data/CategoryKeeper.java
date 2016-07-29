package com.esri.android.nearbyplaces.data;

import android.content.Context;
import com.esri.android.nearbyplaces.R;
import com.esri.android.nearbyplaces.filter.FilterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sand8529 on 7/28/16.
 */
public class CategoryKeeper {
    private static CategoryKeeper instance = null;
    private ArrayList<FilterItem> categories = new ArrayList<>();

    private CategoryKeeper(){
      categories.add(new FilterItem("Bar", R.drawable.ic_local_bar_black_24dp, true));
      categories.add(new FilterItem("Coffee", R.drawable.ic_local_cafe_black_24dp, true));
      categories.add(new FilterItem("Food", R.drawable.ic_local_dining_black_24dp, true));
      categories.add(new FilterItem("Hotel", R.drawable.ic_hotel_black_24dp, true));
      categories.add(new FilterItem("Pizza", R.drawable.ic_local_pizza_black_24dp, false));
    }

    public static CategoryKeeper getInstance(){
      if (instance == null){
        instance = new CategoryKeeper();
      }
      return  instance;
    }

    public ArrayList<FilterItem> getCategories(){
      return categories;
    }
}
