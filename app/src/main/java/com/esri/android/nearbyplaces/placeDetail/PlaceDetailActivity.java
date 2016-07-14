package com.esri.android.nearbyplaces.placeDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.esri.android.nearbyplaces.R;

/**
 * Created by sand8529 on 7/13/16.
 */
public class PlaceDetailActivity extends AppCompatActivity {
  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.place_card_view);

    Bundle extras = getIntent().getExtras();
    String placeName = extras.getString("PLACE_NAME");

    TextView txtName = (TextView) findViewById(R.id.placeNameAndAddress);
    txtName.setText(placeName);


  }
}
