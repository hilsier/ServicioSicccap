/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import com.google.zxing.WriterException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 *
 * @author Alumno
 */
@WebService
@SOAPBinding(style = Style.RPC)

public interface ImplementServicio {
   @WebMethod String Firma(String bse64, String NameFile, String message) throws IOException, WriterException, NoSuchAlgorithmException,InterruptedException,Exception;
  @WebMethod String ConsultaFirma( String base64,String nameFile) throws IOException,Exception;
}
