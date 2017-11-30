
package game;

/**
 *
 * @author santoslopez
 */
public class ControlPrincipal {
    
    private static SuperficieDeDibujo superficieDibujo;
    private static int aps=0;//actualizaciones por segundo
    private static int fps=0;//fotogramas por segundo
    
    public static void main(String[] args){
        startGame();
    }
    
    public static void startGame(){
        createWindow(900,780,"Juego de tetris");
        iterarBuclePrincipal();
    }
    
    public static void createWindow(int width, int height,String name){
        superficieDibujo = new SuperficieDeDibujo(width,height);
        Ventana ventana = new Ventana(name,superficieDibujo);     
    }
    
    //repintar en el jframe, que tenga efecto de video - cambiar fotograma unas 30 veces por segundo
    public static void iterarBuclePrincipal(){
        
        //con final nos aseguramos que mantendran sus valores las variables y no seran sospechosos de sufrir cambios en el futuro
        final int nsXsegundo = 1000000000;
        final int apsObjetivo = 60;//actualizar 60 veces por segundo para que quede una animacion fluida
        
        //cantidad de nano segundos que transcurren para hacer una actualizacion 
        final int nsxUpdate = nsXsegundo / apsObjetivo;
        
        //refrescar fotograma
        
        //System.nanoTime disponible a partir de Java 5, el juego puede ser corrido en windows 98
        
        long tiempoDeReferenciaActualizacion  = System.nanoTime();//devolvemos con precision de nano segundo en donde nos encontramos
        long tiempoDeReferenciaContador = System.nanoTime();
        
        double delta = 0;
        double tiempoSinProcesar;
        
        //permanecemos en el juego hasta que el jugador decide salirse o termina el juego
        while(true){
            long tiempoInicial = System.nanoTime();//el momento exacto en que se ejecuta la linea
            
            
                    
            //averiguamos cuanto tiempo ha transcurrido desde que ejecutamos la primera referencia de la segunda
            
            //la segunda a de ser mayor a la primera se coloca de primero
            tiempoSinProcesar = tiempoInicial - tiempoDeReferenciaActualizacion;
            tiempoDeReferenciaActualizacion = tiempoInicial;
            
            //nos ayuda a que delta nos sirva para decidir si ha llegado el momento indicado para hacer una actualizacion
            delta += tiempoSinProcesar / nsxUpdate;
            
            while(delta>=1){
                //actualizar
                delta--;
                update();
            }
            
            draw();//dibujamos
            
            //cada segundo se muestre un letrero con los aps y fps 
            
            if (System.nanoTime() - tiempoDeReferenciaContador >= nsXsegundo) {
                System.out.println("APS = "+aps + " FPS = "+fps);
                tiempoDeReferenciaContador = System.nanoTime();
                aps = 0;
                fps = 0;
            }
        }
        
    }
    
    public static void update(){
        aps++;
        superficieDibujo.draw();
    }
    
    public static void draw(){
        fps++;
        
    }
}

