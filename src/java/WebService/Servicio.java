

package WebService;

import FADD.*;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.WriterException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.*;
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
       
        String FileName;String Password="04020649";
        int NumOfSigned,HeigthImage,WidthImage,NumOfRequest=0;
        long TimeToSigned;
    public String Firma(String bse64, String NameFile, String message) throws IOException, WriterException, NoSuchAlgorithmException ,InterruptedException,Exception{
        if(bse64==null||NameFile==null||message==null){System.out.println("información Nula");}
        NumOfRequest++;
        FileAux fa=new FileAux();
        Ciphering cifra=new AES();
        String PathRandom=fa.gendir();
        fa.GiveAllPermissions();
        Log log=new Log(); 
        QR qr=new QR();
        String zipfile=fa.CreateFile(NameFile,bse64,"zip");
        String imagen=fa.unzipfile(zipfile,fa.getFolderImage());
        this.FileName=fa.ImageName;
        String extension=getExt(imagen);
              switch(extension){
              case "jpg":
              imagen=fa.jpgToPng(imagen,FileName);  
              break;
              case "JPG":
              imagen=fa.jpgToPng(imagen,FileName);  break;          
      }

              //****
        File f=new File(imagen);
        BufferedImage bi= ImageIO.read(f);
        int w=bi.getWidth();
        String rutafirmada=firmar(message,Password,imagen);
        String hashtext=fa.getHash(rutafirmada);
        System.out.println("Hash de la imagen... "+hashtext);
        
        byte []CifradoHash=cifra.encripta(hashtext,Password,"AES");
        String baseCifradoHash=  Base64.encode(CifradoHash);
        String pathQr=qr.CreateQR(baseCifradoHash, NameFile,w);
        //System.out.println("barpath:"+fa.getbarPath());
        CreateAppend ap=new CreateAppend(w,fa.getGeneralPath(),pathQr,FileName,fa.getbarPath());
        String append=ap.save();
        fa.GiveAllPermissions();
        AppendImage ai=new AppendImage(fa.getGeneralPath(),append,rutafirmada,FileName); 
        String final_img=ai.Append();
        TimeToSigned = System.currentTimeMillis();
        TimeToSigned = System.currentTimeMillis()-TimeToSigned;
        WidthImage=ap.getWidth();
        HeigthImage=ap.getHeigth();
// System.out.println("log created:"+log.CreateLogSigned(WidthImage,HeigthImage,NumOfRequest,TimeToSigned,NumOfSigned));
       String AbsolutePath=fa.getPathAbsolute(final_img);
       String retornar=AbsolutePath.replace("\\","/");
       System.out.println("AbsolutePath: "+AbsolutePath);
       HiloSession session=new HiloSession(PathRandom);
        session.start();
        return retornar;              
    }
    
    private String firmar(String info, String pass, String nomArch){
            String ruta="";
            FileAux aux =new FileAux();
            File archivoBMP =null;
            PNG img=null;
        try {
            File dirImage=new File(FileAux.ImageFolder);
            if(dirImage.isDirectory()&&dirImage.canRead()&&dirImage.canWrite()){
            System.out.println("directory is valid");
            archivoBMP = new File(nomArch);
            img = new PNG(archivoBMP);
            
            }else{
                System.out.println("no es un directorio valido");
            }
           
            Ciphering cifrador = null;
            cifrador = new AES(); 

            byte[] mensajeCifrado = cifrador.encripta(info, pass, "AES");
            if (getExt(archivoBMP.getName()).equalsIgnoreCase("png")) {
                StegaWithPNG stega = new StegaWithPNG(img);
                System.out.println("Is valid: "+img.isValid());
                ruta=FileAux.ImageFolder+"/"+FileName+"Firmada.png";
                boolean execStega = stega.execStega(ruta, mensajeCifrado, "LSBs");
                System.out.println("execStega: "+execStega);
                if (execStega) {//img.savePNG(nFile, stega.getPNG().getChunks())
                   NumOfSigned++; 
                   return ruta;
                  
                } else {
                    return "No se pudo salvar la imagen";}}
            } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            return "Error: al abrir "+ruta;
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
         if (fileName.length() < 0) {return null; }
         char[] nChar = new char[]{fileName.charAt(fileName.length() - 3), fileName.charAt(fileName.length() - 2), fileName.charAt(fileName.length() - 1)};
         return new String(nChar, 0, 3);
        } 


    
    public String ConsultaFirma(String base64, String nameFile) {
            System.out.println("Consultando.");
            FileAux fa = new FileAux();
            QR qr = new QR();
            Ciphering cifrar = new AES();
            String PathRandom = fa.gendir();
            String pathfile = fa.CreateFile(nameFile, base64, "zip");
            String pathImage = null;
            try {
            pathImage = fa.unzipfile(pathfile, fa.getFolderImage() + "/");
            } catch (IOException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
            }
                System.out.println(pathImage);
                System.out.println("Created file" + pathImage);
                String tempo = fa.GetSubImage(pathImage);
                String resultado = "Nada";
                byte[] baseOfQr = null;
                String qr_read = null;

            try {
                qr_read = qr.ReadQr(pathImage);
            } catch (ChecksumException ex) {
                 Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                 Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ReaderException ex) {
                 Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (Base64.decode(qr_read) == null) {
            resultado = "Al parecer hubo un problema con el documento. Motivos: La imagen no ha sido firmada o no se detecta el QR.";
            } else {
                baseOfQr = Base64.decode(qr_read);
                String imghash = fa.getHash(tempo);
                String hashOfQr = "";

                hashOfQr = cifrar.decripta(baseOfQr, Password, "AES");
            if (hashOfQr.equals(fa.getHash(tempo))) {
                resultado = consultar(Password, pathImage);
                resultado = "Operación realizada exitosamente: \n" + resultado;
            } else {
            resultado = "Se ha detectado una anomalía en la firma de la imagen. Por favor contacta con el proveedor.";
            }

            HiloSession se = new HiloSession(PathRandom);
            se.start();
            }

            return resultado;
}
        
        
   
     
  
     
    
}
