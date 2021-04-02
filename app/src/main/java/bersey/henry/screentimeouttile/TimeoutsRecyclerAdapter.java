package bersey.henry.screentimeouttile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeoutsRecyclerAdapter extends RecyclerView.Adapter<TimeoutsRecyclerAdapter.ViewHolder> {
    private final TimeoutManager timeoutManager;

    public TimeoutsRecyclerAdapter(TimeoutManager timeoutManager) {
        this.timeoutManager = timeoutManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeouts_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean isLast = timeoutManager.getTimeouts().size() == position;

        if (isLast) {
            holder.textView.setText(holder.itemView.getContext().getString(R.string.never));
            holder.deleteButton.setVisibility(View.GONE);
            holder.checkBox.setChecked(timeoutManager.isNeverEnabled());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> timeoutManager.setNeverEnabled(isChecked));
        } else {
            Timeout timeout = timeoutManager.get(position);
            holder.textView.setText(timeout.getLonghand(holder.itemView.getContext().getResources()));
            holder.checkBox.setVisibility(View.GONE);
            holder.deleteButton.setOnClickListener(v -> {
                timeoutManager.removeAt(position);
                notifyItemRemoved(position);
            });
        }

    }

    @Override
    public int getItemCount() {
        return timeoutManager.getTimeouts().size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.timeoutTextView);
            checkBox = itemView.findViewById(R.id.timeoutCheckBox);
            deleteButton = itemView.findViewById(R.id.timeoutDeleteButton);
        }
    }
}
