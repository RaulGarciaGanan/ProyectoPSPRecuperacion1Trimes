import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Servidor {
    private static Funciones fun;
    final static int puerto = 5000;
    public static void main(String[] args) throws NoSuchAlgorithmException {
        fun = new Funciones();
        ServerSocket s;
        Socket c;
        ArrayList<Usuario> usuarios = new ArrayList<>();
        //La funcion de abajo esta comenta por si en algun momento del proceso de programacion el archivo binario se estropea y hay que volver a cargarlo
        //fun.cargarArchivoBinario(usuarios);
        usuarios =  fun.leerArchivoBinario("listadoCamareros.dat");


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
                System.out.println("Error en la conexion del servidor");
                throw new RuntimeException(e);
            }
            HiloServidor hilo = new HiloServidor(usuarios,c);
            hilo.start();

        }
    }
}