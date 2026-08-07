package com.sisco.tabpigs.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback
import com.sisco.tabpigs.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InterstitialAdManager(private val context: Context) {
    private var mInterstitialAd: InterstitialAd? = null

    fun loadAd(appId: String, adUnitId: String) {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(
                context,
                InitializationConfig.Builder(appId).build()
            ) {
                InterstitialAd.load(
                    AdRequest.Builder(adUnitId).build(),
                    object : AdLoadCallback<InterstitialAd> {

                        override fun onAdLoaded(ad: InterstitialAd) {
                            mInterstitialAd = ad
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.e("TAG", "onAdFailedToLoad: $adError")
                            mInterstitialAd = null
                        }
                    }
                )
            }
        }
    }

    fun showAd(activity: Activity, onAdDismiss: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.adEventCallback = object : InterstitialAdEventCallback {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("AdMobHelper", "Ad was dismissed.")
                    mInterstitialAd = null
                    onAdDismiss()
                }

                override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                    Log.e("AdMobHelper", "Ad failed to show: $error")
                    mInterstitialAd = null
                    onAdDismiss()
                }

                override fun onAdShowedFullScreenContent() {}
                override fun onAdImpression() {}
                override fun onAdClicked() {}
            }
            mInterstitialAd?.show(activity)
        }else {
            onAdDismiss()
        }
    }
}

@Composable
fun rememberInterstitialAd(): InterstitialAdManager {
    val context = LocalContext.current
    val adManager = remember { InterstitialAdManager(context) }

    val appId = stringResource(id = R.string.ads_app_id)
    val adsUnitId = stringResource(id = R.string.ads_unit_id)

    LaunchedEffect(Unit) {
        adManager.loadAd(appId, adsUnitId)
    }
    return adManager
}