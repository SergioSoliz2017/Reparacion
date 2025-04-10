package com.example.factureroreparacioncelular;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapterTrabajadores extends RecyclerView.Adapter<ListAdapterTrabajadores.ViewHolder> {
    private List<ListTrabajadores> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapterTrabajadores.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick (ListTrabajadores item);
    }
    public ListAdapterTrabajadores(List<ListTrabajadores> itemList, Context context, ListAdapterTrabajadores.OnItemClickListener listener) {
        this.mData = itemList;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount () {return mData.size();}

    @Override
    public ListAdapterTrabajadores.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_trabajadores,null);
        return new ListAdapterTrabajadores.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final ListAdapterTrabajadores.ViewHolder holder,  final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListTrabajadores> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombreTrabajador,usuario,rol;
        ViewHolder(View itemView){
            super (itemView);
            iconImage = itemView.findViewById(R.id.iconImagenView);
            nombreTrabajador=itemView.findViewById(R.id.nombreTrabajador);
            usuario=itemView.findViewById(R.id.usuario);
            rol=itemView.findViewById(R.id.rol);
        }
        void bindData(final ListTrabajadores item){
            usuario.setText(item.getUsuario());
            nombreTrabajador.setText(item.getNombre());
            rol.setText(item.getRol());
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
