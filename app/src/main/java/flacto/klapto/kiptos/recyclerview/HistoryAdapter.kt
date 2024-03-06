package flacto.klapto.kiptos.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import flacto.klapto.kiptos.R
import flacto.klapto.kiptos.model.NumberDataFullEntity


class HistoryAdapter() : ListAdapter<NumberDataFullEntity, NumberItemHolder>(ItemComparator()) {
    class ItemComparator : DiffUtil.ItemCallback<NumberDataFullEntity>() {
        override fun areItemsTheSame(
            oldItem: NumberDataFullEntity,
            newItem: NumberDataFullEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NumberDataFullEntity,
            newItem: NumberDataFullEntity
        ): Boolean {
            return oldItem.number == newItem.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_history_item, parent, false)
        return NumberItemHolder(view)
    }

    override fun onBindViewHolder(holder: NumberItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class NumberItemHolder(val container: View) : RecyclerView.ViewHolder(container) {
    fun bind(data: NumberDataFullEntity) {
        itemView.apply {
            this.findViewById<TextView>(R.id.tvNumber).text = data.number.toString()
            this.findViewById<TextView>(R.id.tvDetails).text = data.textMath


            setOnClickListener {

                val bundle = Bundle()
                bundle.putString(DETAILSMATH, data.textMath)
                bundle.putString(DETAILSDATE, data.textDate)
                bundle.putString(DETAILSTRIVIA, data.textTrivia)
                findNavController().navigate(R.id.action_mainFragment_to_detailsFragment, bundle)

            }
        }


    }
}
const val DETAILSDATE: String = "DETAILSDATE"
const val DETAILSTRIVIA: String = "DETAILSTRIVIA"
const val DETAILSMATH: String = "DETAILSMATH"