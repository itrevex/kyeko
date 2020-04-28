package com.trevexs.kyeko.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Round(
    @PrimaryKey var id: Long = 0,
    var counter: Int = 0,
    var name: String = "Round $counter",
    var questions: RealmList<Question> = RealmList(),
    var gameId: Long = 0,
    var createdAt: Long = System.currentTimeMillis(),
    var score: Int = 0
): RealmObject() {
    var team: Team? = null
}