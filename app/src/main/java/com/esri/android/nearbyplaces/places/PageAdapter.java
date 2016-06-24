package com.esri.android.nearbyplaces.places;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Supports swipe tabs
 */
public class PageAdapter extends FragmentStatePagerAdapter {


  private final List<Fragment> mFragments = new ArrayList<>();
  private final List<String> mFragmentTitles = new ArrayList<>();

  private final static String TAG = PageAdapter.class.getSimpleName();

  public PageAdapter(FragmentManager fm){
    super(fm);

  }
  public void addFragment(Fragment fragment, String title){
    mFragments.add(fragment);
    mFragmentTitles.add(title);
  }

  @Override public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override public int getCount() {
    return mFragments.size();
  }

  @Override public CharSequence getPageTitle(int position){ return mFragmentTitles.get(position); }
}
