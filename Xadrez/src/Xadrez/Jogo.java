package Xadrez;

import Auxiliar.Consts;
import Pecas.*;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Jogo extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Tabuleiro tTabuleiro;//atributo com a janela de desenho
    Conjunto cBrancas;
    Conjunto cPretas;
    boolean bEmJogada;
    boolean jogadaBrancas;
    boolean jogoTerminou;
    Peca pecaEmMovimento;

    public enum CoresConjuntos {
        BRANCAS, PRETAS
    };

    public Jogo() {
        cBrancas = new Conjunto();
        cPretas = new Conjunto();
        tTabuleiro = new Tabuleiro(cBrancas, cPretas);//alocação do painel de desenho
        tTabuleiro.setFocusable(true);        
        tTabuleiro.addMouseListener(this);//Adiciona evento de mouse ao Painel de desenho
        tTabuleiro.addKeyListener(this);
        
        bEmJogada = false;
        jogadaBrancas = true;
        jogoTerminou = false;
        pecaEmMovimento = null;
        initComponents();
    }

    public void addPeca(Peca aPeca, CoresConjuntos aCorConjunto) {
        if (aCorConjunto == CoresConjuntos.BRANCAS) {
            cBrancas.add(aPeca);
        } else {
            cPretas.add(aPeca);
        }
    }

    public Peca getPecaClicada(Posicao aPosicao) {
        Peca pTemp = cBrancas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        pTemp = cPretas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        return null;
    }

    public void paint(Graphics g) {
        super.paint(g);
        cBrancas.AutoDesenho(this.tTabuleiro);
        cPretas.AutoDesenho(this.tTabuleiro);
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.DELAY);
    }

    public Posicao getPosicaoDoClique(MouseEvent aMouseEvent) {
        return new Posicao(aMouseEvent.getY() / Consts.SIZE,
                aMouseEvent.getX() / Consts.SIZE);
    }

    public boolean testaEmpate(Tabuleiro t){
        int cavaloBrancas = 0;
        int bispoBrancas = 0;
        int cavaloPretas = 0;
        int bispoPretas = 0;

        if(this.cPretas.size() >= 3 || this.cBrancas.size() >= 3){
            return false;
        }

        for (int i = 0; i < this.cBrancas.size(); i++) {
            Peca iEsimaPeca = (Peca) this.cBrancas.get(i);
            if (iEsimaPeca.toString().compareTo("Cavalo") == 0) {
                cavaloBrancas += 1;
            }
            if (iEsimaPeca.toString().compareTo("Bispo") == 0) {
                bispoBrancas += 1;
            }
        }

        for (int i = 0; i < this.cPretas.size(); i++) {
            Peca iEsimaPeca = (Peca) this.cPretas.get(i);
            if (iEsimaPeca.toString().compareTo("Cavalo") == 0) {
                cavaloPretas += 1;
            }
            if (iEsimaPeca.toString().compareTo("Bispo") == 0) {
                bispoPretas += 1;
            }
        }

        if((bispoPretas + cavaloPretas < 2) && (bispoBrancas + cavaloBrancas < 2)){
            return true;
        }

        return false;
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();//pega as coordenadas do mouse
        int y = e.getY();
        this.clickLabel.setText("x:" + x + "  y:" + y + "   -   Quadrante: [" + y / Consts.SIZE + "," + x / Consts.SIZE + "]");

        Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e));
        if (pecaClicada == null) {
            System.out.println("Nenhuma peca selecionada");
        } else {
            System.out.println("Peca " + pecaClicada + " selecionada");
        }
        if (bEmJogada && !jogoTerminou) {
            if(pecaEmMovimento.pecaEhBranca() && !(jogadaBrancas)){
                bEmJogada = false;
                pecaEmMovimento = null;
                System.out.println("Jogada ainda em movimento, selecione uma peça valida");
            }else if(!pecaEmMovimento.pecaEhBranca() && jogadaBrancas)
            {
                bEmJogada = false;
                pecaEmMovimento = null;
                System.out.println("Jogada ainda em movimento, selecione uma peça valida");
            }
            else {
                if (pecaClicada == null) { //soh movimenta
                    if (pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), this.tTabuleiro)) {
                        pecaEmMovimento = null;
                        bEmJogada = false;

                        if(jogadaBrancas == false){
                            System.out.println("Vez das brancas!");
                            jogadaBrancas = true;
                        }else{
                            System.out.println("Vez das pretas!");
                            jogadaBrancas = false;
                        }

                    } else {
                        System.out.println("Jogada ainda em movimento, selecione uma posicao valida");
                    }
                } else if (pecaEmMovimento.temAMesmaCorQue(pecaClicada)) {
                    bEmJogada = false;
                    pecaEmMovimento = null;
                    System.out.println("Jogada ainda em movimento, selecione uma posicao valida");
                } else {               //come a peca clicada
                    if (pecaEmMovimento.captura(this.getPosicaoDoClique(e), this.tTabuleiro)) {
                        if (pecaEmMovimento == pecaClicada)
                            System.out.println("A peca foi deselecionada, escolha outra peca");
                        else if (!pecaEmMovimento.temAMesmaCorQue(pecaClicada)) {
                            cPretas.pecaFora(pecaClicada);
                            cBrancas.pecaFora(pecaClicada);
                            /*Completar o codigo para pecas pretas comendo pecas brancas*/
                            pecaEmMovimento = null;
                            bEmJogada = false;
                            if(pecaClicada.toString().compareTo("Rei") == 0){
                                if(jogadaBrancas == true)
                                    System.out.println("Brancas Ganharam!");
                                else
                                    System.out.println("Pretas ganharam!");
                                jogoTerminou = true;
                            }
                            if(jogadaBrancas == false){
                                System.out.println("Vez das brancas");
                                jogadaBrancas = true;
                            }else{
                                System.out.println("Vez das pretas");
                                jogadaBrancas = false;
                            }
                            if(testaEmpate(tTabuleiro)){
                                System.out.println("Empatou!");
                                jogoTerminou = true;
                            }
                        }
                    } else {
                        System.out.println("Jogada ainda em movimento, selecione uma posicao valida");
                    }
                }
            }
        } else {
            if (pecaClicada != null) {
                System.out.println("Movimentacao em andamento, selecione o destino da peca");
                pecaEmMovimento = pecaClicada;
                bEmJogada = true;
            }
        }
        repaint();
    }
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_S){
            File tanque = new File("estados"+File.separator+"POO.zip");
            try{
                tanque.createNewFile();
                FileOutputStream canoOut = new FileOutputStream(tanque);
                GZIPOutputStream compactador = new GZIPOutputStream(canoOut);
                ObjectOutputStream serializador = new ObjectOutputStream(compactador);
                serializador.writeObject(this.cBrancas);
                serializador.writeObject(this.cPretas);
                serializador.writeObject(this.jogadaBrancas);
                serializador.writeObject(this.jogoTerminou);
                serializador.flush();
                serializador.close();
                System.out.println("Jogo salvo com sucesso");
            }catch(Exception execpt){
                System.out.println("Ocorreu o seguinte erro " + execpt.getMessage());    
            }
         }
        if(e.getKeyCode() == KeyEvent.VK_L){
            try{
                FileInputStream fileIn = new FileInputStream("estados/POO.zip");
                GZIPInputStream gzis = new GZIPInputStream(fileIn);
                ObjectInputStream in = new ObjectInputStream(gzis);
                this.cBrancas = (Conjunto) in.readObject();
                this.cPretas = (Conjunto) in.readObject();
                this.jogadaBrancas = (Boolean) in.readObject();
                this.jogoTerminou = (Boolean) in.readObject();
                fileIn.close();
            }catch (Exception exception){
                System.out.println("Ocorreu o seguinte erro " + exception.getMessage());
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_R){
            try{
                FileInputStream fileIn = new FileInputStream("estados/POO_INICIAL.zip");
                GZIPInputStream gzis = new GZIPInputStream(fileIn);
                ObjectInputStream in = new ObjectInputStream(gzis);
                this.cBrancas = (Conjunto) in.readObject();
                this.cPretas = (Conjunto) in.readObject();
                this.jogadaBrancas = (Boolean) in.readObject();
                this.jogoTerminou = (Boolean) in.readObject();
                fileIn.close();
            }catch (Exception exception){
                System.out.println("Ocorreu o seguinte erro " + exception.getMessage());
            }
        }
        /* if(e.getKeyCode() == KeyEvent.VK_UP){
         pPeao.MoveUp();
         }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
         pPeao.MoveDown();
         }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
         pPeao.MoveLeft();
         }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
         pPeao.MoveRight();
         }
         */
        repaint();
    }

    public void mouseClicked(MouseEvent e) {

    }
    
    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelCenario = tTabuleiro;
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        coordenadaLabel = new javax.swing.JLabel();
        clickLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SCC0204 - Xadrez");
        setResizable(false);

        jPanelCenario.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setMinimumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setPreferredSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout jPanelCenarioLayout = new javax.swing.GroupLayout(jPanelCenario);
        jPanelCenario.setLayout(jPanelCenarioLayout);
        jPanelCenarioLayout.setHorizontalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelCenarioLayout.setVerticalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel2.setText("Coordenada:");

        jLabel3.setText("click:");

        coordenadaLabel.setText("10");

        clickLabel.setText("quadrante");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 219, Short.MAX_VALUE)
                                .addComponent(clickLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(coordenadaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(293, 293, 293))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(clickLabel)
                        .addComponent(coordenadaLabel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clickLabel;
    private javax.swing.JLabel coordenadaLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanelCenario;
    // End of variables declaration//GEN-END:variables

}
