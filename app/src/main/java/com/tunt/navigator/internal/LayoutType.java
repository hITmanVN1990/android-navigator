package com.tunt.navigator.internal;

/**
 * Created by TuNT on 8/8/2018.
 * tunt.program.04098@gmail.com
 *
 * Enum for add or replace a fragment
 */
public enum LayoutType {
    /**
     * Replace current fragment by new fragment.
     * In layout, current fragment is removed and the new fragment is replaced
     */
    REPLACE,

    /**
     * Add new fragment on top of current fragment.
     * In layout, current fragment is under the new fragment
     */
    ADD
}