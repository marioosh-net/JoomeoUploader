/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package joomeouploader;

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
      JoomeoClient client = new JoomeoClient();
      client.sessionInit("5e02578c7b2f16e90dfc5155e3f14b22", "marioosh.net", "marioosh.net@gmail.com", "zbc123");
      //System.out.println(client.getSessionId());
      //client.getFilesList();
      //client.getNumberOfFiles();
      //client.addCollection("test111454333");
      //client.getCollectionList();
      //System.out.println(client.getCollectionId("test111454333"));
      //client.uploadBinary("c:\\t.jpg", "nowa kolekcja");
      //client.uploadBinary("c:\\moje\\download\\98a962fff9.jpeg");

      
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
