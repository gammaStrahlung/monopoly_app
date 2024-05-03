package at.gammastrahlung.monopoly_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.gammastrahlung.monopoly_app.R;
import at.gammastrahlung.monopoly_app.fragments.FieldInfoFragment;
import at.gammastrahlung.monopoly_app.game.gameboard.Field;
import at.gammastrahlung.monopoly_app.game.gameboard.Property;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

    List<Field> fields;
    AppCompatActivity context;
    public FieldAdapter(List<Field> fields, Context context) {
        this.fields = fields;
        this.context = (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.field_list_view, parent, false);

        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {

        Field field = fields.get(position);

        // Set field name
        holder.fieldName.setText(field.getName());

        // Set color bar
        if (field instanceof Property) {
            Property p = (Property) field;

            holder.colorBar.setVisibility(View.VISIBLE);
            if (p.getColor() != null)
                holder.colorBar.setBackgroundColor(Color.parseColor(p.getColor().getColorString()));
        } else {
            holder.colorBar.setVisibility(View.GONE);
        }

        // Open field info when clicking on item
        holder.itemView.setOnClickListener(v -> new FieldInfoFragment(field).show(context.getSupportFragmentManager(), "FieldListInfo"));
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class FieldViewHolder extends RecyclerView.ViewHolder {

        protected TextView fieldName;
        protected View colorBar;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldName = itemView.findViewById(R.id.field_name);
            colorBar = itemView.findViewById(R.id.colorBar);
        }
    }
}
