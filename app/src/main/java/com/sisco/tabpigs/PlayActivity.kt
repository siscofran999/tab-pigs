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
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.sisco.tabpigs.databinding.ActivityPlayBinding
import com.sisco.tabpigs.databinding.AlertNextLevelGameOverBinding
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback
import com.sisco.tabpigs.Globals.INIT_LEVEL
import com.sisco.tabpigs.Globals.INIT_TARGET_POINT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayActivity : BaseActivity<ActivityPlayBinding>() {

    private var adapter: PlayAdapter = PlayAdapter()
    private var tempPoint = 0
    private val handler = Handler(Looper.getMainLooper())
    private var moleRunnable: Runnable = object : Runnable {
        override fun run() {
            if (!isGameRunning) return

            val updatedList = adapter.currentList.map { model ->
                model.copy(isShowPig = false)
            }.toMutableList()

            var randomIndex = updatedList.indices.random()
            while (randomIndex == tempRandomIndex) {
                randomIndex = updatedList.indices.random()
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
    private var tempRandomIndex = 0
    private var gameTimer: CountDownTimer? = null
    private val totalTime = 25000L
    private var mLevel: Int = INIT_LEVEL
    private var mTargetPoint = INIT_TARGET_POINT
    private var isGameRunning = false
    private var soundPool: SoundPool? = null
    private var sfxClick = 0
    private var gamePreferences: GamePreferences? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun getViewBinding(): ActivityPlayBinding {
        return ActivityPlayBinding.inflate(layoutInflater)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val initData = (1..9).map { PlayModel(it, false) }
        gamePreferences = GamePreferences(this)

        binding.tvPoint.text =
            getString(R.string.value_point, tempPoint.toString().padStart(2, '0'))
        binding.rvPlay.adapter = adapter
        binding.rvPlay.layoutManager = object : GridLayoutManager(this, 3) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        adapter.submitList(initData)
        isGameRunning = true

        initAds()
        setUpAudio()

        lifecycleScope.launch {
            if (gamePreferences?.hasSavedGame() == true) {
                mLevel = gamePreferences?.getLastLevel() ?: INIT_LEVEL
            }
            mTargetPoint = gamePreferences?.getTargetPoint() ?: mTargetPoint
            binding.tvLevel.text = getString(R.string.value_level, mLevel.toString())
            binding.tvTargetPoint.text = mTargetPoint.toString()
            startTimer()
            startMoleGame()
        }
    }

    private fun initAds() {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize GMA Next-Gen SDK on a background thread.
            MobileAds.initialize(
                this@PlayActivity,
                // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
                InitializationConfig.Builder(getString(R.string.ads_app_id)).build()
            ) {
                // Adapter initialization is complete.
                Log.i(TAG, "initAds: complete masukk -> ")
                InterstitialAd.load(
                    AdRequest.Builder(getString(R.string.ads_unit_id)).build(),
                    object : AdLoadCallback<InterstitialAd> {
                        override fun onAdLoaded(ad: InterstitialAd) {
                            // Called when an ad has loaded.
                            Log.i(TAG, "onAdLoaded: masukk -> ")
                            mInterstitialAd = ad
                            ad.adEventCallback =
                                object : InterstitialAdEventCallback {
                                    override fun onAdDismissedFullScreenContent() {
                                        Log.d(TAG, "Ad was dismissed. masukk -> ")
                                        mInterstitialAd = null
                                        lifecycleScope.launch {
                                            gamePreferences?.saveProgress(INIT_LEVEL, INIT_TARGET_POINT, false)
                                        }
                                        finish()
                                    }

                                    override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                                        Log.d(TAG, "Ad failed to show. masukk -> ")
                                        // Don't forget to set the ad reference to null so you
                                        // don't show the ad a second time.
                                        mInterstitialAd = null
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        Log.d(TAG, "Ad showed fullscreen content. masukk -> ")
                                    }

                                    override fun onAdImpression() {
                                        Log.d(TAG, "Ad recorded an impression. masukk -> ")
                                    }

                                    override fun onAdClicked() {
                                        Log.d(TAG, "Ad was clicked. masukk -> ")
                                    }
                                }
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Called when ad fails to load.
                            Log.e(TAG, "onAdFailedToLoad: masukk -> ${loadAdError.responseInfo} $loadAdError")
                            mInterstitialAd = null
                        }
                    }
                )
            }
            // SDK initialization is complete. If you don't want to wait for bidding adapters to finish
            // initializing, start loading ads now.
        }
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
        handler.removeCallbacks(moleRunnable)
        handler.post(moleRunnable)
    }

    override fun initListener() {
        adapter.itemClickListener(object : PlayAdapter.ItemClickListener {
            override fun onItemClick(item: PlayModel) {
                if (!isGameRunning) return
                if (item.isShowPig == true) {
                    handler.removeCallbacks(moleRunnable)
                    soundPool?.play(sfxClick, 1.0f, 1.0f, 1, 0, 1.0f)
                    tempPoint += 1
                    binding.tvPoint.text =
                        getString(R.string.value_point, tempPoint.toString().padStart(2, '0'))

                    handler.post(moleRunnable)
                }
            }
        })
    }

    private fun startTimer() {
        gameTimer = object : CountDownTimer(totalTime, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val timeElapsed = totalTime - millisUntilFinished
                val progressPercentage =
                    ((timeElapsed.toFloat() / totalTime.toFloat()) * 100).toInt()
                binding.progressTime.progress = progressPercentage
            }

            override fun onFinish() {
                isGameRunning = false
                binding.progressTime.progress = 100

                moleRunnable.let { handler.removeCallbacks(it) }
                gameTimer?.cancel()
                handler.removeCallbacksAndMessages(null)
                if (tempPoint > mTargetPoint) {
                    showGameStatusDialog(false) {
                        mLevel = mLevel.plus(1)
                        mTargetPoint = mTargetPoint.plus(mLevel).plus(2)
                        startActivity(newIntent(this@PlayActivity))
                        lifecycleScope.launch {
                            gamePreferences?.saveProgress(mLevel, mTargetPoint, true)
                        }
                        finish()
                    }
                } else {
                    showGameStatusDialog(true) {
                        mInterstitialAd?.show(this@PlayActivity)
                    }
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

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            if (!isFinishing) {
                gamePreferences?.saveProgress(mLevel, mTargetPoint, true)
            }
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        gameTimer?.cancel()
        soundPool?.release()
        soundPool = null
        super.onDestroy()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PlayActivity::class.java)
        }
    }
}