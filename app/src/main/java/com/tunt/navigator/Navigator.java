package com.tunt.navigator;

import android.annotation.SuppressLint;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tunt.navigator.internal.LayoutType;

import java.util.List;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 */
public class Navigator<ActivityType extends FragmentActivity & NavigatorActivityInterface> {

    private ActivityType activity;

    private FragmentManager fragmentManager;

    /**
     * if true: No animation will return on {@link Fragment#onCreateAnimation(int, boolean, int)}
     * So no animation for transition.
     */
    private boolean disableAnimation;

    @IdRes
    private int contentId;

    @AnimRes
    private int animEnter, animExit, animPopEnter, animPopExit;

    public Navigator(ActivityType activity, FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        setDefaultAnim(R.anim.navigator_slide_in_right, R.anim.navigator_slide_out_left,
                R.anim.navigator_slide_in_left, R.anim.navigator_slide_out_right);
    }

    public static <ActivityType extends FragmentActivity & NavigatorActivityInterface> Navigator fromActivity(ActivityType activity) {
        return new Navigator(activity, activity.getSupportFragmentManager());
    }

    public static <FragmentType extends Fragment & NavigatorFragmentInterface> Navigator fromFragment(FragmentType fragment) {
        return new Navigator(fragment.getActivity(), fragment.getChildFragmentManager());
    }

    public boolean isDisableAnimation() {
        return disableAnimation;
    }

    public void setDisableAnimation(boolean disableAnimation) {
        this.disableAnimation = disableAnimation;
    }

    /**
     * set default layout id whenever using a short version of {@link #openFragment(Fragment, int, boolean, LayoutType, int, int, int, int)}
     *
     * @param contentId your layout id to hold fragment
     */
    public void setDefaultContentId(@IdRes int contentId) {
        this.contentId = contentId;
    }

    /**
     * set default animation when opening fragment
     */
    public void setDefaultAnim(@AnimRes int animEnter, @AnimRes int animExit,
                               @AnimRes int animPopEnter, @AnimRes int animPopExit) {
        this.animEnter = animEnter;
        this.animExit = animExit;
        this.animPopEnter = animPopEnter;
        this.animPopExit = animPopExit;
    }

