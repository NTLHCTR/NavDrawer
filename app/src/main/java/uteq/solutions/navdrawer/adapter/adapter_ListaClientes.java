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
import uteq.solutions.navdrawer.model.clsCliente;

public class adapter_ListaClientes extends RecyclerView.Adapter<adapter_ListaClientes.ClienteViewHolder>{


    private Context Ctx;
    private List<clsCliente> listaClientes;

    public adapter_ListaClientes(Context mCtx, List<clsCliente> listaClientes) {
        this.listaClientes = listaClientes;
        Ctx=mCtx;
    }

    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(Ctx);
        View view = inflater.inflate(R.layout.rvitem_cliente, parent,false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClienteViewHolder holder, int position) {

        clsCliente cliente = listaClientes.get(position);
        holder.txtTipo.setText(cliente.tipocliente);
        holder.txtNombre.setText(cliente.nombre);
        holder.txtID.setText(cliente.tipoidentificacion + ": " + cliente.identificacion);
        //holder.txtCorreo.setText(cliente.correo);

        holder.txtNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clsCliente item = listaClientes.get(position);
                //Toast.makeText(Ctx, "You clicked " + item.descripcion, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public void removeItem(int position) {
        listaClientes.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(clsCliente item, int position) {
        listaClientes.add(position, item);
        notifyItemInserted(position);
    }

    public List<clsCliente> getData() {
        return listaClientes;
    }

    class ClienteViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtTipo, txtID, txtCorreo;

        public ClienteViewHolder(View itemView) {
            super(itemView);

            txtNombre= itemView.findViewById(R.id.rvitem_cliente_nombre);
            txtTipo = itemView.findViewById(R.id.rvitem_cliente_tipo);
            txtID = itemView.findViewById(R.id.rvitem_cliente_identificación);
           // txtCorreo = itemView.findViewById(R.id.rvitem_cliente_correo);
            // TxtID = itemView.findViewById(R.id.rvitem_cliente_correo);


        }
    }
}
