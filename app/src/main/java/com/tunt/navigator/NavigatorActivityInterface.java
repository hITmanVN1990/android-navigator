package com.tunt.navigator;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public interface NavigatorActivityInterface {

    boolean isStateSaved();

    Navigator getNavigator();

    void onFinish();
}