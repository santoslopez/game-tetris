
package game;

//import java.awt.Color;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


/**
 *
 * @author santoslopez
 */
public class Imagen {
    
    //crear instancia
    SuperficieDeDibujo superficieDibujo;
    
    //va representar la posicion del tablero
    Dupla posicionTablero;
    int lado;
    
    
    public Imagen(SuperficieDeDibujo superficieDibujo)
    {
        this.superficieDibujo=superficieDibujo;
        posicionTablero = superficieDibujo.tablero.posicion;
        lado = superficieDibujo.tablero.lado;
    }    
    
    
    //con esta funcion nos vamos a despreocupar de calcular a cada rato la posicion de cada casilla en la superficie de dibujo
    public Dupla calcularPosicion(Dupla posicion){
        int x=(int)(posicion.X * lado + posicionTablero.X);
        int y=(int)(posicion.Y * lado + posicionTablero.Y);
        return new Dupla(x,y);
    }
    
    
    public void dibujarPeriferico(Dupla posicion,Graphics graphics,String figuraTetrimino){
        //el switch es necesario para cambiarle de color a la figura dependiendo el nombre del tetrimino
        switch(figuraTetrimino){
            case "Z":
                graphics.setColor(Color.RED);
            break;//javafx
            case "S":
                graphics.setColor(Color.BLUE);
            break;            
            case "J":
                 graphics.setColor(Color.GREEN);
            break; 
            case "L":
                 graphics.setColor(Color.ORANGE);
            break;
            case "T":
                 graphics.setColor(Color.MAGENTA);
            break;
            case "O":
                 graphics.setColor(Color.YELLOW);
            break;
            case "I":
                 graphics.setColor(Color.CYAN);
            break;         
        }
        
        graphics.fillRect((int)calcularPosicion(posicion).X, (int)calcularPosicion(posicion).Y, lado, lado);
    }
    
    public BufferedImage cargarImagen(String nombre,String extension){
        BufferedImage imagen = null;
        try{
            imagen = ImageIO.read(ClassLoader.class.getResource("/imagenes/"+nombre+extension));
        }catch(Exception exception){
            JOptionPane.showMessageDialog(null, "La imagen no se encontro", "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return imagen;
    }
}
