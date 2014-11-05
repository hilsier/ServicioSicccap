/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import java.net.Inet4Address;

/**
 *
 * @author Yarib
 */
public class FileAux {

    String path = System.getProperty("catalina.base") + "/webapps/ServicioSicccap/Imagenes";//Dejen esta ruta...
String pathAppend=System.getProperty("catalina.base") + "/webapps/ServicioSicccap/barra.png";
String ImageFolder=path+"/Images";
String ZipFolder=path+"/ZipFiles";
String QrFolder=path+"/QR";
File fileGeneralPath;
File fileImageFolder;
File fileZipFolder;
File fileQrFolder;
    public FileAux() {
fileGeneralPath=new File(path);
fileImageFolder=new File(ImageFolder);
fileZipFolder=new File(ZipFolder);
fileQrFolder=new File(QrFolder);

if(!fileGeneralPath.exists()||!fileImageFolder.exists()||!fileZipFolder.exists()||!fileQrFolder.exists()){
fileGeneralPath.mkdirs();
fileImageFolder.mkdirs();
fileZipFolder.mkdirs();
fileQrFolder.mkdirs();

}





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
    }// fin del metodo

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
                fpath = path + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, fpath);
                } else {
                    // if the entry is a directory, make the directory
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
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public String jpgToPng(String JPGPath) {

        File output;
        try {
            File input = new File(JPGPath);
            BufferedImage image = ImageIO.read(input);
            String ar[] = JPGPath.split("\\.");
            System.err.println(ar.length);
            String PNGPath = ar[0];
            output = new File(PNGPath + ".png");
            ImageIO.write(image, "png", output);
        } catch (IOException ex) {
            return ex.toString();
        }
   //     System.err.println(output.getAbsolutePath());
        //  System.err.println(output.getPath());

        return output.getPath();
    }

    public void DeleteFile(String ruta) {
        File f = new File(ruta);
        if (!f.delete()) {
            System.err.println("Failed to Delete " + ruta);
        }
    }

    public String zipFile(String pathToCompress, String NameFile) {

        System.out.println("path to compress:" + pathToCompress);
        String[] path = pathToCompress.split("/");
        String compress = path[path.length - 1];
        System.out.println("path last:" + compress);
        byte[] buffer = new byte[100000];
        String zipath = ZipFolder +"/"+ NameFile + "Firmado.zip";

        try {

            FileOutputStream fos = new FileOutputStream(zipath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(compress);
            zos.putNextEntry(ze);

            FileInputStream in = new FileInputStream(pathToCompress);

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
            zos.closeEntry();

            //remember close it
            zos.close();

            System.out.println("Done");
            return zipath;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public String getGeneralPath() throws UnknownHostException {
        
        return path;
    }

    public String getFolderImage(){

return ImageFolder;

    }

    public String getFolderQR(){

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
      // note : you may also need
      //        HttpURLConnection.setInstanceFollowRedirects(false)
      HttpURLConnection con =
         (HttpURLConnection) new URL(URLName).openConnection();
      con.setRequestMethod("HEAD");
      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
    }
    catch (Exception e) {
       e.printStackTrace();
       return false;
    }
  }









}//fin de la calse
