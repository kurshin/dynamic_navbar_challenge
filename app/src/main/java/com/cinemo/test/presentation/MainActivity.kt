package com.cinemo.test.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.cinemo.data_parser.CinemoParser
import com.cinemo.test.data.AssetFileReader
import com.cinemo.test.data.MediaDataRepository
import com.cinemo.test.databinding.ActivityMainBinding
import com.cinemo.test.presentation.ui.main.SectionsPagerAdapter
import com.cinemo.data_parser.Result

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            MediaDataRepository(
                AssetFileReader(this),
                CinemoParser()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpMediaData()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        viewModel.loadMediaData("media.json")
    }

    private fun setUpMediaData() {
        viewModel.mediaData.observe(this) { result->
            when (result) {
                is Result.Success -> {
                    val mediaData = result.data
                    Log.i("1111", "Media Data: $mediaData")
                }
                is Result.Error -> {
                    // Handle the error case
                    Log.i("1111", "Error: ${result.exception.message}")
                }
            }
        }
    }
}