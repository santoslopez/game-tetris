
package game;

public class Tetrimino {
    //
    Dupla periferico[];
    String nombre;
    
    //los periferiocs son los minos, o cuadrito de cada pieza
    public Tetrimino(Dupla periferico0,Dupla periferico1,Dupla periferico2, Dupla periferico3,String nombre ){
        periferico = new Dupla[4];
        periferico[0] = periferico0;
        periferico[1] = periferico1;
        
        periferico[2] = periferico2;
        
        periferico[3] = periferico3;
        this.nombre=nombre;
    }
    
    //mapa de los tetriminos para llamar a cade pieza o dibujo. Por ejemplo mostramos la l, s, etc
    //de forma rapida
    
    public static Tetrimino consultar(String nombre){
        //dependiendo del valor tomamos una decision
        
        //consultar nombre
        switch(nombre){
            case"Z":
            return new Tetrimino(new Dupla(0,0),new Dupla(-1,-1),new Dupla(0,-1),new Dupla(1,0),"Z");
           
             
            case"S":
            return new Tetrimino(new Dupla(0,0),new Dupla(-1,0),new Dupla(0,-1),new Dupla(1,-1),"S");
            
             
            case"J":
            return new Tetrimino(new Dupla(0,0),new Dupla(-1,-1),new Dupla(-1,0),new Dupla(1,0),"J");
            
               
            case"L":
            return new Tetrimino(new Dupla(0,0),new Dupla(-1,0),new Dupla(1,-1),new Dupla(1,0),"L");
            
               
            case"T":
            return new Tetrimino(new Dupla(0,0),new Dupla(0,-1),new Dupla(-1,0),new Dupla(1,0),"T");
           
            case"O":
            return new Tetrimino(new Dupla(0,0),new Dupla(0,-1),new Dupla(1,-1),new Dupla(1,0),"O");

            
            case"I":
                //En la ultima posicion es valido -2,0
            return new Tetrimino(new Dupla(0,0),new Dupla(-1,0),new Dupla(1,0),new Dupla(2,0),"I");
            
        }
        
        //Tetrimino vacio por defecto
        return new Tetrimino(new Dupla(0,0),new Dupla(-1,0),new Dupla(1,0),new Dupla(0,0),"");
        
    }
    
    public void girarDerecha(){
        for (int i = 0; i < periferico.length; i++) {
            periferico[i].girarDerecha();
        }
    }
    public void girarIzquierda(){
        for (int i = 0; i < periferico.length; i++) {
            periferico[i].girarIzquierda();
        }
    }
    
    //Metodo que genera un tetrimino aleatorio
    public static Tetrimino aleatorio(){
        String todosTetriminos[] = {"Z","S","J","L","T","O","I"};
        
        //generamos un numero aleatorio de 0 a 7
        return consultar(todosTetriminos[(int)(Math.random()*6.9)]);
    }
}
