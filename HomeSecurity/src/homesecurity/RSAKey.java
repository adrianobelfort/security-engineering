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
public class RSAKey
{    
    public BigInteger key;
    public BigInteger n;
    
    public BigInteger phi;
    
    RSAKey(BigInteger key, BigInteger n)
    {
        this.key = key;
        this.n = n;
    }
    
    @Override
    public String toString()
    {
        return new String("[Key length: " + key.toString().length() + "\nKey = " + key.toString() + "\nN = " + n.toString() + "]");
    }
            
}
