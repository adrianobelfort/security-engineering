/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author adria
 */
public class Scatterer
{
    private Scatterer()
    {
        
    }
    
    public static byte[] scatter(byte[] originalElements)
    {
        byte[] elements = originalElements.clone();
        int size = elements.length;
        int mappingSize = size / 2;
        int currentPosition;
        ArrayList<Integer> mappingSequence;
        ClosedSequence closedFibonacci = new ClosedSequence(mappingSize);

        mappingSequence = closedFibonacci.uniqueSequence();
        
        //System.out.println("Mapping sequence: " + mappingSequence.toString());
        
        currentPosition = size / 2;
        if (size % 2 != 0) currentPosition++;
        
        for (Integer mappedPosition : mappingSequence)
        {
            byte auxiliar;
            
            auxiliar = elements[currentPosition];
            elements[currentPosition] = elements[mappedPosition];
            elements[mappedPosition] = auxiliar;
            
            currentPosition++;
        }
        
        return elements;
    }
    
    public static byte[] unscatter(byte[] originalElements)
    {
        byte[] elements = originalElements.clone();
        int size = elements.length;
        int mappingSize = size / 2;
        int currentPosition;
        ArrayList<Integer> mappingSequence;
        ClosedSequence closedFibonacci = new ClosedSequence(mappingSize);

        mappingSequence = closedFibonacci.uniqueSequence();
        
        //System.out.println("Mapping sequence: " + mappingSequence.toString());
        
        currentPosition = size / 2;
        if (size % 2 != 0) currentPosition++;
        
        TreeMap<Integer,Integer> backMapping = new TreeMap<>();
        for(Integer mappedPosition : mappingSequence)
        {
            backMapping.put(mappedPosition, currentPosition);
            currentPosition++;
        }
        
        for (Map.Entry<Integer,Integer> entry : backMapping.entrySet())
        {
            int mappedPosition = entry.getKey();
            int originalPosition = entry.getValue();
            byte aux;
            
            aux = elements[mappedPosition];
            elements[mappedPosition] = elements[originalPosition];
            elements[originalPosition] = aux;
        }
        
        return elements;
    }
}
