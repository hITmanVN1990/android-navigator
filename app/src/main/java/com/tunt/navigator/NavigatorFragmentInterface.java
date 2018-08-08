package com.tunt.navigator;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public interface NavigatorFragmentInterface {

    boolean handleBackIfNeeded();

    Navigator getOwnNavigator();

    Navigator getParentNavigator();

    Navigator getRootNavigator();

    NavigatorFragmentDispatcher getNavigatorDispatcher();
}