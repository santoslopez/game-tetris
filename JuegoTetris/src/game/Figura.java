/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import static java.awt.Transparency.OPAQUE;
import static java.awt.Transparency.TRANSLUCENT;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Figura {

    /**
     * CPS = Cambios por segundo, es la cantidad de veces que se ejecuta el hilo
     * en un segundo.
     */
    double CPS;
    /**
     * es el valor máximo que tomará como referencia para pintar un degradado
     * lineal que tenga la capacidad para cambiar. <code>Solo aplica en el degradado
     * lineal
     */
    double maxDegradadoLineal;
    /**
     * es el valor mínimo que tomará como referencia para pintar un degradado
     * lineal que tenga la capacidad para cambiar. <code>Solo aplica en el degradado
     * lineal
     */
    double minDegradadoLineal;
    /**
     * Es un arreglo de 2 Duplas con las que se grafica el degradado, sus
     * valores intervienen en posición y anchura. <code>Solo aplica en el degradado
     * lineal
     */
    Dupla[] Franja = new Dupla[2];
    /**
     * Es un arreglo de 2 Duplas que representan vectores que modificarán al
     * arreglo de Duplas engadas de graficar el degradado de la forma, sus
     * valores desplazan o modifican la posición y anchura. <code>Solo aplica en el
     * degradado lineal
     */
    Dupla[] DifFranja = new Dupla[2];
    /**
     * Es un dato de tipo ColorRGB que representa al colorPrincipal, el que se
     * mostrará cuando el degradado se desactive.
     */
    ColorRGB colorPrincipal;
    /**
     * Es un dato de tipo ColorRGB que representa al colorSecundario, el que se
     * ocultará cuando el degradado se desactive.
     */
    ColorRGB colorSecundario;
    /**
     * Determina si pintar solo al colorPrincipal o pintar el color principal y
     * el color secundario al mismo tiempo en un degradado.
     * <p>
     * <code> si es true ambos colores serán visibles</code>
     * <p>
     * <code> si es false solo el color principal será visible</code>.
     */
    boolean DegradadoActivado;
    /**
     * Determina si el degradado se moverá o desplazara.
     * <p>
     * <code> si es true se pintará un degradado radial</code>
     * <p>
     * <code> si es false se pintará un degradado lineal</code>.
     */
    boolean DegradadoMoviendose;

    long TInicio, TFin, Ttotal, DiferencialTransicion, dt;

    public Figura() {
        ReiniciarValores();
    }

    public void ReiniciarValores() {
        DegradadoActivado = true;
        DegradadoMoviendose = true;
        CPS = 30;
        maxDegradadoLineal = 3000;
        minDegradadoLineal = -3000;
        colorPrincipal = ColorRGB.aleatorio();
        colorSecundario = ColorRGB.Negro;
        colorSecundario.Detener();
        nuevasFranjasAleatorias();
    }

    public void RectanguloDegradadoDesactivable(Graphics2D g2d, Dupla Tamaño) {
        if (DegradadoActivado) {
            Rectangulo_2Colores(g2d, Tamaño);
        } else {
            Rectangulo_1Color(g2d, Tamaño);
        }
        DesplazarDegradado();
        controlador_LimitesDegradado();
    }

    public void Rectangulo_1Color(Graphics2D g2d, Dupla Tamaño) {
        g2d.setColor(colorPrincipal.Retornarse());
        g2d.fill(new Rectangle2D.Double(0, 0, Tamaño.X, Tamaño.Y));
    }

    public void Rectangulo_2Colores(Graphics2D g2d, Dupla Tamaño) {
        calcularDegradado(g2d, Tamaño);
        g2d.fill(new Rectangle2D.Double(0, 0, Tamaño.X, Tamaño.Y));
    }

    /**
     * Este método permite modificar la anchura y ángulo del degradado de forma
     * aleatoria con un vector para la velocidad aleatorio entre 0 y 1.
     */
    public void nuevasFranjasAleatorias() {
        nuevasFranjasAleatorias(1);
    }

    /**
     * Este método permite modificar la anchura y ángulo del degradado de forma
     * aleatoria con un vector para la velocidad aleatorio entre 0 y n.
     *
     * @param rangoMaximoDelVectorVelocidad se crea un vector para la velocidad
     * aleatorio entre 0 y esta variable
     */
    public void nuevasFranjasAleatorias(int rangoMaximoDelVectorVelocidad) {
        for (int i = 0; i < Franja.length; i++) {
            Franja[i] = new Dupla(aleatorio(maxDegradadoLineal), aleatorio(maxDegradadoLineal));
            DifFranja[i] = new Dupla(aleatorio(rangoMaximoDelVectorVelocidad), aleatorio(rangoMaximoDelVectorVelocidad));
        }
    }

    /**
     * Este método permite cambiar velocidad, anchura y ángulo del degradado de
     * forma aleatoria, con valores entre 0 y 1.
     * <p>
     * para cambiar el ángulo y anchura del degradado puedes usar a
     * nuevasFranjasAleatorias()
     *
     * @see #nuevasFranjasAleatorias
     */
    public void difDegradadoAleatorias() {
        difDegradadoAleatorias(1);
    }

    /**
     * Este método permite cambiar velocidad, anchura y ángulo del degradado de
     * forma aleatoria, con valores entre 0 y n.
     * <p>
     * para cambiar el ángulo y anchura del degradado puedes usar a
     * nuevasFranjasAleatorias()
     *
     * @param n
     * @see #nuevasFranjasAleatorias
     */
    public void difDegradadoAleatorias(int n) {
        for (int i = 0; i < DifFranja.length; i++) {
            DifFranja[i] = new Dupla(aleatorio(n), aleatorio(n));
        }
    }

    /**
     * Este método permite asignar una configuración de anchura y ángulo al
     * degradado.
     * <p>
     * confieso que no se exactamente como funciona, al principio pensé que se
     * trataba un par de puntos (x1,y1)(x2,y2) que manipulaban esto, pero luego
     * de hacer algunos experimentos no pude entender bien cómo funcionaba lo de
     * dirección y anchura
     *
     * @param A
     * @param B
     * @param C
     * @param D
     */
    public void diferencialesSombraAsignar(double A, double B, double C, double D) {
        DifFranja[0].sustituir(A, B);
        DifFranja[1].sustituir(C, D);
    }

    /**
     * Este método determina si se van a pintar dos colores o uno solo, cuando
     * se ejecute el color principal se dibujara como un color plano y el
     * secundario se ocultará.
     * <p>
     * DegradadoActivado = !DegradadoActivado;
     */
    public void colorSecundario_Visible_Invisible() {
        DegradadoActivado = !DegradadoActivado;
    }

    /**
     * Este método detiene o pone en movimiento el deplazamiento de los 2
     * colores.
     * <p>
     * DegradadoMoviendose = !DegradadoMoviendose;
     */
    public void Degradado_Mover_Detener() {
        DegradadoMoviendose = !DegradadoMoviendose;
    }

    /**
     * Este método mueve de forma suave la direccion y anchura del degradado.
     */
    private void DesplazarDegradado() {
        if (DegradadoMoviendose) {
            if (DegradadoRadial) {
                moverPosicionRadial();
                cambiarRadio();
            } else {
                for (int i = 0; i < Franja.length; i++) {
                    Franja[i].Adicionar(DifFranja[i]);
                }
            }
        }
    }

    /**
     * Este método controla que las franjas no se pasen de un número muy alto,
     * porque de ser así el degradado se expandería tanto que se volvería así
     * como un color plano.
     * <p>
     * con lo de franjas me refiero a las líneas de colores que van cambiando
     * respecto al tiempo.
     */
    private void controlador_LimitesDegradado() {
        for (int i = 0; i < Franja.length; i++) {
            Franja[i].X = (Franja[i].X > maxDegradadoLineal) ? maxDegradadoLineal : Franja[i].X;
            Franja[i].Y = (Franja[i].Y > maxDegradadoLineal) ? maxDegradadoLineal : Franja[i].Y;

            DifFranja[i].X = (Franja[i].X + DifFranja[i].X > maxDegradadoLineal//Determina que no se salga del valor máximo
                    || Franja[i].X + DifFranja[i].X < minDegradadoLineal) ? //Determina que no se salga del valor mínimo
                            -DifFranja[i].X : //Lo cambia de sentido
                            DifFranja[i].X; //No le hace nada

            DifFranja[i].Y = (Franja[i].Y + DifFranja[i].Y > maxDegradadoLineal//Determina que no se salga del valor máximo
                    || Franja[i].Y + DifFranja[i].Y < minDegradadoLineal) ? //Determina que no se salga del valor mínimo
                            -DifFranja[i].Y : //Lo cambia de sentido
                            DifFranja[i].Y; //No le hace nada
        }
    }

    /**
     * Este método calcula el degradado en el instante de ejecucion.
     * <p>
     * es una herramienta auxiliar que uso para generalizar el degradado de las
     * figuras
     */
    private void calcularDegradado(Graphics2D g2d, Dupla Tamaño) {
        if (DegradadoRadial) {
            LimRadial = Tamaño;
            DegradadoCircular roundGradient = new DegradadoCircular(PosRadial,
                    colorSecundario.Retornarse(), colorPrincipal.Retornarse(), (int) Radio);
            g2d.setPaint(roundGradient);
            moverPosicionRadial();
            cambiarRadio();
        } else {
            g2d.setPaint(new GradientPaint(Franja[0].intX(), Franja[0].intY(), colorSecundario.Retornarse(),
                    Franja[1].intX(), Franja[1].intY(), colorPrincipal.Retornarse(), true));
            g2d.fill(new Rectangle2D.Double(0, 0, Tamaño.X, Tamaño.Y));
        }
    }

    /**
     * Devuelve un valor aleatorio entre 0 y 1.
     * <p>
     * es una herramienta auxiliar que uso para acceder de forma rápida a esta
     * acción
     */
    private double aleatorio() {
        return Math.random();
    }

    /**
     * Devuelve un valor aleatorio entre 0 y un valor que se pase por parámetro.
     * <p>
     * es una herramienta auxiliar que uso para acceder de forma rápida a esta
     * acción
     *
     * @param n rango Máximo del aleatorio
     */
    private double aleatorio(double n) {
        return Math.random() * n;
    }

    /**
     * Baja un renglon en la consola.
     * <p>
     * es una herramienta auxiliar que uso para acceder de forma rápida a esta
     * acción
     */
    private void ImpNuevaLinea() {
        System.out.println("\n");
    }

    /**
     * Imprime un mensaje en la consola.
     * <p>
     * es una herramienta auxiliar que uso para acceder de forma rápida a esta
     * acción
     */
    private void msg(String msg) {
        System.out.println(msg);
    }

    /**
     * Imprime los valores RGB de un color en la consola.
     * <p>
     * es una herramienta auxiliar que uso para acceder de forma rápida a esta
     * acción
     */
    private void imprimirRGB(Color color) {
        msg("Color = R:" + color.getRed() + " -  G:" + color.getGreen() + " - B: " + color.getBlue());
    }

    /*
     * De aquí para abajo puedes omitir, era un experimento que estaba haciendo
     * Pero consume demasiado recurso del computador, lo que quería hacer era un
     * degradado radial, en forma de círculos, pero las transformaciones que le
     * he hecho al degradado lineal parece que ponen lento el computador al
     * Momento de ejecutarlo.
     *
     * dejar estas líneas de código de abajo no afecta en nada, solo se pone el
     * Computador lento cuando se ponen en funcionamiento.
     *
     * De todas maneras, si quieres experimentar solo es que pases todo lo
     * private a public, hay comentarios que logre dejar porque pensé que este
     * Experimento iba a resultar exitoso.
     *
     * Si pones la variable DegradadoRadial en true automáticamente se dibujara
     * un degradado de forma circular, pero como digo, no lo recomiendo porque
     * Al ejecutarlo el computador tiene que hacer demasiadas operaciones por
     * Esta forma de hacer degradados.
     *
     * Eso lo puedes comprobar comparando los FPS, IPS, APS de la clase control
     * Principal de 17 millones de iteraciones pasa a tan solo 100 o 200, lo
     * Cual me parece un cambio desastroso.
     *
     * No borro estas líneas solo porque me tomo como 3 días armar el
     * Algoritmo para hacer degradados a base de círculos y sé que a algunas
     * Personas les puede parecer interesante o útil.
     *
     * Si llegas a encontrar o conoces una forma mucho más óptima de lograr este mismo
     * Efecto no dudes en hacérmelo saber, estaría muy agradecido.
     *
     */
    /**
     * Es un punto respecto a la posición de la figura que determina en que
     * posición irá el color principal para desvanecerse suavemente hasta el
     * color secundario. <code>solo aplica en el degradado radial
     */
    private Dupla PosRadial = new Dupla(0, 0);
    /**
     * Determina si pintar de manera radial o lineal.
     * <p>
     * <code> si es true se pintará un degradado radial</code>
     * <p>
     * <code> si es false se pintará un degradado lineal</code>.
     */
    private boolean DegradadoRadial = false;
    //esta es la variable que tienes que cambiar a true si quieres experimentar
    /**
     * Es un punto respecto a la posición de la figura que determina valor
     * máximo que puede tomar la posición radial en X y en Y. <code>solo aplica en el degradado radial
     */
    private Dupla LimRadial = new Dupla(0, 0);
    /**
     * Es un vector que desplazará la posición radial en X y en Y con cada
     * iteración del hilo a través del límite radial. <code>solo aplica en el degradado radial
     */
    private Dupla movRadial = new Dupla(Math.random() * 5, Math.random() * 5);
    /**
     * Es un valor que determina la longitud del degradado desde el color
     * principal hasta el color secundario. <code>solo aplica en el degradado radial
     */
    private double Radio = aleatorio() * 600;
    /**
     * Es un vector que da la impresión de inflar o desinflar el degradado
     * cambiando el valor del radio. <code>solo aplica en el degradado radial
     */
    private double inflaciónRadial = aleatorio(5);
    /**
     * Es un valor que determina la longitud máxima que puede tomar el radio. <code>
     * solo aplica en el degradado radial
     */
    private double MaximaInflacion = Radio;
    /**
     * Es un valor que determina la longitud mínima que puede tomar el radio. <code>
     * solo aplica en el degradado radial
     */
    private double MinimaInflacion = 50;

    private void modificar_MinimoRadial(double v) {
        if (v >= MaximaInflacion) {
            throw new Error("El valor mínimo del radio debe ser menor al máximo valor");
        } else {
            if (v <= 1) {
                throw new Error("El valor mínimo del radio debe ser mayor a 1");
            } else {
                MinimaInflacion = v;
            }
        }
    }

    private void modificar_MaximoRadial(double v) {
        if (v <= MinimaInflacion) {
            throw new Error("El valor máximo del radio debe ser mayor al mínimo");
        } else {
            MaximaInflacion = v;
        }

    }

    private void moverPosicionRadial() {
        PosRadial.Adicionar(movRadial);

        if (PosRadial.X > LimRadial.X) {
            movRadial.X = -movRadial.X;
            PosRadial.X = LimRadial.X + movRadial.X;
        }
        if (PosRadial.X < 0) {
            movRadial.X = -movRadial.X;
            PosRadial.X = 0;
        }
        if (PosRadial.Y > LimRadial.Y) {
            movRadial.Y = -movRadial.Y;
            PosRadial.Y = LimRadial.Y;
        }
        if (PosRadial.Y < 0) {
            movRadial.Y = -movRadial.Y;
            PosRadial.Y = 0;
        }
        Radio = Radio < MinimaInflacion ? MinimaInflacion : Radio;
        Radio = Radio > MaximaInflacion ? MaximaInflacion : Radio;
    }

    private void cambiarRadio() {
        if (Radio < MinimaInflacion || Radio > MaximaInflacion) {
            inflaciónRadial = -inflaciónRadial;
        }
        Radio += inflaciónRadial;
    }

    private class DegradadoCircular implements Paint {

        protected Point2D posicion;
        protected Point2D Radio;
        protected Color ColorPrincipal, ColorSecundario;

        public DegradadoCircular(Dupla Posicion, Color ColorSecundario, Color ColorPrincipal, int radio) {
            Radio = new Point(radio, radio);
            if (Radio.distance(0, 0) <= 0) {
                throw new IllegalArgumentException("El Radio del degradado debe ser mayor a cero (0)");
            }
            posicion = new Point2D.Double(Posicion.X, Posicion.Y);
            this.ColorPrincipal = ColorPrincipal;
            this.ColorSecundario = ColorSecundario;
        }

        @Override
        public int getTransparency() {
            int a1 = ColorPrincipal.getAlpha();
            int a2 = ColorSecundario.getAlpha();
            return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
        }

        @Override
        public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds,
                AffineTransform xform, RenderingHints hints) {
            Point2D transformedPoint = xform.transform(posicion, null);
            Point2D transformedRadius = xform.deltaTransform(Radio, null);
            return new RoundGradientContext(transformedPoint, ColorPrincipal, transformedRadius, ColorSecundario);
        }

        private class RoundGradientContext implements PaintContext {

            protected Point2D mPoint;
            protected Point2D mRadius;
            protected Color color1, color2;

            public RoundGradientContext(Point2D p, Color c1, Point2D r, Color c2) {
                mPoint = p;
                color1 = c1;
                mRadius = r;
                color2 = c2;
            }

            public void dispose() {
            }

            public ColorModel getColorModel() {
                return ColorModel.getRGBdefault();
            }

            public Raster getRaster(int x, int y, int w, int h) {
                WritableRaster raster = getColorModel().createCompatibleWritableRaster(w, h);

                int[] data = new int[w * h * 4];
                for (int j = 0; j < h; j++) {
                    for (int i = 0; i < w; i++) {
                        double distance = mPoint.distance(x + i, y + j);
                        double radius = mRadius.distance(0, 0);
                        double ratio = distance / radius;
                        if (ratio > 1.0) {
                            ratio = 1.0;
                        }

                        int base = (j * w + i) * 4;
                        data[base + 0] = (int) (color1.getRed() + ratio * (color2.getRed() - color1.getRed()));
                        data[base + 1] = (int) (color1.getGreen() + ratio * (color2.getGreen() - color1.getGreen()));
                        data[base + 2] = (int) (color1.getBlue() + ratio * (color2.getBlue() - color1.getBlue()));
                        data[base + 3] = (int) (color1.getAlpha() + ratio * (color2.getAlpha() - color1.getAlpha()));
                    }
                }
                raster.setPixels(0, 0, w, h, data);

                return raster;
            }
        }

    }

}






//Página de la documentación: https://sites.google.com/view/jeff-aporta/clases-para-java/figura
