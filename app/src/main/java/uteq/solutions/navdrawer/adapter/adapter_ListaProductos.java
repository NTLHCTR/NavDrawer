package uteq.solutions.navdrawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uteq.solutions.navdrawer.R;
import uteq.solutions.navdrawer.model.clsProducto;

public class adapter_ListaProductos extends RecyclerView.Adapter<adapter_ListaProductos.ProductoViewHolder>{


    private Context Ctx;
    private List<clsProducto> listaProductos;

    public adapter_ListaProductos(Context mCtx, List<clsProducto> listaProductos) {
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
        holder.txtprecio.setText(String.format("$ %.2f",producto.valor));
        holder.txtDesc.setText(producto.descripcion);
        holder.txtCat.setText(producto.categoria);

        holder.txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clsProducto item = listaProductos.get(position);
                //Toast.makeText(Ctx, "You clicked " + item.descripcion, Toast.LENGTH_LONG).show();
            }
        });
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

    class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView txtDesc, txtCat, txtprecio;

        public ProductoViewHolder(View itemView) {
            super(itemView);

            txtDesc= itemView.findViewById(R.id.rvitem_producto_description);
            txtCat = itemView.findViewById(R.id.rvitem_producto_categoria);
            txtprecio = itemView.findViewById(R.id.rvitem_producto_precio);

        }
    }
}
