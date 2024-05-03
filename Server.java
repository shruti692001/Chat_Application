import java.net.*;
import java.io.*;

class Server
{   


    ServerSocket server;
    Socket socket;


     BufferedReader br;
    PrintWriter out;
      

    //CONSTRUCTOR
    public Server(){
        try{
         server=new ServerSocket(7777);
         System.out.println("server is ready to accept connection");
         System.out.println("waiting..");
         socket=server.accept();

         br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
         
         out=new PrintWriter(socket.getOutputStream());

         startReading();
         startWriting();
        } catch (Exception e)
        {
           e.printStackTrace();
        }
    }
    
    public void startReading(){
         
        //thread-read karke deta rahega
        Runnable r1=()->{
          
            System.out.println("reader started..");
      
            try{
                // any exception in while loop will close the loop and thread will get close
            while(true){
            
               
                String msg=br.readLine();
                // client se agr aisa msg aa gaya toh 
                // reader bnd ho jaaega
                if(msg.equals("exit")){
                    System.out.println("Client terminated the chat");
                    
                    socket.close(); // connection close
                }

                System.out.println("Client: "+msg);
            }
             
        }catch(Exception e){
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
                   
                   String content=br1.readLine();
                  
                  out.println(content); // send kr diya client pr // if socket close primt to nhi hi hoga
                  out.flush();
                  if(content.equals("exit"))// exit word aa jaae
                  {
                    socket.close();
                   break;
                  }
               
            }
            
        }catch(Exception e){
            System.out.println("Connection closed");
        } 
        };
        new Thread(r2).start();
    }

  



    public static void main(String[] args){
         System.out.println("this is server...going to start server");
         new Server();
    }
} 