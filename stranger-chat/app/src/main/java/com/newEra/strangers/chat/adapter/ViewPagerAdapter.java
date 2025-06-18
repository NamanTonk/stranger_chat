package com.newEra.strangers.chat.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList();
    private List<String> titleList = new ArrayList();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        return  this.fragmentList.get(position);
    }

    public int getCount() {
        return this.fragmentList.size();
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return  this.titleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        this.fragmentList.add(fragment);
        this.titleList.add(title);
    }
}
