

package servidorm;

import java.awt.Dimension;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

 
public class ServidorM
{      
        
    public static void main(String[] args) throws IOException
    {  
        
        JTextArea  areaTexto1;
        JFrame ventana = new JFrame();
        ServerSocket Master = null;
        boolean escucha = true;
        int contador=0;
       
      
        
       
      
        
        JPanel contentPane= new JPanel();
        
        areaTexto1 = new JTextArea();
        areaTexto1.setBounds(0,0,200,80);
        areaTexto1.setText("\nServidor Maestro Conectado en el puerto 4000\n Profesores conectados:"+contador);
        areaTexto1.setEnabled(false);
        areaTexto1.setVisible(true);
     
      
        
        contentPane.add(areaTexto1);
        ventana.setTitle("Servidor Maestro");
        ventana.add(contentPane);
        ventana.setMinimumSize(new Dimension(200, 80)); 
        ventana.setLocation(0,0); 
        
        ventana.pack();
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        try
        {
            Master = new ServerSocket(4000);//crea un server socket
        }
        catch (IOException e)
        {
            System.err.println("No puedo Utilizar el puerto 4000");
            System.exit(-1);
        }
        while (escucha)
        {
            	System.out.println("Esperando una conexión:");

                new ServidorHijo(Master.accept(),contador).start();//inicia una nueva hebra
                contador++;
        areaTexto1.setText("\nServidor Maestro Conectado en el puerto 4000\n Profesores conectados:"+contador);
       
      
        
        contentPane.add(areaTexto1);
                
             
            
                
        }
        

      
        Master.close();
    }
}
