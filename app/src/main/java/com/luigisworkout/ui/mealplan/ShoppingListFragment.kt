package com.luigisworkout.ui.mealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luigisworkout.R
import com.luigisworkout.data.database.entities.ShoppingItem
import com.luigisworkout.databinding.FragmentShoppingListBinding
import com.luigisworkout.luigiApp
import com.luigisworkout.viewmodel.MealPlanViewModel
import com.luigisworkout.viewmodel.appViewModelFactory

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealPlanViewModel by activityViewModels {
        appViewModelFactory(requireActivity().luigiApp)
    }

    private lateinit var adapter: ShoppingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = ShoppingAdapter(
            onToggle = { item -> viewModel.toggleShoppingItem(item) },
            onDelete = { item -> viewModel.deleteShoppingItem(item) }
        )
        binding.rvShoppingList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvShoppingList.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.shoppingItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            val pendingCount = items.count { !it.isChecked }
            binding.tvItemsCount.text = resources.getQuantityString(
                R.plurals.shopping_pending_count, pendingCount, pendingCount
            )
        }
    }

    private fun setupListeners() {
        binding.btnAddItem.setOnClickListener {
            val name = binding.etItemName.text.toString().trim()
            if (name.isNotEmpty()) {
                viewModel.addShoppingItem(name)
                binding.etItemName.text?.clear()
            }
        }

        binding.btnClearChecked.setOnClickListener {
            viewModel.clearCheckedItems()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ShoppingAdapter(
    private val onToggle: (ShoppingItem) -> Unit,
    private val onDelete: (ShoppingItem) -> Unit
) : ListAdapter<ShoppingItem, ShoppingAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbChecked: CheckBox = view.findViewById(R.id.cbChecked)
        val tvName: TextView = view.findViewById(R.id.tvItemName)
        val tvDetails: TextView = view.findViewById(R.id.tvItemDetails)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvName.text = item.name
        holder.tvDetails.text = if (item.quantity.isNotEmpty()) item.quantity else item.supermarket

        // Detach listener before mutating state so the recycled checkbox doesn't fire
        // our toggle callback with stale data.
        holder.cbChecked.setOnCheckedChangeListener(null)
        holder.cbChecked.isChecked = item.isChecked
        holder.cbChecked.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != item.isChecked) onToggle(item)
        }

        holder.btnDelete.setOnClickListener { onDelete(item) }
    }
}
