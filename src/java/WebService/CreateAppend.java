/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WebService;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Font;
/**
 *
 * @author Yarib
 */
public class CreateAppend extends JPanel {
      ImageIcon qr_icon,info_icon;
      Image qr_image,info_image;
      BufferedImage  bi;
      String filename,path;
      Rectangle r;
      int aux=0,w=0,h;
    public CreateAppend(int s,String path,String qr,String filename,String barPath){
        this.filename=filename;
        this.path=path;
        w=s;
        qr_icon=new ImageIcon(path+"/QR/"+qr);
        info_icon=new ImageIcon(barPath);
        info_image=info_icon.getImage();
        qr_image=qr_icon.getImage();
        this.setBackground(Color.WHITE);
        h=w/6;/////--------------tamño del append-------------///////
        this.setSize(w,h+20);
        r=this.getBounds();
      //  bi=new BufferedImage(r.width,r.height-20,BufferedImage.TYPE_3BYTE_BGR); 
        bi=new BufferedImage(r.width,r.height,BufferedImage.TYPE_3BYTE_BGR); 
        System.out.println("w:"+w+"h:"+h);
    }
    
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.red);
        aux=h;
        g2.drawImage(qr_image, 100, 10, aux, aux, null);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, aux/5));
        g2.drawString("Cotejado con el Original.",120+aux,aux/2);
        //g2.drawImage(info_image,300,20,400,100,null); 
    }

    public String save() throws IOException {
        repaint();
        Graphics g=bi.getGraphics();
        paint(g);    
        String name="/Images/"+filename+"append.png";
        ImageIO.write(bi, "png", new File(path+name));
        System.out.println("Saving" +path+name);
        File f=new File(path+name);
        return f.getAbsolutePath();
    }

    public int getWidth(){

return r.width;

    }


    public int getHeigth(){

        return r.height;
    }
}
