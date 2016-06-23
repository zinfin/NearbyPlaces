package com.esri.android.nearbyplaces.data;

import android.support.annotation.Nullable;
import com.esri.arcgisruntime.geometry.Point;

/**
 * Created by sand8529 on 6/16/16.
 */
public final class Place {

  private final String mName;

  @Nullable
  private final String mType;
  @Nullable
  private final Point mLocation;
  @Nullable
  private final String mAddress;
  @Nullable
  private final String mURL;
  @Nullable
  private final String mPhone;
  @Nullable
  private final String mBearing;

  public Place(String name, @Nullable String type, @Nullable Point location, @Nullable String address, @Nullable String URL, @Nullable String phone,
      @Nullable String bearing) {
    this.mName = name;
    this.mType = type;
    this.mLocation = location;
    this.mAddress = address;
    this.mURL = URL;
    this.mPhone = phone;
    this.mBearing = bearing;
  }

  public String getmName() {
    return mName;
  }

  @Nullable public String getmType() {
    return mType;
  }

  @Nullable public Point getmLocation() {
    return mLocation;
  }

  @Nullable public String getmAddress() {
    return mAddress;
  }

  @Nullable public String getmURL() {
    return mURL;
  }

  @Nullable public String getmPhone() {
    return mPhone;
  }

  @Nullable public String getmBearing() {
    return mBearing;
  }
}
