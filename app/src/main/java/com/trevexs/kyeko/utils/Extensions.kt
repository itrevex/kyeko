package com.trevexs.kyeko.utils

import android.app.Activity
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.exceptions.RealmMigrationNeededException

fun Context.showSnackBar(msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make((this as Activity).findViewById(android.R.id.content), msg, length).show()
}

fun Context.createRealmObject(): Realm {
    return try {
        Realm.getDefaultInstance()
    } catch (r: RealmMigrationNeededException) {
        clearGameData()
        Realm.deleteRealm(Realm.getDefaultConfiguration()!!)
        Realm.getDefaultInstance()
    }
}

fun Context.clearGameData() {
    val prefs = SharedPrefs(this)
    prefs.setCurrentRound(1)
    prefs.setCurrentTeam(0)
    prefs.setGameId(-1)
}