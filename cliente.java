package Cliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
 


public class cliente extends JFrame {
     
     
    File filas[] = new File[3]; 
    Socket echoSocket = null; 
    String servidor="192.168.24.100"; // Cambiar aqui la ip
        
    private JButton boton;
    private JButton boton2;
    private JButton boton3;
    private JButton boton4;
    JPanel pnlButton = new JPanel();
    String pathArchivo="",pathArchivo2="",pathArchivo3="";
    public cliente() {
    
    super("Comunicacion de datos");
    boton = new JButton("Subir Texto con Notas");
    boton.setBounds(0, 100, 220, 30);
    boton2 = new JButton("Subir Texto Descripcion del Curso");
    boton2.setBounds(60, 100, 220, 30);
    boton3= new JButton("Subir Imagen Firma");
    boton3.setBounds(0,100,220,30);
    boton4= new JButton("Enviar");
    boton4.setBounds(0,100,220,30);

    pnlButton.setBounds(800, 800, 200, 100);
    pnlButton.add(boton);
    pnlButton.add(boton2);
    pnlButton.add(boton3);
    pnlButton.add(boton4);
    add(pnlButton);

        boton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                    JFileChooser elegir = new JFileChooser();
                    
                    int opcion = elegir.showOpenDialog(boton);
                    
                    //Si presionamos el boton ABRIR en pathArchivo obtenemos el path del archivo
                    if (opcion == JFileChooser.APPROVE_OPTION) {
                         //boton.setEnabled(false);
                        pathArchivo = elegir.getSelectedFile().getPath(); //Obtiene path del archivo
                        String nombre = elegir.getSelectedFile().getName(); //obtiene nombre del archivo
                       
                        System.out.println("El nombre del archivo es: "+ nombre);
                        System.out.println("El path del archivo es: "+ pathArchivo);
                    }
                }
        }); 
        boton2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evento) {
           
                JFileChooser elegir2= new JFileChooser();
                int opcion2=elegir2.showOpenDialog(boton2);
          
                    
                   
                    if (opcion2 == JFileChooser.APPROVE_OPTION) {
                      
                        pathArchivo2 = elegir2.getSelectedFile().getPath(); //Obtiene path del archivo
                        String nombre2 = elegir2.getSelectedFile().getName(); //obtiene nombre del archivo
                       
                        System.out.println("El nombre del archivo es: "+ nombre2);
                        System.out.println("El path del archivo es: "+ pathArchivo2);
                    }
               }
        });
         
        boton3.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent evento) {
           JFileChooser elegir3= new JFileChooser();
           int opcion3=elegir3.showOpenDialog(boton3);
          
                    
                   
                    if (opcion3 == JFileChooser.APPROVE_OPTION) {
                        
                        pathArchivo3 = elegir3.getSelectedFile().getPath(); //Obtiene path del archivo
                        String nombre3 = elegir3.getSelectedFile().getName(); //obtiene nombre del archivo
                       
                        System.out.println("El nombre del archivo es: "+ pathArchivo3);
                        System.out.println("El path del archivo es: "+ pathArchivo3);
                    }
               }
        });
        
        boton4.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent evento) {
         if(pathArchivo=="" || pathArchivo2=="" || pathArchivo3==""){
           JOptionPane.showMessageDialog(null, "No ha seleccionado todos los archivos para enviarlos", "Error", JOptionPane.ERROR_MESSAGE);
                    
         }else{
             boton4.setEnabled(false);

             try {
                 echoSocket = new Socket(servidor, 4000);//se conecta al mismo puerto del server
                 filas[0]= new File(pathArchivo);
                 filas[1]= new File(pathArchivo2);
                 filas[2]= new File(pathArchivo3);
                 BufferedOutputStream bos = new BufferedOutputStream(echoSocket.getOutputStream());
                 DataOutputStream dos = new DataOutputStream(bos);
                 dos.writeInt(filas.length);
//System.out.printf("%d",filas.length);
for(File file : filas)
{
    long tamanofila = file.length();
    dos.writeLong(tamanofila);

    String nombrefila = file.getName();
    //System.out.printf("filename: %s",name);
    dos.writeUTF(nombrefila);

    FileInputStream fis = new FileInputStream(file);
    BufferedInputStream bis = new BufferedInputStream(fis);

    int Byte = 0;
    while((Byte = bis.read()) != -1) bos.write(Byte);

    bis.close();
}
bos.flush();
dos.flush();
////////////////////RECEPCION CALCULOS PROMEDIOS/
echoSocket.shutdownOutput(); //importante
File fila=new File(pathArchivo+"promedios.txt");
 

      
    int leerBytes;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bus = null;
      byte [] buffer  = new byte [1024];
      InputStream is = echoSocket.getInputStream();
      fos = new FileOutputStream(fila);
      bus = new BufferedOutputStream(fos);
      leerBytes = is.read(buffer,0,buffer.length);
      current = leerBytes;

      do {
         leerBytes =
            is.read(buffer, current, (buffer.length-current));
         if(leerBytes >= 0) current += leerBytes;
      } while(leerBytes > -1);

      bus.write(buffer, 0 , current);
      bus.flush();
      



//////////////////////////////////////
dos.close(); //CIERRE DATA OUTPUT
bos.close();
JOptionPane.showMessageDialog(null, "Archivos Enviados Con exito al Servidor", "Envio Exitoso ", JOptionPane.INFORMATION_MESSAGE); 
                 
                 
                 
             } catch (IOException ex) {
                 Logger.getLogger(cliente.class.getName()).log(Level.SEVERE, null, ex);
             }
         
         
         }
                
           
                    
               }
        });
    
    
    }
 
 
   
    public static void main(String[] args) throws IOException  {
        
        cliente abrir = new cliente();
        abrir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        abrir.setSize(800, 200);
        abrir.setLocationRelativeTo(null); //centra el frame
        abrir.setVisible(true); //pone visible en frame
    }
}
