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
        boolean enAplicacion = false, datosCorrectos = false;
        byte[] contraseña;

        try {
            //Generamos la clave
            KeyPairGenerator keygen;

            keygen = KeyPairGenerator.getInstance("RSA");

            System.out.println("Generando claves");
            par = keygen.generateKeyPair();
            privada = par.getPrivate();
            publica = par.getPublic();

            //Se crean los flujos
            ObjectOutputStream oos = new ObjectOutputStream(c.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(c.getInputStream());

            oos.writeObject(publica);
            System.out.println("Clave publica: " + publica);
            do {
                textoServer = "Tiene cuenta?, en lo contrario se le redireccionara a la pagina de registro(Y/N)";
                oos.writeObject(textoServer);

                texto = fun.descifrarRecibirMensaje((byte[]) ois.readObject(), privada);

                if (texto.equalsIgnoreCase("n")) {

                    Usuario usu = (Usuario) ois.readObject();
                    insertarUsuario(usu);
                    textoServer = "Usuario añadido a la base de datos";
                    oos.writeObject(textoServer);
                    texto = fun.descifrarRecibirMensaje((byte[]) ois.readObject(), privada);
                }

            } while (!texto.equalsIgnoreCase("y"));
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
        }
    }

    public void insertarUsuario(Usuario usu)  {
        usuarios.add(usu);

    }

    public void cargarArrayUsu(ArrayList<Usuario> usuarios){
        Usuario usu = new Usuario("raul","AS123","1234567A","12345a");
        usuarios.add(usu);

    }
}
