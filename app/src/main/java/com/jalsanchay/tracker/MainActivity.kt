package com.jalsanchay.tracker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jalsanchay.tracker.databinding.ActivityMainBinding
import com.jalsanchay.tracker.viewmodel.WaterViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val viewModel: WaterViewModel by viewModels()

        // On first launch → show Setup
        // On every relaunch after setup is done → go straight to Dashboard
        if (viewModel.isSetupDone) {
            navController.navigate(R.id.dashboardFragment)
        }

        val bottomNav: BottomNavigationView = binding.bottomNavigation
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.setupFragment,
                R.id.dashboardFragment,
                R.id.dataEntryFragment,
                R.id.monthlyReportFragment,
                R.id.tipsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)

        // Override bottom nav so Setup icon ALWAYS opens Setup page
        // even after setup is already done — user can re-edit anytime
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.setupFragment -> {
                    navController.navigate(R.id.setupFragment)
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}