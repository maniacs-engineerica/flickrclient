package com.matiascohen.flickrclient.extensions

import org.json.JSONArray

operator fun <T> JSONArray.iterator(): List<T> = (0 until length()).asSequence().map { get(it) as T }.toList()