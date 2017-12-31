package com.tevinjeffrey.rutgersct.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tevinjeffrey.rutgersct.dagger.PerApp
import com.tevinjeffrey.rutgersct.data.database.entities.UCTSubscription
import timber.log.Timber
import java.nio.charset.Charset
import javax.inject.Inject

@PerApp
class HawkHelper @Inject constructor(
    val context: Context,
    val moshi: Moshi) : SQLiteOpenHelper(
    context,
    "Hawk",
    null,
    1) {

  override fun onCreate(p0: SQLiteDatabase?) {}

  override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

  fun dropDatabase() {
    context.deleteDatabase("Hawk")
  }

  fun getUniversity(): List<UCTSubscription> {

    if (!context.databaseList().contains("Hawk")) {
      return emptyList()
    }

    var uniStr = ""

    try {
      val db = readableDatabase
      val cursor = db.rawQuery("SELECT hawk_value FROM hawk WHERE hawk_key = 'trackedsections'",
          null)

      if (cursor.count > 0) {
        cursor.moveToNext()
        uniStr = cursor.getString(0) ?: ""
      }


      cursor.close()
      db.close()
    } catch (e: SQLiteException) {
      Timber.e(e)
      return emptyList()
    }

    if (uniStr.contains("UCTSubscription")) {
      uniStr = String(Base64.decode(uniStr.substringAfter('@'), Base64.DEFAULT),
          Charset.forName("UTF-8"))

      if (uniStr.isNotBlank()) {
        val type = Types.newParameterizedType(List::class.java, UCTSubscription::class.java)
        val adapter: JsonAdapter<List<UCTSubscription>> = moshi.adapter(type)
        return adapter.fromJson(uniStr).orEmpty().filter { it.sectionTopicName.isNotEmpty() }
      }
    }

    return emptyList()
  }
}