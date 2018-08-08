package com.tunt.navigator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;

import com.tunt.navigator.Navigator;
import com.tunt.navigator.NavigatorFragmentDispatcher;
import com.tunt.navigator.NavigatorFragmentDispatcherImpl;
import com.tunt.navigator.NavigatorFragmentInterface;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public class NavigatorFragment extends Fragment implements NavigatorFragmentInterface {

    private NavigatorFragmentDispatcher<NavigatorFragment> dispatcher = new NavigatorFragmentDispatcherImpl<>();

    /**
     * @return true if Fragment need interact with back command e.g: hide the popup layout,
     * hide search layout,... and more
     */
    public boolean handleBackIfNeeded() {
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dispatcher.onActivityCreated(this);
    }

    @Override
    public NavigatorFragmentDispatcher getNavigatorDispatcher() {
        return dispatcher;
    }

    @Override
    public Navigator getRootNavigator() {
        return dispatcher.getRootNavigator();
    }

    @Override
    public Navigator getParentNavigator() {
        return dispatcher.getParentNavigator();
    }

    @Override
    public Navigator getOwnNavigator() {
        return dispatcher.getOwnNavigator();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return dispatcher.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }
}