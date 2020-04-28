package com.trevexs.kyeko.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Question(
    @PrimaryKey var id: Long = 0,
    var question: String = "",
    var answeredCorrectly: Boolean = false,
    var createdAt: Long = System.currentTimeMillis()
): RealmObject()