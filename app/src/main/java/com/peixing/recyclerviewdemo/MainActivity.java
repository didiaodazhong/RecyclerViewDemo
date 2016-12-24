package com.peixing.recyclerviewdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.peixing.recyclerviewdemo.fragment.GridLayoutFragment;
import com.peixing.recyclerviewdemo.fragment.LinearLayoutFragment;
import com.peixing.recyclerviewdemo.fragment.StaggeredGridLayoutFragment;

public class MainActivity extends AppCompatActivity {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int mTabCount = 3;
    String[] titles = new String[]{"LinearLayout", "GridLayout", "StaggeredGridLayout"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                                  @Override
                                  public Fragment getItem(int position) {
                                      switch (position) {
                                          case 0:
                                              return new LinearLayoutFragment();
                                          case 1:

                                              return new GridLayoutFragment();
                                          case 2:

                                              return new StaggeredGridLayoutFragment();
                                      }
                                      return null;
                                  }

                                  @Override
                                  public int getCount() {
                                      return mTabCount;
                                  }

                                  @Override
                                  public CharSequence getPageTitle(int position) {
                                      return titles[position];
                                  }
                              }
        );

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
