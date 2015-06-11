package com.uma.tfg.appartment.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.uma.tfg.appartment.activities.fragments.ReceiptsListFragment;
import com.uma.tfg.appartment.activities.fragments.UsersListFragment;

public class GroupPagerAdapter extends FragmentPagerAdapter {

    private ReceiptsListFragment mReceiptsListFragment;
    private UsersListFragment mUsersListFragment;

    public GroupPagerAdapter(FragmentManager fm, ReceiptsListFragment receiptsListFragment, UsersListFragment usersListFragment) {
        super(fm);
        this.mReceiptsListFragment = receiptsListFragment;
        this.mUsersListFragment = usersListFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mReceiptsListFragment;
            case 1:
                return mUsersListFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Recibos";
            case 1:
                return "Usuarios";
            default:
                return "";
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
