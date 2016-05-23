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
    
    public static void printBits(byte[] data){
        String s = new String();
        
        for(int i=0; i<data.length; i++){
            for(int j=0; j<8; j++){
                s = s + ((data[i] >> j) & 1);
            }
        }
        System.out.println(s);
    }
    
    public static byte[] p1, p2, p3, p4;
    
    static int count = 0;
    public static boolean compareBytes(byte[] a, byte[] b)
    {
        System.out.println("Comparison #" + count++);
        
        if(a.length!=b.length){
            System.out.println("\tSizes not matching");
            return false;
        }
        
        boolean equal = true;
        for(int i=0; i<a.length; i++){
            if(a[i]!=b[i])
                equal = false;
        }
        
        System.out.println(equal ? "\tEqual" : "\tNot equal");
        return equal;
    }
    
    public byte[] encrypt(byte[] data)
    {
        byte[] encryptedData, receivedData, scatteredData, encodedData;
        
        System.out.println("Beginning encryption");
        receivedData = data.clone();
        
        encryptedData = receivedData;
        encryptedData = Huffman.Encode(encryptedData);
        encryptedData = Scatterer.scatter(encryptedData);
        encryptedData = rsaEncrypter.encrypt(encryptedData);
        
        return encryptedData;
    }
    
    public byte[] decrypt(byte[] data)
    {
        byte[] decryptedData, receivedData, unscatteredData, plainData;
        
        System.out.println("Beginning decryption");
        receivedData = data.clone();
        
        plainData = receivedData;
        plainData = rsaEncrypter.decrypt(plainData);
        plainData = Scatterer.unscatter(plainData);
        plainData = Huffman.Decode(plainData);
        
        return plainData;
    }
}
