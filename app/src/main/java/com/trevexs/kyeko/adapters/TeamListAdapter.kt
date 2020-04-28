package com.trevexs.kyeko.adapters

import com.trevexs.kyeko.R
import com.trevexs.kyeko.binding.StringItemData
import java.util.*

class TeamListAdapter(private var data: ArrayList<StringItemData>, var layout: Int = 0) : RecyclerViewBaseAdapter() {

    public override fun getItemForPosition(position: Int): StringItemData {
        return data[position]
    }

    public override fun getLayoutIdForPosition(position: Int): Int {
        return if (layout != 0) {layout} else {R.layout.string_list_item }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}