package com.trevexs.kyeko.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Team (
    @PrimaryKey var id: Long = 0,
    var name: String = "Team $id",
    var gameId: Long = 0,
    var createdAt: Long = System.currentTimeMillis()
): RealmObject()