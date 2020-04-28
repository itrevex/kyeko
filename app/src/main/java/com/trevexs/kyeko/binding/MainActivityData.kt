package com.trevexs.kyeko.binding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlin.properties.Delegates
import com.trevexs.kyeko.BR

class MainActivityData(currentTeam: String = ""): BaseObservable() {
    @get: Bindable
    var currentTeam: String by Delegates.observable(currentTeam) {
        _, _, _-> notifyPropertyChanged(BR.currentTeam)
    }
}