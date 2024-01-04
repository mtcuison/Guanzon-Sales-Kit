/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.g3appdriver;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB;
import static org.rmj.g3appdriver.GCircle.room.GGC_GCircleDB.MIGRATION_V42;

import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MigrationDBTesting {
    private static final Migration[] ALL_MIGRATIONS = new Migration[]{
            MIGRATION_V42};
    @Rule
    public MigrationTestHelper helper;
    @Before
    public void setUp(){
        helper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                GGC_GCircleDB.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }
    /*TEST LOCAL DB IF MIGRATING SUCCESSFULLY*/
    @Test
    public void MigrateDB() throws IOException {
        // Create earliest version of the database.
        SupportSQLiteDatabase db = helper.createDatabase("TEST_DB", 45);
        db.close();

        // Open latest version of the database. Room validates the schema
        // once all migrations execute.
        GGC_GCircleDB appDb = Room.databaseBuilder(
                        InstrumentationRegistry.getInstrumentation().getTargetContext(),
                        GGC_GCircleDB.class,
                        "TEST_DB")
                .addMigrations(ALL_MIGRATIONS).build();
        appDb.getOpenHelper().getWritableDatabase();
        appDb.close();
    }
}
