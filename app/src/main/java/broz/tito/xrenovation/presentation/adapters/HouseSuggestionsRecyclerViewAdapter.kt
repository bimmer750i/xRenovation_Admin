package broz.tito.xrenovation.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import broz.tito.xrenovation.admin.databinding.HouseSuggestionItemBinding
import broz.tito.xrenovation.data.add_house.entities.House
import broz.tito.xrenovation.presentation.entities.DisplayCorrection
import broz.tito.xrenovation.presentation.entities.DisplayHouseSuggestion

class HouseSuggestionsRecyclerViewAdapter(val displayItemClickListener : (String,House) -> Unit) : RecyclerView.Adapter<HouseSuggestionsRecyclerViewAdapter.ViewHolder>() {

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
    }

    inner class ViewHolder(val binding : HouseSuggestionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.CardViewHouseSuggestion.setOnClickListener {
                displayItemClickListener.invoke(list.get(absoluteAdapterPosition).houseId,list.get(absoluteAdapterPosition).suggestedHouse)
            }
        }

    }

}