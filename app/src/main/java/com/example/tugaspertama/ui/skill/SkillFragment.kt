package com.example.tugaspertama.ui.skill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tugaspertama.R
import com.example.tugaspertama.databinding.FragmentSkillBinding
import com.example.tugaspertama.databinding.FragmentSkillItemBinding
import java.util.Locale

class SkillFragment : Fragment() {

    private var _binding: FragmentSkillBinding? = null
    private val binding get() = _binding!!
    private var langList = ArrayList<SkillData>()
    private lateinit var adapter: SkillAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val skillViewModel = ViewModelProvider(this)[SkillViewModel::class.java]
        _binding = FragmentSkillBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recycleViewSkill
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        addDataToList()

        adapter = SkillAdapter(langList){
            navigateToDetail(it)
        }
        recyclerView.adapter = adapter
        skillViewModel.texts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        return root
    }

    private fun addDataToList() {
        langList.add(SkillData("Java", R.drawable.java))
        langList.add(SkillData("Python", R.drawable.python_logo_icon))
        langList.add(SkillData("C++", R.drawable.iso_c___logo_svg))
        langList.add(SkillData("Kotlin", R.drawable.kotlin_1_logo_png_transparent))
        langList.add(SkillData("PHP", R.drawable.php))
        langList.add(SkillData("Ruby", R.drawable.ruby))
        langList.add(SkillData("HTML", R.drawable.htm))
        langList.add(SkillData("CSS", R.drawable.css3_logo_and_wordmark_svg))
        langList.add(SkillData("JavaScript", R.drawable.javascript_logo))
        langList.add(SkillData("C#", R.drawable.c_sharp_c_logo_02f17714ba_seeklogo_com))
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = java.util.ArrayList<SkillData>()
            for (i in langList) {
                if (i.title.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(getActivity(),"Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetail(extraName: String){
        findNavController().navigate(SkillFragmentDirections.actionNavSkillToSkillDetail(extraName))
    }

    class SkillAdapter(var langlist: List<SkillData>, val listener: (string: String) -> Unit) :
        ListAdapter<String, SkillViewHolder>(object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        })  {

        fun setFilteredList(langlist: List<SkillData>) {
            this.langlist = langlist
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
            val binding = FragmentSkillItemBinding.inflate(LayoutInflater.from(parent.context))
            return SkillViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
            holder.contentView.text = langlist[position].title
            holder.imageView.setImageResource(langlist[position].logo)

            holder.root.setOnClickListener { _->
                listener(langlist[position].title)
            }
        }
        override fun getItemCount(): Int {
            return langlist.size
        }
    }

    class SkillViewHolder(binding: FragmentSkillItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView: ImageView = binding.itemImage
        val contentView: TextView = binding.itemContent
        val root = binding.root
    }
}
