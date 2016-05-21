/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.math.BigInteger;

/**
 *
 * @author adria
 */
public class RSA
{
    private RSAKey key;   
    
    public RSA(RSAKey givenKey)
    {
        key = givenKey;
    }
    
    public byte[] encrypt(byte[] message)
    {
        BigInteger messageAsInteger;    // Conversion from bytes to BigInteger
        BigInteger C;                   // Encrypted message (in BigInteger form)
        
        messageAsInteger = new BigInteger(message);
        C = messageAsInteger.modPow(key.key, key.n);
        //System.out.println("Using key " + key.key.toString() + " -> C: " + C.toString());
        
        return C.toByteArray();
    }

    public byte[] decrypt(byte[] message)
    {
        return encrypt(message);
    }
}
