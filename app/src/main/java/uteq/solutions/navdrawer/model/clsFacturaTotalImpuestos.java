package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class clsFacturaTotalImpuestos {

    public Integer ID=0;
    public Integer idfactura=0;
    public Integer idimpuesto;
    public Integer tipoimpuesto;
    public String descripcionimp;
    public Double baseimponible, valor;


    public clsFacturaTotalImpuestos(JSONObject a) throws JSONException {
        ID = a.getInt("id");
        idfactura = a.getInt("idfactura") ;
        idimpuesto = a.getInt("idimpuesto") ;
        tipoimpuesto = a.getInt("tipoimpuesto") ;
        baseimponible = a.getDouble("baseimponible") ;
        valor = a.getDouble("valor") ;
        descripcionimp = a.getString("descripcionimp") ;
    }


    public clsFacturaTotalImpuestos(Integer tipoimpuesto,Integer idimpuesto, String descripcionimp,
                                    Double baseimponible, Double valor)
     {

         this.tipoimpuesto = tipoimpuesto;
         this.idimpuesto = idimpuesto;
         this.descripcionimp =  descripcionimp ;
         this.baseimponible = baseimponible;
         this.valor = valor;

    }


    public static ArrayList<clsFacturaTotalImpuestos> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsFacturaTotalImpuestos> productos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            productos.add(new clsFacturaTotalImpuestos(datos.getJSONObject(i)));
        }
        return productos;
    }
}
