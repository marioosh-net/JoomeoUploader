/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package joomeouploader;

import java.io.Console;
import java.util.Properties;
import java.io.*;

/**
 *
 * @author muniek
 */
public class Uploader {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {

      if (args.length > 1) {
        String filename = args[1];
        String collectionName = args[0];

        Properties configFile = new Properties();
        configFile.load(new FileInputStream(new File("JoomeoUploader.conf")));
        String apikey = configFile.getProperty("apikey");
        String spacename = configFile.getProperty("spacename");
        String login = configFile.getProperty("login");
        String password = configFile.getProperty("password");

        JoomeoClient client = new JoomeoClient();
        client.sessionInit(apikey, spacename, login, password);

        //client.getFilesList();
        //client.getNumberOfFiles();
        //client.addCollection("test111454333");
        //client.getCollectionList();
        //System.out.println(client.getCollectionId("test111454333"));
        //client.uploadBinary("c:\\t.jpg", "nowa kolekcja");
        //client.uploadBinary("c:\\moje\\download\\98a962fff9.jpeg");
        
        for(int i=1;i<args.length; i++) {
          filename = args[i];
          boolean checkCollection = i==1 ? true : false;
          client.uploadBinary(filename, collectionName, checkCollection);
        }


      } else {

        System.out.println("syntax: java -jar JoomeoUploader.jar <collectionName> <photo.jpg>");
      }


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }
}
