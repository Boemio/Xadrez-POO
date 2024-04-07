package Pecas;

import Xadrez.Jogo;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.io.Serializable;

import static java.lang.Math.abs;

public class Bispo extends Peca implements Serializable {

    public Bispo(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
    }

    public String toString() {
        return "Bispo";
    }

    public boolean setPosicao(Posicao umaPosicao, Tabuleiro t) {
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
