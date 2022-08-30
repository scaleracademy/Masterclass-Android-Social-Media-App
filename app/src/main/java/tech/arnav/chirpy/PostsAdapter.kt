package tech.arnav.chirpy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tech.arnav.chirpy.models.Post

class PostsAdapter : ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = parent.context.getSystemService(LayoutInflater::class.java).inflate(R.layout.list_item_post, parent, false)

        return PostViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post) {
            itemView.findViewById<TextView>(R.id.tvMessage).text = post.message
            itemView.findViewById<TextView>(R.id.tvUid).text = post.uid
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem === newItem

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = (oldItem == newItem)

    }

}