import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
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
        String mensaje = "", nTrabajador = "", nombre = "", dni = "", usuario = "", contraseñaLogin = "", contraseña;
        int menu = 0;
        byte[] contraseñaBytes = new byte[0];
        boolean datosCorrectos = false, login = true;


        //Se crean los flujos
        oos = new ObjectOutputStream(s.getOutputStream());
        ois = new ObjectInputStream(s.getInputStream());


        //Obtenemos la clave publica
        PublicKey publica = (PublicKey) ois.readObject();
        PrivateKey privada = (PrivateKey) ois.readObject();
        do {
            System.out.println(ois.readObject());
            mensaje = sc.nextLine();
            oos.writeObject(fun.cifrarMensaje(mensaje, publica));
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
                        System.out.println("El dni debe estar escrito de forma con 8 numeros y una letra");
                        datosCorrectos = false;
                    }
                }
                datosCorrectos = false;
                //Pedimos contraseña
                while (!datosCorrectos) {
                    System.out.println("Introduce tu contraseña");
                    contraseña = sc.nextLine();
                    String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$";
                    Pattern patron = Pattern.compile(regex);
                    Matcher matcher = patron.matcher(contraseña);
                    if (matcher.matches()) {
                        datosCorrectos = true;
                        contraseñaBytes = contraseña.getBytes();
                    } else {
                        System.out.println("La contraseña debe tener 4 caracteres como minimo y 8 como maximo, y tiene que contener numero, letra mayuscula y  minuscula");
                        datosCorrectos = false;
                    }
                }
                oos.writeObject(fun.cifrarMensajeUsuario(new Usuario(nombre, dni, nTrabajador, fun.hasearContraseña(contraseñaBytes)), publica));

                System.out.println(ois.readObject());
            } else{
                System.out.println("Debe introducir \"y\" para continuar a la aplicacion  o \"n\" para registrarse");
            }
        } while (!mensaje.equalsIgnoreCase("y"));
        do {
            System.out.println("Introduce el nombre de usuario");
            usuario = sc.nextLine();
            oos.writeObject(fun.cifrarMensaje(usuario, publica));
            System.out.println("Introduce el contraseña ");
            contraseñaLogin = sc.nextLine();
            oos.writeObject(fun.cifrarMensaje(contraseñaLogin, publica));

            login = ois.readBoolean();
            if (!login) {
                System.out.println(ois.readObject());
            }else{
                System.out.println(ois.readObject());
            }
        } while (!login);
        do {
            System.out.println(ois.readObject());
            try {
                menu = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("La seleccion debe ser numerica");
            }
            oos.writeObject(fun.cifrarMensaje(String.valueOf(menu), publica));
            System.out.println(fun.descifrarRecibirMensaje((byte[]) ois.readObject(), privada));
        } while (menu != 5);
    }
}
