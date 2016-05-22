/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author adria
 */
public class TextWriter
{
    public static void writeOnFile(String path, byte[] message) throws IOException
    {
        //Files.readAllBytes(Paths.get(path));]
        Files.write(Paths.get(path), message);
    }
}
