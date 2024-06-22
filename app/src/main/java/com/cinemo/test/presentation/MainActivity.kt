package com.cinemo.test.presentation

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.cinemo.data_parser.CinemoParser
import com.cinemo.test.data.AssetFileReader
import com.cinemo.test.data.MediaDataRepository
import com.cinemo.test.databinding.ActivityMainBinding
import com.cinemo.data_parser.Result
import com.cinemo.test.R
import com.cinemo.test.domain.Item
import com.cinemo.test.domain.MediaData
import com.cinemo.test.presentation.ui.main.page.PageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
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

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)

        setUpMediaData()
        viewModel.loadMediaData("media.json")
    }

    private fun setUpMediaData() {
        viewModel.mediaData.observe(this) { result->
            when (result) {
                is Result.Success -> {
                    val mediaData = result.data
                    constructUI(mediaData)
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
            binding.progressBar.isVisible = false
        }
    }

    private fun constructUI(mediaData: MediaData) {
        constructAppBar(mediaData)
        constructNavigation(mediaData)

        val firstItem = mediaData.items.first()
        navigateToPage(firstItem.id.hashCode(), getBundle(firstItem), true)

        setupBottomNavigationMenu(binding.navView, mediaData.items)

        binding.navView.setOnItemSelectedListener { item ->
            val selectedItem = mediaData.items.first { it.id.hashCode() == item.itemId }
            navigateToPage(
                item.itemId,
                getBundle(selectedItem),
                mediaData.items.first().id.hashCode() == item.itemId
            )
            true
        }
    }

    private fun getBundle(item: Item): Bundle {
        return Bundle().apply {
            putSerializable(PageFragment.ARG_ITEM, item)
        }
    }

    private fun setupBottomNavigationMenu(
        bottomNavigationView: BottomNavigationView,
        items: List<Item>
    ) {
        bottomNavigationView.menu.clear()
        items.forEachIndexed { index, item ->
            val menuItem = bottomNavigationView.menu.add(0, item.id.hashCode(), index, item.title)
            val imgUrl = item.thumbnail

            Glide.with(bottomNavigationView.context)
                .asBitmap()
                .load(imgUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        val drawable = BitmapDrawable(bottomNavigationView.resources, resource)
                        menuItem.icon = drawable
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }

    private fun navigateToPage(destinationId: Int, bundle: Bundle, isInclusive: Boolean = false) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestinationId, isInclusive)
            .build()

        navController.navigate(destinationId, bundle, navOptions)
    }

    private fun constructNavigation(mediaData: MediaData) {
        navController.graph = navController.createGraph(startDestination = mediaData.items[0].id.hashCode()) {
            mediaData.items.forEach { item ->
                fragment<PageFragment>(item.id.hashCode()) {
                    argument(PageFragment.ARG_ITEM) {
                        type = NavType.SerializableType(Item::class.java)
                    }
                }
            }
            fragment<PageFragment>("/add".hashCode()) {
                argument(PageFragment.ARG_ITEM) {
                    type = NavType.SerializableType(Item::class.java)
                }
            }
        }
    }

    private fun constructAppBar(mediaData: MediaData) {
        val appBarConfiguration = AppBarConfiguration(
            mediaData.items.map { it.id.hashCode() }.toSet()
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}