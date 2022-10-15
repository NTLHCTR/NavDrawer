package uteq.solutions.navdrawer.ui.facturacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    public MutableLiveData<CharSequence> text = new MutableLiveData<CharSequence>();
    public MutableLiveData<Integer> valor = new MutableLiveData<Integer>();
    public MutableLiveData<String> texto = new MutableLiveData<String>();
    public MutableLiveData<Double> costo = new MutableLiveData<Double>();

    public  SharedViewModel(){

        text.setValue("");
        valor.setValue(0);
        texto.setValue("");
        costo.setValue(0.0);
    }
    public LiveData<Integer> getValor() { return valor;  }
    public void setValor(Integer valor){ this.valor.setValue(valor);  }

    public LiveData<String> getTexto() {   return texto; }
    public void setTexto(String texto) {   this.texto.setValue(texto);   }

    public LiveData<Double> getCosto() {  return costo;   }

    public void setCosto(Double costo) {  this.costo.setValue(costo);  }

    public void setText(CharSequence input) {  text.setValue(input);  }
    public LiveData<CharSequence> getText() {   return text;   }

}