    /**
     * @param fragment              A fragment you next open
     * @param contentId             A layout id hosts the fragment
     * @param backToCurrentFragment If you want after open new fragment and no need to back
     *                              to current fragment set it false. default we will back to current fragment
     * @param layoutType            Add or Replace
     * @param animEnter             Enter animation
     * @param animExit              Exit animation
     * @param animPopEnter          Pop enter animation
     * @param animPopExit           Pop exit animation
     */
    @SuppressLint("ResourceType")
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment, LayoutType layoutType,
                             @AnimRes int animEnter, @AnimRes int animExit,
                             @AnimRes int animPopEnter, @AnimRes int animPopExit) {
        ensureAnimationForFragment(fragment);
        ensureAnimationForFragmentsInBackStack(1);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (animEnter > 0 || animExit > 0 || animPopEnter > 0 || animPopExit > 0) {
            fragmentTransaction.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
        }
        if (layoutType == LayoutType.ADD) {
            fragmentTransaction.add(contentId, fragment);
        } else {
            fragmentTransaction.replace(contentId, fragment);
        }

        if (!backToCurrentFragment) {
            popBackStack();
        }
        fragmentTransaction.addToBackStack(Integer.toString((int) (2147483646.0D * Math.random())));
        fragmentTransaction.commit();
    }

    /**
     * a bit shorter of full version with enable or disable animation when changing fragment
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment, LayoutType layoutType, boolean animation) {
        if (animation) {
            openFragment(fragment, contentId, backToCurrentFragment, layoutType, animEnter, animExit, animPopEnter, animPopExit);
        } else {
            openFragment(fragment, contentId, backToCurrentFragment, layoutType, 0, 0, 0, 0);
        }
    }

    /**
     * animation is not declared equivalent animation is enabled
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment, LayoutType layoutType) {
        openFragment(fragment, contentId, backToCurrentFragment, layoutType, true);
    }

    /**
     * Open a fragment on a default container id
     */
    public void openFragment(Fragment fragment, boolean backToCurrentFragment, LayoutType layoutType, boolean animation) {
        if (contentId == 0) {
            throw new IllegalStateException("call setDefaultContentId first");
        }
        openFragment(fragment, contentId, backToCurrentFragment, layoutType, animation);
    }

    /**
     * id is not declared equivalent default content id is used
     * animation is not declared equivalent animation is enabled
     */
    public void openFragment(Fragment fragment, boolean backToCurrentFragment, LayoutType layoutType) {
        openFragment(fragment, backToCurrentFragment, layoutType, true);
    }

    /**
     * id is not declared equivalent default content id is used
     * animation is not declared equivalent animation is enabled
     * backToCurrent is not declared equivalent will back
     */
    public void openFragment(Fragment fragment, LayoutType layoutType) {
        openFragment(fragment, true, layoutType);
    }

    /**
     * id is not declared equivalent default content id is used
     * animation is not declared equivalent animation is enabled
     * backToCurrent is not declared equivalent will back
     */
    public void openFragment(Fragment fragment, LayoutType layoutType, boolean animation) {
        openFragment(fragment, this.contentId, true, layoutType, animation);
    }

    /**
     * get current fragment laid out on layout with contentId
     */
    public Fragment getCurrentFragment(@IdRes int contentId) {
        return fragmentManager.findFragmentById(contentId);
    }

    public boolean goBack(boolean forceBack) {
        boolean isNavigateFromActivity = activity.getSupportFragmentManager() == fragmentManager;
        if (!isBackStackEmpty() && !forceBack) {
            Fragment lastFragment = getLastFragmentInBackStack();

            if (lastFragment != null && lastFragment instanceof NavigatorFragmentInterface) {
                NavigatorFragmentInterface currentFragment = (NavigatorFragmentInterface) lastFragment;
                // Check if current fragment need back
                if (currentFragment.handleBackIfNeeded()) return true;
            }
        }

        boolean pop = (!isNavigateFromActivity && !isBackStackEmpty()) || (isNavigateFromActivity && !isRoot());

        if (pop) {
            ensureAnimationForFragmentsInBackStack(2);
            popBackStack();
            return true;
        }
        return false;

    }

    /**
     * @param fragment: ensure disable animation for this fragment or not(from navigator)
     * @return true: if fragment implement {@link NavigatorFragmentInterface} and we can set
     * disableAnimation from Navigator. other wise return false
     */
    private boolean ensureAnimationForFragment(Fragment fragment) {
        if (fragment instanceof NavigatorFragmentInterface) {
            ((NavigatorFragmentInterface) fragment).getNavigatorDispatcher().setDisableAnimation(disableAnimation);
            return true;
        }
        return false;
    }

    /**
     * @param count: number of fragment if back stack will apply disableAnimation flag from Navigator. count is always 1 or 2.
     *               When 1: when user open a new fragment.
     *               When 2: when we pop back stack the "last in" and the previous one will create animation for popAnimIn and exitAnimOut effect.
     *               So ensure enable or disable animation from navigator here must be apply to at least for 2 fragments.
     *               <p>
     *               -1: for all
     *               positive: for count fragment
     */
    private void ensureAnimationForFragmentsInBackStack(int count) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.isEmpty()) return;
        int sum = 0;
        for (int size = fragments.size(), i = size - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment == null) continue;
            if (ensureAnimationForFragment(fragment)) {
                sum++;
                if (sum == count) return;
            }
        }
    }

    private Fragment getLastFragmentInBackStack() {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.isEmpty()) return null;
        for (int size = fragments.size(), i = size - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment == null) continue;
            if (fragment.isVisible()) return fragment;
        }
        return null;
    }

    public void backToRoot() {
        ensureAnimationForFragmentsInBackStack(-1);
        if (!isRoot()) {
            int backStackCount = fragmentManager.getBackStackEntryCount();
            for (int i = backStackCount - 1; i >= 1; i--) {
                popBackStack();
            }
        }
    }

    public boolean isBackStackEmpty() {
        return fragmentManager.getBackStackEntryCount() == 0;
    }

    public boolean isRoot() {
        return fragmentManager.getBackStackEntryCount() <= 1;
    }

    public void clean() {
        activity = null;
        fragmentManager = null;
    }

    public void popBackStack() {
        if (activity == null || activity.isStateSaved()) {
            return;
        }
        fragmentManager.popBackStack();
    }
}
