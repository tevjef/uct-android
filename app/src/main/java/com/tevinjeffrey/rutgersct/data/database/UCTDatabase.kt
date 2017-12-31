package com.tevinjeffrey.rutgersct.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

import com.tevinjeffrey.rutgersct.data.database.entities.DefaultSemester
import com.tevinjeffrey.rutgersct.data.database.entities.DefaultUniversity
import com.tevinjeffrey.rutgersct.data.database.entities.UCTSubscription

@Database(entities = [
  UCTSubscription::class,
  DefaultUniversity::class,
  DefaultSemester::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class UCTDatabase : RoomDatabase() {
  abstract fun preferenceDao(): PreferenceDao
  abstract fun subscriptionDao(): UCTSubscriptionDao
}
