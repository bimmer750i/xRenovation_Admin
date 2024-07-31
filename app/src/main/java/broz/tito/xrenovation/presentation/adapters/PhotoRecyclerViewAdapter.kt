package broz.tito.xrenovation.presentation.adapters

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.ChosenPhotoViewholderBinding
import broz.tito.xrenovation.admin.databinding.DisplayPhotoViewholderBinding
import broz.tito.xrenovation.admin.databinding.EditPhotoViewholderBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import java.io.File
import java.security.Key
import java.security.Signature

class PhotoRecyclerViewAdapter(val viewHolderType : Int,val displayPhotoClickListener: OnClickListener) : Adapter<PhotoRecyclerViewAdapter.BaseViewHolder>() {

    private val TAG = "PhotoRecyclerViewAdapter"

    var list : ArrayList<String> = ArrayList<String>()
    set(value) {
        field = value
        notifyDataSetChanged()
        Log.d(TAG, "new adapter list value : $value")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

            when (viewHolderType) {
            0 -> {
                val binding = ChosenPhotoViewholderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return AddPhotoViewHolder(binding)
            }
            1 -> {
                val binding = DisplayPhotoViewholderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return DisplayPhotoViewHolder(binding,displayPhotoClickListener)
            }
                   else -> {
                       val binding = EditPhotoViewholderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                       return EditPhotoViewHolder(binding)
                }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        Log.d(TAG, "image loaded from file: ${list.get(position)}")
        if (holder is AddPhotoViewHolder) {
            Glide.with(holder.binding.root)
                .load(Uri.fromFile(File(list.get(position))))
                .signature(ObjectKey(File(list.get(position)).lastModified()))
                .into(holder.binding.imageViewPhoto)
        }
        else if (holder is DisplayPhotoViewHolder) {
            Glide.with(holder.binding.root)
                .load(list.get(position))
                .into(holder.binding.imageViewDisplayPhoto)
        }
        else if (holder is EditPhotoViewHolder) {
            if (list.get(position).startsWith("https")) {
                Glide.with(holder.binding.root)
                    .load(list.get(position))
                    .into(holder.binding.imageViewPhoto)
            }
            else {
                Glide.with(holder.binding.root)
                    .load(Uri.fromFile(File(list.get(position))))
                    .signature(ObjectKey(File(list.get(position)).lastModified()))
                    .into(holder.binding.imageViewPhoto)
            }
        }

    }

    fun showYesNoAlertDialog(context : Context?, title : String, clickListener : () -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setPositiveButton(R.string.yes) { dialogInterface, num ->
                clickListener.invoke()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, num ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    open inner class BaseViewHolder(view: View) : ViewHolder(view)

    inner class AddPhotoViewHolder(val binding: ChosenPhotoViewholderBinding) : BaseViewHolder(binding.root), OnClickListener {

        init {
            binding.imageViewDeletePhoto.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            list.removeAt(bindingAdapterPosition)
            notifyDataSetChanged()
        }
    }

    inner class DisplayPhotoViewHolder(val binding : DisplayPhotoViewholderBinding,clickListener: OnClickListener) : BaseViewHolder(binding.root) {

        init {
            binding.imageViewDisplayPhoto.setOnClickListener(clickListener)
        }

    }

    inner class EditPhotoViewHolder(val binding : EditPhotoViewholderBinding) : BaseViewHolder(binding.root),OnClickListener {

        init {
            binding.imageViewDeletePhoto.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            showYesNoAlertDialog(p0?.context,"FUCK YEAH") {
                list.removeAt(bindingAdapterPosition)
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val ADD_PHOTO_VIEWHOLDER = 0
        const val DISPLAY_PHOTO_VIEWHOLDER = 1
        const val EDIT_PHOTO_VIEWHOLDER = 2
    }


}