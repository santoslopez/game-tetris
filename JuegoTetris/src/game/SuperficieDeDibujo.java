package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

/**
 *
 * @author santoslopez
 */
public class SuperficieDeDibujo extends Canvas{
    private BufferStrategy buffer;
    
    private Graphics graphics;
    Tablero tablero;
    
    Imagen imagen;
    Pieza pieza;
    //ColorRGB colorRGB =ColorRGB.Rojo_Escarlata;//utilizamos la clase ColorRGB para dar un color de inicio del juego
    
    Figura fondo = new Figura();
    //fijar un ancho y alto que se le indique
    public SuperficieDeDibujo(int width,int height){
        setSize(width,height);
        
        //le pasamos esta superficie de dibujo
        tablero = new Tablero(this);
        //this.setPreferredSize(new Dimension(width,heigth));
        
        imagen = new Imagen(this);
        tablero.imagen = imagen;
        pieza = new Pieza(this);
        
        //invocamos al metodo que se encarga del teclado presionado o soltado
        capturarTeclado();
        //por defecto la captura de eventos viene desactivado, lo vamos habilitar
        setFocusable(true);//vamos a querer activar los eventos
    }
    
    public void draw(){
        //cargamos con una estrategia de buffer para el canvas
        buffer = getBufferStrategy();
    
        if(buffer==null){
            createBufferStrategy(2);
            return;//escapamos del metodo
        }
        
        //dibujamos entre el buffer
        graphics = buffer.getDrawGraphics();
        fondo.RectanguloDegradadoDesactivable((Graphics2D)graphics, tamano());
        
        
        
        
        /*Seleccionamos un color de fondo aleatorio*/
        //graphics.setColor(colorRGB.Retornarse());
        //graphics.fillRect(0, 0, this.getWidth(), this.getHeight());//dibujamos un rectangulo del tamano de la ventana
        
        //seleccionamos un color para el tablero, el lugar donde esta la matriz con el tetrimino o pieza
        //graphics.setColor(Color.BLACK);
        
        //dibujando
        tablero.draw((Graphics2D)graphics);
        
        //graphics.fillOval(0, 0, 100, 100);
        pieza.dibujar(graphics);
        
        graphics.dispose();//desechamos el contenido de graphics (en la ram de cosas innecesarias) hasta volverlo a utilizar        
        buffer.show();
    }
    
    public Dupla tamano(){
        return new Dupla(this.getWidth(),this.getHeight());
    }
    //Metodo que sirve para capturar las teclas presionadas por el usuario
    public void capturarTeclado(){
        addKeyListener(new KeyListener() {
            @Override
            /*no es necesario para el juego pero no lo ponemos de comentario de lo contrario genera error por ser metodos abstractos*/
            public void keyTyped(KeyEvent e) {               
            }

            @Override
            //averiguamos que tecla se esta presionando
            public void keyPressed(KeyEvent e) {
                //System.out.println(e); -----> me devuelve que teclado fue presionado
                
                //verificamos si la tecla presionada es derecha
                if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
                    pieza.moverDerecha();
                    //nos salimos del metodo ya que no tendria sentido que el programa siguiera comprobando las demas teclas
                    return;
                    
                //verificamos si la tecla presionada es izquierda
                }else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
                    pieza.moverIzquierda();
                    //nos salimos del metodo ya que no tendria sentido que el programa siguiera comprobando las demas teclas
                    return;
                    
                //verificamos si la tecla presionada es arriba
                }else if (e.getKeyCode()==KeyEvent.VK_UP) {
                    //pieza.posicion.movimientoArriba();
                    pieza.girarDerecha();
                    //nos salimos del metodo ya que no tendria sentido que el programa siguiera comprobando las demas teclas
                    return;
                    
                //verificamos si la tecla presionada es abajo
                }else if (e.getKeyCode()==KeyEvent.VK_DOWN) {
                    pieza.moverAbajo();
                    //nos salimos del metodo ya que no tendria sentido que el programa siguiera comprobando las demas teclas
                    return;
                }
            }

            @Override
            //sirve para que tecla fue soltada
            public void keyReleased(KeyEvent e) {
  
            }
        });       
    }
}