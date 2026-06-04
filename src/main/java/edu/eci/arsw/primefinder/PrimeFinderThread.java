package edu.eci.arsw.primefinder;

import edu.eci.arsw.primefinder.PauseControl;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	
	private List<Integer> primes;
    private PauseControl pauseControl;
	
	public PrimeFinderThread(int a, int b, PauseControl pauseControl) {
		super();
        this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
        this.pauseControl= pauseControl;
	}

    @Override
	public void run(){
            for (int i= a;i < b;i++){
                try {
                    pauseControl.checkPause();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (isPrime(i)){
                    primes.add(i);
                }
            }
	}
	
	boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n%2 != 0;
            for(int i = 3;ans && i*i <= n; i+=2 ) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
}
