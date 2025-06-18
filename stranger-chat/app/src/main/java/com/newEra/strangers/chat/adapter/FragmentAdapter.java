package com.newEra.strangers.chat.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public Fragment getItem(int position) {
        return (Fragment) this.fragmentList.get(position);
    }

    public int getCount() {
        return this.fragmentList.size();
    }
}
