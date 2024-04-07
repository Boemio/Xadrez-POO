package Xadrez;

import Pecas.Peca;
import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

public class Conjunto extends ArrayList<Peca> implements Serializable{
    public Conjunto(){
        super();
    }
    public void AutoDesenho(Tabuleiro tTabuleiro){
        for(int i = 0; i < this.size(); i++)
            this.get(i).autoDesenho(tTabuleiro);
    }
    public Peca getPecaClicada(Posicao aPosicao){
        for(int i = 0; i < this.size(); i++)
            if(this.get(i).foiClicada(aPosicao))
                return this.get(i);
        return null;
    }
    public void pecaFora(Peca aPeca){
        for(int i = 0; i < this.size(); i++)
            if(this.get(i) == aPeca)
                this.remove(i);
    }
}