package com.tevinjeffrey.rutgersct.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.tevinjeffrey.rutgersct.data.preference.DefaultSemester;
import com.tevinjeffrey.rutgersct.data.preference.DefaultUniversity;
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;

@Database(entities = {
    UCTSubscription.class,
    DefaultUniversity.class,
    DefaultSemester.class
}, version = 1)
@TypeConverters({ Converters.class })
public abstract class UCTDatabase extends RoomDatabase {
  public abstract PreferenceDao preferenceDao();

  public abstract UCTSubscriptionDao subscriptionDao();
}
