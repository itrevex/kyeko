package com.trevexs.kyeko.binding

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import com.trevexs.kyeko.BR
import com.trevexs.kyeko.R
import com.trevexs.kyeko.adapters.TeamListAdapter
import kotlin.properties.Delegates

class QuestionsData(
    seconds: Int = 30,
    score: Int = 0,
    val data: ArrayList<StringItemData> = ArrayList(),
    val adapter: TeamListAdapter = TeamListAdapter(data, R.layout.question_list)
) : BaseObservable() {

    @get: Bindable
    var seconds: Int by Delegates.observable(seconds) { _, _, _ ->
        notifyPropertyChanged(BR.seconds)
    }

    fun layoutManager(view: View) = LinearLayoutManager(view.context)

    fun setData(data: ArrayList<StringItemData>) {
        this.data.clear()
        this.data.addAll(data)
        adapter.notifyDataSetChanged()
    }

    @get: Bindable
    var score: Int by Delegates.observable(score) {
            _, _, _ -> notifyPropertyChanged(BR.score)
    }
}