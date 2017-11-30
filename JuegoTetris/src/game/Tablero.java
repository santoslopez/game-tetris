/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JOptionPane;

/**
 *
 * @author santoslopez
 */
public class Tablero {
    final int noColumnas = 10;
    final int noFilas = 20;
    final int lado = 28;
    
    //Declaracion de vector
    String tablero[][];
    
   
    SuperficieDeDibujo superficieDibujo;//nos va permitir acceder a los metodos, variables, etc
    
    
    //ancho y alto de la superficie de dibujo
    int widthSuperficieDibujo;
    int heightSuperficieDibujo;
    
    
    Dupla posicion;
    
    Imagen imagen;
    
    //constructor
    public Tablero(SuperficieDeDibujo superficieDibujo){
        this.superficieDibujo=superficieDibujo;
        calcularPosicion();
        
        //creamos una matriz, un tablero con filas y columnas
        tablero = new String[noColumnas][noFilas];
        
        casillasVacias();
    }
    
    //nos aseguramos que la matriz fila y columna su valor por defecto, null, se cambie por un texto vacio
    public void casillasVacias(){
        for (int y = 0; y < noFilas; y++) {
            for (int x = 0; x < noColumnas; x++) {
                tablero[x][y]="";
            }
        }
    }
    
    //vamos a dibujar en el buffer de forma directa
    public void draw(Graphics2D graphics){
        for (int y = 0; y < noFilas; y++) {
            for (int x = 0; x < noColumnas; x++) {
                
                //x*lado me da la distancia que necesito a partir de la primera columna
                Dupla posicionTemporal = new Dupla(x * lado + posicion.X,y * lado + posicion.Y);//va guarda la posicion en x y y 
                
                //dibujamos la casilla , los primeros dos parametros son las posiciones, los otros dos son las dimenciones
                if (tablero[x][y]=="") {
                    Color color1 = new Color(0,0,0);
                    Color color2 = new Color(0,0,0);
                    //nos da como residuo cero
                    if ((x+y)%2==0) {
                        color1 = new Color(153,0,0);
                        //color2 = new Color(254,173,156);
                        color2 = new Color(234,192,168);
                    }else{
                        
                        color1 = new Color(144,1,20);
                        color2 = new Color(206,3,30);
                    }
                    //seleccionamos una forma especial de pintar
                    graphics.setPaint(new GradientPaint(0,0,color1,0,20,color2,true));
                    
                    graphics.fillRect(posicionTemporal.intX(), posicionTemporal.intY(), lado, lado);
                    
                    //creamos un borde negro
                    graphics.setColor(new Color(0,0,0));
                    graphics.drawRect(posicionTemporal.intX(), posicionTemporal.intY(), lado, lado);
                //seguro tiene una letra
                }else{
                    imagen.dibujarPeriferico(new Dupla(x,y), graphics, tablero[x][y]);
                }
               
                //dibujar posicion dentro de la casilla
                if (false) {
                
                //el Font recibe 3 parametros, nombre de letra, estilo y tama;o
                graphics.setFont(new Font("verdana",Font.PLAIN,9));
                
                
                //le sumamos 11 porque la posicion en y esta desfasada hacia arriba debemos bajarla
                graphics.drawString(x+","+y, (int)posicionTemporal.X + 5, (int)posicionTemporal.Y+11);
                                
                }

            }
        }


    }
    
    public void calcularPosicion(){
        widthSuperficieDibujo = superficieDibujo.getWidth();
        heightSuperficieDibujo = superficieDibujo.getHeight();
        posicion = new Dupla((widthSuperficieDibujo - noColumnas * lado) / 2, (heightSuperficieDibujo - noFilas * lado) / 2);
    }
    
    
    public String obtener(int x,int y){
        if (y>=0) {
            return tablero[x][y];
        }            

        return "";//si estamos fuera del tablero
    }
    
    //Metodo que me borra todo los tetriminos cuando hemos perdidos 
    public void borrarTetriminos(){
        int Y = noFilas-1,lineas=0;
        while(Y>=0){
            int X = 0;
            
            //si la casilla esta llena incrementamos el contador
            while(X<noColumnas && !tablero[X][Y].equals((""))){
                X++;
            }
            
            //entonces la fila esta llena
            if (X==noColumnas) {
                lineas++;
                bajarBasurilla(Y);
            }else{
                Y--; 
            }
            
        }
        //aqui incrementamos las lineas del puntaje
        System.out.println("Lineas = "+lineas);
    }
    
    //limpiamos cuando hemos llenado la pantalla con los tetriminos
    public void bajarBasurilla(int y){
        for (int x = 0; x < noColumnas; x++) {
            tablero[x][y]="";
        }
        while(y>=0){
            for (int x = 0; x < noColumnas; x++) {
                tablero[x][y]=y==0?"":tablero[x][y-1];
            }
            y--;
        }
    }
}
