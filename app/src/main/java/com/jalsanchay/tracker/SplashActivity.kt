package com.jalsanchay.tracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.jalsanchay.tracker.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar on splash
        supportActionBar?.hide()

        startAnimations()

        // Navigate to MainActivity after 4 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 4000)
    }

    private fun startAnimations() {

        // Step 1 Droplet falls from top (0s)
        binding.ivDroplet.translationY = -300f
        binding.ivDroplet.alpha = 0f
        binding.ivDroplet.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(700)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setStartDelay(200)
            .start()

        // Step 2 Water fills up from bottom (after droplet lands ~900ms)
        binding.viewWaterFill.scaleY = 0f
        binding.viewWaterFill.pivotY = binding.viewWaterFill.height.toFloat()
        binding.viewWaterFill.animate()
            .scaleY(1f)
            .setDuration(1800)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setStartDelay(900)
            .start()

        // Step 3 Ripple rings appear (1s)
        binding.viewRipple1.alpha = 0f
        binding.viewRipple1.scaleX = 0.3f
        binding.viewRipple1.scaleY = 0.3f
        binding.viewRipple1.animate()
            .alpha(0.6f).scaleX(1f).scaleY(1f)
            .setDuration(600).setStartDelay(950)
            .withEndAction {
                binding.viewRipple1.animate().alpha(0f).setDuration(400).start()
            }.start()

        binding.viewRipple2.alpha = 0f
        binding.viewRipple2.scaleX = 0.2f
        binding.viewRipple2.scaleY = 0.2f
        binding.viewRipple2.animate()
            .alpha(0.4f).scaleX(1.4f).scaleY(1.4f)
            .setDuration(800).setStartDelay(1100)
            .withEndAction {
                binding.viewRipple2.animate().alpha(0f).setDuration(400).start()
            }.start()

        // Step 4 App name slides up + fades in (2s)
        binding.tvAppName.translationY = 80f
        binding.tvAppName.alpha = 0f
        binding.tvAppName.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(700)
            .setStartDelay(2000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // Step 5 Tagline fades in (2.5s)
        binding.tvTagline.alpha = 0f
        binding.tvTagline.animate()
            .alpha(1f)
            .setDuration(600)
            .setStartDelay(2600)
            .start()

        // Step 6 Bottom text fades in (3s)
        binding.tvBottomText.alpha = 0f
        binding.tvBottomText.animate()
            .alpha(1f)
            .setDuration(500)
            .setStartDelay(3100)
            .start()
    }
}
