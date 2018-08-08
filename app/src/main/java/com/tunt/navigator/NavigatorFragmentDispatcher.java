package com.tunt.navigator;

import android.support.v4.app.Fragment;
import android.view.animation.Animation;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public interface NavigatorFragmentDispatcher<FragmentType extends Fragment & NavigatorFragmentInterface> {

    void onActivityCreated(FragmentType fragment);

    boolean isDisableAnimation();

    void setDisableAnimation(boolean disableAnimation);

    Navigator getRootNavigator();

    Navigator getParentNavigator();

    Navigator getOwnNavigator();

    void onDestroy();

    Animation onCreateAnimation(int transit, boolean enter, int nextAnim);
}