package com.example.studypool.adapters

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.appctrl.UpcomingItem

class UpcomingAdapter(private val list: List<UpcomingItem>) :
    RecyclerView.Adapter<UpcomingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iconType)
        val courseTitle: TextView = view.findViewById(R.id.courseTitle)
        val itemTitle: TextView = view.findViewById(R.id.itemTitle)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.courseTitle.text = item.courseName
        holder.itemTitle.text = item.title
        holder.dateTime.text = item.dateTime

        val iconRes = when (item.type) {
            "class" -> R.drawable.teacher
            "cat" -> R.drawable.test
            "exam" -> R.drawable.exam
            else -> android.R.drawable.ic_menu_today
        }
        holder.icon.setImageResource(iconRes)
    }

    override fun getItemCount(): Int = list.size
}
