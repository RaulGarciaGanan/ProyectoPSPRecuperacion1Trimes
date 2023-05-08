import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String dni;
    private String nTrabajador;
    private String contraseña;

    public Usuario() {
    }

    public Usuario(String nombre, String dni, String nTrabajador, String contraseña) {
        this.nombre = nombre;
        this.dni = dni;
        this.nTrabajador = nTrabajador;
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getnTrabajador() {
        return nTrabajador;
    }

    public void setnTrabajador(String nTrabajador) {
        this.nTrabajador = nTrabajador;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return nombre + "$" +
                dni + "$" +
                nTrabajador + "$" +
                contraseña + "$";
    }
}
