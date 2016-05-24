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
    
    public static byte[] concat0(byte b){
        byte[] c = new byte[1];
        c[0] = b;

        return concat1((byte)0, c);
    }
    
    public byte[] encrypt(byte[] message)
    {
        // Calculates the amount of bits necessary to represent n
        float bytesRequired = ((float)(key.n.bitLength())/8.0f);
        
        // Calculates the size of the message and cipher segment
        int segmentMessage = (int) Math.floor(bytesRequired-1);
        int segmentCipher = (int) Math.ceil(bytesRequired);
        
        // Calculates the amount of segments needed
        int segmentAmount = (int) Math.ceil((float)message.length/segmentMessage);
        
        // Alocates a Vector to function as a segment buffer
        Vector segmentBuffer = new Vector();

        // Adds to the buffer the amount of bytes at the last segment
        segmentBuffer.add((byte)(message.length%segmentMessage));
        
        for(int i=0; i<segmentAmount; i++){
            // Calculates the number corresponding to segment i
            BigInteger segmentAsNumber = new BigInteger(
                concat0(Arrays.copyOfRange(message, i*segmentMessage, (i+1)*segmentMessage))
                );
            
            // Cryptographs the segment and converts to a byte array
            BigInteger segmentAsCipher = segmentAsNumber.modPow(key.key, key.n);
            byte[] segmentBytes = segmentAsCipher.toByteArray();
            

            // Transposes bytes to the buffer
            int blankBytes;
            for(blankBytes=0; segmentBytes[blankBytes]==(byte)0; blankBytes++);
            
            for(int j=0; j<(segmentCipher-(segmentBytes.length-blankBytes)); j++){
                segmentBuffer.add((byte)0);
            }
            for(int j=blankBytes; j<segmentBytes.length; j++){
                segmentBuffer.add(segmentBytes[j]);
            }
        }
        
        // Returns value
        byte[] returnArray = new byte[segmentCipher*segmentAmount+1];
        for(int i=0; i<returnArray.length; i++){
            returnArray[i] = (byte)segmentBuffer.get(i);
        }
        
        return returnArray;
    }

    public byte[] decrypt(byte[] message)
    {
        // Calculates the amount of bytes required to represent n
        float bytesRequired = ((float)(key.n.bitLength())/8.0f);
        
        // Calculates size of message and cipher segment
        int segmentMessage = (int) Math.floor(bytesRequired-1);
        int segmentCipher = (int) Math.ceil(bytesRequired);
        
        // Retrieves amount of bytes at the last segment
        int lengthMod = (new BigInteger(concat0(message[0]))).intValue();
        message = Arrays.copyOfRange(message, 1, message.length);
        
        // Calculates the amount of cipher segments
        int segmentAmount = (int) Math.ceil((float)message.length/segmentCipher);
        
        Vector segmentBuffer = new Vector();
        
        for(int i=0; i<segmentAmount; i++){
            // Retrieves the number that representes the cryptographed segment
            BigInteger segmentAsNumber = new BigInteger(
                concat0(Arrays.copyOfRange(message, i*segmentCipher, (i+1)*segmentCipher))
                );
            
            // Decryptographs the segment and saves it as a byte array
            BigInteger segmentAsMessage = segmentAsNumber.modPow(key.key, key.n);
            byte[] segmentBytes = segmentAsMessage.toByteArray();
            
            // Transposes the resulting bytes to the byte buffer
            int blankBytes;
            for(blankBytes=0; segmentBytes[blankBytes]==(byte)0; blankBytes++);
            
            for(int j=0; j<(segmentMessage-segmentBytes.length+blankBytes); j++){
                segmentBuffer.add((byte)0);
            }
            for(int j=blankBytes; j<segmentBytes.length; j++){
                segmentBuffer.add(segmentBytes[j]);
            }
        }
        
        // Concatenates bytes for returning
        byte[] returnArray = new byte[segmentMessage*segmentAmount-(segmentMessage-lengthMod)];
        for(int i=0; i<returnArray.length; i++){
            returnArray[i] = (byte)segmentBuffer.get(i);
        }
        
        //Encrypter.printBits(returnArray);
        
        return returnArray;
    }
}
