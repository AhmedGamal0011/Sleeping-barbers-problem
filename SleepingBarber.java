import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SleepingBarber {
	
	public static void main (String a[]) throws InterruptedException {	
		
		int noOfBarbers=2, customerId=1, noOfCustomers=100, noOfChairs;	
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the number of barbers(M):");			
    	noOfBarbers=sc.nextInt();
    	
    	System.out.println("Enter the number of waiting room"			
    			+ " chairs(N):");
    	noOfChairs=sc.nextInt();
    	
    	System.out.println("Enter the number of customers:");			
    	noOfCustomers=sc.nextInt();
    	
		ExecutorService exec = Executors.newFixedThreadPool(12);		
    	Bshop shop = new Bshop(noOfBarbers, noOfChairs);				
    	Random r = new Random();  										
       	    	
        System.out.println("\nBarber shop opened with "
        		+noOfBarbers+" barber(s)\n");
        
        long startTime  = System.currentTimeMillis();					
        
        for(int i=1; i<=noOfBarbers;i++) {								
        	
        	Barber barber = new Barber(shop, i);	
        	Thread thbarber = new Thread(barber);
            exec.execute(thbarber);
        }
        
        for(int i=0;i<noOfCustomers;i++) {								
        
            Customer customer = new Customer(shop);
            customer.setInTime(new Date());
            Thread thcustomer = new Thread(customer);
            customer.setcustomerId(customerId++);
            exec.execute(thcustomer);
            
            try {
            	
            	double val = r.nextGaussian() * 2000 + 2000;				
            	int millisDelay = Math.abs((int) Math.round(val));		
            	Thread.sleep(millisDelay);								
            }
            catch(InterruptedException iex) {
            
                iex.printStackTrace();
            }
            
        }
        
        exec.shutdown();												
        exec.awaitTermination(12, SECONDS);								
 
        long elapsedTime = System.currentTimeMillis() - startTime;		
        
        System.out.println("\nBarber shop closed");
        System.out.println("\nTotal time elapsed in seconds"
        		+ " for serving "+noOfCustomers+" customers by "
        		+noOfBarbers+" barbers with "+noOfChairs+
        		" chairs in the waiting room is: "
        		+TimeUnit.MILLISECONDS
        	    .toSeconds(elapsedTime));
        System.out.println("\nTotal customers: "+noOfCustomers+
        		"\nTotal customers served: "+shop.getTotalHairCuts()
        		+"\nTotal customers lost: "+shop.getCustomerLost());
               
        sc.close();
    }
}
 