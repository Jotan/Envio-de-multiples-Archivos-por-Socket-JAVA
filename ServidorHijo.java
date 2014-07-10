

package servidorm;


import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Scanner; 
import java.util.StringTokenizer;
import javax.swing.*;
 
 
public class ServidorHijo extends Thread 
{
    private Socket socket = null;
   
   

    public ServidorHijo(Socket socket, int contador) 
    {  
        
       
  
        System.out.println("Se Conecto Nuevo Profesor!!");
        System.out.println("Se Inicia Nueva Hebra de Servidor");
        this.socket = socket;
        recibir();//llama a la funcion de recepcion de los archivos
    }  
    
   public void recibir(){ 
         
JTextArea  areaTexto1;
areaTexto1 = new JTextArea();
JFrame ventana = new JFrame();
areaTexto1.setBounds(0,0,200,800);
areaTexto1.setVisible(true);
ventana.setTitle("Servidor Hijo Calculo de Promedios");
ventana.setMinimumSize(new Dimension(300, 800)); 
ventana.setLocation(0,0); 
ventana.add(areaTexto1);
ventana.pack();
ventana.setVisible(true);
        
    try  
        {
//variables de recepcion de los archivos
BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
DataInputStream dis = new DataInputStream(bis);
String[] NombreArchivo = new String[3];
int TotalArchivos = dis.readInt(); // LEE EL TOTAL DE ARCHIVOS ENVIADOS
File[] Archivos = new File[TotalArchivos];
///////////////variables de lectura y calculo///////////////
int  cont = 0,contador=0,j=0;
StringTokenizer st;
Scanner entrada = null;
String cadena,texto="",aux="",textointerfaz="";
String [][] fichero = new String[100][100];
float [][] notas= new float[100][100];
float [] promedio=new float[100];
float suma=0;




////////////////RECIBE LOS ARCHIVOS Y LOS GUARDA////
 for(int i = 0; i < TotalArchivos; i++){

    long tamanoArchivo = dis.readLong(); //LEE EL TAMAÑO DE CADA ARCHIVO
    NombreArchivo[i] = dis.readUTF(); //LEE EL NOMBRE DE CADA ARCHIVO

    Archivos[i] = new File(NombreArchivo[i]); //CREA UN ARCHIVO EN EL SERVIDOR DEL MISMO NOMBRE

    FileOutputStream fos = new FileOutputStream(Archivos[i]);
    BufferedOutputStream bos = new BufferedOutputStream(fos);

    for(j = 0; j < tamanoArchivo; j++) bos.write(bis.read()); //ESCRIBE EL ARCHIVO HASTA QUE LLEGA A SU TAMAÑO

    bos.close();// CIERRA EL BUFFERED OUTPUT (VER SI LO PUEDO CERRAR MAS ABAJO)
 }

 //recepcion de las notas y calculo promedio
    FileWriter fw = new FileWriter("promedios"+NombreArchivo[0]);
    BufferedWriter bw = new BufferedWriter(fw);
        
    entrada = new Scanner(Archivos[0]); //ESCANER PARA EL ARCHIVO NOTAS
            while (entrada.hasNext()) {
            cadena = entrada.nextLine();
            st = new StringTokenizer(cadena, " ,");
            while (st.hasMoreTokens()) {
            fichero[contador][cont]=st.nextToken();
            cont++; //cuenta los separadores         
                              }            
                
               for(j=0;j<cont;j++){
                   
                   if(j!=0){        //condicion para que no transforme el nombre del alumno a float
                   notas[contador][j]= Float.parseFloat(fichero[contador][j]);
                   if(j<=3){
                   suma=suma+notas[contador][j];
                   }
                  }
                 }
              promedio[contador]=(float) (((suma/3)*0.6)+((notas[contador][4])*0.4));
              texto = String.format("%s.Promedio: %.1f",fichero[contador][0],promedio[contador]); 
              bw.write(texto);//Escribe el promedio en el archivo
                
                
                
                //Escribir en el fichero un salto de línea
                 bw.newLine();
                 
                   System.out.printf("%s ",fichero[contador][0]);
                   System.out.printf("Notas:");
                      for(j=1;j<cont;j++){
                   System.out.printf("%s ",fichero[contador][j]);
                      }
                   System.out.printf("Promedio:%.1f %n",promedio[contador]);
              
       
        
      
                contador++; 
                cont=0;//Reinicia variables
                suma=0;
               
            } // TERMINO DEL WHILE HAS.NEXT()
             bw.close();//CIERRA EL BUFFERED WRITER
        
    
          
   


      

            //dis.close();//CIERRA EL DATA INPUT
    //Envio de los calculos al Cliente
   
         FileInputStream fis = null;
         BufferedInputStream bus = null;
         OutputStream os = null;
          File Envio_Archivo = new File ("promedios"+NombreArchivo[0]);
          byte [] buffer  = new byte [(int)Envio_Archivo.length()];
          fis = new FileInputStream(Envio_Archivo);
          bus = new BufferedInputStream(fis);
          bus.read(buffer,0,buffer.length);
          os = socket.getOutputStream();
         
          os.write(buffer,0,buffer.length);
          os.flush();
          dis.close();//// CIERRA DATAINPUTSTREAM
          //Variables de lectura y muestra en pantalla en un JTextArea
          FileReader archivos=new FileReader(Envio_Archivo);
          BufferedReader lee=new BufferedReader(archivos);
          
           while((aux=lee.readLine())!=null)
      {
         textointerfaz+= aux+ "\n";
      }
        areaTexto1.setText(textointerfaz);
      
        lee.close();///CIERRA Y TERMINA LECTURA DE Y MUESTRA EN PANTALLA
   //////////////////////////////////////////////
          
      
 
    
}
    
    
  

     
              
                  
                  
                
            
         
        catch (Exception e){  
            System.out.printf("Error");  
        }  
    
    }

    
        

}


        
    
      
    
    
    
    
    


