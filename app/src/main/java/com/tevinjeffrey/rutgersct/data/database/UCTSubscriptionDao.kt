package com.tevinjeffrey.rutgersct.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.tevinjeffrey.rutgersct.data.search.UCTSubscription
import org.intellij.lang.annotations.Language

@Dao
interface UCTSubscriptionDao {
  @Language("RoomSql")
  @Query("SELECT * FROM uct_subscription")
  fun all(): List<UCTSubscription>

  @Language("RoomSql")
  @Query("DELETE FROM uct_subscription WHERE section_topic_name = :topicName")
  fun deleteAll(vararg topicName: String)

  @Language("RoomSql")
  @Query("SELECT * FROM uct_subscription WHERE section_topic_name = :topicName")
  fun getSubscription(topicName: String): UCTSubscription

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(uctSubscriptions: List<UCTSubscription>)

  @Language("RoomSql")
  @Query("SELECT EXISTS(SELECT 1 FROM uct_subscription WHERE section_topic_name = :topicName LIMIT 1)")
  fun isSectionTracked(topicName: String): Boolean

  @Update
  fun updateAll(uctSubscriptions: List<UCTSubscription>)
}
