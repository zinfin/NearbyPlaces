package com.esri.android.nearbyplaces;

import com.esri.android.nearbyplaces.data.Place;
import com.esri.android.nearbyplaces.data.PlacesRepository;
import com.esri.android.nearbyplaces.data.PlacesServiceApi;
import com.esri.android.nearbyplaces.places.PlacesContract;
import com.esri.android.nearbyplaces.places.PlacesPresenter;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PlacesPresenterTest {

  PlacesPresenter mPlacesPresenter;

  private static List<Place> PLACES;

  @Mock
  private PlacesRepository mPlacesDataSource;
  @Mock
  private PlacesContract.View mPlacesView;

  /**
   * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
   * perform further actions or assertions on them.
   */
  @Captor
  private ArgumentCaptor<PlacesServiceApi.PlacesServiceCallback> mPlacesServiceCallbackCaptor;

  @Before
  public void setupTasksPresenter() {
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this);

    mPlacesPresenter = new PlacesPresenter(mPlacesDataSource, mPlacesView);

    // The presenter won't update the view unless it's active.

    when(mPlacesView.isActive()).thenReturn(true);

    PLACES = Lists.newArrayList(
        new Place("Powells Books",null,null,null,null,null,null),
        new Place("Stumptown Coffee", null,null,null,null,null,null),
        new Place("Mt. Hood", null,null,null,null,null,null)
    );
  }
  @Test
  public void loadPlacesIntoView(){

    // When clicking on a place in the list of geocoded com.esri.android.nearbyplaces.places
    mPlacesPresenter.loadPlaces(true, new GeocodeParameters());

    // Callback is captured and invoked with stubbed tasks
   /* verify(mPlacesDataSource).getPlaces(new GeocodeParameters(), );
    mLoadPlacesCallbackCaptor.getValue().onLoaded(PLACES);

    // Then progress indicator is shown
    verify(mPlacesView).showProgressIndicator(true);
    // Then progress indicator is hidden and all places are shown in UI
    verify(mPlacesView).showProgressIndicator(false);
    ArgumentCaptor<List> showPlacesListArgumentCaptor = ArgumentCaptor.forClass(List.class);
    verify(mPlacesView).showPlaces(showPlacesListArgumentCaptor.capture());
    assertTrue(showPlacesListArgumentCaptor.getValue().size() == 3);*/

  }

}