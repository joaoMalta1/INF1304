package br.com.meslin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.meslin.lib.MyMath;

public class Main {
//    private static final long problemSize = 10000000;
    private static final long problemSize = 1000;
//    private static final long problemSize = 10;
    private static final int poolSize = 5;
    private static List<Integer> lBase;
    private static List<Integer> lExp;
    private static List<Integer> lResp;
    
    public static void main(String[] args) {
        System.out.println("Começando...");

        lBase = new ArrayList<>();
        lExp = new ArrayList<>();
        lResp = new ArrayList<>();
        for(int i=0; i<problemSize; i++) {
            lBase.add((int)(Math.random()*10)+1);
            lExp.add((int)(Math.random()*10));
            lResp.add(0);
        }

        /*
        System.out.print("Base: ");
        for(int i=0; i<problemSize; i++) {
            System.out.print(lBase.get(i) + " ");
        }
        */

        System.out.print(("\nUsando Executor: "));
        long startTime = System.currentTimeMillis();
        usingExecutor();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " milissegundos");
        checkResult();

        for(int i=0; i<problemSize; i++) {
            lResp.set(i, 0);
        }
        
        /*
        System.out.print("Base: ");
        for(int i=0; i<problemSize; i++) {
            System.out.print(lBase.get(i) + " ");
        }
        */

        System.out.print(("\nUsando pool manual: "));
        startTime = System.currentTimeMillis();
        usingManualPool();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " milissegundos");
        checkResult();

        for(int i=0; i<problemSize; i++) {
            lResp.set(i, 0);
        }

        System.out.print(("\nUsando método sequencial: "));
        startTime = System.currentTimeMillis();
        usingSequencial();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Tempo de execução: " + duration + " milissegundos");
        checkResult();

        System.out.println("\nFinished all threads");
    }

    private static void usingExecutor() {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        for(int i = 0; i < problemSize; i++) {
            Runnable worker = new WorkerThread1(i, lBase, lExp, lResp);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    private static void usingManualPool() {
        // Lista para armazenar as threads
        Thread[] threads = new Thread[poolSize];

        // Criando e iniciando as threads
        for(int i = 0; i < poolSize; i++) {
            WorkerThread2 worker = new WorkerThread2(lBase, lExp, lResp);
            threads[i] = new Thread(worker);
            threads[i].start();
        }

        // Esperando que todas as threads terminem
        for(int i = 0; i < 5; i++) {
            try {
                threads[i].join();  // Espera a thread terminar
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void usingSequencial() {
        for(int i=0; i<lBase.size(); i++) {
            lResp.set(i, MyMath.power(lBase.get(i), lExp.get(i)));
        }
    }
    
    private static void checkResult() {
        boolean error;
        System.out.print("Verificação: ");
        error = false;
        for(int i=0; i<problemSize; i++) {
            if(lResp.get(i) != BigInteger.valueOf(lBase.get(i)).pow((int)lExp.get(i)).longValue()) {
                error = true;
                System.out.println("Erro na posição " + i + " (" + lBase.get(i) + "^" + lExp.get(i) + " = " + lResp.get(i) + ")");
                for(int j=0; j<10; j++) System.out.print(lResp.get(i) + " ");
                System.out.println();
                break;
            } 
        }
        System.out.println(error? "NOK" : "OK");
    }
}