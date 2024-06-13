package com.cinemo.test.presentation.ui.main.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemo.test.databinding.FragmentMainBinding
import com.cinemo.test.domain.Item
import com.cinemo.test.presentation.ui.main.page.ItemAdapter
import com.cinemo.test.presentation.ui.main.page.PageViewModel

class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ItemAdapter()
        recyclerView.adapter = adapter

        pageViewModel.item.observe(viewLifecycleOwner) {
            adapter.submitList(it.content?.items ?: emptyList())
        }

        // Retrieve the item from arguments and set it to the ViewModel
        arguments?.getSerializable(ARG_ITEM)?.let {
            Log.i("1111", "it = $it")
            pageViewModel.setItem(it as Item)
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
