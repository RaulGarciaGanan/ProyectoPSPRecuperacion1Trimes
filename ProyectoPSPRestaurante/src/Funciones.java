import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.util.*;


public class Funciones {
    public byte[] cifrarMensaje(String mensaje, PublicKey clave) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clave);

        //Lo ciframos en un array de bytes para enviarlo y no hacer conversiones al String
        byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes());
        return mensajeCifrado;
    }

    public byte[] cifrarMensajeUsuario(Usuario usu, PublicKey clave) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clave);

        //Lo ciframos en un array de bytes para enviarlo y no hacer conversiones al String
        byte[] mensajeCifrado = cipher.doFinal(usu.toString().getBytes());
        return mensajeCifrado;
    }

    public String descifrarRecibirMensaje(byte[] mensaje, PrivateKey privada) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        //Descibramos el mensjae que acabamos de recibir
        System.out.println("Mensaje cifrado recibido es: " + new String(mensaje));

        Cipher deschiper = Cipher.getInstance("RSA");
        deschiper.init(Cipher.DECRYPT_MODE, privada);

        String mDescifrado = new String(deschiper.doFinal(mensaje));

        System.out.println("Mensaje descifrado con clave privada " + mDescifrado);

        return mDescifrado;
    }

    public  String hasearContraseña(byte[] mensaje) throws NoSuchAlgorithmException {
        byte[] resumen = null;
        MessageDigest alg = MessageDigest.getInstance("SHA-256");
        alg.reset();
        alg.update(mensaje);
        resumen = alg.digest();
        return aHexadecimal(resumen);
    }

    public  String aHexadecimal(byte[] mensaje){
        StringBuilder sb = new StringBuilder(mensaje.length*2);
        try(Formatter formatter = new Formatter(sb)){
            for(byte b : mensaje){
                formatter.format("%02x",b);
            }
        }
        return sb.toString();
    }

    public void escribirEnArchivoBinario(ArrayList<Usuario> listaUsuarios, String nombreArchivo) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(nombreArchivo);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(listaUsuarios);
        } catch (IOException ex) {
            System.out.println("Error al escribir en el archivo: " + ex.getMessage());
}
    }

    public ArrayList<Usuario> leerArchivoBinario(String nombreArchivo) {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(nombreArchivo);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            listaUsuarios = (ArrayList<Usuario>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error al leer del archivo: " + ex.getMessage());
        }
        return listaUsuarios;
    }

    public String menuPrincipal() {
        String menu = new String("Bienvenido al restaurante, que plato desea elegir: \n" +
                "1. Espaguetti boloñesa \n" +
                "2. Ensalada \n" +
                "3. Chuleta \n" +
                "4. Chuletillas\n" +
                "5. Salir");
        return menu;
    }
}
