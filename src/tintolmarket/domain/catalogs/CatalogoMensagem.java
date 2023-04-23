package tintolmarket.domain.catalogs;

import tintolmarket.domain.Mensagem;
import java.util.ArrayList;
import java.util.List;

public class CatalogoMensagem {
    private List<Mensagem> catMensagem;

    public List<Mensagem> getCatMensagem() {
        return catMensagem;
    }

    private CatalogoMensagem() {
        this.catMensagem = new ArrayList<>();
    }

    private CatalogoMensagem(ArrayList<Mensagem> aw) {
        this.catMensagem = aw;
    }

    private static CatalogoMensagem INSTANCE;

    //SINGLETON
    public static CatalogoMensagem getInstance( Object o) {
        if(o != null) {
            CatalogoMensagem.INSTANCE = new CatalogoMensagem((ArrayList<Mensagem>) o);
            return INSTANCE;
        }
        INSTANCE = new CatalogoMensagem();
        return INSTANCE;
    }

    public void addMensagem(String from, String to, byte[] message){

        Mensagem m = new Mensagem(from,to,message);
        synchronized (catMensagem){
            this.catMensagem.add(m);
        }

    }

    public List<Mensagem> getMensagensToUser(String to){
        List<Mensagem> newCat = new ArrayList<>();
        List<Mensagem> mensagemsUser = new ArrayList<>();
        synchronized (catMensagem){
            for (Mensagem m:this.catMensagem){
                if(m.getTo().equals(to)){
                    mensagemsUser.add(m);
                } else {
                    newCat.add(m);
                }
            }
            this.catMensagem = newCat;
        }
        return mensagemsUser;
    }

}
