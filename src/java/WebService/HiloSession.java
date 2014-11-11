/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebService;

import java.io.*;
import java.io.IOException;
/**
 *
 * @author hilsierivan
 */

public class HiloSession extends Thread {
    FileAux aux;
    String rutaimagen;
    File file;
    boolean deleted=false;
    public HiloSession(String rutaimagen){
    this.rutaimagen=rutaimagen;
        aux=new FileAux();
        file=new File(rutaimagen);
    }
    @Override
    public void run(){
        try {
            System.out.println("empezo sesion");
            Thread.sleep(60000);
            System.out.println("termino sesion");
            delete(file);
           
        } catch (InterruptedException ex ) {
            System.out.println(ex.getMessage());
           
    
    }
}

 public  void delete(File file){
 
        if(file.isDirectory()){
 
            //directory is empty, then delete it
            if(file.list().length==0){
 
               file.delete();
               System.out.println("Directory is deleted : " 
                                                 + file.getAbsolutePath());
 
            }else{
 
               //list all the directory contents
               String files[] = file.list();
 
               for (String temp : files) {
                  //construct the file structure
                  File fileDelete = new File(file, temp);
 
                  //recursive delete
                 delete(fileDelete);
               }
 
               //check the directory again, if empty then delete it
               if(file.list().length==0){
                 file.delete();
                 System.out.println("Directory is deleted : " 
                                                  + file.getAbsolutePath());
               }
            }
 
        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }






    
}