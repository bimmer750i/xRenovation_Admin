package broz.tito.xrenovation.presentation.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.CommentSuggestionItemBinding
import broz.tito.xrenovation.admin.databinding.HouseSuggestionItemBinding
import broz.tito.xrenovation.data.add_house.entities.Comment
import broz.tito.xrenovation.data.add_house.entities.House
import broz.tito.xrenovation.presentation.entities.DisplayCorrection
import broz.tito.xrenovation.presentation.entities.DisplayHouseSuggestion

class HouseSuggestionsRecyclerViewAdapter(val displayItemClickListener : (String,House) -> Unit, val deleteClicker : (suggestedHouseId : String,urlList : ArrayList<String>) -> Unit) : RecyclerView.Adapter<HouseSuggestionsRecyclerViewAdapter.ViewHolder>() {

    var list : ArrayList<DisplayHouseSuggestion> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HouseSuggestionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val houseSuggestion = list.get(position)
        holder.binding.textViewSuggestionAddress.text = houseSuggestion.suggestedHouse.address
        holder.binding.textViewSuggestionText.text =houseSuggestion.suggestedHouse.description
        holder.binding.textViewHouseLocalid.text = houseSuggestion.suggestedHouse.localid
    }

    inner class ViewHolder(val binding : HouseSuggestionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.CardViewHouseSuggestion.setOnClickListener {
                displayItemClickListener.invoke(list.get(absoluteAdapterPosition).houseId,list.get(absoluteAdapterPosition).suggestedHouse)
            }
            binding.imageViewSuggestionOptions.setOnClickListener {
                showMenu(binding.root.context,binding,list.get(absoluteAdapterPosition).houseId,list.get(absoluteAdapterPosition).suggestedHouse.photos)
            }
        }

    }

    private fun showMenu(context: Context, binding: HouseSuggestionItemBinding,suggestedHouseId: String,urlList : ArrayList<String>) {
        val popupMenu = PopupMenu(context,binding.imageViewSuggestionOptions)
        popupMenu.menuInflater.inflate(R.menu.house_suggestions_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete_suggested_house -> {
                    deleteClicker.invoke(suggestedHouseId,urlList)
                }
            }
            true }
        popupMenu.show()
    }

}