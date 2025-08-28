package br.com.meslin;

public class ExemploRunnable {
    public static void main(String[] args) {
        // Criação das tarefas Runnable
        Runnable tarefa1 = new MinhaRunnable("Thread 1");
        Runnable tarefa2 = new MinhaRunnable("Thread 2");

        // Criação e início das threads
        Thread thread1 = new Thread(tarefa1);
        Thread thread2 = new Thread(tarefa2);

        thread1.start();
        thread2.start();
    }
}
