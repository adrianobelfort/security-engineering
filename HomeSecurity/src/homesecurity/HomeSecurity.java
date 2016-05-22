/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.util.ArrayList;
import java.util.Map;
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
        Scanner scanner = new Scanner(System.in);
        String originalString, decryptedString;
        byte[] stringInBytes;
        
        keys = KeyFactory.generatePair();
        
        //System.out.println("Private key: " + keys[PRIVATEKEY].key.toString() + " (n = " + keys[PRIVATEKEY].n + ")");
        //System.out.println("Public key: " + keys[PUBLICKEY].key.toString() + " (n = " + keys[PUBLICKEY].n + ")");
        
        Encrypter rsaEncrypter = new Encrypter(keys[PRIVATEKEY]);
        Encrypter rsaDecrypter = new Encrypter(keys[PUBLICKEY]);
        
        rsaEncryption = new RSA(keys[PRIVATEKEY]);
        rsaDecryption = new RSA(keys[PUBLICKEY]);     
        
        // ENCRYPTION
        System.out.print("Enter a string to encrypt: ");
        originalString = scanner.nextLine();
        
        System.out.println("Encrypting " + originalString + " ...");
        //encryptedMessage = new EncryptedBytes(rsaEncryption.encrypt(originalString.getBytes()));
        stringInBytes = originalString.getBytes();
        encryptedbytes = rsaEncrypter.encrypt(stringInBytes);
        /*stringInBytes = Scatterer.scatter(stringInBytes);
        encryptedbytes = rsaEncryption.encrypt(stringInBytes);
        System.out.println("");*/
        
        //System.out.println("The encrypted string is " + convertToString(encryptedMessage.extractInformation()));
        
        System.out.println("Decrypting the encrypted string...");
        //decryptedbytes = rsaDecryption.decrypt(encryptedMessage.extractInformation());
        /*decryptedbytes = rsaDecryption.decrypt(encryptedbytes);
        System.out.println("Scattered, decrypted message: " + new String(decryptedbytes));
        decryptedbytes = Scatterer.unscatter(decryptedbytes);*/
        decryptedbytes = rsaDecrypter.decrypt(encryptedbytes);
        decryptedString = new String(decryptedbytes);
        
        System.out.println("");
        System.out.println("The decrypted string is: " + decryptedString);
        
        System.out.println("The original and decrypted strings are " + (originalString.equalsIgnoreCase(decryptedString) ?
                "" : "not ") + "equal");
    }
    
}
