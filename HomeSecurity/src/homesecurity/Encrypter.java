/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

/**
 *
 * @author adria
 */
public class Encrypter
{
    RSA rsaEncrypter;
    RSAKey key;
    
    Encrypter(RSAKey encryptionKey)
    {
        key = encryptionKey;
        rsaEncrypter = new RSA(key);
    }
    
    public byte[] encrypt(byte[] data)
    {
        byte[] encryptedData, receivedData, scatteredData;
        
        receivedData = data.clone();
        
        // Insert Denilson's method here
        scatteredData = Scatterer.scatter(receivedData);
        encryptedData = rsaEncrypter.encrypt(scatteredData);
        
        return encryptedData;
    }
    
    public byte[] decrypt(byte[] data)
    {
        byte[] decryptedData, receivedData, unscatteredData;
        
        receivedData = data.clone();
        decryptedData = rsaEncrypter.decrypt(receivedData);
        unscatteredData = Scatterer.unscatter(decryptedData);
        // Insert Denilson's method here
        
        return unscatteredData;
    }
}
