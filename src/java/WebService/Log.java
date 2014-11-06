package WebService;
import java.io.*;

public class Log{

String pathLog;
File filePath;
public  Log(){
pathLog=System.getProperty("catalina.base") + "/webapps/ServicioSicccap/Logs/";
filePath=new File(pathLog);
if(!filePath.exists()){
filePath.mkdirs();
}
}

public boolean CreateLogSigned(int WidthImage,int HeightImage,int NumRequest,long TimeToSigned,int NumOfImagesSigned){

boolean log=false;
 FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(pathLog+"Log.txt",true);
            pw = new PrintWriter(fichero);
 			 pw.println("Numero Peticion:"+NumRequest+" Ancho:"+WidthImage+" Alto:"+HeightImage+" Tiempo Firma"+TimeToSigned+"mls"+" #imagenes firmadas:"+NumOfImagesSigned);
 log=true;
        } catch (Exception e) {
            e.printStackTrace();
            log=false;
        } finally {
           try {
           	if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
              log=false;
           }
           
        }




return log;
}

public boolean CreateLogVerify(){

return false;
}





}