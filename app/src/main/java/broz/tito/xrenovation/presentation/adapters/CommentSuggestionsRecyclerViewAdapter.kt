package broz.tito.xrenovation.presentation.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.CommentItemBinding
import broz.tito.xrenovation.admin.databinding.CommentSuggestionItemBinding
import broz.tito.xrenovation.data.add_house.entities.Comment
import broz.tito.xrenovation.presentation.HouseFragmentDirections
import broz.tito.xrenovation.presentation.entities.DisplayComment
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class CommentSuggestionsRecyclerViewAdapter(val publishClicker : (houseId : String, commentId : String,comment : Comment) -> Unit, val deleteClicker : (commentId : String) -> Unit) : RecyclerView.Adapter<CommentSuggestionsRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "CommentsRecyclerViewAdapter"

    var commentItems : ArrayList<DisplayComment> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommentSuggestionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return commentItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val displayComment = commentItems.get(position)
        Log.d(TAG, "onBindViewHolder -- comment -- ${displayComment.comment}")
        val sdf = SimpleDateFormat("dd/MM/YYYY HH:mm")
        sdf.timeZone = TimeZone.getDefault()
        val date = sdf.format(Date(displayComment.comment.timeAdded*1000))
        holder.binding.textViewTimeAdded.text = date
        Glide.with(holder.binding.root)
            .load(displayComment.comment.photoUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.binding.imageViewUserPhoto)
        holder.binding.textViewUserName.text = displayComment.comment.displayName
        holder.binding.textViewLocalId.text = displayComment.comment.localId
        holder.binding.textViewCommentText.text = displayComment.comment.text
    }

    inner class ViewHolder(val binding : CommentSuggestionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageViewCommentSuggestionOptions.setOnClickListener {
                showMenu(binding.root.context,binding,commentItems
                    .get(absoluteAdapterPosition).comment.houseId,
                    commentItems.get(absoluteAdapterPosition).commentId,
                    commentItems.get(absoluteAdapterPosition).comment)
            }
        }

    }

    private fun showMenu(context: Context,binding: CommentSuggestionItemBinding,houseId: String,commentId: String,comment: Comment) {
        val popupMenu = PopupMenu(context,binding.imageViewCommentSuggestionOptions)
        popupMenu.menuInflater.inflate(R.menu.comment_suggestion_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.publish_comment -> {
                    publishClicker.invoke(houseId, commentId,comment)
                }
                R.id.delete_comment -> {
                    deleteClicker.invoke(commentId)
                }
            }
            true }
        popupMenu.show()
    }

}