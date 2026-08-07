package com.sisco.tabpigs

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.SystemClock
import android.util.Log
import android.view.View
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun View.setOnSingleClickListener(crossinline onClick: (View?) -> Unit) {
    val minClickInterval = 500L
    var mLastClickTime = 0L
    this.setOnClickListener {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - mLastClickTime >= minClickInterval) {
            mLastClickTime = currentTime
            onClick.invoke(this)
        }
    }
}

inline fun Activity.loadInterstitialAd(
    appId: String,
    adUnitId: String,
    crossinline onAdLoaded: (InterstitialAd) -> Unit,
    crossinline onAdFailed: () -> Unit = {},
    crossinline onAdDismissed: () -> Unit = {}
) {
    val backgroundScope = CoroutineScope(Dispatchers.IO)
    backgroundScope.launch {
        MobileAds.initialize(
            this@loadInterstitialAd,
            InitializationConfig.Builder(appId).build()
        ) {
            Log.i("AdMobHelper", "initAds: complete")

            InterstitialAd.load(
                AdRequest.Builder(adUnitId).build(),
                object : AdLoadCallback<InterstitialAd> {

                    override fun onAdLoaded(ad: InterstitialAd) {

                        // Set event callback
                        ad.adEventCallback = object : InterstitialAdEventCallback {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d("AdMobHelper", "Ad was dismissed.")
                                onAdDismissed()
                            }

                            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                                Log.e("AdMobHelper", "Ad failed to show: $error")
                            }

                            override fun onAdShowedFullScreenContent() {}
                            override fun onAdImpression() {}
                            override fun onAdClicked() {}
                        }

                        onAdLoaded(ad)
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("AdMobHelper", "onAdFailedToLoad: $error")
                        onAdFailed()
                    }
                }
            )
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}