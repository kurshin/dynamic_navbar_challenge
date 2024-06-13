package com.cinemo.test.presentation.ui.main.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemo.test.R
import com.cinemo.test.databinding.FragmentMainBinding
import com.cinemo.test.domain.GRID_TYPE
import com.cinemo.test.domain.Item

class PlaceholderFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        arguments?.getSerializable(ARG_ITEM)?.let {
            val media = it as Item
            if (media.content?.displayStyle == GRID_TYPE) {
                recyclerView.layoutManager = GridLayoutManager(context, 2)
            } else {
                recyclerView.layoutManager = LinearLayoutManager(context)
            }

            val adapter = ItemAdapter(media.content?.displayStyle ?: GRID_TYPE) { item ->
//                val fragment = newInstance(item) // Pass item ID or other data as needed
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.view_pager, fragment)
//                    .addToBackStack(null)
//                    .commit()
            }
            recyclerView.adapter = adapter
            adapter.submitList(it.content?.items ?: emptyList())
        }

        return root
    }

    companion object {

        private const val ARG_ITEM = "item"
        @JvmStatic
        fun newInstance(item: Item): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM, item)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
