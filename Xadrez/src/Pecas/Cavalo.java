package Pecas;

import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.io.Serializable;

import static java.lang.Math.abs;

public class Cavalo extends Peca implements Serializable{

    public Cavalo(String sAFileName, Posicao aPosicao, boolean bBrancas) {
           super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Cavalo";
    }
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro t) {
        if(abs(this.pPosicao.getColuna() - umaPosicao.getColuna()) == 2 &&
                abs(this.pPosicao.getLinha() - umaPosicao.getLinha()) == 1){
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
        if(abs(this.pPosicao.getLinha() - umaPosicao.getLinha()) == 2 &&
                abs(this.pPosicao.getColuna() - umaPosicao.getColuna()) == 1){
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
