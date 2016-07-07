package com.esri.android.nearbyplaces;

import android.app.Application;
import android.content.Context;

/**
 * Created by sand8529 on 7/7/16.
 */
public class NearbyPlaces extends Application {

  private static Context mContext;

  @Override
  public void onCreate(){
    super.onCreate();
    mContext = this;
  }

  public static Context getContext(){
    return mContext;
  }
}
