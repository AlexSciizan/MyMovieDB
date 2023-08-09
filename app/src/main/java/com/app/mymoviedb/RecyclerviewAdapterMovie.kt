package com.app.mymoviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerviewAdapterMovie : RecyclerView.Adapter<RecyclerViewHolderMovie> {
    private var layoutView: LinearLayout? = null

    constructor()
    constructor(itemLists: List<ItemObject>?) {
        Companion.itemList = itemLists
        itemListBackup = itemLists
    }

    val itemList: List<ItemObject>?
        get() = Companion.itemList

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolderMovie {
        layoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_list_movie, null) as LinearLayout
        layoutView!!.background = MainActivity2.setRoundRect(9, 1, "#000000", "#fafafa")
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //        lp.setMargins(0, 10, 0, 10);
        lp.setMargins(10, 17, 10, 15)
        layoutView!!.layoutParams = lp
        return RecyclerViewHolderMovie(layoutView)
    }

    override fun onBindViewHolder(
        holder: RecyclerViewHolderMovie,
        position: Int
    ) { /*final int position*/
        try {
            holder.foto.setImageBitmap(Companion.itemList!![holder.adapterPosition].bitmap)
            holder.id = Companion.itemList!![holder.adapterPosition].getItem(0)
            holder.title.text = Companion.itemList!![holder.adapterPosition].getItem(1)
            holder.date.text = Companion.itemList!![holder.adapterPosition].getItem(2)
            holder.root.setOnClickListener { }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        try {
            return Companion.itemList!!.size
        } catch (ignored: NullPointerException) {
        }
        return 0
    }

    companion object {
        var itemList: List<ItemObject>? = null
        var itemListBackup: List<ItemObject>? = null
    }
}

class RecyclerViewHolderMovie(itemView: View?) : RecyclerView.ViewHolder(
    itemView!!
) {
    var root: LinearLayout
    var id: String? = null
    var foto: ImageView
    var title: TextView
    var date: TextView

    init {
        root = itemView!!.findViewById(R.id.root)
        foto = itemView.findViewById(R.id.list_item_foto)
        title = itemView.findViewById(R.id.list_item_title)
        date = itemView.findViewById(R.id.list_item_date)
    }
}