package joomeouploader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClient;
import java.net.URL;
import java.util.*;
import java.io.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

/**
 *
 * @author muniek
 */
public class JoomeoClient {

  private String sessionId;
  private String apikey;
  private XmlRpcClient client;

  JoomeoClient() {
    try {
      XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
      config.setServerURL(new java.net.URL("http://api.joomeo.com/xmlrpc.php"));
      client = new XmlRpcClient();
      //client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
      client.setConfig(config);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void sessionInit(String apikey, String spacename, String login, String password) {

    HashMap p = new HashMap();
    p.put("apikey", apikey);
    p.put("spacename", spacename);
    p.put("login", login);
    p.put("password", password);
    Map params2[] = new Map[1];
    params2[0] = p;

    this.apikey = apikey;

    try {
      HashMap result = (HashMap) client.execute("joomeo.session.init", params2);
      this.sessionId = (String) result.get("sessionid");

    /*Iterator j = result.keySet().iterator();
    while(j.hasNext()) {
    Object key = j.next();
    Object value = result.get(key);
    System.out.println(key + "," + value);
    }*/

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  String getSessionId() {
    return this.sessionId;
  }

  String getCollectionId(String collectionName) {
    HashMap p = new HashMap();
    p.put("apikey", this.apikey);
    p.put("sessionid", this.sessionId);
    Map params2[] = new Map[1];
    params2[0] = p;

    try {
      Object[] collections = (Object[]) client.execute("joomeo.user.getCollectionList", params2);

      for (int i = 0; i < collections.length; i++) {
        HashMap collection = (HashMap) (collections[i]);

        System.out.println(collection.get("label"));
        if (collection.get("label").equals(collectionName) ) {
          return (String) collection.get("collectionid");
        }

      }
      return null;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  void getCollectionList() {
    HashMap p = new HashMap();
    p.put("apikey", this.apikey);
    p.put("sessionid", this.sessionId);
    Map params2[] = new Map[1];
    params2[0] = p;

    try {
      Object[] collections = (Object[]) client.execute("joomeo.user.getCollectionList", params2);

      for (int i = 0; i < collections.length; i++) {
        HashMap collection = (HashMap) (collections[i]);
        //{allowdownload=1, folderid=, orderby=date, label=nowa kolekcja, allowupload=0, createddate=1240402322, collectionid=OTdlYTc3NjAAC%2FeIOBrZRQ%3D%3D%0D%0A, public=0, date=1240402322}
        System.out.println("\""+collection.get("label") + "\" : " + collection.get("collectionid"));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void getFilesList() {
    HashMap p = new HashMap();
    p.put("apikey", this.apikey);
    p.put("sessionid", this.sessionId);
    Map params2[] = new Map[1];
    params2[0] = p;

    try {
      Object[] images = (Object[]) client.execute("joomeo.user.getFilesList", params2);

      for (int i = 0; i < images.length; i++) {
        HashMap image;
        image = (HashMap) (images[i]);
        //{date_creation=1.240132624E12, allowdownload=1, height=1224, rotation=0, width=1632, filename=dsc01324.jpg, collectionid=ZDkyMGY4YjLFANZEkD6KNQ%3D%3D%0D%0A, rating=0, fileid=ZDkyMGY4YjI3YKL4GGxUZw%3D%3D%0D%0A, date_shooting=1.204273416E12, type_mime=image/jpeg, size=301889}
        System.out.println(image.get("filename"));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void getNumberOfFiles() {
    HashMap p = new HashMap();
    p.put("apikey", this.apikey);
    p.put("sessionid", this.sessionId);
    Map params2[] = new Map[1];
    params2[0] = p;

    try {
      HashMap result = (HashMap) client.execute("joomeo.user.getNumberOfFiles", params2);
      System.out.println(result.get("nbfiles"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  String addCollection(String label) {
    HashMap p = new HashMap();
    p.put("apikey", this.apikey);
    p.put("sessionid", this.sessionId);
    p.put("label", label);
    Map params2[] = new Map[1];
    params2[0] = p;

    try {
      HashMap result = (HashMap) client.execute("joomeo.user.addCollection", params2);
      //System.out.println("NEW COLLECTION ID = " + result.get("collectionid"));
      return (String) result.get("collectionid");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private void copy(InputStream in, OutputStream out) throws IOException {
    byte[] barr = new byte[1024];
    while (true) {
      int r = in.read(barr);
      if (r <= 0) {
        break;
      }
      out.write(barr, 0, r);
    }
  }

  private byte[] loadFile(File file) throws IOException {
    InputStream in = new FileInputStream(file);
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      copy(in, buffer);
      return buffer.toByteArray();
    } finally {
      in.close();
    }
  }

  void uploadBinary(String path, String collectionName) {
    try {

      File file = new File(path);
      byte[] bytes = this.loadFile(file);
      byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
      String filename = file.getName();
      FileInputStream in = new FileInputStream(file);
      int size = in.available();

      HashMap p = new HashMap();
      p.put("apikey", this.apikey);
      p.put("sessionid", this.sessionId);
      p.put("data", bytes);
      p.put("filename", filename);
      p.put("size", size);
      Map params2[] = new Map[1];
      params2[0] = p;

      //System.out.println(filename);
      //System.out.println("DATA:\n" + data + ":DATA\n");
      HashMap result = (HashMap) client.execute("joomeo.user.uploadBinary", params2);
      //System.out.println("NEW UPLOAD ID = " + result.get("uploadid"));
      String uploadId = (String) result.get("uploadid");

      if (uploadId != null) {

        String collectionId = this.addCollection(collectionName);

        if (collectionId != null) {
          // teraz save pliku
          HashMap p1 = new HashMap();
          p1.put("apikey", this.apikey);
          p1.put("sessionid", this.sessionId);
          p1.put("collectionid", collectionId);
          p1.put("uploadid", uploadId);
          p1.put("filename", filename);
          Map params3[] = new Map[1];
          params3[0] = p1;

          result = (HashMap) client.execute("joomeo.user.collection.saveUploadedFile", params3);
          String fileId = (String) result.get("fileid");
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
