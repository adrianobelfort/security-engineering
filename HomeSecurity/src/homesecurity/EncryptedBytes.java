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
public class EncryptedBytes
{
    private byte[] information;

    public EncryptedBytes(byte[] information)
    {
        this.information = information;
    }
    
    public byte[] extractInformation()
    {
        return information;
    }
    
    @Override
    public String toString()
    {
        return new String(information);
    }
}
