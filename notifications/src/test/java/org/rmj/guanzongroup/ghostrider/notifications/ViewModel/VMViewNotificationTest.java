/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.notifications
 * Electronic Personnel Access Control Security System
 * project file created : 10/19/21, 1:39 PM
 * project file last modified : 10/19/21, 1:39 PM
 */

package org.rmj.guanzongroup.ghostrider.notifications.ViewModel;

import android.os.Build;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1}, manifest= Config.NONE)
public class VMViewNotificationTest extends TestCase {
    private VMViewNotification mViewModel;

    @Before
    public void setUp() {
//        mViewModel = new VMViewNotification(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        mViewModel = null;
    }

    @Test
    public void testGetNotificationInfo() {
        mViewModel.getNotificationInfo("00001").observeForever(notif -> assertNotNull(notif));
    }



}
