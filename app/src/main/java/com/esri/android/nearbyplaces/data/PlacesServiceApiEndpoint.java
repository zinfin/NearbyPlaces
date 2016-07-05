package com.esri.android.nearbyplaces.data;

import android.support.v4.util.ArrayMap;
import com.esri.arcgisruntime.geometry.Point;

/**
 * This is the endpoint for your data source. Typically, it would be a SQLite db and/or a server
 * API. In this example, we fake this by creating the data on the fly.
 */
public final class PlacesServiceApiEndpoint {

  private final static ArrayMap<String,Place> DATA;

  static {
    DATA = new ArrayMap(2);
    addPlace("Powell's Books", "bookstore", new Point(45.521658, -122.7035132), "1055 W Burnside Portland, OR 97209",null, "(503) 228-4651", "NE");
    addPlace("Portland Japanese Garden", "garden", new Point(45.5188089, -122.7101633 ), "611 SW Kingston Ave, Portland, OR 97205", "japanesegarden.com","(503) 223-1321", "W");
  }

  private static void addPlace(String placeName, String type, Point location, String address, String URL, String phone, String bearing){
    Place newPlace = new Place(placeName, type, location, address, URL, phone, bearing );
    DATA.put(newPlace.getName(), newPlace);
  }

  /**
   * @return the Places to show when starting the app.
   */
  public static ArrayMap<String,Place> loadPersistedPlaces(){
    return DATA;
  }
}
