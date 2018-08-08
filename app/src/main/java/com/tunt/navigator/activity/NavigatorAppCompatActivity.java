package com.tunt.navigator.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tunt.navigator.Navigator;
import com.tunt.navigator.NavigatorActivityDispatcher;
import com.tunt.navigator.NavigatorActivityDispatcherImpl;
import com.tunt.navigator.NavigatorActivityInterface;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public class NavigatorAppCompatActivity extends AppCompatActivity implements NavigatorActivityInterface {

    private NavigatorActivityDispatcher<NavigatorAppCompatActivity> dispatcher = new NavigatorActivityDispatcherImpl<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher.onCreate(this);
    }

    @Override
    public boolean isStateSaved() {
        return dispatcher.isStateSaved();
    }

    @Override
    public Navigator getNavigator() {
        return dispatcher.getNavigator();
    }

    @Override
    public void onFinish() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }

    @Override
    public void onBackPressed() {
        dispatcher.onBackPressed();
    }
}
