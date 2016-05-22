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
    public static void compareBytes(byte[] a, byte[] b)
    {
        System.out.println("Comparison #" + count);
        System.out.println(a.length == b.length ? "\tSizes match" : "\tNope, they dont");
        
        if(a.length!=b.length){
            return;
        }
        
        boolean equal = true;
        for(int i=0; i<a.length; i++){
            if(a[i]!=b[i])
                equal = false;
        }
        
        System.out.println(equal ? "\tEqual" : "\tNot qual");
    }
    
    public byte[] encrypt(byte[] data)
    {
        byte[] encryptedData, receivedData, scatteredData, encodedData;
        
        receivedData = data.clone();
        
        System.out.println("Beginning encryption");
        // Insert Denilson's method here
        //printBits(receivedData);
        //encodedData = Huffman.Encode(receivedData);
        //encodedData =  receivedData;
        //printBits(encodedData);
        //scatteredData = Scatterer.scatter(encodedData);
        //printBits(scatteredData);
        //encryptedData = rsaEncrypter.encrypt(scatteredData);
        //printBits(encryptedData);
        
        /*encryptedData = receivedData;
        p1 = encryptedData;
        encryptedData = rsaEncrypter.encrypt(encryptedData);
        p2 = encryptedData;
        encryptedData = Scatterer.scatter(encryptedData);
        p3 = encryptedData;
        encryptedData = Huffman.Encode(encryptedData);
        p4 = encryptedData;*/
        
        encryptedData = Huffman.Encode(data);
        encryptedData = Scatterer.scatter(encryptedData);
        encryptedData = rsaEncrypter.encrypt(encryptedData);
        
        //encryptedData = Huffman.Encode(receivedData);
        
        return encryptedData;
    }
    
    public byte[] decrypt(byte[] data)
    {
        byte[] decryptedData, receivedData, unscatteredData, plainData;
        
        System.out.println("Beginning decryption");
        receivedData = data.clone();
        //printBits(receivedData);
        //decryptedData = rsaEncrypter.decrypt(receivedData);
        //printBits(decryptedData);
        //unscatteredData = Scatterer.unscatter(decryptedData);
        //printBits(unscatteredData);
        //plainData = Huffman.Decode(unscatteredData);
        //plainData = unscatteredData;
        //printBits(plainData);
        
        /*plainData = receivedData;
        compareBytes(plainData, p4);
        plainData = Huffman.Decode(plainData);
        compareBytes(plainData, p3);
        plainData = Scatterer.unscatter(plainData);
        compareBytes(plainData, p2);
        plainData = rsaEncrypter.decrypt(plainData);
        compareBytes(plainData, p1);*/
        
        plainData = rsaEncrypter.decrypt(data);
        plainData = Scatterer.unscatter(plainData);
        plainData = Huffman.Decode(plainData);
        
        return plainData;
    }
}
