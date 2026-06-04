package edu.eci.arsw.primefinder;

public class PauseControl {
    private boolean paused = false;

    /**
     * Metodo para pausar, es llamado por
     * cada hilo hasta que la condicion sea True
     */
    public synchronized void checkPause() throws InterruptedException {
        while (paused){
            wait();
        }
    }
    /**Metodo para activar la pausa
     */
    public synchronized void pause(){
        paused= true;
    }
    /**
     * Metodo que desactiva y despierta a todos lo hilos que estan
     * dormidos
     */
    public synchronized void resume(){
        paused=false;
        notifyAll();
    }

    /**
     * Metodo que verifica el estado de la ejecucion
     */
    public boolean isPaused(){
        return paused;
    }
}

