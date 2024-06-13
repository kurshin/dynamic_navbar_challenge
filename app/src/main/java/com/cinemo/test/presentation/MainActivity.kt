package com.cinemo.test.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cinemo.data_parser.CinemoParser
import com.cinemo.test.data.AssetFileReader
import com.cinemo.test.data.MediaDataRepository
import com.cinemo.test.databinding.ActivityMainBinding
import com.cinemo.test.presentation.ui.main.SectionsPagerAdapter
import com.cinemo.data_parser.Result

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
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

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, emptyList())
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        viewModel.loadMediaData("media.json")
    }

    private fun setUpMediaData() {
        viewModel.mediaData.observe(this) { result->
            when (result) {
                is Result.Success -> {
                    val mediaData = result.data
                    sectionsPagerAdapter.updateItems(mediaData.items)
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
            binding.progressBar.isVisible = false
        }
    }
}