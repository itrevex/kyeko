package com.trevexs.kyeko.models

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.Sort

class Mutations(val realm: Realm?) {
    inline fun <reified T : RealmObject> getItem(id: Long): T? {
        return realm?.where(T::class.java)?.equalTo("id", id)?.findFirst()
    }

    inline fun <reified T : RealmObject> getItems(id: Long, fieldName:String = "id"): ArrayList<T> {
        val items = ArrayList<T>()
        val results = realm?.where(T::class.java)?.equalTo(fieldName, id)?.findAll()
        items.addAll(realm!!.copyFromRealm(results!!))
        return items
    }

    inline fun <reified T : RealmObject> getItemsRealmList(id: Long, fieldName:String = "id"): RealmList<T> {
        val items = RealmList<T>()
        val results = realm?.where(T::class.java)?.equalTo(fieldName, id)?.findAll()
        items.addAll(results!!)
        return items
    }

    inline fun <reified T : RealmObject> getAllItems(): RealmList<T> {
        val items = RealmList<T>()
        var results = realm?.where(T::class.java)?.findAll()
        results = results?.sort("createdAt", Sort.DESCENDING)
        items.addAll(results!!)
        return items
    }

    inline fun <reified T: RealmObject> createObject(item: T) {
        realm?.executeTransaction {
            val currentIndex = realm.where(T::class.java)?.max("id")
            val nextId = if (currentIndex == null) 1 else currentIndex as Int + 1
            val realmItem = realm.createObject(T::class.java, nextId)
        }
    }

    inline fun <reified T : RealmObject> getNextId(): Long {
        val currentIndex = realm?.where(T::class.java)?.max("id")
        return if (currentIndex == null) 1 else currentIndex as Long + 1
    }
}