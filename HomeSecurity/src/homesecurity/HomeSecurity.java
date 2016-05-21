/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.util.Scanner;

/**
 *
 * @author adria
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
        String originalString, encryptedString, decryptedString;
        
        keys = KeyFactory.generatePair();
        
        System.out.println("Private key: " + keys[PRIVATEKEY].key.toString() + " (n = " + keys[PRIVATEKEY].n + ")");
        System.out.println("Public key: " + keys[PUBLICKEY].key.toString() + " (n = " + keys[PUBLICKEY].n + ")");
        
        rsaEncryption = new RSA(keys[PRIVATEKEY]);
        rsaDecryption = new RSA(keys[PUBLICKEY]);
        
        System.out.print("Enter a string to encrypt: ");
        originalString = scanner.nextLine();
        
        System.out.println("Encrypting " + originalString + " ...");
        System.out.println("Its byte representation is " + convertToString(originalString.getBytes()));
        encryptedbytes = rsaEncryption.encrypt(originalString.getBytes());
        
        System.out.println("The encrypted string is " + convertToString(encryptedbytes));
        
        System.out.println("Decrypting the encrypted string...");
        decryptedbytes = rsaDecryption.decrypt(encryptedbytes);
        
        System.out.println("The decrypted string is: " + new String(decryptedbytes));
    }
    
}
