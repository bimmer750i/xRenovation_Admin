package broz.tito.xrenovation.presentation.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import broz.tito.xrenovation.admin.R
import broz.tito.xrenovation.admin.databinding.CorrectionItemBinding
import broz.tito.xrenovation.presentation.entities.DisplayCorrection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CorrectionsRecyclerViewAdapter(val cardViewClicker: (String) -> Unit, val optionsClicker: (String) -> Unit) : RecyclerView.Adapter<CorrectionsRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "CorrectionsRecyclerViewAdapter"

    var list : ArrayList<DisplayCorrection> = ArrayList()
    set(value) {
        field = value
        notifyDataSetChanged()
        Log.d(TAG,  "New adapter value: $field")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CorrectionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val correction = list.get(position)
        holder.binding.textViewLocalId.text = correction.correction.localId
        val sdf = SimpleDateFormat("dd/MM/YYYY HH:mm")
        sdf.timeZone = TimeZone.getDefault()
        val date = sdf.format(Date(correction.correction.timeAdded))
        holder.binding.textViewTimeAdded.text = date
        holder.binding.textViewCorrectionText.text = correction.correction.correctionText
    }

    inner class ViewHolder(val binding : CorrectionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.CardViewCorrection.setOnClickListener { cardViewClicker(list.get(absoluteAdapterPosition).correction.houseId) }
            binding.imageViewCorrectionOptions.setOnClickListener {
                showYesNoAlertDialog(it.context,it.context.getString(R.string.delete_correction)) {
                    optionsClicker(list.get(absoluteAdapterPosition).correctionId)
                    list.removeAt(absoluteAdapterPosition)
                    notifyDataSetChanged()
                }
            }
        }

    }

    private fun showYesNoAlertDialog(context: Context, title : String, clicker : () -> Unit) {

        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setPositiveButton(R.string.yes) { dialogInterface, num ->
                clicker()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, num ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

}