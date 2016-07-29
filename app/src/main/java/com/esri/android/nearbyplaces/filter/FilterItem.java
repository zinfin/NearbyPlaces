package com.esri.android.nearbyplaces.filter;

import android.media.Image;

/**
 * Created by sand8529 on 7/28/16.
 */
public class FilterItem {

  private String mTitle;
  private int mIconId;
  private boolean mSelected;

  public FilterItem (String title, int icon){
    mTitle = title;
    mIconId = icon;
    mSelected = false;
  }
  public FilterItem (String title, int icon, boolean s){
    mTitle = title;
    mIconId = icon;
    mSelected = s;
  }
  public FilterItem(){}

  public int getIconId() {
    return mIconId;
  }

  public void setIconId(int iconId) {
    this.mIconId = iconId;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String mTitle) {
    this.mTitle = mTitle;
  }

  public boolean getSelected() { return mSelected ;}

  public void setSelected(boolean selected){ mSelected = selected; }
}
