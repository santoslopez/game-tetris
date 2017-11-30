
package game;

import java.awt.Graphics;
import javax.swing.JOptionPane;

/**
 *
 * @author santoslopez
 */
public class Pieza {
    
    
    /*
    En la clase Pieza.java nos aseguramos que no se dibuje fuera del limite del tablero
    */
    
    SuperficieDeDibujo superficieDeDibujo;
    Tablero tablero;
    Imagen imagen;
    Dupla posicion = new Dupla(3,10);//esto es la posicion de origen
    
    //mostramos un tetrimino distinto al empezar el juego
    Tetrimino piezaActual = Tetrimino.aleatorio();
    
    //Arreglo de tetrimino almacenar las 7 piezas -> luego lo usamos para mostrar las proximas piezas
    Tetrimino proximosTetriminos[] = new Tetrimino[7];
    
    
    public Pieza(SuperficieDeDibujo superficieDeDibujo){
        this.superficieDeDibujo = superficieDeDibujo;
        this.tablero=superficieDeDibujo.tablero;
        this.imagen=superficieDeDibujo.imagen;
        
        /*generamos 7 tetriminos aleatorios, que posteriormente vamos a usar para mostrarlos en pantalla
        como el proximo tetrimino que viene*/
        for (int i = 0; i < proximosTetriminos.length; i++) {
          proximosTetriminos[i] = Tetrimino.aleatorio();
        }
    }
    
    /*metodo que se encarga de dibujar las piezas, en cualquier posicion, para cambiar la posicion con
    la que empieza en donde instanciamos necesitamos cambiarle la posicion
    */
    public void dibujar(Graphics graphics){
        for (int i = 0; i < 4; i++) {
             imagen.dibujarPeriferico(new Dupla(posicion.X+piezaActual.periferico[i].X,posicion.Y+piezaActual.periferico[i].Y), graphics,piezaActual.nombre);  
        }
        for (int i = 0; i < proximosTetriminos.length; i++) {
            Dupla posicion = new Dupla(12,i+i*3);//esto es para que las piezas se dibujen separadas
            for (int j = 0; j < proximosTetriminos[i].periferico.length; j++) {
                double xReal = proximosTetriminos[i].periferico[j].X + posicion.X;
                double yReal = proximosTetriminos[i].periferico[j].Y + posicion.Y;
                imagen.dibujarPeriferico(new Dupla(xReal,yReal), graphics, proximosTetriminos[i].nombre);
            }
        }
    }
    
    public void moverDerecha(){
        posicion.movimientoDerecha();
        if (movimientoErroneo()) {
            posicion.movimientoIzquierda();
        }
    }
    
    //darle inteligencia al metodo para que no realice el movimiento si estamos en una columna<=0 o columna>=19
    
    public void girarDerecha(){
        piezaActual.girarDerecha();//si el movimiento es erroneo
        if (movimientoErroneo()) {
            piezaActual.girarIzquierda();
        }
    }    
    public void moverIzquierda(){
        posicion.movimientoIzquierda();
        if (movimientoErroneo()) {
            posicion.movimientoDerecha();
        }
    }    
    
    public void moverAbajo(){
        posicion.movimientoAbajo();
        if (movimientoErroneo()) {
            posicion.movimientoArriba();
            //depositimas el tetrimino y generamos otro
            System.out.println("Tetrimino depositado. Generando tetrimino aleatoriamente...");
             almacenarEnTablero();
             
            reiniciarTetrimino();//generamos un tetrimino aleatoriamente en una posicion establecida
            tablero.borrarTetriminos();
        }
    }    
        
    /*metodo que permite blockear el tetrimino en caso este movimiento es invalido.
    por ejemplo, si la figura quisiera moverse mas que el numero de columnas, no debe permitirse
    */
    public boolean movimientoErroneo(){
        for (int i = 0; i < piezaActual.periferico.length; i++) {
            double posicionXActual = piezaActual.periferico[i].X + posicion.X; 
            double posicionYActual = piezaActual.periferico[i].Y + posicion.Y;
            
            /*
            no va permitir que se mueva mas a la derecha si el tope de columnas ya llego 
            o si estamos moviendonos a la izquierda pero estamos en la primera columna
            */
            if (posicionXActual>tablero.noColumnas-1 || posicionXActual<0) {
                 System.out.println("columna derecha");
                return true;//El movimiento es erroneo
            }
            //movimiento hacia abajo, si llegamos a la ultima fila entonces ya no debemos permitir movernos
            if (posicionYActual>tablero.noFilas-1) {
                 System.out.println("columna izquierda");
                return true;//el movimiento es erroneo
            }
            //significa que hay algo en la casilla
            if (!(tablero.obtener((int)posicionXActual, (int)posicionYActual).equals("") )) {
                return true;//se va detener la pieza y volvera a reiniciarse la pieza
                
            }
        }
        return false;//el movimiento es permitido
    }
    
    //cuando una pieza ya se agrego a la ultima fila entonces reiniciamos otro tetrimino
    public void reiniciarTetrimino(){
       
        posicion = new Dupla(4,0);//posicion que le damos por defecto
        //piezaActual = Tetrimino.aleatorio();//generamos una figura del juego aleatoriamente
        
        //usamos el arreglo de tetriminos proximos creados para desplegarlo en el juego
        piezaActual = proximosTetriminos[0];//
        
        //necesario para que el primero tome el valor del segundo,el segundo del tercero, etc
        
        for (int i = 0; i < proximosTetriminos.length-1; i++) {
            proximosTetriminos[i] = proximosTetriminos[i+1];
        }
        proximosTetriminos[proximosTetriminos.length-1]=Tetrimino.aleatorio();//cuando llegamos al final del arreglo generamos otro tetrimino
    }
    
    public void almacenarEnTablero(){
        try{
            for (int i = 0; i < piezaActual.periferico.length; i++) {
                //aqui vamos almacenar el periferico
                int columna=piezaActual.periferico[i].intX()+posicion.intX(),fila=piezaActual.periferico[i].intY()+posicion.intY();

                String dato=piezaActual.nombre;

                tablero.tablero[columna][fila] = dato;//almacenamos la pieza en el tablero

            }            
        }catch(Exception e){
            //si genera un error significa que ya perdimos, o sea que llenamos las casillas        
            String opciones[] = {"Jugar","Salir"};
            String mensaje = "¿Quieres reiniciar el juego o salir?";
            String titulo = "Tetris";
            int confirmarReinicio = JOptionPane.showOptionDialog(null,mensaje,titulo,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones,"Aceptar");
            if (confirmarReinicio==JOptionPane.YES_OPTION) {
           
            }else{
                System.exit(0);
            }
            //JOptionPane.showMessageDialog(null, "Perdiste", "Game over", JOptionPane.ERROR_MESSAGE);
        }

    }
    
}
