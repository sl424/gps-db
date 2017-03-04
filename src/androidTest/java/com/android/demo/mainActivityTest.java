package com.android.demo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.android.demo.mainActivityTest \
 * com.android.demo.tests/android.test.InstrumentationTestRunner
 */
public class mainActivityTest extends ActivityInstrumentationTestCase2<mainActivity> {

    public mainActivityTest() {
        super("com.android.demo", mainActivity.class);
    }

}
