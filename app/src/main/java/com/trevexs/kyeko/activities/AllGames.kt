package com.trevexs.kyeko.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trevexs.kyeko.R
import com.trevexs.kyeko.binding.AddTeamData
import com.trevexs.kyeko.binding.StringItemData
import com.trevexs.kyeko.databinding.ActivityAllGamesBinding
import com.trevexs.kyeko.models.Game
import com.trevexs.kyeko.models.Mutations
import com.trevexs.kyeko.models.Team
import com.trevexs.kyeko.utils.createRealmObject
import io.realm.Realm

class AllGames : AppCompatActivity() {
    val binding: ActivityAllGamesBinding by lazy {
        DataBindingUtil.setContentView<ActivityAllGamesBinding>(this, R.layout.activity_all_games)
    }
    private var realm: Realm? = null
    private var mutations: Mutations? = null
    val data = AddTeamData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "All Games"
        binding.obj = data
        realm = createRealmObject()
        mutations = Mutations(realm)
        setItems()
    }

    private fun setItems(){
        val games = mutations?.getAllItems<Game>()
        val stringData: ArrayList<StringItemData> = ArrayList()
        games?.let {
            for (game in games) {
                val stringItem = StringItemData(game.id.toInt(), game.name)
                stringData.add(stringItem)
            }
            data.setData(stringData)
        }
    }
}
