package com.korkort.cquiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korkort.cquiz.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categories: Map<String, String>,
    private val prefs: Prefs,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.VH>() {

    private val keys = categories.keys.toList()

    inner class VH(val b: ItemCategoryBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = keys.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val key = keys[position]
        val name = categories[key] ?: key
        val correct = prefs.getCorrect(key)
        val total = prefs.getTotal(key)
        val pct = if (total > 0) correct * 100 / total else 0

        holder.b.tvCategoryName.text = name
        holder.b.tvCategoryStats.text = if (total == 0) "Ej startad" else "$correct/$total rätt ($pct%)"
        holder.b.progressBar.progress = pct

        holder.b.root.setOnClickListener { onClick(key) }
    }
}
