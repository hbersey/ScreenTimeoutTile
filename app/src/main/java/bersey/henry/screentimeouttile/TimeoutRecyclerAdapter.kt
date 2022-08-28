package bersey.henry.screentimeouttile

import android.content.Context
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimeoutRecyclerAdapter constructor(private val context: Context) :
    RecyclerView.Adapter<TimeoutRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.timeout_recycler_item, null)
        view.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == getTimeouts().size) {
            holder.textView.setText(R.string.always_on);
            holder.imageView.setImageResource(android.R.drawable.checkbox_off_background);
            return
        }

        holder.textView.text = when (val duration = getTimeouts()[position]) {
            1 -> "1 ${context.resources.getString(R.string.second)}"
            in 2..59 -> "$duration ${context.resources.getString(R.string.seconds)}"
            60 -> "1 ${context.resources.getString(R.string.minute)}"
            else -> {
                val mins = duration / 60
                "$mins ${context.resources.getString(R.string.minutes)}"
            }
        }
        holder.imageView.setImageResource(android.R.drawable.ic_menu_delete);

    }

    override fun getItemCount(): Int {
        return getTimeouts().size + 1;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView;
        val imageView: ImageView;

        init {
            textView = itemView.findViewById(R.id.timeoutRecyclerTextView);
            imageView = itemView.findViewById(R.id.timeoutRecyclerImageView);
        }
    }

}