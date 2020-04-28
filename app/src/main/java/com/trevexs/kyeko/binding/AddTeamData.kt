package com.trevexs.kyeko.binding

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.properties.Delegates
import com.trevexs.kyeko.adapters.TeamListAdapter
import com.trevexs.kyeko.BR

class AddTeamData(
    val data: ArrayList<StringItemData> = ArrayList(),
    val adapter: TeamListAdapter =  TeamListAdapter(data)
): BaseObservable() {

    @get: Bindable
    var teamName: String by Delegates.observable("") {
        _, _, _ -> notifyPropertyChanged(BR.teamName)
    }

    fun layoutManager(view: View) = LinearLayoutManager(view.context)

    fun setData(data: ArrayList<StringItemData>) {
        this.data.clear()
        this.data.addAll(data)
        adapter.notifyDataSetChanged()
    }
}