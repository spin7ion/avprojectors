/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pjLink;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class PJLinkC1 {

    enum CommandType
    {
        Power,
        Input
    }

    private static final int    CLASS_NUMBER    = 1;
    private static final int    TIMEOUT         = 3000;
    private static final int    PasswordIndex   = 2;
    private static final String MD5             = "MD5";
    protected static boolean    Initialized     = false;

    protected static final String HEADER_PREFIX     = "%";
    protected static final String POWER             = "POWR";
    protected static final String INPUT             = "INPT";

    static HashMap PowerStatus;
    static HashMap PowerCommands;
    static HashMap PowerErrorCodes;
    
    static HashMap InputStatus;
    static HashMap InputCommands;
    static HashMap InputErrorCodes;

    public static void IntializeCommands()
    {

        if( Initialized )
        {
            return;
        }
        //Power commands/status
        PowerStatus = new HashMap();
        PowerStatus.put( "0", "Off");
        PowerStatus.put( "1", "On");
        PowerStatus.put( "2", "Cooling");
        PowerStatus.put( "3", "Warmup");

        PowerCommands = new HashMap();
        PowerCommands.put( "Off", "0");
        PowerCommands.put( "On", "1");
        PowerCommands.put( "Status", "?");

        PowerErrorCodes = new HashMap();
        PowerErrorCodes.put( "ERR2", "Parameter out of range");
        PowerErrorCodes.put( "ERR3", "Projector not on");
        PowerErrorCodes.put( "ERR4", "Projector error");

        //Input
        InputStatus = new HashMap();
        InputStatus.put( "21", "Video");
        InputStatus.put( "22", "S-Video");
        InputStatus.put( "31", "Input A");
        InputStatus.put( "32", "Input B");
        InputStatus.put( "33", "Input C");
        InputStatus.put( "34", "Input D");
        InputStatus.put( "35", "Input E");
        InputStatus.put( "36", "Input F");
        InputStatus.put( "51", "Network");

        InputCommands = new HashMap();
        InputCommands.put( "Video",   "21");
        InputCommands.put( "Input A", "31");
        InputCommands.put( "Status", "?" );

        InputErrorCodes = new HashMap();
        InputErrorCodes.put("ERR2", "Non-existent input");
        InputErrorCodes.put("ERR3", "Projector not on");
        InputErrorCodes.put("ERR4", "Projector error");

        Initialized = true;
    }

    public static String GetPasswordDigest( String password, String randomString )
    {
        String pwDigest = "";
        try
        {
            MessageDigest digest = MessageDigest.getInstance( MD5 );

            String passwordString = randomString + password;

            digest.update(passwordString.getBytes());
            byte[] bytes = digest.digest();
            BigInteger bi = new BigInteger(1, bytes);
            pwDigest = String.format("%0" + (bytes.length << 1) + "x", bi);
        }

        catch (NoSuchAlgorithmException e)
        {
            //assert
            pwDigest = "NO SUCH ALGORITHM";
        }

        return pwDigest;

    }

    public static void CheckPower( InetAddress projIP, int pjLinkPort, String password, int row, int column )
    {

        String query = CreateQuery( CLASS_NUMBER, POWER, (String)PowerCommands.get("Status") );

        //connect to the server

        PJLinkQueryThread pjQuery = new PJLinkQueryThread( CommandType.Power, query, password, projIP, pjLinkPort, row, column );
        pjQuery.start();

        return;
    }

    public static void CheckInput( InetAddress projIP, int pjLinkPort, String password, int row, int column )
    {
        String query = CreateQuery( CLASS_NUMBER, INPUT, (String)InputCommands.get("Status") );

        //connect to the server

        PJLinkQueryThread pjQuery = new PJLinkQueryThread( CommandType.Input, query, password, projIP, pjLinkPort, row, column );
        pjQuery.start();

        return;
    }

    public static String CreateQuery( int classNumber, String command, String parameter )
    {
        return HEADER_PREFIX + Integer.toString(classNumber) + command + " " + parameter + "\r";
    }

}
