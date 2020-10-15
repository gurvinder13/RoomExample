package com.example.task.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.task.R;
import com.example.task.notedb.model.Note;

import java.util.List;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.BeanHolder> {

    private List<Note> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnNoteItemClick onNoteItemClick;

    public DataAdapter(List<Note> list, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.onNoteItemClick = (OnNoteItemClick) context;
    }


    @Override
    public BeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.data_list_item, parent, false);
        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(BeanHolder holder, int position) {
        holder.textViewName.setText(list.get(position).getName());
        holder.textViewSalary.setText(String.valueOf(list.get(position).getSalary()));
        holder.textViewDesig.setText(list.get(position).getDesignation());
       holder.checkBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context, String.valueOf(grandTotal(list)), Toast.LENGTH_SHORT).show();

           }
       });

    }
    private int grandTotal(List<Note> items){

        int totalPrice = 0;
        for(int i = 0 ; i < items.size(); i++) {
            totalPrice += items.get(i).getSalary();
        }

        return totalPrice;}
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewSalary;
        TextView textViewName;
        TextView textViewDesig;
        ImageView edit, delete;
        CheckBox checkBox;

        public BeanHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewSalary = itemView.findViewById(R.id.item_text);
            textViewName = itemView.findViewById(R.id.tv_title);
            textViewDesig = itemView.findViewById(R.id.item_desig);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            checkBox=itemView.findViewById(R.id.checkbox);
        }

        @Override
        public void onClick(View view) {
            onNoteItemClick.onNoteClick(getAdapterPosition());
            onNoteItemClick.onEditClick(getAdapterPosition());
            onNoteItemClick.onDelete(getAdapterPosition());

        }
    }

    public interface OnNoteItemClick {
        void onNoteClick(int pos);
        void onEditClick(int pos);
        void onDelete(int pos);
    }
}