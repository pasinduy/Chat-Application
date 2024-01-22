package lk.ijse.Server;

import lk.ijse.Controller.ServerFormController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    public static final List<ClientHandler> clientHandlerList = new ArrayList<>();
    public static ServerFormController serverFormController;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final String clientName;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        clientName = dataInputStream.readUTF();
        clientHandlerList.add(this);
    }

    public static void broadcast(String msg) throws IOException {
        for (ClientHandler handler : clientHandlerList) {
            handler.sendMessage("Server", msg);
        }
    }

    @Override
    public void run() {
        l1: while (socket.isConnected()) {
            try {
                String utf = dataInputStream.readUTF();
                if (utf.equals("*image*")) {
                    receiveImage();
                } else {
                    for (ClientHandler handler : clientHandlerList) {
                        if (!handler.clientName.equals(clientName)) {
                            handler.sendMessage(clientName, utf);
                        }
                    }
                }
            } catch (IOException e) {

                clientHandlerList.remove(this);
//                System.out.println(clientName+" removed");
//                System.out.println(clientHandlerList.size());
                break;
            }
        }
    }

    public void sendMessage(String sender, String msg) throws IOException {
        dataOutputStream.writeUTF(sender + ": " + msg);
        dataOutputStream.flush();
    }

    private void receiveImage() throws IOException {
        int size = dataInputStream.readInt();
        byte[] bytes = new byte[size];
        dataInputStream.readFully(bytes);
        for (ClientHandler handler : clientHandlerList) {
            if (!handler.clientName.equals(clientName)) {
                handler.sendImage(clientName, bytes);
//                System.out.println(clientName+" - image sent ");
            }
        }
    }

    private void sendImage(String sender, byte[] bytes) throws IOException {
        dataOutputStream.writeUTF("*image*");
        dataOutputStream.writeUTF(sender);
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
        dataOutputStream.flush();
//        System.out.println("Image Sent");
    }
}
