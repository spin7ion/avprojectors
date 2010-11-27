/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package avprojector.controller;

import avprojector.SpinnerEditor;
import avprojector.model.AVProjectorParser;
import avprojector.model.AVProjector;
import avprojector.controller.AVProjectorTableModel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import com.apple.eawt.Application;
import java.security.MessageDigest;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import pjLink.PJLinkC1;

/**
 *
 * @author Long
 */
public class AVProjectorController {

    private static Semaphore                sProjTableModelMutex;
    public static ArrayList<AVProjector>    sProjList;
    public static String                    sFilepath = "/Users/Long/Desktop/projList.txt";
    public static int                       sColumnCount = 4;
    public static AVProjectorTableModel     sProjTableModel;

    public static void Initialize()
    {
        PJLinkC1.IntializeCommands();
        Parse();
        sProjTableModelMutex = new Semaphore(1, true);
        sProjTableModel = new AVProjectorTableModel( GetModelData() );
        CheckAllStatus();
    }

    public static void SetProjModelValueAt( String data, int row, int column )
    {
        try {
            sProjTableModelMutex.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(AVProjectorController.class.getName()).log(Level.SEVERE, null, ex);
            sProjTableModelMutex.release();
        }
        sProjTableModel.setValueAt(data, row, column);
        sProjTableModelMutex.release();
        return;
    }

    // parses a tab delimited file with ipAddress, hostname, projector name
    public static ArrayList<AVProjector> Parse( )
    {
        sProjList = AVProjectorParser.Parse( sFilepath );
        return sProjList;
    }

    public static Object[][] GetModelData()
    {
        Object[][] data = new Object[sProjList.size()][sColumnCount];
        for( int i = 0; i < sProjList.size(); i++ )
        {
            data[i][0] = sProjList.get(i).GetProjName();
            data[i][1] = "Unknown";
            data[i][2] = "Unknown";

        }

        return data;
    }

    public static void CheckAllStatus( )
    {

        for( int i = 0; i < sProjList.size(); i++ )
        {
            sProjList.get(i).CheckPower(i, 1);
            sProjList.get(i).CheckInput(i, 2);

        }
        Application app = Application.getApplication();
        app.requestUserAttention( true );

    }

}
