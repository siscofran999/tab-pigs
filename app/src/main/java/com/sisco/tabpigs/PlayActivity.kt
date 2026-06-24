package com.sisco.tabpigs

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.sisco.tabpigs.databinding.ActivityPlayBinding
import com.sisco.tabpigs.databinding.AlertNextLevelGameOverBinding
import androidx.core.graphics.drawable.toDrawable

class PlayActivity : BaseActivity<ActivityPlayBinding>() {

    private var adapter: PlayAdapter = PlayAdapter()
    private var tempPoint = 0
    private val handler = Handler(Looper.getMainLooper())
    private var moleRunnable: Runnable? = null
    private var tempRandomIndex = 0
    private var gameTimer: CountDownTimer? = null
    private val totalTime = 30000L
    private var mLevel: Int = 1
    private var mTargetPoint = 30
    private var isGameRunning = false
    private var soundPool: SoundPool? = null
    private var sfxClick = 0

    override fun getViewBinding(): ActivityPlayBinding {
        return ActivityPlayBinding.inflate(layoutInflater)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val initData = (1..9).map { PlayModel(it, false) }
        mLevel = intent.getIntExtra(INTENT_LEVEL, 1)
        binding.tvLevel.text = getString(R.string.value_level, mLevel.toString())
        binding.tvPoint.text = tempPoint.toString()
        binding.rvPlay.adapter = adapter
        binding.rvPlay.layoutManager = GridLayoutManager(this, 3)
        adapter.submitList(initData)
        isGameRunning = true

        setUpAudio()
        startTimer()
        startMoleGame()
    }

    private fun setUpAudio() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

        sfxClick = soundPool?.load(this, R.raw.sfx_click, 1) ?: 0
    }

    private fun startMoleGame() {
        if (!isGameRunning) return
        moleRunnable?.let { handler.removeCallbacks(it) }
        moleRunnable = object : Runnable {
            override fun run() {
                val updatedList = adapter.currentList.map { model ->
                    model.copy(isShowPig = false)
                }.toMutableList()

                var randomIndex = (0 until updatedList.size).random()
                while (randomIndex == tempRandomIndex) {
                    randomIndex = (0 until updatedList.size).random()
                }
                tempRandomIndex = randomIndex

                if (updatedList.isNotEmpty()) {
                    val selectedItem = updatedList[tempRandomIndex]
                    updatedList[tempRandomIndex] = selectedItem.copy(isShowPig = true)
                }

                adapter.submitList(updatedList)

                val delaySpeed = (3000 - (mLevel * 300)).coerceAtLeast(1000).toLong()
                handler.postDelayed(this, delaySpeed)
            }
        }

        handler.post(moleRunnable as Runnable)
    }

    override fun initListener() {
        adapter.itemClickListener(object : PlayAdapter.ItemClickListener {
            override fun onItemClick(item: PlayModel) {
                if (!isGameRunning) return
                if (item.isShowPig == true) {
                    soundPool?.play(sfxClick, 1.0f, 1.0f, 1, 0, 1.0f)
                    tempPoint += 1
                    binding.tvPoint.text = tempPoint.toString()
                    startMoleGame()
                }
            }
        })
    }

    private fun startTimer() {
        gameTimer = object : CountDownTimer(totalTime, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val timeElapsed = totalTime - millisUntilFinished
                val progressPercentage = ((timeElapsed.toFloat() / totalTime.toFloat()) * 100).toInt()
                binding.progressTime.progress = progressPercentage
            }

            override fun onFinish() {
                isGameRunning = false
                binding.progressTime.progress = 100

                moleRunnable?.let { handler.removeCallbacks(it) }
                gameTimer?.cancel()
                handler.removeCallbacksAndMessages(null)
                mTargetPoint = mTargetPoint.plus(mLevel).plus(2)
                if (tempPoint > mTargetPoint) {
                    showGameStatusDialog(false, {
                        startActivity(newIntent(this@PlayActivity, mLevel.plus(1)))
                        finish()
                    })
                }else {
                    showGameStatusDialog(true, {
                        startActivity(Intent(this@PlayActivity, MainActivity::class.java))
                    })
                }
            }
        }.start()
    }

    private fun showGameStatusDialog(isGameOver: Boolean, onDialogClosed: () -> Unit) {
        val binding = AlertNextLevelGameOverBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        binding.apply {
            tvValuePoint.text = tempPoint.toString()
            if (isGameOver) {
                img.setImageResource(R.drawable.img_game_over)
            } else {
                img.setImageResource(R.drawable.img_next_level)
            }
            btnNext.setOnClickListener {
                dialog.dismiss()
                onDialogClosed.invoke()
            }
        }

        dialog.show()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        gameTimer?.cancel()
        soundPool?.release()
        soundPool = null
        super.onDestroy()
    }

    companion object {
        private const val INTENT_LEVEL = "LEVEL"
        fun newIntent(
            context: Context, level: Int = 1,
        ): Intent {
            return Intent(context, PlayActivity::class.java).apply {
                putExtra(INTENT_LEVEL, level)
            }
        }
    }
}