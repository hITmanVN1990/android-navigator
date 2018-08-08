package com.tunt.navigator;

import android.support.v4.app.FragmentActivity;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public interface NavigatorActivityDispatcher<ActivityType extends FragmentActivity & NavigatorActivityInterface> {
    void onCreate(ActivityType activity);

    void onResume();

    void onSaveInstanceState();

    void onDestroy();

    void onBackPressed();

    void onFinish();

    boolean isStateSaved();

    Navigator getNavigator();
}
