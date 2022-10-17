package uteq.solutions.navdrawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsProducto;

public class adapter_BusqProducts extends RecyclerView.Adapter<adapter_BusqProducts.ProductoViewHolder>{


    private Context Ctx;
    private List<clsProducto> listaProductos;
    private AdapterItemProductoClickListener clickListener;

    public adapter_BusqProducts(Context mCtx, List<clsProducto> listaProductos) {
        this.listaProductos = listaProductos;
        Ctx=mCtx;
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(Ctx);
        View view = inflater.inflate(R.layout.rvitem_producto, parent,false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {

        clsProducto producto = listaProductos.get(position);
        holder.txtpvp.setText("PVP: $" + String.format("%.2f",producto.PVP));
        Double descuento = producto.PVP * producto.DESC/100;
        Double PNeto = producto.PVP - descuento;
        Double iva = PNeto * producto.IVA/100;

        holder.txtdescuento.setText("DESC: " + (int)producto.DESC +"%");
        holder.txtimpuesto.setText("IVA: " + (int)producto.IVA +"%");
        holder.txttotal.setText("Final: $" + String.format("%.2f",PNeto + iva));

        holder.txtDesc.setText(producto.descripcion);
        holder.txtCat.setText(producto.categoria + "  " + (!producto.UnidadMedida.equals("")?"UM: " + producto.UnidadMedida:""));

    }

    public void setClickListener(AdapterItemProductoClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public void removeItem(int position) {
        listaProductos.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(clsProducto item, int position) {
        listaProductos.add(position, item);
        notifyItemInserted(position);
    }

    public List<clsProducto> getData() {
        return listaProductos;
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView txtDesc, txtCat, txtpvp, txtdescuento, txtimpuesto, txttotal;

        public ProductoViewHolder(View itemView) {
            super(itemView);

            txtDesc= itemView.findViewById(R.id.rvitem_producto_description);
            txtCat = itemView.findViewById(R.id.rvitem_producto_categoria);
            txtpvp = itemView.findViewById(R.id.rvitem_producto_pvp);
            txtdescuento = itemView.findViewById(R.id.rvitem_producto_desc);
            txtimpuesto = itemView.findViewById(R.id.rvitem_producto_impuesto);
            txttotal = itemView.findViewById(R.id.rvitem_producto_preciofinal);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getData().get(getAdapterPosition()));
        }
    }
}
