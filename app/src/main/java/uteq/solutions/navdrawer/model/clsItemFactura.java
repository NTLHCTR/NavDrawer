package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class clsItemFactura {

    public Integer ID;
    public Integer idfactura;
    public Integer producto_id;
    public String producto_codigo;
    public String producto_descripcion;
    public double cantidad;
    public double preciocompra;
    public double pvp;
    public double descuento;
    public Integer iva_codigo;
    public String iva_descripcion;
    public double iva_baseimponible;
    public double iva_valor;
    public Integer ice_codigo;
    public double ice_baseimponible;
    public double ice_valor;



    public clsItemFactura(JSONObject a) throws JSONException {
        ID = a.getInt("id");
        idfactura = a.getInt("idfactura") ;
        producto_id = a.getInt("producto_id") ;
        producto_descripcion =  a.getString("producto_descripcion") ;
        producto_codigo =  a.getString("producto_codigo") ;
        cantidad = a.getDouble("cantidad");
        descuento = a.getDouble("descuento");

        iva_codigo = a.getInt("iva_codigo") ;
        iva_baseimponible = a.getDouble("iva_baseimponible");
        iva_valor = a.getDouble("iva_valor");
        iva_descripcion = a.getString("p_impuesto");

        ice_codigo = a.getInt("ice_codigo") ;
        ice_baseimponible = a.getDouble("ice_baseimponible");
        ice_valor = a.getDouble("ice_valor");

    }


    public clsItemFactura(Integer producto_id,String producto_codigo,
            String producto_descripcion, double cantidad, double preciocompra, double pvp,
            double descuento, Integer iva_codigo, double iva_baseimponible, double iva_valor,
            Integer ice_codigo, double ice_baseimponible, double ice_valor, String iva_descripcion)
     {

         this.producto_id = producto_id;
         this.producto_descripcion = producto_descripcion;
         this.producto_codigo =  producto_codigo ;
         this.cantidad = cantidad;
         this.descuento = descuento;
         this.preciocompra=preciocompra;
         this.pvp = pvp;
         this.iva_codigo = iva_codigo ;
         this.iva_baseimponible = iva_baseimponible;
         this.iva_valor = iva_valor;
         this.iva_descripcion = iva_descripcion;

         this.ice_codigo = ice_codigo ;
         this.ice_baseimponible = ice_baseimponible;
         this.ice_valor = ice_valor;

    }

    public  Double getSubTotalItem(){
        if(ice_codigo>0)
             return cantidad  * (pvp - descuento + iva_valor + ice_valor);
        else return cantidad  * (pvp - descuento + iva_valor);
    }

    public  Double getBaseImponibleItem(){
            return cantidad  * (pvp - descuento);

    }

    public static ArrayList<clsItemFactura> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsItemFactura> productos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            productos.add(new clsItemFactura(datos.getJSONObject(i)));
        }
        return productos;
    }
}
