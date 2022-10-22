package uteq.solutions.navdrawer.ui.facturacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uteq.solutions.navdrawer.helper.GlobalInfo;
import uteq.solutions.navdrawer.model.clsFacturaTotalImpuestos;
import uteq.solutions.navdrawer.model.clsItemFactura;
import uteq.solutions.navdrawer.model.clsProducto;

public class viewModelFacturacion extends ViewModel {

    public MutableLiveData<String> fecha = new MutableLiveData<String>();
    public MutableLiveData<Integer> tipo = new MutableLiveData<Integer>();
    public MutableLiveData<Integer> idcliente = new MutableLiveData<Integer>();
    public MutableLiveData<String> cliente_tipoid = new MutableLiveData<String>();
    public MutableLiveData<String> cliente_id = new MutableLiveData<String>();
    public MutableLiveData<String> cliente_razonsocial = new MutableLiveData<String>();
    public MutableLiveData<String> cliente_direccion = new MutableLiveData<String>();
    public MutableLiveData<String> cliente_correo = new MutableLiveData<String>();

    public MutableLiveData<Double> totalsinimpuestos = new MutableLiveData<Double>();
    public MutableLiveData<Double> totaldescuento = new MutableLiveData<Double>();
    public MutableLiveData<Double> propina = new MutableLiveData<Double>();
    public MutableLiveData<Double> importetotal = new MutableLiveData<Double>();
    public MutableLiveData<Double> totalice = new MutableLiveData<Double>();


    private MutableLiveData<ArrayList<clsItemFactura>> lstProductos = new MutableLiveData<ArrayList<clsItemFactura>>();
    private MutableLiveData<ArrayList<clsFacturaTotalImpuestos>> lstTotalImpuestos = new MutableLiveData<ArrayList<clsFacturaTotalImpuestos>>();



    public viewModelFacturacion(){
        fecha.setValue("");
        tipo.setValue(-1); idcliente.setValue(-1);
        cliente_tipoid.setValue("");        cliente_id.setValue("");
        cliente_razonsocial.setValue("");   cliente_direccion.setValue("");
        cliente_correo.setValue("");
        totalsinimpuestos.setValue(0.0);  totaldescuento.setValue(0.0);
        propina.setValue(0.0);  importetotal.setValue(0.0);
        totalice.setValue(0.0);

        lstProductos.setValue(new ArrayList<clsItemFactura>());
        lstTotalImpuestos.setValue(new ArrayList<clsFacturaTotalImpuestos>());

    }

    public void resetFactura(){
        fecha.setValue("");
        tipo.setValue(-1); idcliente.setValue(-1);
        cliente_tipoid.setValue("");        cliente_id.setValue("");
        cliente_razonsocial.setValue("");   cliente_direccion.setValue("");
        cliente_correo.setValue("");
        totalsinimpuestos.setValue(0.0);  totaldescuento.setValue(0.0);
        propina.setValue(0.0);  importetotal.setValue(0.0);
        totalice.setValue(0.0);

        ArrayList<clsItemFactura> items =  lstProductos.getValue();
        items.clear();    lstProductos.setValue(items);

        ArrayList<clsFacturaTotalImpuestos> items2 =  lstTotalImpuestos.getValue();
        items.clear();    lstTotalImpuestos.setValue(items2);

    }

    public LiveData<ArrayList<clsItemFactura>> getlstProductos() {   return lstProductos; }
    public void setProductoinList(clsItemFactura producto) {

        updateListOfTotalesImouesto(producto);
        totalsinimpuestos.setValue(totalsinimpuestos.getValue() + producto.iva_baseimponible);
        totaldescuento.setValue(totaldescuento.getValue() + producto.descuento);
        if(producto.ice_codigo>0)
            totalice.setValue(totalice.getValue() + producto.ice_valor);
        importetotal.setValue(importetotal.getValue() + producto.getSubTotalItem());


        ArrayList<clsItemFactura> items =  lstProductos.getValue();
        items.add(producto);
        lstProductos.setValue(items);


    }

    public LiveData<ArrayList<clsFacturaTotalImpuestos>> getlstTotalesImpuestos() {   return lstTotalImpuestos; }
    public void setTotalesImpuestosinList(clsFacturaTotalImpuestos totalImpuesto) {
        ArrayList<clsFacturaTotalImpuestos> items =  lstTotalImpuestos.getValue();
        items.add(totalImpuesto);
        lstTotalImpuestos.setValue(items);
    }

    public void updateListOfTotalesImouesto(clsItemFactura addedItem){
        boolean added=false;
        for (clsFacturaTotalImpuestos item: lstTotalImpuestos.getValue() ) {
            if(item.idimpuesto == addedItem.iva_codigo &&
                    item.tipoimpuesto==GlobalInfo.CONST_tipoImpuestoIVA){
                item.baseimponible += addedItem.iva_baseimponible;
                item.valor += addedItem.iva_valor;
                added=true;
                break;
            }
        }
        if(!added){
            clsFacturaTotalImpuestos item = new clsFacturaTotalImpuestos(GlobalInfo.CONST_tipoImpuestoIVA,addedItem.iva_codigo,
                    addedItem.iva_descripcion,addedItem.iva_baseimponible, addedItem.iva_valor);
            this.setTotalesImpuestosinList(item);
        }

        added=false;
        if(addedItem.ice_codigo>0){
            for (clsFacturaTotalImpuestos item: lstTotalImpuestos.getValue() ) {
                if(item.idimpuesto == addedItem.ice_codigo &&
                        item.tipoimpuesto==GlobalInfo.CONST_tipoImpuestoICE){
                    item.baseimponible += addedItem.ice_baseimponible;
                    item.valor += addedItem.ice_valor;
                    added=true;
                    break;
                }
            }
            if(!added){
                clsFacturaTotalImpuestos item = new clsFacturaTotalImpuestos(GlobalInfo.CONST_tipoImpuestoICE,
                        addedItem.ice_codigo,"ICE",addedItem.ice_baseimponible, addedItem.ice_valor);
                this.setTotalesImpuestosinList(item);
            }
        }
    }


}

