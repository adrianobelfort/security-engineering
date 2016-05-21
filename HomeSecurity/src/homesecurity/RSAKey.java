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
    public final int KEYLENGTH;
    
    public BigInteger key;
    public BigInteger n;
    
    RSAKey(int keylength, BigInteger key, BigInteger n)
    {
        this.KEYLENGTH = keylength;
        this.key = key;
        this.n = n;
    }
}
