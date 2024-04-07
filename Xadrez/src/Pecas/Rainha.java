package Pecas;

import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.io.Serializable;

import static java.lang.Math.abs;

public class Rainha extends Peca implements Serializable{
    public Rainha(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }
    public String toString(){
        return "Rainha";
    }
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro t) {

        if(this.pPosicao.getColuna() == umaPosicao.getColuna()&&
                this.pPosicao.getLinha() != umaPosicao.getLinha()){
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
        if(this.pPosicao.getColuna() != umaPosicao.getColuna()&&
                this.pPosicao.getLinha() == umaPosicao.getLinha()){
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
        if(abs(this.pPosicao.getColuna() - umaPosicao.getColuna()) == abs(this.pPosicao.getLinha() - umaPosicao.getLinha())) {
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
