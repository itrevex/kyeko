package com.trevexs.kyeko.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Game (
    @PrimaryKey var id: Long = 0,
    var name: String = "",
    var rounds: RealmList<Round> = RealmList(),
    var createdAt: Long = System.currentTimeMillis()
): RealmObject() {
    init {
        name = "Game $id"
    }
}