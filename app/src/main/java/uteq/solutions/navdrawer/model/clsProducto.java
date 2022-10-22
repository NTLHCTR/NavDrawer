package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class clsProducto {

    public String codigo;
    public String descripcion;
    public String categoria;
    public double PVP;
    public double preciocompra;
    public  int   codigoImpuestoIVA;
    public double IVA;
    public double DESC;
    public double PMayorista;
    public String ImpuestoDescr;
    public String UnidadMedida;
    public  int codigoImpuestoICE;
    public  String ImpuestoICEDescr;
    public int ID;

    public clsProducto(JSONObject a) throws JSONException {
        ID = a.getInt("id");
        codigo =  a.getString("barcode") ;
        descripcion =  a.getString("descripcion") ;
        categoria=a.getString("p_categoria");
        preciocompra = a.getDouble("costo");
        PVP = a.getDouble("precio_unidad");
        PMayorista = a.getDouble("precio_mayorista");
        codigoImpuestoIVA =  a.getInt("impuesto");
        IVA =  a.getDouble("p_impuestovalor");
        ImpuestoDescr = a.getString("p_impuesto") ;
        DESC = a.isNull("descuento")?0:a.getDouble("descuento");
        UnidadMedida = a.isNull("p_unidad")?"":a.getString("p_unidad");
        codigoImpuestoICE = a.isNull("ice")?-1:a.getInt("ice");
        ImpuestoICEDescr = a.isNull("p_icedescripcion")?"":a.getString("p_icedescripcion");
    }



    public static ArrayList<clsProducto> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsProducto> productos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            productos.add(new clsProducto(datos.getJSONObject(i)));
        }
        return productos;
    }
}
