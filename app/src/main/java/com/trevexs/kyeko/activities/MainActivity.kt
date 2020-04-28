package com.trevexs.kyeko.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trevexs.kyeko.R
import com.trevexs.kyeko.binding.MainActivityData
import com.trevexs.kyeko.databinding.ActivityMainBinding
import com.trevexs.kyeko.interfaces.Constants.Intents.GAME_ID
import com.trevexs.kyeko.interfaces.Constants.Intents.ROUND_ID
import com.trevexs.kyeko.interfaces.Constants.Intents.TEAM_NAME
import com.trevexs.kyeko.models.*
import com.trevexs.kyeko.utils.SharedPrefs
import com.trevexs.kyeko.utils.createRealmObject
import com.trevexs.kyeko.utils.showSnackBar
import io.realm.Realm
import io.realm.RealmList

class MainActivity : AppCompatActivity() {
    private lateinit var gamePlay: GamePlay
    private lateinit var realm: Realm
    private var gameId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Kyekyo"
        Realm.init(this)
        realm = createRealmObject()
        gameId = intent.getLongExtra(GAME_ID, -1)
        if (gameId < 0) {
            val sharedPrefs = SharedPrefs(this)
            gameId = sharedPrefs.getGameId()
        }
        gamePlay = GamePlay(this, realm, gameId)
        var currentTeam = ""
        if (gamePlay.teamsList.size > 1) {
            currentTeam = gamePlay.teamsList[gamePlay.currentTeam]?.name ?: ""
        }

        val obj = MainActivityData(currentTeam)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
            )
        binding.obj = obj
    }

    override fun onPause() {
        super.onPause()
        realm.close()
    }

    override fun onResume() {
        super.onResume()
        realm = createRealmObject()
    }

    fun newGame(view: View) {
        gamePlay = GamePlay(this, realm)
        val intent = Intent(this, AddTeam::class.java)
        intent.putExtra(GAME_ID, gamePlay.game.id)
        startActivity(intent)
    }

    fun play(view: View) {
        if (gamePlay.teamsList.size <= 1) {
            showSnackBar("Please first Add Teams Before Playing")
        } else {
            gamePlay.addRound()
            val intent = Intent(this, Questions::class.java)
            intent.putExtra(ROUND_ID, gamePlay.currentRound)
            intent.putExtra(TEAM_NAME, gamePlay.teamsList[gamePlay.currentTeam]?.name)
            startActivity(intent)
        }

    }

    private fun getCurrentRound(roundId: Long) {
        val round: Round? = gamePlay.mutations.getItem<Round>(roundId)
        Log.d("MainActivity", round?.questions.toString())
    }

    fun allGames(view: View) {
        startActivity(Intent(this, AllGames::class.java))
    }

    fun scores(view: View) {}

}

class GamePlay(
    var context: Context,
    var realm: Realm,
    var gameId: Long = -1,
    var currentTeam: Int = 0,
    private var startIndex: Int = 0,
    private var roundCounter: Int = 1,
    private var maxItemsPerRound: Int = 5,
    var currentRound: Long = 0
) {
    private val sharedPrefs = SharedPrefs(context)
    private val questionsList = mutableListOf<String>(
        "Kaguta",
        "Wacha",
        "Muchina",
        "Machuwa",
        "Gonya",
        "Idea",
        "Zero",
        "Kenjoy",
        "Namungona"
    )
    var game: Game
    val mutations by lazy { Mutations(realm) }
    var teamsList: RealmList<Team> = RealmList<Team>()
    private val teams = listOf<String>("A", "B", "C")
    private var gameEnded = false

    private fun createTeams(): RealmList<Team> {
        val teamsObjects = RealmList<Team>()
        realm.beginTransaction()
        for (team in teams) {
            val nextId = mutations.getNextId<Team>()
            val teamObject = Team(name = team, id = nextId, gameId = game.id)
            teamsObjects.add(realm.copyToRealm(teamObject))
        }
        realm.commitTransaction()
        return teamsObjects
    }

    private fun getTeams() {
        teamsList = mutations.getItemsRealmList<Team>(game.id, fieldName = "gameId")
    }

    private fun getNextTeam(): Team? {
        val team = teamsList[currentTeam]
        if (currentTeam >= teamsList.size - 1) {
            currentTeam = 0
            roundCounter += 1
        } else {
            currentTeam += 1
        }
        sharedPrefs.setCurrentTeam(currentTeam)
        sharedPrefs.setCurrentRound(roundCounter)
        return team;
    }

    fun addRound() {
        realm.beginTransaction()
        val nextId = mutations.getNextId<Round>()
        val round = realm.createObject(Round::class.java, nextId)
        round.counter = roundCounter
        round.questions = getQuestions()
        getNextTeam()?.let {
            round.team = it
        }
        round.gameId = game.id
        game.rounds.add(round)
        currentRound = round.id
        realm.commitTransaction()
    }

    private fun getQuestions(): RealmList<Question> {
        // begin and commit transaction externally
        val questions: RealmList<Question> = RealmList()
        var endIndex = startIndex + maxItemsPerRound
        if (endIndex > questionsList.size) endIndex = questionsList.size
        val roundQuestions = questionsList.subList(startIndex, endIndex)

        if (roundQuestions.size == 0) {
            context.showSnackBar("Questions Finished")
            gameEnded = true
        }

        for (i in 0..4) {
            if (i >= roundQuestions.size) {
                break
            }
            val item = roundQuestions[i]
//            questionsList.removeAt(i)
            val nextId = mutations.getNextId<Question>()
            val question = Question(question = item, id = nextId)
            questions.add(realm.copyToRealm(question))
        }
        startIndex = endIndex
        return questions
    }

    private fun createNewGame(): Game {
        realm.beginTransaction()
        val nextId = mutations.getNextId<Game>()
        val game = realm.createObject(Game::class.java, nextId)
        sharedPrefs.setGameId(nextId)
        sharedPrefs.setCurrentTeam(0) // reset starting team
        sharedPrefs.setCurrentRound(1) // reset current team
        realm.commitTransaction()
        return game
    }

    init {
        game = if (gameId <= -1) {
            createNewGame()
        } else {
            mutations.getItem(gameId) ?: createNewGame()
        }
        roundCounter = sharedPrefs.getCurrentRound()
        currentTeam = sharedPrefs.getCurrentTeam()

//        teamsList = createTeams()
        getTeams() //create TeamsList
    }

}