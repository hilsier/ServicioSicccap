/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import java.net.Inet4Address;
import java.net.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;

/**
 *
 * @author Yarib
 */
public class FileAux {

    String path=System.getProperty("catalina.base") + "/webapps/ServicioSicccap/Imagenes";
String pathAppend=System.getProperty("catalina.base") + "/webapps/ServicioSicccap/barra.png";
String servicePath=System.getProperty("catalina.base") + "/webapps/ServicioSicccap";
public static String ImageFolder;
public static String ZipFolder;
public static String QrFolder;
File fileGeneralPath;
File fileImageFolder;
File fileZipFolder;
File fileQrFolder;
    public FileAux() {
       

    }

    public String  gendir(){
          
        Random r=new Random();
        int num=r.nextInt(1+10000000);
        path=path+"/"+num;
         ImageFolder=path+"/Images";
         ZipFolder=path+"/ZipFiles";
         QrFolder=path+"/QR";
        fileGeneralPath=new File(path);
        fileImageFolder=new File(ImageFolder);
        fileZipFolder=new File(ZipFolder);
        fileQrFolder=new File(QrFolder);
        System.out.println("path actual:"+path);
        System.out.println("zip path:"+ZipFolder);

if(!fileGeneralPath.exists()||!fileImageFolder.exists()||!fileZipFolder.exists()||!fileQrFolder.exists()){
        fileGeneralPath.mkdirs();
        fileImageFolder.mkdirs();
        fileZipFolder.mkdirs();
        fileQrFolder.mkdirs();
        fileGeneralPath.setReadable(true);
        fileGeneralPath.setWritable(true);
}

return path;

    }
    String ImageName = null;

    public byte[] decodeBase64(String base64) {
        byte[] DataFile = null;
        try {
            DataFile = new sun.misc.BASE64Decoder().decodeBuffer(base64);
        } catch (IOException ex) {
        }
        return DataFile;
    }

    public String CreateFile(String Nombre, String base64, String ext) {
        if (ext.equals("zip")) {
        }
        String source = ZipFolder + "/"+Nombre + "." + ext;
        File file = new File(source);
        FileOutputStream fos = null;
        byte[] filebyte = this.decodeBase64(base64);
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
        try {
            fos.write(filebyte);
            String res = source;
            fos.flush();
            fos.close();
            return res;
        } catch (IOException ex) {
            System.out.println(ex.toString());
            return "failed";
        }
    }

    public String unzipfile(String zpath, String path) throws FileNotFoundException, IOException {
        String fname = null;
        String fpath = null;
        File destDir = new File(zpath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zpath));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            try {
                fname = entry.getName();
                ImageName = fname.substring(0, fname.length() - 4);
                fpath = path+"/"+entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, fpath);
                } else {
                    File dir = new File(fpath);
                    dir.mkdir();   
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        
        zipIn.close();
        return fpath;
    }
    private final int BUFFER_SIZE = 5096;

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        FileOutputStream input=new FileOutputStream(filePath);
        BufferedOutputStream bos = new BufferedOutputStream(input);
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        input.close();
    }

    public String jpgToPng(String JPGPath,String name) {

        File output;
        try {
            File input = new File(JPGPath);
            BufferedImage image = ImageIO.read(input);
            output = new File(ImageFolder+"/"+name+".png");
            ImageIO.write(image, "png", output);
        } catch (IOException ex) {
            return ex.toString();
        }

        return output.getPath();
    }

    public void DeleteFile(String ruta) {
        File f = new File(ruta);
        if (!f.delete()) {
            System.err.println("Failed to Delete " + ruta);
        }
    }


    public String getGeneralPath() throws UnknownHostException {
        
        return path;
    }

    public static String getFolderImage(){
        return ImageFolder;

    }

    public static String getFolderQR(){
        return QrFolder;
    }

    public String getbarPath(){
        return pathAppend;
    }

 public String getPathAbsolute(String path)throws UnknownHostException{

  String ip=Inet4Address.getLocalHost().getHostAddress();
  String array[]=path.split("ServicioSicccap");  
  String pathAbsolute=ip+":8080/ServicioSicccap"+array[1];
    return pathAbsolute;

    }

 private boolean exists(String URLName){
    try {
      HttpURLConnection.setFollowRedirects(false);
      HttpURLConnection con =(HttpURLConnection) new URL(URLName).openConnection();
      con.setRequestMethod("HEAD");
      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
    }
    catch (Exception e) {
       e.printStackTrace();
       return false;
    }
  }

 public void GiveAllPermissions()throws InterruptedException{
      String SystemName=System.getProperty("os.name");
    if(!SystemName.contains("Win")){
    try{
        StringBuffer output = new StringBuffer();
        Process p2=Runtime.getRuntime().exec("chmod -R go+w /var/lib/tomcat7/webapps");
        p2.waitFor();
        BufferedReader reader =  new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String line = "";           
         while ((line = reader.readLine())!= null) {
            output.append(line + "\n");   }
        System.out.println("\nOutput Console:"+output);}
        catch(IOException e){
        System.out.println("\n Errr Run console:"+e.toString());
                  
        }}}

    public String getHash(String file){
         try {
            File input = new File(file);
            BufferedImage buffImg = ImageIO.read(input);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", outputStream);
            byte[] data = outputStream.toByteArray();  
            System.out.println("Start MD5 Digest");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] hash = md.digest();
            return returnHex(hash);
        } catch (IOException | NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
            return null;
        } catch (Exception ex) {
           System.err.println(ex.getMessage());
           return null;
        } 


        }

  static String returnHex(byte[] inBytes) throws Exception {
       String hexString = "";
        for (int i=0; i < inBytes.length; i++) { //for loop ID:1
        hexString +=
        Integer.toString( ( inBytes[i] & 0xff ) + 0x100, 16).substring( 1 );
        }                                  
                return hexString;
  }                
  public String appendTempo=null;
    public String GetSubImage(String image){
        String retpath=null;
     try {
        BufferedImage biapp=ImageIO.read(new File(image));
        BufferedImage bi= ImageIO.read(new File(image));
        int w=bi.getWidth(); 
        int aux=w/6;/////--------------tamño del append-------------///////
        int h=bi.getHeight()-aux-20;
        int ha=bi.getHeight();
        BufferedImage bic=new BufferedImage(w,h,BufferedImage.TYPE_3BYTE_BGR);
        bic= bi.getSubimage(0, 0,w,h);
      /*  BufferedImage append=new BufferedImage(w,aux+20,BufferedImage.TYPE_3BYTE_BGR);
        append=bi.getSubimage(0,h,w,ha);
        appendTempo=image+"append.png";*/
        retpath=image+"tempo.png";
        ImageIO.write(bic, "png", new File(retpath));
       // ImageIO.write(append,"png",new File(appendTempo));
            } catch (IOException ex) {
            ex.printStackTrace();
        }
        return retpath;
        }

public String getTempoAppend(){
    return appendTempo;
}

 


}//fin de la calse
