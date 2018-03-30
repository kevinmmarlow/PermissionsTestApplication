package com.foxconn.permissionstestapplication

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView

typealias ItemClickListener = (position: Int) -> Unit

class PermissionsAdapter : RecyclerView.Adapter<PermissionViewHolder>() {

    private var clickListener: ItemClickListener? = null
    private var items: List<PermissionViewModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        return PermissionViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        holder.bind(items[position])
        holder.setClickListener(clickListener)
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.clickListener = listener
    }

    fun setData(items: List<PermissionViewModel>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class PermissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvTitle = itemView.findViewById<TextView>(R.id.title)
    private val switchCheck = itemView.findViewById<Switch>(R.id.switchCheck)
    private var clickListener: ItemClickListener? = null

    companion object {
        fun create(parent: ViewGroup): PermissionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_permission, parent, false)
            return PermissionViewHolder(view)
        }
    }

    fun bind(viewModel: PermissionViewModel) {
        with(viewModel) {
            tvTitle.text = title
            switchCheck.isChecked = granted
        }

        itemView.setOnClickListener {
            clickListener?.invoke(adapterPosition)
        }
    }

    fun setClickListener(clickListener: ItemClickListener?) {
        this.clickListener = clickListener
    }
}