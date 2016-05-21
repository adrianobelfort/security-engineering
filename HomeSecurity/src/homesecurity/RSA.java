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
    private int keyLength;
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
        
        return C.toByteArray();
    }
    
    public byte[] decrypt(byte[] message)
    {
        return encrypt(message);
    }
}
