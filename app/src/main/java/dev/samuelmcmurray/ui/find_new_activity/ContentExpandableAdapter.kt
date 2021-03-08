package dev.samuelmcmurray.ui.find_new_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.ui.main.MainActivity
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class ContentExpandableAdapter(private val activity: Activity) :
    RecyclerView.Adapter<ContentExpandableAdapter.ViewHolder>() {



    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_HEADER = 2

        private const val IC_EXPANDED_ROTATION_DEG = 0F
        private const val IC_COLLAPSED_ROTATION_DEG = 180F
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class ItemVH(itemView: View) : ViewHolder(itemView) {

            fun bind(content: String) {
                itemView.apply {
                    itemView.findViewById<CheckedTextView>(R.id.activity_filter).text = content
                }
            }
        }

        class HeaderVH(itemView: View) : ViewHolder(itemView) {
            internal val tvTitle = itemView.findViewById<TextView>(R.id.activity_name)
            internal val icExpand = itemView.findViewById<ImageView>(R.id.expand_icon)

            fun bind(content: String, expanded: Boolean, onClickListener: View.OnClickListener) {
                tvTitle.text = content
                icExpand.rotation =
                    if (expanded) IC_EXPANDED_ROTATION_DEG else IC_COLLAPSED_ROTATION_DEG
                itemView.setOnClickListener(onClickListener)
            }
        }
    }


    var isExpanded: Boolean by Delegates.observable(true) { _: KProperty<*>, _: Boolean, newExpandedValue: Boolean ->
        if (newExpandedValue) {
            notifyItemRangeInserted(1, activity.filter.size)
            //To update the header expand icon
            notifyItemChanged(0)
        } else {
            notifyItemRangeRemoved(1, activity.filter.size)
            //To update the header expand icon
            notifyItemChanged(0)
        }
    }

    private val onHeaderClickListener = View.OnClickListener {
        isExpanded = !isExpanded
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_HEADER -> ViewHolder.HeaderVH(
                inflater.inflate(R.layout.activity_card, parent, false)
            )
            else -> ViewHolder.ItemVH(
                inflater.inflate(
                    R.layout.activity_card_content,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ItemVH -> {
                holder.bind(activity.filter[position - 1])
                val checkText = holder.itemView.findViewById<CheckedTextView>(R.id.activity_filter)
                checkText.setOnClickListener {
                    if (checkText.isChecked) {
                        checkText.checkMarkDrawable = null;
                        checkText.isChecked = false;
                        MainActivity.selectedFilter.remove(checkText.text.toString())
                    } else {
                        checkText.setCheckMarkDrawable(R.drawable.ic_baseline_check_24)
                        checkText.isChecked = true
                        println(checkText.text.toString())
                        if (checkText.text as String !in MainActivity.selectedFilter) {
                            MainActivity.selectedFilter.add(checkText.text.toString())
                        }
                        println(MainActivity.selectedFilter)
                    }
                }
            }
            is ViewHolder.HeaderVH -> {
                holder.bind(activity.name, isExpanded, onHeaderClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isExpanded) activity.filter.size + 1 else 1
    }
}