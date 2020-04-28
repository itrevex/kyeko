package com.trevexs.kyeko.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trevexs.kyeko.R
import com.trevexs.kyeko.binding.AddTeamData
import com.trevexs.kyeko.databinding.ActivityScoresBinding
import com.trevexs.kyeko.interfaces.Constants
import com.trevexs.kyeko.models.Mutations
import com.trevexs.kyeko.utils.createRealmObject
import io.realm.Realm

class Scores : AppCompatActivity() {
    val binding: ActivityScoresBinding by lazy {
        DataBindingUtil.setContentView<ActivityScoresBinding>(this, R.layout.activity_scores)
    }
    var gameId: Long = 0
    private var realm: Realm? = null
    private var mutations: Mutations? = null
    val data = AddTeamData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameId = intent.getLongExtra(Constants.Intents.GAME_ID, 0)
        binding.obj = data
        realm = createRealmObject()
        mutations = Mutations(realm)
    }
}
