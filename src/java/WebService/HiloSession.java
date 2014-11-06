/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebService;

import java.io.*;
/**
 *
 * @author hilsierivan
 */

public class HiloSession extends Thread {
    FileAux aux;
    String rutaimagen;
    File file;
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
            System.out.println(deleteDir(file));
           
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    
    }

public boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
    }




    return dir.delete(); // The directory is empty now and can be deleted.
}


    
}