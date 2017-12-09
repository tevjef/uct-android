package com.tevinjeffrey.rutgersct.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tevinjeffrey.rutgersct.data.search.UCTSubscription;

import java.util.List;

@Dao
public interface UCTSubscriptionDao {
  @Delete
  void delete(UCTSubscription... uctSubscriptions);

  @Query("SELECT * FROM uct_subscription")
  List<UCTSubscription> getAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<UCTSubscription> uctSubscriptions);

  @Query("SELECT EXISTS("
             + "SELECT 1 FROM uct_subscription WHERE section_topic_name = :topicName LIMIT 1)")
  boolean isSectionTracked(String topicName);

  @Update
  void updateAll(List<UCTSubscription> uctSubscriptions);
}
