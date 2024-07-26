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
import my.trackerapp.vacationtracker.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {


    class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        //Constructs VacationViewHolder
        private VacationViewHolder(View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Vacation current = Vacation.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);

//                    Log.d("VacationAdapter", "Name: " + current.getVacationName());
//                    Log.d("VacationAdapter", "Hotel: " + current.getLodgingName());

                    intent.putExtra("ID", current.getVacationID());
                    intent.putExtra("Name", current.getVacationName());
                    intent.putExtra("Lodging", current.getLodgingName());
                    intent.putExtra("Start Date", current.getStartDate());
                    intent.putExtra("End Date", current.getEndDate());
                    intent.putExtra("Transportation", current.getTransportation());
                    intent.putExtra("Departure Date", current.getPlaneDepartureDate());
                    intent.putExtra("Departure Time", current.getPlaneDepartureTime());
                    intent.putExtra("Additional Details", current.getAdditionalDetails());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Vacation> Vacation;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (Vacation != null) {
            Vacation current = Vacation.get(position);
            String name = current.getVacationName();
            holder.vacationItemView.setText(name);
        } else {
            holder.vacationItemView.setText("No vacation name");
        }

    }

    //Set vacations
    public void setVacations(List<Vacation> vacations) {
        Vacation = vacations;
        notifyDataSetChanged();
    }

    //Get item count
    @Override
    public int getItemCount() {

        if (Vacation != null) {
            return Vacation.size();
        }
        else return 0;
    }
};
