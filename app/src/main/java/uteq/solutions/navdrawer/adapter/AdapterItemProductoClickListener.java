package uteq.solutions.navdrawer.adapter;

import android.view.View;

import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsProducto;

public interface AdapterItemProductoClickListener {
    void onClick(View view, clsProducto producto);
}
