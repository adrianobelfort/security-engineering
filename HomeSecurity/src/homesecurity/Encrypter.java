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
        
        /*RSAKey[] keys = KeyFactory.generatePair();
        RSA rsaDecrypter = new RSA(keys[0]);
        rsaEncrypter = new RSA(keys[1]);
        rsaEncrypter.otherKey = keys[0].key;*/
        
        encryptedData = receivedData;
        p1 = encryptedData;
        encryptedData = Huffman.Encode(encryptedData);
        p2 = encryptedData;
        encryptedData = Scatterer.scatter(encryptedData);
        p3 = encryptedData;
        encryptedData = rsaEncrypter.encrypt(encryptedData);
        p4 = encryptedData;
        
        
        //compareBytes(rsaDecrypter.decrypt(encryptedData), p3);
        
        /*boolean worked = true;
        encryptedData = receivedData;
        
        int i, last = -1;
        for(i=0; i<1000; i++){
            System.out.println("Test " + i);
            RSAKey[] keys = KeyFactory.generatePair();
            RSA rsaDecrypter = new RSA(keys[0]);
            rsaEncrypter = new RSA(keys[1]);
            rsaEncrypter.otherKey = keys[0].key;

            encryptedData = receivedData;
            p1 = encryptedData;
            encryptedData = Huffman.Encode(encryptedData);
            p2 = encryptedData;
            encryptedData = Scatterer.scatter(encryptedData);
            p3 = encryptedData;
            encryptedData = rsaEncrypter.encrypt(encryptedData);
            p4 = encryptedData;

            if(!compareBytes(rsaDecrypter.decrypt(encryptedData), p3)){
                worked = false;
                last=i;
                i=1000;
            }
        }
        
        System.out.println("STATUS(" + last + "): " + worked);*/
        
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
        
        plainData = receivedData;
        compareBytes(plainData, p4);
        plainData = rsaEncrypter.decrypt(plainData);
        compareBytes(plainData, p3);
        plainData = Scatterer.unscatter(plainData);
        compareBytes(plainData, p2);
        plainData = Huffman.Decode(plainData);
        compareBytes(plainData, p1);
        
        /*plainData = receivedData;
        plainData = rsaEncrypter.decrypt(plainData);
        plainData = Scatterer.unscatter(plainData);
        plainData = Huffman.Decode(plainData);*/
        
        return plainData;
    }
}
