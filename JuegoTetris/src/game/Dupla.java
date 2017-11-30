package game;


/**
 *
 * @author santoslopez
 */
public class Dupla {
    
    double X;
    double Y;

    public Dupla(double X, double Y) {
        this.X = X;
        this.Y = Y;
    }
    
    public void sustituir(double a,double b){
        X = a;
        Y = b;
    }
    
    /*Metodos adicionales, necesarios para trabajarlo con la clase Figura.java*/
    
    public int intX(){
        return (int)X;
    }
    
    public int intY(){
        return (int)Y;
    }
    
    public void Adicionar(Dupla dupla){
        X+=dupla.X;
        Y+=dupla.Y;
    }
    
    /*Fin de metodos adicionales para la clase Figura.java*/
    
    
    /**
     * 
     * Creacion de metodos para darle movimiento (izquierda, derecha, arriba y abajo) 
       a las piezas o tetriminos del juego (rectangulo, l, cuadrado, etc) 
       
       Para darle movimiento al tetrimino unicamente jugamos con las cordenadas, eje X y Y
        
     */
    
    public void movimientoDerecha(){
        X++;//cuando presionamos la tecla hacia la derecha nos movemos en el eje x hacia la derecha
    }
    
    public void movimientoIzquierda(){
        X--;//cuando presionamos la tecla hacia la izquierda nos movemos en el eje x en el lado izquierdo

    }
    
    public void movimientoArriba(){
        Y--;//cuando presionamos la tecla hacia arriba nos movemos en el eje y hacia arriba
    }
    
    public void movimientoAbajo(){
        Y++;//cuando presionamos la tecla hacia la abajo nos movemos en el eje y hacia abajo

    }

    public double getX() {
        return X;
    }

    public void setX(double X) {
        this.X = X;
    }

    public double getY() {
        return Y;
    }

    public void setY(double Y) {
        this.Y = Y;
    }    
    
    /*Basicamente se juega con las coordenadas, por ejemplo:
    (X,Y) ---> (Y,-X) girando 90 grados a la derecha
    (X,Y) ---> (-Y,X) girando 90 grados a la izquierda
    */
    public void girarDerecha(){
        double temporal = X;
        X = -Y;//
        Y = temporal;
    }
    
    public void girarIzquierda(){
        double temporal  = X;
        X= Y;
        Y = -temporal;
    }
}
