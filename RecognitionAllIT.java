package net.sf.javaanpr.test;

import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ismailcam on 07/03/2017.
 */

@RunWith( Parameterized.class )
public class RecognitionAllIT
{

    private File plateFile;
    private String plateExpected;
    private CarSnapshot carSnap;
    private Intelligence intelligence;

    public RecognitionAllIT(File plateFile, String plateExpected)
    {
        this.plateFile = plateFile;
        this.plateExpected = plateExpected;
    }


    @Before
    public void init() throws IOException, ParserConfigurationException, SAXException
    {
        intelligence = new Intelligence();
        carSnap = new CarSnapshot( new FileInputStream( plateFile ) );
    }


    @Parameterized.Parameters
    public static Collection< Object[] > testDataSet() throws IOException
    {
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream( new File( resultsPath ) );

        Properties properties = new Properties();
        properties.load( resultsStream );
        resultsStream.close();

        File snapshotDir = new File( snapshotDirPath );
        File[] snapshots = snapshotDir.listFiles();


        Collection< Object[] > dataForOneImage = new ArrayList();
        for( File file : snapshots )
        {
            String name = file.getName();
            String plateExpected = properties.getProperty( name );
            dataForOneImage.add( new Object[]{ file, plateExpected } );
        }
        return dataForOneImage;
    }

    @Test
    public void testAllPlates() throws Exception
    {
        String numberPlate = intelligence.recognize( carSnap, false );
        assertThat( numberPlate, equalTo( plateExpected ) );

    }
}
