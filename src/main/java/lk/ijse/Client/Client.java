package lk.ijse.Client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.Controller.ClientFormController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Runnable, Serializable {
    public final String name;
    public final Socket socket;
    public final DataInputStream dataInputStream;
    public final DataOutputStream dataOutputStream;
    public ClientFormController clientFormController;


    public Client(String name) throws IOException {
        this.name = name;

        socket = new Socket("Localhost",1234);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        dataOutputStream.writeUTF(name);
        dataOutputStream.flush();

        try {
            loadScene();
        }catch (RuntimeException e) {
            e.getCause().printStackTrace();
        }
    }
  /*  @Override
    protected void finalize() throws Throwable {
        Thread.interrupted(); // To terminate the thread, interrupt it
        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
    }*/
    @Override
    public void run() {
        String message = "";
        while (!message.equals("finish")){
            try {
                message = dataInputStream.readUTF();
                if (message.equals("*image*")) {
                    receiveImg();
                }else {
                    clientFormController.writeMsg(message);
                }
            } catch (IOException e) {
                try {
                    socket.close();
                }catch (IOException ex) {

                }
            }
        }
    }

    public void sendMsg(String msg) throws IOException {
        dataOutputStream.writeUTF(msg);
        dataOutputStream.flush();
    }

    public void sendImg(byte[] bytes) throws IOException {
        dataOutputStream.writeUTF("*image*");
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
        dataOutputStream.flush();
    }
    public void loadScene() throws IOException {
        System.out.println("Load scene triggered");
        Stage stage =new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientForm.fxml"));
        System.out.println("Loaded");
        Parent parent = loader.load();
        clientFormController =loader.getController();
        System.out.println("controller set..");
        clientFormController.setClient(this);

        stage.setResizable(false);
        stage.setScene(new Scene(parent));
        stage.setTitle(name + "'s Chat");
        stage.show();

        System.out.println("done");
        stage.setOnCloseRequest(windowEvent -> {
            try {
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }
    public String getName() {
        return name;
    }
    public void receiveImg() throws IOException {
        String utf = dataInputStream.readUTF();
        int size = dataInputStream.readInt();
        byte[] bytes = new byte[size];
        dataInputStream.readFully(bytes);
        System.out.println(name + "-Image Received: from " + utf);
        clientFormController.setImg(bytes, utf);
    }
}
