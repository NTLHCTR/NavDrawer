package uteq.solutions.navdrawer.adapter;

import android.view.View;

import uteq.solutions.navdrawer.model.clsCliente;
import uteq.solutions.navdrawer.model.clsProducto;

public interface AdapterItemClickListener {
    void onClick(View view, clsCliente cliente);
}
