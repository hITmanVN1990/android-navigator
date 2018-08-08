package com.tunt.navigator;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public class NavigatorFragmentDispatcherImpl<FragmentType extends Fragment & NavigatorFragmentInterface> implements NavigatorFragmentDispatcher<FragmentType> {

    private Navigator navigator;

    private FragmentType fragment;

    /**
     * Disable animation for fragment when transiting
     */
    private boolean disableAnimation;

    @Override
    public void onActivityCreated(FragmentType fragment) {
        this.fragment = fragment;
        this.navigator = Navigator.fromFragment(fragment);
    }

    @Override
    public boolean isDisableAnimation() {
        return disableAnimation;
    }

    @Override
    public void setDisableAnimation(boolean disableAnimation) {
        this.disableAnimation = disableAnimation;
    }

    @Override
    public Navigator getRootNavigator() {
        Activity activity = fragment.getActivity();
        if (activity instanceof NavigatorActivityInterface) {
            return ((NavigatorActivityInterface) activity).getNavigator();
        }
        return null;
    }

    @Override
    public Navigator getParentNavigator() {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment instanceof NavigatorFragmentInterface) {
            return ((NavigatorFragmentInterface) parentFragment).getOwnNavigator();
        }
        return null;
    }

    @Override
    public Navigator getOwnNavigator() {
        return navigator;
    }

    @Override
    public void onDestroy() {
        if (navigator != null) {
            navigator.clean();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (disableAnimation) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return null;
    }
}