/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homesecurity;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author adria
 */
public class RSAKeyFileFilter extends FileFilter
{
    @Override
    public boolean accept(File f) {
        if(f.isDirectory()) return true;
        return f.getName().endsWith(".rkey");
    }

    @Override
    public String getDescription() {
        return "Chaves RSA (*.rkey)";
    }
}
