package uteq.solutions.navdrawer.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class clsFactura {

    public String fecha;
    public int tipo, idcliente;
    public String cliente_tipoid, cliente_id, cliente_razonsocial, cliente_direccion, cliente_correo;
    public Double totalsinimpuestos, totaldescuento, propina, importetotal;



    public clsFactura(JSONObject a) throws JSONException {

        fecha = a.getString("fecha");;
        tipo = a.getInt("tipo");
        idcliente = a.getInt("idcliente");
        cliente_tipoid = a.getString("cliente_tipoid");
        cliente_id = a.getString("cliente_id");
        cliente_razonsocial =  a.getString("cliente_razonsocial");
        cliente_direccion = a.getString("cliente_direccion");
        cliente_correo = a.getString("cliente_correo");
        totalsinimpuestos = a.getDouble("totalsinimpuestos");
        totaldescuento = a.getDouble("totaldescuento");
        propina = a.getDouble("propina");
        importetotal = a.getDouble("importetotal");

    }

    public static ArrayList<clsFactura> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<clsFactura> productos = new ArrayList<>();
        for (int i = 0; i < datos.length(); i++) {
            productos.add(new clsFactura(datos.getJSONObject(i)));
        }
        return productos;
    }
}
