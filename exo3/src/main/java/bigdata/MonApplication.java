//=====================================================================
/**
 * Squelette minimal d'une application Hadoop
 * A exporter dans un jar sans les librairies externes
 * A exécuter avec la commande ./hadoop jar NOMDUFICHER.jar ARGUMENTS....
 */
    package bigdata;
import java.net.URI;
import java.io.*;
import java.lang.StringBuilder;
import java.nio.charset.StandardCharsets;
import java.lang.Integer;
import java.util.Random;
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
            int size = Integer.parseInt(args[0]);
            URI uri = new URI(args[1]);
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
            //open a imput stream
            for(int i = 0 ; i < size; i++){
                InputStream is = new ByteArrayInputStream(randomWord(10).getBytes(StandardCharsets.UTF_8));
                IOUtils.copyBytes(is,os,conf,false);
                is.close();
            }
            //copy the intout in to the output using the conf format

            //close the streams
            os.close();

            return 0;
        }
    }
    public static void main( String[] args ) throws Exception {
        int returnCode = ToolRunner.run(new MonApplication.MonProg(), args);
        System.exit(returnCode);
    }
    public static char getRandom(char[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static String randomSyllable(){
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        return  new StringBuilder().append(getRandom(alphabet)).append(getRandom(alphabet)).toString();
    }

    public static String randomWord(int size){
        String exampleString = "";
        for(int i=0 ; i < size; i++){
            exampleString += randomSyllable();
        }
        return exampleString + " ";
     }

}
//=====================================================================

