/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebService;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;

/**
 *
 * @author Yarib
 */
public class QR {
    public int heigthQrCode=250;
    public int widthQrCode=250;
    FileAux a;
    public QR(){
a=new FileAux();



    }
    
    
    public String getHash(String text) throws NoSuchAlgorithmException{
    
MessageDigest m = MessageDigest.getInstance("MD5");
m.reset();
m.update(text.getBytes());
byte[] digest = m.digest();
BigInteger bigInt = new BigInteger(1,digest);
String hashtext = bigInt.toString(16);
// Now we need to zero pad it if you actually want the full 32 chars.
while(hashtext.length() < 32 ){
  hashtext = "0"+hashtext;
}
return hashtext;
        

    }
      
      private BufferedImage invertirColores(BufferedImage imagen) {
        for (int i = 0; i < heigthQrCode; i++) {
            for (int j = 0; j < widthQrCode; j++) {
                int rgb = imagen.getRGB(i, j);
                if (rgb == -16777216) {
                    imagen.setRGB(i, j, -1);
                } else {
                    imagen.setRGB(i, j, -16777216);
                }
            }
        }
        return imagen;       
}
      
       public String CreateQR(String message,String Filename) throws WriterException, FileNotFoundException, IOException{
             BitMatrix bm;
             File dir = new File(FileAux.QrFolder);
             Writer writer = new QRCodeWriter();                                                 
             bm = writer.encode(message, BarcodeFormat.QR_CODE,widthQrCode, heigthQrCode);
                    // Crear un buffer para escribir la imagen
             BufferedImage imagex = new BufferedImage(widthQrCode, heigthQrCode, BufferedImage.TYPE_INT_RGB);
                     for (int i = 0; i < widthQrCode; i++) {
                         for (int j = 0; j < heigthQrCode; j++) {
                                int grayValue = (bm.get(j, i) ? 1 : 0) & 0xff;
                                imagex.setRGB(j, i, (grayValue == 0 ? 0 : 0xFFFFFF));
                            }
                         }
                        imagex = invertirColores(imagex);
                        FileOutputStream qrCode = new FileOutputStream(dir+"/"+Filename+".jpg");                        
                        ImageIO.write(imagex, "jpg", qrCode);
                        qrCode.close(); 
                        File qr=new File(dir+"/"+Filename+".jpg");
                        ImageIO.read(qr);
             if(!dir.exists())
                    {
                        dir.mkdirs();
                    }    
             
               return qr.getName();           
        }
       
       public String ReadQr(String path) throws NotFoundException, FormatException, ChecksumException, IOException{
           
           
         
           File f=new File(path);
           Result result = null;
       BufferedImage   image;
        image = ImageIO.read(f);
BufferedImageLuminanceSource bils = new BufferedImageLuminanceSource(image);
HybridBinarizer hb = new HybridBinarizer(bils);
BitMatrix bm = hb.getBlackMatrix();
 MultiDetector detector = new MultiDetector(bm);
DetectorResult dResult = detector.detect();
if(dResult == null)
{
    System.out.println("Image does not contain any barcode");
}
else
{
    System.err.println("la imagen contiene  un QR");
   

BinaryBitmap binaryBitmap;
 
try{
 
binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
result = new MultiFormatReader().decode(binaryBitmap);
System.out.println("QR Code : "+result.getText());
 
}catch(Exception ex){
    System.out.println(ex.toString());
}
    
    
   
} 
       
       return result.getText();
       }
       
       
       
    
}
