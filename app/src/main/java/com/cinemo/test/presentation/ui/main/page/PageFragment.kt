package com.cinemo.test.presentation.ui.main.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemo.test.R
import com.cinemo.test.databinding.FragmentPageBinding
import com.cinemo.test.domain.GRID_TYPE
import com.cinemo.test.domain.Item

class PageFragment : Fragment() {

    private var _binding: FragmentPageBinding? = null
    private val viewModel:PageViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPageBinding.inflate(inflater, container, false)
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
                viewModel.setItem(item)
            }

            recyclerView.adapter = adapter
            adapter.submitList(it.content?.items ?: emptyList())

            viewModel.item.observe(viewLifecycleOwner) { item ->
                val subitems = item.content?.items ?: emptyList()
                if (subitems.isEmpty()) {
                    Toast.makeText(requireContext(), "No items", Toast.LENGTH_SHORT).show()
                } else {
                    val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
                    val bnd = Bundle().apply { putSerializable(ARG_ITEM, item) }
                    navController.navigate("/add".hashCode(), bnd)

//                    val fragment = newInstance(item)
//                    parentFragmentManager.beginTransaction()
//                        .add(R.id.pageContainer, fragment)
//                        .commit()
                }
            }
        }

        return root
    }

    companion object {

        const val ARG_ITEM = "item"
        @JvmStatic
        fun newInstance(item: Item): PageFragment {
            return PageFragment().apply {
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
