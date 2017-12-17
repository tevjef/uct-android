package com.tevinjeffrey.rmp.common.utils

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Arrays

object JaroWinklerDistance {

  fun getDistance(s1: String, s2: String, threshold: Float): Float {
    val mtp = matches(s1, s2)
    val m = mtp[0].toFloat()
    if (m == 0f) {
      return 0f
    }
    val j = (m / s1.length + m / s2.length + (m - mtp[1]) / m) / 3
    return if (j < threshold)
      j
    else
      j + (Math.min(0.1f, 1f / mtp[3]) * mtp[2].toFloat()
          * (1 - j))
  }

  private fun matches(s1: String, s2: String): IntArray {
    val max: String
    val min: String
    if (s1.length > s2.length) {
      max = s1
      min = s2
    } else {
      max = s2
      min = s1
    }
    val range = Math.max(max.length / 2 - 1, 0)
    val matchIndexes = IntArray(min.length)
    Arrays.fill(matchIndexes, -1)
    val matchFlags = BooleanArray(max.length)
    var matches = 0
    for (mi in 0 until min.length) {
      val c1 = min[mi]
      var xi = Math.max(mi - range, 0)
      val xn = Math.min(mi + range + 1, max
          .length)
      while (xi < xn) {
        if (!matchFlags[xi] && c1 == max[xi]) {
          matchIndexes[mi] = xi
          matchFlags[xi] = true
          matches++
          break
        }
        xi++
      }
    }
    val ms1 = CharArray(matches)
    val ms2 = CharArray(matches)
    run {
      var i = 0
      var si = 0
      while (i < min.length) {
        if (matchIndexes[i] != -1) {
          ms1[si] = min[i]
          si++
        }
        i++
      }
    }
    var i = 0
    var si = 0
    while (i < max.length) {
      if (matchFlags[i]) {
        ms2[si] = max[i]
        si++
      }
      i++
    }
    var transpositions = 0
    for (mi in ms1.indices) {
      if (ms1[mi] != ms2[mi]) {
        transpositions++
      }
    }
    var prefix = 0
    for (mi in 0 until min.length) {
      if (s1[mi] == s2[mi]) {
        prefix++
      } else {
        break
      }
    }
    return intArrayOf(matches, transpositions / 2, prefix, max.length)
  }
}