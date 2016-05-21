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

    public ClosedSequence(int size)
    {
        this.modulo = size;
        lookingAt = 0;
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
        
        int nextElement = (lookedAtElement + lastElement) % modulo;
        
        sequence.add(nextElement);
        
        // Modulo is long, lookingAt is int -> watch
        lookingAt = (int) ((lookingAt + 1) % modulo);
        
        return nextElement;
    }
    
    public ArrayList<Integer> uniqueSequence()
    {
        ArrayList<Integer> unique = new ArrayList<>();
        int i = 0;
        
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
            
            if (!unique.contains(nextElement))
            {
                unique.add(nextElement);
                i++;
            }
        }
        
        return unique;
    }
}
