import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
// cs for clear

public class Client extends JFrame {

     Socket socket;

     BufferedReader br;
    PrintWriter out;
      
     //Declare Components(3 component)
     private JLabel heading =new JLabel("Client Area");
     private JTextArea messageArea=new JTextArea();
     private JTextField messageInput=new JTextField();
     private Font font = new Font("Roboto",Font.PLAIN,20);

      
    //constructor
    public Client(){

        try{
            System.out.println("Sending request to server");
            socket =new Socket("127.0.0.1",7777);
            System.out.print("connection done.");
             
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
         
         out=new PrintWriter(socket.getOutputStream());
         
        createGUI(); // before reading and writing
        handleEvents(); //event handle
         startReading();
        //  startWriting(); 
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void handleEvents() {
        // TODO Auto-generated method stub  //interface keylisterner
         messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override   // after pressing and realasing key kya kaam hoga
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub       // konsi key release hui hai(asciee)
            //    System.out.println("key Released" + e.getKeyCode());
               if(e.getKeyCode()==10)//10---> enter
               {
                 String contentTSend = messageInput.getText();
                messageArea.append("Me:" + contentTSend +"\n");
                 out.println(contentTSend); // OUTPUT me woh text write ho jaega
                 out.flush();
                 messageInput.setText("");
                 messageInput.requestFocus();
               }
            }
            
         });
    }

    private void createGUI(){
              //gui code

        this.setTitle("Client Messager[END]"); // this-->>>>> window
         this.setSize(600,700); 
         this.setLocationRelativeTo(null);  // center
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// cross click kroge toh bnd ho jaaega program
         

         //coding for component
         //front set
         heading.setFont(font);
         messageArea.setFont(font);
         messageInput.setFont(font);
         heading.setIcon(new ImageIcon("clogo.jpg"));
         heading.setHorizontalTextPosition(SwingConstants.CENTER);
         heading.setVerticalTextPosition(SwingConstants.BOTTOM);
         heading.setHorizontalAlignment(SwingConstants.CENTER);
         heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
         //no change in text area
          messageArea.setEditable(false);
         //frame ka laout set karenge
         this.setLayout(new BorderLayout());
       
         //adding the compoents to frame
         this.add(heading,BorderLayout.NORTH);
         JScrollPane jScrollPane=new JScrollPane(messageArea); //provide scroll bar
         this.add(messageArea,BorderLayout.CENTER);
         this.add(messageInput,BorderLayout.SOUTH);

          
         this.setVisible(true);

    }
    public void startReading(){
         
        //thread-read karke deta rahega
        Runnable r1=()->{
          
            System.out.println("reader started..");
           try{
            while(true){
            
            
                String msg=br.readLine();
                // client se agr aisa msg aa gaya toh 
                // reader bnd ho jaaega
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    
                    messageInput.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                    socket.close();
                    break;
                }

                messageArea.append("Server:"+msg+"\n"); 
            }
           
        }catch(Exception e){
                // e.printStackTrace();
                System.out.println("Connection closed");
        }
        };
      new Thread(r1).start(); // start the thread
         
    }

    public void startWriting(){
        System.out.println("writer started..");
        // thread -data user lega and send karega client tak 
        Runnable r2=()->{
            try{
            while(true && !socket.isClosed()){

                
                      // data read krna hai console se (keyboard se)
                   BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                   
                   String content=br1.readLine(); // console se read kr rhe the
                  out.println(content); // send kr diya client pr
                  out.flush();
                  if(content.equals("exit"))// exit word aa jaae
                  {
                    socket.close();
                   break;
                  }
              
              
            }
         
          
        }catch(Exception e){
            e.printStackTrace();
    }
            
        };
        new Thread(r2).start();
    }

  

    
    public static void main(String[] args){

        System.out.println("this is client...");
        new Client();
    }
}
