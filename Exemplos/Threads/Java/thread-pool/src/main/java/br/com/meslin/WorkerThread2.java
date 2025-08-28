package br.com.meslin;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import br.com.meslin.lib.MyMath;

public class WorkerThread2 implements Runnable {
    private static int index =0;
    private static final Lock lock = new ReentrantLock();
    private List<Integer> lBase;
    private List<Integer> lExp;
    private List<Integer> lResp;

    public WorkerThread2(List<Integer> lBase, List<Integer> lExp, List<Integer> lResp) {
        this.lBase = lBase;
        this.lExp = lExp;
        this.lResp = lResp;
    }

    @Override
    public void run() {
        int index;
        while(this.index < this.lBase.size()) {
            synchronized(this.lock) {
                if(this.index >= this.lBase.size()) break;
                index = this.index;
                this.index++;
            }
    
            // realiza a computação
            int base = this.lBase.get(index);
            int exp = this.lExp.get(index);
            int res = MyMath.power(base, exp);
            //System.out.printf("Calculando %d^%d = %d na posição %d\n", base, exp, res, index);
    
            // devolve a resposta
            if(this.lResp.get(index) == 0) {
                // se a resposta ainda não foi calculada, coloca a resposta no vetor
                this.lResp.set(index, res);
            } else {
                // se alguém já calculou a resposta
                if(this.lBase.get(index) > 0) {
                    // se é o 1o a detectar que a resposta já foi calculada, informa colocando um valor negativo
                    this.lResp.set(index, -1);
                } else {
                    // decrementa o contador se alguma outra thread já percebeu que a resposta já foi calculada
                    this.lResp.set(index, this.lResp.get(index)-1);
                }
            }
        }
    }
}
