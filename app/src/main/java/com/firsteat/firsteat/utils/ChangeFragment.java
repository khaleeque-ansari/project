package com.firsteat.firsteat.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class ChangeFragment {
    public static final String TAG = ChangeFragment.class.getSimpleName();

    public ChangeFragment() {
        // TODO Auto-generated constructor stub
    }

    public static void replaceFragment(FragmentManager fm,int container, Fragment target,String TAG)
    {

        Fragment fragment = target;
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = fm;
        fragmentManager.executePendingTransactions();
        fragmentManager.beginTransaction()
                .replace(container, fragment,TAG)
                .addToBackStack("replaced_with_"+TAG)
                .commit();
    }


    public static void changeFragment(FragmentManager fm, int container, Fragment target) {


        Fragment fragment = target;
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = fm;

        fragmentManager.executePendingTransactions();
        fragmentManager.beginTransaction()
                .replace(container, fragment)
                .commit();

    }

    public void changeFragmentWithExtra(FragmentManager fm, int container, Fragment target, Bundle bundle) {

        Fragment fragment = target;
        fragment.setArguments(bundle);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = fm;
        fragmentManager.beginTransaction()
                .replace(container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
