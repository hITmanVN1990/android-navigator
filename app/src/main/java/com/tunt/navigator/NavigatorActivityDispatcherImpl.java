package com.tunt.navigator;

import android.support.v4.app.FragmentActivity;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public class NavigatorActivityDispatcherImpl<ActivityType extends FragmentActivity & NavigatorActivityInterface> implements NavigatorActivityDispatcher<ActivityType> {

    private boolean isStateSaved;

    private Navigator navigator;

    private ActivityType activity;

    @Override
    public void onCreate(ActivityType activity) {
        this.activity = activity;
        this.navigator = Navigator.fromActivity(activity);
    }

    @Override
    public void onResume() {
        isStateSaved = false;
    }

    @Override
    public void onSaveInstanceState() {
        isStateSaved = true;
    }

    @Override
    public void onDestroy() {
        if (navigator != null) {
            navigator.clean();
            navigator = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (navigator != null && !navigator.goBack(false) && navigator.isRoot()) {
            onFinish();
        }
    }

    @Override
    public void onFinish() {
        activity.onFinish();
    }

    @Override
    public boolean isStateSaved() {
        return this.isStateSaved;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }
}
