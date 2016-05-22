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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author adriano
 */

public class TextReader
{
    public static byte[] readFile(String path) throws IOException
    {
        byte[] fileContent = Files.readAllBytes(Paths.get(path));
        return fileContent;
    }
}
