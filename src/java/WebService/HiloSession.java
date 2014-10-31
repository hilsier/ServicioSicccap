/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebService;

/**
 *
 * @author hilsierivan
 */

public class HiloSession extends Thread {
    FileAux aux;
    String rutaimagen;
    
    public HiloSession(String rutaimagen){
    this.rutaimagen=rutaimagen;
        aux=new FileAux();
    }
    @Override
    public void run(){
        try {
            System.out.println("empezo sesion");
            Thread.sleep(200000);
            System.out.println("termino sesion");
            aux.DeleteFile(rutaimagen);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    
    }
    
}