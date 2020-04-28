package com.trevexs.kyeko.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trevexs.kyeko.R
import com.trevexs.kyeko.binding.AddTeamData
import com.trevexs.kyeko.binding.StringItemData
import com.trevexs.kyeko.databinding.ActivityAddTeamBinding
import com.trevexs.kyeko.interfaces.Constants.Intents.GAME_ID
import com.trevexs.kyeko.models.Mutations
import com.trevexs.kyeko.models.Team
import com.trevexs.kyeko.utils.createRealmObject
import com.trevexs.kyeko.utils.showSnackBar
import io.realm.Realm

class AddTeam : AppCompatActivity() {
    private var realm: Realm? = null
    private val teams: ArrayList<Team> = ArrayList()
    private var mutations: Mutations? = null
    var gameId: Long = 0
    val data = AddTeamData()
    val binding:ActivityAddTeamBinding by lazy {
        DataBindingUtil.setContentView<ActivityAddTeamBinding>(this, R.layout.activity_add_team)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameId = intent.getLongExtra(GAME_ID, 0)
        realm = createRealmObject()
        mutations = Mutations(realm)
        if (mutations != null) {
            teams.addAll(mutations!!.getItems<Team>(gameId, fieldName = "gameId"))
        }

        binding.obj = data
        setItems()
    }

    private fun setItems(){
        val teams = mutations?.getItems<Team>(gameId, fieldName = "gameId")
        val stringData: ArrayList<StringItemData> = ArrayList()
        teams?.let {
            for (team in teams) {
                val stringItem = StringItemData(team.id.toInt(), team.name)
                stringData.add(stringItem)
            }
            data.setData(stringData)
        }
    }

    override fun onPause() {
        super.onPause()
        realm?.close()
    }

    override fun onResume() {
        super.onResume()
        if (realm == null) {
            realm = createRealmObject()
        }
    }

    fun addTeam(view: View) {
        val team = binding.teamText.text.toString()
        if (team == ""){
            binding.teamText.error = "Please add a team"
        }else {
            binding.teamText.setText("")
            createTeam(team)
            setItems()
        }
    }

    private fun createTeam(team: String) {
        realm?.beginTransaction()
        val nextId = mutations?.getNextId<Team>()!!
        val teamObject = Team(name = team, id = nextId, gameId = gameId)
        realm?.copyToRealm(teamObject)
        realm?.commitTransaction()
    }

    fun startGame(view: View) {
        if (data.data.size <= 1) {
            showSnackBar("Please add teams > 1 to start playing")
        }else {
            // show playing page
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(GAME_ID, gameId)
            startActivity(intent)
        }
    }
}
