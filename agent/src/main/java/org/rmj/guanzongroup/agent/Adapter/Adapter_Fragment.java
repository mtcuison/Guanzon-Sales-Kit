package org.rmj.guanzongroup.agent.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Fragment extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentlist = new ArrayList<>();

    private List<String> mTitle = new ArrayList<>();

    public Adapter_Fragment(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentlist.size();
    }

    @Override
    public String getPageTitle(int position) {
        return mTitle.get(position);
    }

    public void addFragment(Fragment fragment) {
        this.mFragmentlist.add(fragment);
    }

    public void clear() {
        this.mFragmentlist.clear();
        this.mTitle.clear();
    }

    public void addTitle(String title) {
        this.mTitle.add(title);
    }
}

