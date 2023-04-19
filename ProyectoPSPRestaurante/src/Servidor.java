import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
    final static int puerto = 5000;
    public static void main(String[] args) {
        ServerSocket s;
        Socket c;
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            s= new ServerSocket(puerto);
        } catch (IOException e) {
            System.out.println("Error en la creacion del servidor");
            throw new RuntimeException(e);
        }
        while (true){
            try {
                c = s.accept();
            } catch (IOException e) {
                System.out.println("Error en la creacion del servidor");
                throw new RuntimeException(e);
            }
            HiloServidor hilo = new HiloServidor(usuarios,c);
            hilo.start();
        }
    }
}