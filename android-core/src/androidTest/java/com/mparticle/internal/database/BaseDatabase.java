package com.mparticle.internal.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.mparticle.internal.database.services.SQLiteOpenHelperWrapper;

import java.util.concurrent.CountDownLatch;

public class BaseDatabase extends SQLiteOpenHelper {
    SQLiteOpenHelperWrapper helper;
    CountDownLatch timer;

    public BaseDatabase(SQLiteOpenHelperWrapper helper, String databaseName) {
        this(helper, databaseName, new CountDownLatch(1), 1);
    }

    public BaseDatabase(SQLiteOpenHelperWrapper helper, String databaseName, CountDownLatch timer, int version) {
        super(InstrumentationRegistry.getTargetContext(), databaseName, null, version);
        this.helper = helper;
        this.timer = timer;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        helper.onCreate(db);
        timer.countDown();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        helper.onUpgrade(db, oldVersion, newVersion);
        timer.countDown();
    }
}