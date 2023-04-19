import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cliente {
    final static int puerto = 5000;
    private static Socket s;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static Funciones fun;

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//Nos conectamos
        s = new Socket("localhost", puerto);
        Scanner sc = new Scanner(System.in);
        fun = new Funciones();
        String mensaje = "", nTrabajador = "", nombre = "", dni = "";
        int menu = 0;
        byte[] contraseña;
        boolean datosCorrectos = false, enAplicacion = true;


        //Se crean los flujos
        oos = new ObjectOutputStream(s.getOutputStream());
        ois = new ObjectInputStream(s.getInputStream());
        System.out.println("Leemos la clave");

        //Obtenemos la clave publica
        PublicKey clave = (PublicKey) ois.readObject();
        System.out.println("Clave recibida: " + clave);
        do {
            System.out.println(ois.readObject());
            mensaje = sc.nextLine();
            oos.writeObject(fun.cifrarMensaje(mensaje, clave));
            if (mensaje.equalsIgnoreCase("n")) {
                //Pedimos nombre
                System.out.println("Introduce tu nombre");
                nombre = sc.nextLine();
                //Pedimos el numero del trabajador
                while (!datosCorrectos) {
                    System.out.println("Introduce tu numero de empleado (2 Letras y 3 numeros)");
                    nTrabajador = sc.nextLine();
                    String regex = "(^[a-zA-Z]{2})([0-9]{3}$)";
                    Pattern patron = Pattern.compile(regex);
                    Matcher matcher = patron.matcher(nTrabajador);
                    if (matcher.matches()) {
                        datosCorrectos = true;
                    } else {
                        System.out.println("El numero debe estar escrito de forma correcta");
                        datosCorrectos = false;
                    }
                }
                datosCorrectos = false;
                //Pedimos el DNI
                while (!datosCorrectos) {
                    System.out.println("Introduce tu DNI");
                    dni = sc.nextLine();
                    String regex = "[0-9]{7,8}[A-Z a-z]";
                    Pattern patron = Pattern.compile(regex);
                    Matcher matcher = patron.matcher(dni);
                    if (matcher.matches()) {
                        datosCorrectos = true;
                    } else {
                        System.out.println("El dni debe estar escrito de forma correcta");
                        datosCorrectos = false;
                    }
                }

                //Pedimos contraseña
                System.out.println("Introduce tu contraseña");
                contraseña = sc.nextLine().getBytes();
                oos.writeObject(fun.cifrarMensajeUsuario(new Usuario(nombre, dni, nTrabajador, fun.hasearContraseña(contraseña)), clave));
                //oos.writeObject(new Usuario(nombre, dni, nTrabajador, fun.hasearContraseña(contraseña)));
            }
        } while (!mensaje.equalsIgnoreCase("y"));
    }
}
