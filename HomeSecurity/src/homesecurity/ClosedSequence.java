/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.util.ArrayList;

/**
 *
 * @author adria
 */
public class ClosedSequence
{
    private final int modulo;
    private int lookingAt;
    private ArrayList<Integer> sequence;
    private boolean gotStuck;

    public ClosedSequence(int size)
    {
        this.modulo = size;
        lookingAt = 0;
        gotStuck = false;
        sequence = fibonacciSequenceUntil(size);
    }
    
    private ArrayList fibonacciSequenceUntil(long n)
    {
        sequence = new ArrayList<>();
        
        if (n < 0)
        {
            return null;
        }
        if (n >= 0)
        {
            sequence.add(0);
        }
        if (n >= 1)
        {
            sequence.add(1);
        }
        if (n >= 2)
        {
            for (int i = 2; sequence.get(i-2) + sequence.get(i-1) < n; i++)
            {
                sequence.add(sequence.get(i-2) + sequence.get(i-1));
            }
        }
        
        return sequence;
    }
    
    public int next()
    {
        int lastElement = sequence.get(sequence.size() - 1);
        int lookedAtElement = sequence.get(lookingAt);
        
        int nextElement = (lookedAtElement + lastElement + (gotStuck ? 1 : 0)) % modulo;
        
        /*  In case the algorithm was stuck in a loop, we signal there was an 
            attempt of change */
        if (gotStuck) gotStuck = false;
        
        sequence.add(nextElement);
        
        // Modulo is long, lookingAt is int -> watch
        lookingAt = (int) ((lookingAt + 1) % modulo);
        
        return nextElement;
    }
    
    public ArrayList<Integer> uniqueSequence()
    {
        ArrayList<Integer> unique = new ArrayList<>();
        int i = 0;
        long timesOnSameI = 0;
        
        for (Integer l : sequence)
        {
            if (!unique.contains(l))
            {
                unique.add(l);
                i++;
            }
        }
        
        while(i < modulo)
        {
            int nextElement = next();
            
            timesOnSameI++;
            
            if (!unique.contains(nextElement))
            {
                unique.add(nextElement);
                i++;
                
                timesOnSameI = 0;
            }
            
            /*  If we can't find a new element within the range of modulo after
                more than 1000 times the modulo, it means we might be stuck.
                Therefore, we set gotStuck to true as a means of unstucking the
                modular fibonacci sequence, thus finding a new element
            */
            if (timesOnSameI > 1000 * modulo)
            {
                gotStuck = true;
            }
        }
        
        return unique;
    }
}
