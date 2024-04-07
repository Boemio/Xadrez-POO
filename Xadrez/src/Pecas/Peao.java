package Pecas;

import Auxiliar.Consts;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import static java.lang.Math.abs;

public class Peao extends Peca implements Serializable{

    private boolean bPrimeiroLance;

    public Peao(String sAFileName, Posicao aPosicao, boolean bBrancas) {
        super(sAFileName, aPosicao, bBrancas);
        this.bPrimeiroLance = true;
    }

    public String toString() {
        return "Peao";
    }

    @Override
    public boolean captura(Posicao umaPosicao, Tabuleiro t){
        if (abs(this.pPosicao.getColuna() - umaPosicao.getColuna()) != 1) {
            return false;
        }

        if(this.pecaEhBranca()){
            if(this.pPosicao.getLinha() < umaPosicao.getLinha())
                return false;
            else if (umaPosicao.getLinha() == (this.pPosicao.getLinha() - 1)) {
                this.pPosicao.setPosicao(umaPosicao);
                return true;
            }
        } else {
            if(this.pPosicao.getLinha() > umaPosicao.getLinha()){
                return false;
            }
            else {
                if (umaPosicao.getLinha() == (this.pPosicao.getLinha() + 1)) {
                    this.pPosicao.setPosicao(umaPosicao);
                    return true;
                }
            }
        }
            return false;
    }

    public boolean setPosicao(Posicao umaPosicao, Tabuleiro t) {
        if (this.pPosicao.getColuna() != umaPosicao.getColuna()) {
            return false;
        }
        if (this.bBrancas) {
            if(this.pPosicao.getLinha() < umaPosicao.getLinha())
                return false;
            if (bPrimeiroLance) {
                if (umaPosicao.getLinha() >= (this.pPosicao.getLinha() - 2)) {
                    this.pPosicao.setPosicao(umaPosicao);
                    bPrimeiroLance = false;
                    return true;
                } }
            else {
                if (umaPosicao.getLinha() >= (this.pPosicao.getLinha() - 1)) {
                    this.pPosicao.setPosicao(umaPosicao);
                    return true;
                }
            }
        } else {
            if(this.pPosicao.getLinha() > umaPosicao.getLinha())
                return false;
            if (bPrimeiroLance) {
                if (umaPosicao.getLinha() <= (this.pPosicao.getLinha() + 2)) {
                    this.pPosicao.setPosicao(umaPosicao);
                    bPrimeiroLance = false;
                    return true;
                } }
            else {
                if (umaPosicao.getLinha() <= (this.pPosicao.getLinha() + 1)) {
                    this.pPosicao.setPosicao(umaPosicao);
                    return true;
                }
            }
        }
        return false;
    }
}
