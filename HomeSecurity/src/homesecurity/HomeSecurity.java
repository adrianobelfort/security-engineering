/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author adriano
 */
public class HomeSecurity
{
    public static String convertToString(byte[] message)
    {
        String stringMessage = "";
        
        for (Byte character : message)
        {
            stringMessage += Byte.toString(character);
        }
        
        return stringMessage;
    }
    
    public static ArrayList fibonacciSequence(int n)
    {
        ArrayList<Integer> sequence = new ArrayList<>();
        
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
            for (int i = 2; i <= n; i++)
            {
                sequence.add(sequence.get(i-2) + sequence.get(i-1));
            }
        }
        
        return sequence;
    }
    
    public static ArrayList fibonacciSequenceUntil(int n)
    {
        ArrayList<Integer> sequence = new ArrayList<>();
        
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
            for (int i = 2; sequence.get(i-2) + sequence.get(i-1) <= n; i++)
            {
                sequence.add(sequence.get(i-2) + sequence.get(i-1));
            }
        }
        
        return sequence;
    }
    
    public static int nextFibonacci(ArrayList<Integer> sequence)
    {
        int size = sequence.size();
        sequence.add(sequence.get(size-1) + sequence.get(size - 2));
        
        return sequence.get(size);
    }
    
    public static void scatter(byte[] elements)
    {
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
    }
    
    public static void unscatter(byte[] elements)
    {
        int size = elements.length;
        int mappingSize = size / 2;
        int currentPosition, offset;
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
    }
    
    public static void printArray(int[] elements)
    {
        System.out.print("[" + elements[0]);
        
        for (int i = 1; i < elements.length; i++)
        {
            System.out.print(", " + elements[i]);
        }
        System.out.println("]");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        final int PUBLICKEY = 0;
        final int PRIVATEKEY = 1;
        
        RSAKey[] keys;
        RSA rsaEncryption, rsaDecryption;
        byte[] encryptedbytes, decryptedbytes;
        EncryptedBytes encryptedMessage;
        Scanner scanner = new Scanner(System.in);
        String originalString, decryptedString;
        byte[] stringInBytes;
        
        keys = KeyFactory.generatePair();
        
        //System.out.println("Private key: " + keys[PRIVATEKEY].key.toString() + " (n = " + keys[PRIVATEKEY].n + ")");
        //System.out.println("Public key: " + keys[PUBLICKEY].key.toString() + " (n = " + keys[PUBLICKEY].n + ")");
        
        rsaEncryption = new RSA(keys[PRIVATEKEY]);
        rsaDecryption = new RSA(keys[PUBLICKEY]);
        
        /*Random r = new Random();
        int[] elements = new int[23];
   
        for (int i = 0; i < elements.length; i++)
        {
            elements[i] = r.nextInt() % 30;
        }
        
        System.out.println("Elements: ");
        printArray(elements);
        
        System.out.println("Scrobbled elements: ");
        scatter(elements);
        printArray(elements);
        
        System.out.println("Unscrobbled elements: ");
        unscatter(elements);
        printArray(elements); */       
        
        // ENCRYPTION
        System.out.print("Enter a string to encrypt: ");
        originalString = scanner.nextLine();
        
        System.out.println("Encrypting " + originalString + " ...");
        //encryptedMessage = new EncryptedBytes(rsaEncryption.encrypt(originalString.getBytes()));
        stringInBytes = originalString.getBytes();
        scatter(stringInBytes);
        encryptedbytes = rsaEncryption.encrypt(stringInBytes);
        
        //System.out.println("The encrypted string is " + convertToString(encryptedMessage.extractInformation()));
        
        System.out.println("Decrypting the encrypted string...");
        //decryptedbytes = rsaDecryption.decrypt(encryptedMessage.extractInformation());
        decryptedbytes = rsaDecryption.decrypt(encryptedbytes);
        unscatter(decryptedbytes);
        decryptedString = new String(decryptedbytes);
        
        System.out.println("The decrypted string is: " + decryptedString);
        
        System.out.println("The original and decrypted strings are " + (originalString.equalsIgnoreCase(decryptedString) ?
                "" : "not ") + "equal");
    }
    
}
