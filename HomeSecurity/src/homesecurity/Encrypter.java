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
    
    // Função para imprimir na tela bits, para propósitos de debugging
    public static void printBits(byte[] data){
        String s = new String();
        
        for(int i=0; i<data.length; i++){
            for(int j=0; j<8; j++){
                s = s + ((data[i] >> j) & 1);
            }
        }
        System.out.println(s);
    }
    
    public static int count;

    // Função para propósito de comparação entre arrays de bytes
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
        // Primeiro comprimimos com Árvore de Huffman
        encryptedData = Huffman.Encode(encryptedData);
        // Depois fazemos transposição com Fibonacci
        encryptedData = Scatterer.scatter(encryptedData);
        // Depois encriptamos com RSA
        encryptedData = rsaEncrypter.encrypt(encryptedData);
        
        return encryptedData;
    }
    
    public byte[] decrypt(byte[] data) throws Exception
    {
        byte[] decryptedData, receivedData, unscatteredData, plainData;
        
        System.out.println("Beginning decryption");
        receivedData = data.clone();
        
        plainData = receivedData;
        // Primeiro decriptamos com RSA
        plainData = rsaEncrypter.decrypt(plainData);
        // Depois detranspomos com Fibonacci
        plainData = Scatterer.unscatter(plainData);
        // Depois descomprimimos com Árvore de Huffman
        plainData = Huffman.Decode(plainData);
        
        return plainData;
    }
}
