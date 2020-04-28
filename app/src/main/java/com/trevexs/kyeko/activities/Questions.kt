package com.trevexs.kyeko.activities

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trevexs.kyeko.R
import com.trevexs.kyeko.binding.QuestionsData
import com.trevexs.kyeko.binding.StringItemData
import com.trevexs.kyeko.databinding.ActivityQuestionsBinding
import com.trevexs.kyeko.interfaces.Constants
import com.trevexs.kyeko.interfaces.OnAdapterItemClick
import com.trevexs.kyeko.interfaces.OnAdapterItemClickListener
import com.trevexs.kyeko.models.Mutations
import com.trevexs.kyeko.models.Question
import com.trevexs.kyeko.models.Round
import com.trevexs.kyeko.utils.createRealmObject
import io.realm.Realm


class Questions : AppCompatActivity() {
    private var realm: Realm? = null
    private var countDownTimer: CountDownTimer? = null
    val binding: ActivityQuestionsBinding by lazy {
        DataBindingUtil.setContentView<ActivityQuestionsBinding>(this, R.layout.activity_questions)
    }
    var roundId: Long = 0
    private val questions: ArrayList<Question> = ArrayList()
    private var mutations: Mutations? = null
    val data = QuestionsData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roundId = intent.getLongExtra(Constants.Intents.ROUND_ID, 0)
        val teamName = intent.getStringExtra(Constants.Intents.TEAM_NAME)
        supportActionBar?.title = teamName
        realm = createRealmObject()
        mutations = Mutations(realm)
        binding.obj = data
        setItems()
        startTimer()

        OnAdapterItemClickListener.onAdapterItemClick = object: OnAdapterItemClick {
            override fun onClick(view: View) {
                if (view is CheckBox) {
                    if (view.isChecked) {
                        data.score += 1
                    }else {
                        data.score -= 1
                    }
                }
            }
        }
    }

    private fun setItems(){
        val round: Round? = mutations?.getItem(roundId)
        val stringData: ArrayList<StringItemData> = ArrayList()
        round?.questions?.let {
            for (question in round.questions) {
                val stringItem = StringItemData(question.id.toInt(), question.question)
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

    //start timer function
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                data.seconds = (millisUntilFinished / 1000).toInt()
            }
            override fun onFinish() {
                //play buzzing sound
            }
        }
        countDownTimer?.start()
    }

    fun submit(view: View) {

        for (item in data.data) {
            val question = Question(question = item.name, id = item.id.toLong(), answeredCorrectly = item.checked)
            realm?.beginTransaction()
            realm?.copyToRealmOrUpdate(question)
            realm?.commitTransaction()
        }
        onBackPressed()
    }
}
