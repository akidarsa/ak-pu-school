/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab7;

/**
 *
 * @author akidarsa
 */

import java.util.*;

class PartialSum {

    private int sum;

    PartialSum()
    {
        sum = 0;
    }

    synchronized public void update(int amount) {
        sum += amount;
		notifyAll();
    }

    public int getSum()
    {
        return sum;
    }

}


class PrimeCounter extends Thread {

    private int lowLimit;
    private int highLimit;
    PartialSum psum;

    PrimeCounter(int low, int high, PartialSum sum)
    {
        lowLimit = low;
        highLimit = high;
        psum = sum;
    }

	@Override
    public void run()
    {
        psum.update(TotalPrime(lowLimit, highLimit));
    }


    public int TotalPrime(int lowLimit, int highLimit) {
    	int sum = 0;
        if (lowLimit == 2) {
            sum++;
            // cout << "2 is prime" << endl;
        }
        if ((lowLimit % 2) == 0) // even number
        {
            lowLimit++;
        }
        for (int curNum = lowLimit; curNum <= highLimit; curNum += 2) {
            if (prime.IsPrime(curNum) == true) {
                sum++;
                // cout << curNum << " is prime" << endl;
            }
        }
        return sum;
    }
}

class Total{
	Total() {}

    public static int totalPrime(int lowLimit, int highLimit) {
    	int sum = 0;
        if (lowLimit == 2) {
            sum++;
            // cout << "2 is prime" << endl;
        }
        if ((lowLimit % 2) == 0) // even number
        {
            lowLimit++;
        }
        for (int curNum = lowLimit; curNum <= highLimit; curNum += 2) {
            if (prime.IsPrime(curNum) == true) {
                sum++;
                // cout << curNum << " is prime" << endl;
            }
        }
        return sum;

    }
}

class prime {

    public static boolean IsPrime(int num) {
        if (num == 2) {
            return true;
        }

        if (num == 3) {
            return true;
        }

        if (num == 5) {
            return true;
        }

        if ((num % 2) == 0) {
            return false;
        }

        if ((num % 3) == 0) {
            return false;
        }

        if ((num % 5) == 0) {
            return false;
        }

        int factor = 5;
        do {
            if ((num % factor) == 0) {
                return false;
            }
            factor += 2;
        } while ((factor * factor) <= num);
        return true;

    }
}
	


public class Main {
    private static int lowLimit;
            private static int highLimit;
            private static int numPrime;
            private static long tp1;
            private static long tp2;
            private static int numThread;
            private static int interval;
            private static int tcnt;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
            numThread = 8;
            lowLimit = 2;
            highLimit = 10000000;
            if(args.length == 1)
            {
                highLimit = Integer.parseInt(args[0]);
            }
            if(args.length > 1)
            {
                lowLimit = Integer.parseInt(args[0]);
                highLimit = Integer.parseInt(args[1]);
            }
            tp1 = System.nanoTime();
			Total tot = new Total();
            numPrime = tot.totalPrime(lowLimit, highLimit);
            tp2 = System.nanoTime();
            System.out.println("Total Number of Primes = " + numPrime);
            System.out.println("spent time = " + (tp2 - tp1));

            tp1 = System.nanoTime();
            PartialSum psum = new PartialSum();
            PrimeCounter counter[] = new PrimeCounter[numThread];
            interval = ((highLimit - lowLimit)/ numThread);
            for (tcnt = 0; tcnt < numThread -1; tcnt++)
            {
                counter[tcnt] = new PrimeCounter(lowLimit, (lowLimit + interval), psum)  ;
                lowLimit += interval + 1;
            }
            counter[numThread -1] = new PrimeCounter(lowLimit, highLimit, psum);
            for(tcnt = 0; tcnt < numThread; tcnt ++)
            {
                counter[tcnt].start();
            }
            for(tcnt = 0; tcnt < numThread; tcnt ++)
            {
                try
                {
                    counter[tcnt].join();
                }
                catch(Exception e1)
                {
                }
            }
            tp2 = System.nanoTime();
            System.out.println("Total Number of Primes = " + (psum.getSum()));
            System.out.println("spent time = " + (tp2 - tp1));
    }


}
