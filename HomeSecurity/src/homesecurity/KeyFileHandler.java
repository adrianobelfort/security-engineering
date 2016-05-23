/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;

/**
 *
 * @author adria
 */
public class KeyFileHandler
{
    public static RSAKey readKey(String path) throws FileNotFoundException, NumberFormatException
    {
        Scanner scanner;
        String key, n;
        RSAKey keyRead;
        BigInteger integerKey, integerN;
        
        scanner = new Scanner(new FileReader(path));

        key = scanner.nextLine();
        n = scanner.nextLine();

        integerKey = new BigInteger(key);
        integerN = new BigInteger(n);
        
        keyRead = new RSAKey(integerKey, integerN);
        
        return keyRead;
    }
    
    public static void saveKey(RSAKey key, String path) throws FileNotFoundException
    {
        try (PrintWriter writer = new PrintWriter(path))
        {
            String keyString, nString;
            keyString = key.key.toString();
            nString = key.n.toString();
            writer.println(keyString);
            writer.println(nString);
        }
    }
}
