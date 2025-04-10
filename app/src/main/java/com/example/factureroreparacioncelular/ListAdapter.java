package com.example.factureroreparacioncelular;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick (ListElement item);
    }
    public ListAdapter(List<ListElement> itemList, Context context, ListAdapter.OnItemClickListener listener) {
        this.mData = itemList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount () {return mData.size();}

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final ListAdapter.ViewHolder holder,  final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListElement> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView modeloCelular,nombreCliente,estado,textFecha;
        ViewHolder(View itemView){
            super (itemView);
            iconImage = itemView.findViewById(R.id.iconImagenView);
            modeloCelular=itemView.findViewById(R.id.modeloCelular);
            nombreCliente=itemView.findViewById(R.id.nombreCliente);
            estado=itemView.findViewById(R.id.estado);
            textFecha=itemView.findViewById(R.id.fechaText);
        }
        void bindData(final ListElement item){
            estado.setTextColor(Color.parseColor(item.getColor()));
            modeloCelular.setText(item.getModeloCelular());
            nombreCliente.setText(item.getNombreCliente());
            estado.setText(item.getEstado());
            textFecha.setText(item.getFecha());
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
