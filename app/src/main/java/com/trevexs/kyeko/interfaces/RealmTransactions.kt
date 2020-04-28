package com.trevexs.kyeko.interfaces

import android.view.View
import io.realm.RealmObject

interface RealmTransactions {
    fun <T : RealmObject> onCreateObject(newObject: T);
}

interface OnAdapterItemClick {
    fun onClick(view: View);
}

object TransactionsListener {
    @JvmStatic var realmTransactions: RealmTransactions? = null
}

object OnAdapterItemClickListener {
    @JvmStatic var onAdapterItemClick: OnAdapterItemClick? = null
}