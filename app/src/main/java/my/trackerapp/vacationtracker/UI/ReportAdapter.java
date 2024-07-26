package my.trackerapp.vacationtracker.UI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import my.trackerapp.vacationtracker.R;

import java.util.List;
import java.util.Map;

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<Map<String, String>> data;
    private List<String> fields;

    //Constuctor
    public ReportAdapter(List<Map<String, String>> data, List<String> fields) {
        this.data = data;
        this.fields = fields;
        Log.d("ReportAdapter", "Data: " + data.toString());
        Log.d("ReportAdapter", "Fields: " + fields.toString());
    }

    //Set header or item
    @Override
    public int getItemViewType(int position) {
        if (position < fields.size()) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.report_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.report_item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Log.d("ReportAdapter", "Position: " + position + " Type: " + getItemViewType(position));

        //Header
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.textView.setText(fields.get(position));
        }

        //Item
        else {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            // Adjust index calculation logic
            int dataIndex = (position - fields.size()) / fields.size();
            int fieldIndex = (position - fields.size()) % fields.size();

            //Log.d("ReportAdapter", "Data Index: " + dataIndex);
            //Log.d("ReportAdapter", "Field Index: " + fieldIndex);

            if (dataIndex >= 0 && dataIndex < data.size() && fieldIndex >= 0 && fieldIndex < fields.size()) {
                String fieldValue = data.get(dataIndex).get(fields.get(fieldIndex));
                itemHolder.textView.setText(fieldValue);
            }
            //else {
                //Log.e("ReportAdapter", "Index out of bounds: Data Index = " + dataIndex + ", Field Index = " + fieldIndex);
            //}
        }
    }


    //get item count
    @Override
    public int getItemCount() {
            int itemCount = fields.size() + data.size() * fields.size();
            Log.d("ReportAdapter", "getItemCount: " + itemCount);
            return itemCount;
    }

    //Header viewholder
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        HeaderViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.headerTextView);
        }
    }

    //Item view holder
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ItemViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.itemTextView);
        }
    }
}