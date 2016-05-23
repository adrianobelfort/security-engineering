/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.util.*;
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
    
    public static BigInteger p1, p2;
    
    public static byte[] b1, b2;
    
    public BigInteger otherKey;
    
    public static byte[] concat(byte[] a, byte[] b){
        byte[] ret = new byte[a.length+b.length];
        for(int i=0; i<(a.length+b.length); i++){
            ret[i] = (i < a.length) ? a[i] : b[i-a.length];
        }
        
        return ret;
    }
    
    public static byte[] concat1(byte a, byte[] b){
        byte[] c = new byte[1];
        c[0] = a;
        
        return concat(c, b);
    }
    
    public static byte[] concat0(byte[] b){
        return concat1((byte)0, b);
    }
    
    public byte[] encrypt(byte[] message)
    {
        float bytesRequired = ((float)(key.n.bitLength())/8.0f);
        
        int segmentMessage = (int) Math.floor(bytesRequired-1);
        int segmentCipher = (int) Math.ceil(bytesRequired);
        
        int segmentAmount = (int) Math.ceil((float)message.length/segmentMessage);
        
        Vector segmentBuffer = new Vector();
        segmentBuffer.add((byte)(message.length%segmentMessage));
        
        Encrypter.printBits(message);
        
        //System.out.println(segmentMessage + " / " + segmentCipher + " / byte: " + message.length);
        
        for(int i=0; i<segmentAmount; i++){
            BigInteger segmentAsNumber = new BigInteger(
                //Arrays.copyOfRange(message, i*segmentMessage, Math.min((i+1)*segmentCipher, message.length))
                concat0(Arrays.copyOfRange(message, i*segmentMessage, (i+1)*segmentMessage))
                );
            
            b1 = Arrays.copyOfRange(message, i*segmentMessage, (i+1)*segmentMessage);
            p1 = segmentAsNumber;
            
            BigInteger segmentAsCipher = segmentAsNumber.modPow(key.key, key.n);
            byte[] segmentBytes = segmentAsCipher.toByteArray();
            
            
            
            int blankBytes;
            for(blankBytes=0; segmentBytes[blankBytes]==(byte)0; blankBytes++);
            
            /*for(int j=0; j<(segmentCipher-segmentBytes.length-blankBytes); j++){
                segmentBuffer.add((byte)0);
            }
            for(int j=blankBytes; j<segmentBytes.length; j++){
                segmentBuffer.add(segmentBytes[j]);
            }*/
            
            for(int j=0; j<(segmentCipher-(segmentBytes.length-blankBytes)); j++){
                segmentBuffer.add((byte)0);
            }
            for(int j=blankBytes; j<segmentBytes.length; j++){
                segmentBuffer.add(segmentBytes[j]);
            }
            
            /*System.out.println(
                (segmentCipher-segmentBytes.length) + " + " +
                segmentBytes.length + " = " +
                segmentCipher
                );*/
        
            //BigInteger g;
            //g = new BigInteger("5352345");

            //System.out.println("Hai!" + g.modPow(key.key, key.n).modPow(otherKey, key.n) + " / " + otherKey.multiply(key.key).mod(key.phi));
            
            //System.out.println(segmentAsNumber.equals(segmentAsCipher.modPow(otherKey, key.n)));
            //System.out.println(segmentAsNumber.equals(segmentAsNumber.modPow(key.key, key.n).modPow(otherKey, key.n)));
            //System.out.println(segmentAsNumber);
            //System.out.println(segmentAsNumber.modPow(key.key, key.n).modPow(otherKey, key.n));
            /*for(int j=0; j<segmentCipher; j++){
                segmentBuffer.add(
                        (j<(segmentCipher-segmentBytes.length)) ? (byte)0 : segmentBytes[j-segmentCipher+segmentBytes.length]
                        //(j<segmentBytes.length) ? segmentBytes[j] : (byte)0
                        //segmentBytes[j]
                    );
                    
            }*/
            
            p2 = segmentAsCipher;
            b2 = Arrays.copyOfRange(
                Huffman.vectorToByte(segmentBuffer),
                segmentBuffer.size()-segmentCipher,
                segmentBuffer.size()
                );
           
            System.out.println("TEST: " +
                    segmentAsCipher.equals(
                    new BigInteger(
                        Arrays.copyOfRange(
                            Huffman.vectorToByte(segmentBuffer),
                            segmentBuffer.size()-segmentCipher,
                            segmentBuffer.size()
                            )
                        )
                    )
                );
            
            //System.out.println("seg: " + segmentAsNumber);
        }
        
        /*{
            BigInteger a, b;
            a = new BigInteger("43243242342");
            Vector v = new Vector();
            for(int i=0; i<a.toByteArray().length; i++){
                v.add((byte)a.toByteArray()[i]);
            }
            v.add((byte)0);
            v.add((byte)0);
            System.out.println("TEST2: " + 
                    (a.equals( 
                    new BigInteger(
                            Huffman.vectorToByte(v)
                            //a.toByteArray()
                        )
                    )));
        }*/
        
        byte[] returnArray = new byte[segmentCipher*segmentAmount+1];
        for(int i=0; i<returnArray.length; i++){
            returnArray[i] = (byte)segmentBuffer.get(i);
        }
        
        return returnArray;
        
        /*BigInteger messageAsInteger;    // Conversion from bytes to BigInteger
        BigInteger C;                   // Encrypted message (in BigInteger form)
        
        messageAsInteger = new BigInteger(message);
        C = messageAsInteger.modPow(key.key, key.n); 
        //System.out.println("Using key " + key.key.toString() + " -> C: " + C.toString());
        
        return C.toByteArray();*/
    }

    public byte[] decrypt(byte[] message)
    {
        //System.out.println("dKey: " + key.n + " / " + key.key);
        float bytesRequired = ((float)(key.n.bitLength())/8.0f);
        
        int segmentMessage = (int) Math.floor(bytesRequired-1);
        int segmentCipher = (int) Math.ceil(bytesRequired);
        
        int lengthMod = (int)message[0];
        message = Arrays.copyOfRange(message, 1, message.length);
        
        
        int segmentAmount = (int) Math.ceil((float)message.length/segmentCipher);
        
        Vector segmentBuffer = new Vector();
        
        for(int i=0; i<segmentAmount; i++){
            BigInteger segmentAsNumber = new BigInteger(
                //Arrays.copyOfRange(message, i*segmentCipher, (i+1)*segmentCipher)
                concat0(Arrays.copyOfRange(message, i*segmentCipher, (i+1)*segmentCipher))
                );
            
            BigInteger segmentAsMessage = segmentAsNumber.modPow(key.key, key.n);
            byte[] segmentBytes = segmentAsMessage.toByteArray();
            
            int blankBytes;
            for(blankBytes=0; segmentBytes[blankBytes]==(byte)0; blankBytes++);
            
            for(int j=0; j<(segmentMessage-segmentBytes.length+blankBytes); j++){
                segmentBuffer.add((byte)0);
            }
            for(int j=blankBytes; j<segmentBytes.length; j++){
                segmentBuffer.add(segmentBytes[j]);
            }
        }
        
        byte[] returnArray = new byte[segmentMessage*segmentAmount-(segmentMessage-lengthMod)];
        for(int i=0; i<returnArray.length; i++){
            returnArray[i] = (byte)segmentBuffer.get(i);
        }
        
        Encrypter.printBits(returnArray);
        
        return returnArray;
    }
}
