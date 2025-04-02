package com.example.studypool.admin
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.studypool.R
import com.example.studypool.appctrl.LeaderboardItem

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    private var items: List<LeaderboardItem> = emptyList()

    fun submitList(list: List<LeaderboardItem>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val positionText: TextView = view.findViewById(R.id.rankPosition)
        val nameText: TextView = view.findViewById(R.id.userName)
        val xpText: TextView = view.findViewById(R.id.userXP)
        val trophy: ImageView = view.findViewById(R.id.rankTrophy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.positionText.text = "${position + 1}"
        holder.nameText.text = user.name
        holder.xpText.text = "${user.xp} XP"

        when (position) {
            0 -> holder.trophy.setImageResource(R.drawable.gold)
            1 -> holder.trophy.setImageResource(R.drawable.silver)
            2 -> holder.trophy.setImageResource(R.drawable.bronze)
            else -> holder.trophy.setImageResource(R.drawable.user)
        }
    }
}
