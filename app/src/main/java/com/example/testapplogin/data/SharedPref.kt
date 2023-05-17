package com.example.testapplogin.data

import android.content.Context
import com.google.gson.GsonBuilder
import javax.inject.Inject

class SharedPref @Inject constructor(private val context: Context) {
    private var pref = context.getSharedPreferences("appPref", Context.MODE_PRIVATE)
    private val editor = pref.edit()


    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun putLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    fun putDouble(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.commit()
    }

    fun getValueString(key: String): String? {
        return pref.getString(key, null)
    }

    fun getValueInt(key: String): Int {
        return pref.getInt(key, 0)
    }

    fun getValueBoolean(key: String, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun removeValue(key: String) {
        editor.remove(key)
        editor.apply()
    }

    fun <T> put(key: String, `object`: T) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        putString(key, jsonString)
    }

    inline fun <reified T> get(key: String): T? {
        val value = getValueString(key)
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}