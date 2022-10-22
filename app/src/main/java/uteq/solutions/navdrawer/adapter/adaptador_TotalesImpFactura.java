package uteq.solutions.navdrawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.model.clsListItem;
import uteq.solutions.navdrawer.model.clsListItemTotalImpFactura;

public class adaptador_TotalesImpFactura extends RecyclerView.Adapter<adaptador_TotalesImpFactura.ViewHolder>{
    private List<clsListItemTotalImpFactura> listdata;

    public adaptador_TotalesImpFactura(List<clsListItemTotalImpFactura> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rvitem_totalesimpfact, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtdesc.setText(listdata.get(position).descripcion);
        holder.txttotal.setText(String.format("%.2f",listdata.get(position).Total));

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtdesc;
        TextView txttotal;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtdesc = (TextView) itemView.findViewById(R.id.lblTituloTotalImpFacItem);
            this.txttotal = (TextView) itemView.findViewById(R.id.txtTotalImpFacItem);

        }
    }
}