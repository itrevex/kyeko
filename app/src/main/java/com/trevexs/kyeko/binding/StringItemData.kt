package com.trevexs.kyeko.binding

import android.view.View
import android.widget.CheckBox
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlin.properties.Delegates
import com.trevexs.kyeko.BR
import com.trevexs.kyeko.interfaces.OnAdapterItemClickListener

class StringItemData(id: Int, name: String = ""): BaseObservable() {
    @get: Bindable
    var id: Int by Delegates.observable(id) {
            _, _, _ -> notifyPropertyChanged(BR.id)
    }

    @get: Bindable
    var name: String by Delegates.observable(name) {
        _, _, _ -> notifyPropertyChanged(BR.name)
    }

    @get: Bindable
    var description: String by Delegates.observable("") {
            _, _, _ -> notifyPropertyChanged(BR.description)
    }

    @get: Bindable
    var checked: Boolean by Delegates.observable(false) {
            _, _, _ -> notifyPropertyChanged(BR.checked)
    }

    fun onItemClick(view: View) {
        OnAdapterItemClickListener.onAdapterItemClick?.onClick(view)
    }
}