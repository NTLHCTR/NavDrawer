package uteq.solutions.navdrawer.ui.facturacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uteq.solutions.navdrawer.model.clsProducto;

public class viewModelFacturacion extends ViewModel {

    private MutableLiveData<Date> fecha = new MutableLiveData<Date>();
    private MutableLiveData<String> cliente_tipoid = new MutableLiveData<String>();
    private MutableLiveData<String> cliente_id = new MutableLiveData<String>();
    private MutableLiveData<String> cliente_razonsocial = new MutableLiveData<String>();
    private MutableLiveData<String> cliente_direccion = new MutableLiveData<String>();

    private MutableLiveData<ArrayList<clsProducto>> lstProductos = new MutableLiveData<ArrayList<clsProducto>>();


    public viewModelFacturacion(){
        cliente_tipoid.setValue("");        cliente_id.setValue("");
        cliente_razonsocial.setValue("");   cliente_direccion.setValue("");
        lstProductos.setValue(new ArrayList<clsProducto>());

    }

    public void resetFactura(){
        cliente_tipoid.setValue("");        cliente_id.setValue("");
        cliente_razonsocial.setValue("");   cliente_direccion.setValue("");

        ArrayList<clsProducto> items =  lstProductos.getValue();
        items.clear();    lstProductos.setValue(items);

    }

    public LiveData<ArrayList<clsProducto>> getlstProductos() {   return lstProductos; }
    public void setProductoinList(clsProducto producto) {
        ArrayList<clsProducto> items =  lstProductos.getValue();
        items.add(producto);
        lstProductos.setValue(items);
    }

    public LiveData<String> getCliente_tipoid() {   return cliente_tipoid; }
    public void setCliente_tipoid(String texto) {   this.cliente_tipoid.setValue(texto);   }
    public LiveData<String> getCliente_id() {   return cliente_id; }
    public void setCliente_id(String texto) {   this.cliente_id.setValue(texto);   }
    public LiveData<String> getCliente_razonsocial() {   return cliente_razonsocial; }
    public void setCliente_razonsocial(String texto) {   this.cliente_razonsocial.setValue(texto);   }
    public LiveData<String> getCliente_direccion() {   return cliente_direccion; }
    public void setCliente_direccion(String texto) {   this.cliente_direccion.setValue(texto);   }




}

