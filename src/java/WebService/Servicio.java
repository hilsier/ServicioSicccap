/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebService;

import FADD.AES;
import FADD.Ciphering;
import FADD.PNG;
import FADD.StegaWithPNG;
import FADD.Text_XML;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Yarib
 */
@WebService(endpointInterface = "WebService.ImplementServicio")
public class Servicio  implements ImplementServicio  {

        String FileName;
        public String Firma(String bse64, String NameFile, String message) throws IOException, WriterException, NoSuchAlgorithmException {
       

 String Password="123";
        FileAux fa=new FileAux();
        QR qr=new QR();
        String zipfile=fa.CreateFile(NameFile,bse64,"zip");
        String imagen=fa.unzipfile(zipfile,fa.getFolderImage()+"/");
        this.FileName=fa.ImageName;
       // System.err.println("path imagen:"+imagen);
        String extension=getExt(imagen);
              switch(extension){
              case "jpg":
             // System.err.println("archivo es jpg");
              imagen=fa.jpgToPng(imagen);  
              break;
              case "JPG":
               //   System.err.println("archivo es jpg");
              imagen=fa.jpgToPng(imagen);  break;          
      }
              //****
        File f=new File(imagen);
        BufferedImage bi= ImageIO.read(f);
        int w=bi.getWidth();
        String hashtext=qr.getHash(message);
            System.err.println(hashtext);
        
        String pathQr=qr.CreateQR(hashtext, NameFile);
        
        CreateAppend ap=new CreateAppend(w,fa.getGeneralPath(),pathQr,FileName);
        String append=ap.save();
        AppendImage ai=new AppendImage(fa.getGeneralPath(),append,imagen,FileName);
        String final_img=ai.Append();
        System.err.println(final_img);
             //****
        
        String rutafirmada=firmar(message,Password,final_img);
       
       String AbsolutePath=fa.getPathAbsolute(rutafirmada);
       fa.DeleteFile(imagen);
        fa.DeleteFile(zipfile);
        HiloSession session=new HiloSession(rutafirmada);
        session.start();
        return AbsolutePath;
       //return imagen;*/

        
    }
    
    private String firmar(String info, String pass, String nomArch){
            String ruta="";
            FileAux aux =new FileAux();
        try {
            File archivoBMP = new File(nomArch);
            PNG img = new PNG(archivoBMP);
            Ciphering cifrador = null;
            cifrador = new AES(); 
            byte[] mensajeCifrado = cifrador.encripta(info, pass, "AES");
            if (getExt(archivoBMP.getName()).equalsIgnoreCase("png")) {
                StegaWithPNG stega = new StegaWithPNG(img);
                System.out.println("Is valid: "+img.isValid());
                ruta=aux.getFolderImage()+"/"+FileName+"Firmada.png";
                boolean execStega = stega.execStega(ruta, mensajeCifrado, "LSBs");
                System.out.println("execStega: "+execStega);
                if (execStega) {//img.savePNG(nFile, stega.getPNG().getChunks())
                   return ruta; 
                } else {
                    return "No se pudo salvar la imagen";}}
            } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            return "Error: al abrir "+nomArch;
        }
        return ruta;
    }
    
    
     private String consultar(String pass, String nomArch){
        try {
            File archivoBMP = new File(nomArch);
            PNG img = new PNG(archivoBMP);
            byte[] cifrado=new StegaWithPNG(img).getInfo(img, "AES");
            Ciphering cifrador=null;
            cifrador=new AES();
            String mensaje_txt=" -Error- ";
            String mensaje_xml=" -Error- ";
            mensaje_xml=cifrador.decripta(cifrado, pass,"LSBs");
            mensaje_xml=mensaje_xml!=null?mensaje_xml:"";

            if(mensaje_xml.startsWith("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>")){
                Text_XML txtxml = new Text_XML();
                mensaje_txt = txtxml.toText(mensaje_xml, "\n");
                mensaje_txt = mensaje_txt!=null?mensaje_txt:"ERROR";
                return mensaje_txt;
            }
            return mensaje_xml;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            return "Error : "+ex.getMessage();
        }
        
        //return "Final..";
    }
    
    private String getExt(String fileName) {
        if (fileName.length() < 0) {
            return null;
        }
        char[] nChar = new char[]{fileName.charAt(fileName.length() - 3), fileName.charAt(fileName.length() - 2), fileName.charAt(fileName.length() - 1)};
        return new String(nChar, 0, 3);
        }   
    
        public String ConsultaFirma( String base64,String nameFile) throws IOException {

System.out.println("base64"+base64+"namefile"+nameFile);
        FileAux fa=new FileAux();
        QR qr=new QR();
        String pathfile=fa.CreateFile(nameFile,base64,"zip");
        String pathImage=fa.unzipfile(pathfile, fa.getFolderImage()+"/");
        
            System.out.println(pathImage);
        //System.out.println("Created file"+pathImage);
        String resultado=consultar("123",pathImage);
            try {
                if(qr.ReadQr(pathImage).equals(qr.getHash(resultado))){
                
                resultado=resultado+"la informacion del qr coincide";
                }
                
                else{
                
                resultado=resultado+"la informacion del qr NO coincide";
                }
            } catch (NotFoundException | FormatException | ChecksumException | NoSuchAlgorithmException ex) {
                System.out.println(ex.toString());
                
            }
            System.out.println(resultado);
        fa.DeleteFile(pathImage);
        fa.DeleteFile(pathfile);
         return  resultado; 
        }
        
        
   
     
  
     
    
}
