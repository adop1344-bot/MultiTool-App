package com.multitool.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.multitool.app.databinding.ItemToolBinding

enum class ToolCategory {
    NETWORK, TEXT, FILE, CONVERTER
}

data class ToolItem(
    val name: String,
    val description: String,
    val iconRes: Int,
    val category: ToolCategory
)

class ToolAdapter(
    private val tools: List<ToolItem>,
    private val onItemClick: (ToolItem) -> Unit
) : RecyclerView.Adapter<ToolAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemToolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tools[position])
    }

    override fun getItemCount() = tools.size

    inner class ViewHolder(private val binding: ItemToolBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tool: ToolItem) {
            binding.toolName.text = tool.name
            binding.toolDescription.text = tool.description
            binding.toolIcon.setImageResource(tool.iconRes)

            val color = when (tool.category) {
                ToolCategory.NETWORK -> R.color.tool_network
                ToolCategory.TEXT -> R.color.tool_text
                ToolCategory.FILE -> R.color.tool_file
                ToolCategory.CONVERTER -> R.color.tool_converter
            }
            binding.cardView.setCardBackgroundColor(binding.root.context.getColor(color))

            binding.root.setOnClickListener { onItemClick(tool) }
        }
    }
}
