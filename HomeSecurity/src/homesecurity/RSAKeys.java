/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author adria
 */
public class RSAKeys
{
    private static final int KEYLENGTH = 512;
    
    public BigInteger e;
    public BigInteger d;
    public BigInteger n;
    
    public static RSAKey[] generate()
    {
        RSAKey keys[] = new RSAKey[2];
        
        Random random = new Random();
        
        /*  First we calculate two large primes p and q of the size of the key length */
        BigInteger p = BigInteger.probablePrime(KEYLENGTH, random);
        BigInteger q = BigInteger.probablePrime(KEYLENGTH, random);
        
        /*  Then we calculate n, which is the multiplication of p and q */
        BigInteger n = p.multiply(q);
        
        /*  Phi is an auxiliar number to find the exponent e */
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        
        /*  The exponent of the RSA method is calculated by finding 
            a number which is relative to phi, so we attempt to find
            such number, e 
        */
        BigInteger e = BigInteger.probablePrime(KEYLENGTH / 2, random);
        
        /*  A number is a relative prime of another if their greatest
            common divisor is one. So we iteratively add one until 
            gcd(phi,e) = 1
        */
        while(e.gcd(phi).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        
        /*  After finding e, we then take the inverse of e mod phi */
        BigInteger d = e.modInverse(phi);
        
        /*  The first element of the array is the private key; the second is
            the public key
        */
        keys[0] = new RSAKey(KEYLENGTH, e, n);
        keys[1] = new RSAKey(KEYLENGTH, d, n);
        
        return keys;
    }
}
