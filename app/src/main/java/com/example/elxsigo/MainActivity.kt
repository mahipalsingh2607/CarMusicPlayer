package com.example.elxsigo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.elxsigo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var selectedMenuId = R.id.menuMusicFeed
    private lateinit var binding: ActivityMainBinding
    val stereoFragment = StereoFragment()
    val navigationFragment = NavigationFragment()
    val dialFragment = DialFragment()
    val weatherFragment = WeatherFragment()
    private var active: Fragment = stereoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        loadAllFragments()
        initView()
    }

    private fun initView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuMusicFeed -> {
                    selectedMenuId = it.itemId
                    supportFragmentManager.beginTransaction().hide(active).show(stereoFragment)
                        .commit()
                    active = stereoFragment
                }

                R.id.menuMap -> {
                    selectedMenuId = it.itemId
                    supportFragmentManager.beginTransaction().hide(active).show(navigationFragment)
                        .commit()
                    active = navigationFragment
                }

                R.id.menuPhone -> {
                    selectedMenuId = it.itemId
                    supportFragmentManager.beginTransaction().hide(active).show(dialFragment)
                        .commit()
                    active = dialFragment
                }

                R.id.menuWeather -> {
                    selectedMenuId = it.itemId
                    supportFragmentManager.beginTransaction().hide(active).show(weatherFragment)
                        .commit()
                    active = weatherFragment
                }
            }
            true
        }
    }

    fun loadAllFragments() {
        supportFragmentManager.beginTransaction().add(R.id.flContainer, weatherFragment)
            .hide(weatherFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.flContainer, dialFragment)
            .hide(dialFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.flContainer, navigationFragment)
            .hide(navigationFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.flContainer, stereoFragment).commit()
    }

    override fun onBackPressed() {
        val musicIntent = Intent(this, MusicService::class.java)
        stopService(musicIntent)
        finish()
    }
}