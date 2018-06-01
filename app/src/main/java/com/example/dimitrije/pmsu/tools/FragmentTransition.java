package com.example.dimitrije.pmsu.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.dimitrije.pmsu.R;

/**
 * Created by Dimitrije on 5/29/2018.
 */

public class FragmentTransition {

    public static void to(Fragment newFragment, FragmentActivity activity)
    {
        to(newFragment, activity, true);
    }

    public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.mainContentPost, newFragment);
        if(addToBackstack) transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void remove(Fragment fragment, FragmentActivity activity)
    {
        activity.getSupportFragmentManager().popBackStack();
    }
}
