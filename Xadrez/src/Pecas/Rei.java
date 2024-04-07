package Pecas;

import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.io.Serializable;

import static java.lang.Math.abs;

public class Rei extends Peca implements Serializable{
    public Rei(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Rei";
    }
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro t) {

        if(umaPosicao.getColuna() == this.pPosicao.getColuna() &&
                umaPosicao.getLinha() == this.pPosicao.getLinha()){
            return false;
        }
        if(abs(this.pPosicao.getLinha() - umaPosicao.getLinha()) <= 1 &&
                abs(this.pPosicao.getColuna() - umaPosicao.getColuna()) <= 1){
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
        return false;
    }

    @Override
    public boolean captura(Posicao umaPosicao, Tabuleiro t) {
        return setPosicao(umaPosicao,t);
    }
}