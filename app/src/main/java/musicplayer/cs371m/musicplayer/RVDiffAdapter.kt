package musicplayer.cs371m.musicplayer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import musicplayer.cs371m.musicplayer.databinding.SongRowBinding

// Pass in a function called clickListener that takes a view and a songName
// as parameters.  Call clickListener when the row is clicked.
class RVDiffAdapter(private val clickListener: (view: View, songName: String)->Unit)
    : ListAdapter<SongInfo,
        RVDiffAdapter.ViewHolder>(Diff())
{
    companion object {
        val TAG = "RVDiffAdapter"
    }

    // ViewHolder pattern holds row binding
    inner class ViewHolder(songRowBinding : SongRowBinding) : RecyclerView.ViewHolder(songRowBinding.root) {

        val songNameTv = songRowBinding.songNameTv
        val songDurationTv = songRowBinding.songDurationTv

        init {
            //XXX Write me.
            songRowBinding.root.setOnClickListener {
                clickListener(it, songNameTv.text as String)
            }
        }

        fun bind(item: SongInfo)
        {
            songNameTv.text = item.name
            songDurationTv.text = item.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //XXX Write me.
        val binding = SongRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //XXX Write me.
        //holder.bind(currentList[position])
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class Diff : DiffUtil.ItemCallback<SongInfo>() {
        // Item identity
        override fun areItemsTheSame(oldItem: SongInfo, newItem: SongInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        // Item contents are the same, but the object might have changed
        override fun areContentsTheSame(oldItem: SongInfo, newItem: SongInfo): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.rawId == newItem.rawId
                    && oldItem.time == newItem.time
        }
    }

}

