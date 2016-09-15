//=====================================================================
/**
 * Squelette minimal d'une application Hadoop
 * A exporter dans un jar sans les librairies externes
 * A exécuter avec la commande ./hadoop jar NOMDUFICHER.jar ARGUMENTS....
 */
    package bigdata;
import java.net.URI;
import java.io.*;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class MonApplication {
    public static class MonProg extends Configured implements Tool {
        public int run(String[] args) throws Exception {
            //create the URI object with the remote path
            URI uri = new URI(args[args.length -1]);
            //normilize the path
            uri = uri.normalize();
            //create the configuration object
            Configuration conf = getConf();
            //create de object filesystem
            FileSystem fs = FileSystem.get(uri, conf, "wvervale");
            //create the output path from the URI
            Path outPut = new Path(uri.getPath());
            //open a out put stream
            OutputStream os = fs.create(outPut);
            for(int x = 0 ; x < args.length -1; ++x){
                //open a imput stream
                InputStream is = new BufferedInputStream( new FileInputStream (args[x]));

                //copy the intout in to the output using the conf format
                IOUtils.copyBytes(is,os,conf,false);

                is.close();
            }

            //close the streams
            os.close();

            return 0;
        }
    }
    public static void main( String[] args ) throws Exception {
        int returnCode = ToolRunner.run(new MonApplication.MonProg(), args);
        System.exit(returnCode);
    }
}
//=====================================================================

