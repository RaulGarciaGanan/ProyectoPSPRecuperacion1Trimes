import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HiloServidor extends Thread {
    public Funciones fun;
    public KeyPair par;
    public PrivateKey privada;
    public PublicKey publica;
    public ArrayList<Usuario> usuarios;
    Socket c;

    public HiloServidor(ArrayList<Usuario> usuarios, Socket c) {
        this.usuarios = usuarios;
        this.c = c;
    }

    @Override
    public void run() {
        fun = new Funciones();
        String textoServer = "", texto = "", usuario = "";
        int menu = 0;
        boolean login;

        try {
            //Generamos la clave
            KeyPairGenerator keygen;

            keygen = KeyPairGenerator.getInstance("RSA");

            System.out.println("Conexion extablecida");
            par = keygen.generateKeyPair();
            privada = par.getPrivate();
            publica = par.getPublic();

            //Se crean los flujos
            ObjectOutputStream oos = new ObjectOutputStream(c.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(c.getInputStream());

            oos.writeObject(publica);
            oos.writeObject(privada);
            do {
                textoServer = "Tiene cuenta?, en lo contrario se le redireccionara a la pagina de registro(Y/N)";
                oos.writeObject(textoServer);

                texto = fun.descifrarRecibirMensaje((byte[]) ois.readObject(), privada);

                if (texto.equalsIgnoreCase("n")) {
                    String m = fun.descifrarRecibirMensajeUsuario(ois.readObject(), privada);
                    String[] result = m.split("\\$");
                    Usuario usu = new Usuario(result[0], result[1], result[2], result[3]);
                    usuarios.add(usu);
                    fun.escribirEnArchivoBinario(usuarios, "listadoCamareros.dat");
                    textoServer = "Usuario añadido a la base de datos";
                    System.out.println("Usuario añadido a la base de datos");
                    oos.writeObject(textoServer);
                }
            } while (!texto.equalsIgnoreCase("y"));
            do {
                String nombre = fun.descifrarRecibirMensaje((byte[]) ois.readObject(), privada);
                String contraseñaLogin = fun.descifrarRecibirMensaje((byte[]) ois.readObject(), privada);
                if (!fun.comprobarUsuario(nombre, contraseñaLogin)) {
                    login = false;
                    oos.writeBoolean(false);
                    textoServer = "Usuario o contraseña incorrectos";
                    oos.writeObject(textoServer);
                } else {
                    login = true;
                    oos.writeBoolean(true);
                    textoServer = "Usuario correcto";
                    oos.writeObject(textoServer);
                }
            } while (!login);
            do {
                oos.writeObject(fun.menuPrincipal());
                menu = Integer.parseInt(fun.descifrarRecibirMensaje((byte[]) ois.readObject(), privada));
                switch (menu) {
                    case 1:
                        textoServer = "Ya puede recoger su pedido: espaguetti boloñesa";
                        sleep(1000);
                        oos.writeObject(fun.cifrarMensaje(textoServer, publica));
                        break;
                    case 2:
                        textoServer = "Ya puede recoger su pedido: ensalada";
                        sleep(200);
                        oos.writeObject(fun.cifrarMensaje(textoServer, publica));

                        break;
                    case 3:
                        textoServer = "Ya puede recoger su pedido: chuleta";
                        sleep(1000);
                        oos.writeObject(fun.cifrarMensaje(textoServer, publica));
                        break;
                    case 4:
                        textoServer = "Ya puede recoger su pedido: chuletillas";
                        sleep(800);
                        oos.writeObject(fun.cifrarMensaje(textoServer, publica));
                        break;
                    case 5:
                        textoServer = "Gracias por usar nuestro programa";
                        oos.writeObject(fun.cifrarMensaje(textoServer, publica));
                        break;
                    default:
                        textoServer = "Debe seleccionar un numero de los que aparecen en el menu";
                        oos.writeObject(fun.cifrarMensaje(textoServer, publica));
                        ;
                        break;
                }
            } while (menu != 5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
