package br.com.meslin;

import java.util.List;
import br.com.meslin.lib.MyMath;

public class WorkerThread1 implements Runnable {
    private int index;
    private List<Integer> lBase;
    private List<Integer> lExp;
    private List<Integer> lResp;

    public WorkerThread1(int index, List<Integer> lBase, List<Integer> lExp, List<Integer> lResp) {
        this.index = index;
        this.lBase = lBase;
        this.lExp = lExp;
        this.lResp = lResp;
    }

    @Override
    public void run() {
        int base;
        int exp;

        // realiza a computação
        base = lBase.get(index);
        exp = lExp.get(index);
        int res = MyMath.power(base, exp);

        // devolve a resposta
        if(lResp.get(index) == 0) {
            // se a resposta ainda não foi calculada, coloca a resposta no vetor
            lResp.set(index, res);
            //System.out.println("Criando resposta " + base + "^" + exp + " = " + res + " na posição " + index);
        } else {
            // se alguém já calculou a resposta
            if(lResp.get(index) > 0) {
                // se é o 1o a detectar que a resposta já foi calculada, informa colocando um valor negativo
                lResp.set(index, -1);
            } else {
                // decrementa o contador se alguma outra thread já percebeu que a resposta já foi calculada
                lResp.set(index, lResp.get(index)-1);
            }
        }
    }
}
