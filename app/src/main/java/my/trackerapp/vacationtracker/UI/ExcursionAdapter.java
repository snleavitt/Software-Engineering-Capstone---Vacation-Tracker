package my.trackerapp.vacationtracker.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import my.trackerapp.vacationtracker.R;
import my.trackerapp.vacationtracker.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder>{

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;

        //Constructs ExcursionViewHolder
        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = excursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    intent.putExtra("name", current.getExcursionName());
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("Start Date", current.getStartDate());
                    intent.putExtra("Start Time", current.getStartTime());
                    intent.putExtra("Additional Information", current.getAdditionalInformation());
                    context.startActivity(intent);
                }
            });
        }
    }


    private List<Excursion> excursions;
    private final Context context;
    private final LayoutInflater Inflater;

    public ExcursionAdapter(Context context) {
        Inflater = LayoutInflater.from(context);
        this.context = context;
    }



    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = Inflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (excursions != null) {
            Excursion current = excursions.get(position);
            String name = current.getExcursionName();
            int vacationID = current.getVacationID();
            holder.excursionItemView.setText(name);
        } else {
            holder.excursionItemView.setText("No name");
        }
    }

    //Set excursions
    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
        notifyDataSetChanged();
    }

    //Get item count
    @Override
    public int getItemCount() {
        return excursions.size();
    }
}


