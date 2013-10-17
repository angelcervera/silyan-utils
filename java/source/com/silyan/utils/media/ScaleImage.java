/**
 * 
 */
package com.silyan.utils.media;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * @author Angel Cervera Claudio ( email: angelcervera@silyan.com )
 *
 */
public class ScaleImage {
	
	private static String LOGNAME = ScaleImage.class.getName();
	
    /**
     * Modifica el tamaño de la imagen.<br>
     * Si alto o ancho es igual o menor a cero, se calcula a partir de la otra dimensión,<br>
     * de forma que se mantenga la proporción.
     * 
     * @param width Ancho deseado.
     * @param height Alto deseado.
     * @param targetFormat
     * @param source Imagen origen, que va a ser modificada.
     * @return
     * @throws IOException
     */
    public static byte[] scale(Integer width, Integer height, String targetFormat, byte[] source ) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ByteArrayInputStream bi = new ByteArrayInputStream(source);
        scale(width, height, targetFormat, bi, bo);
        return bo.toByteArray();
    }
    
    /**
     * 
     * @param width
     * @param height
     * @param targetFormat
     * @param source
     * @param target
     * @throws IOException
     */
    public static void scale(Integer width, Integer height, String targetFormat, InputStream source, OutputStream target ) throws IOException {
        long initTime = System.currentTimeMillis();
        
        // Cargamos la imagen.
        BufferedImage img = ImageIO.read(source);
        
        // Si una de las dos dimensiones es menor o igual a cero, se supone que se debe calcular en función a la otra
        // para conseguir la proporción correcta.
        if(width == null || height<=0) {
            height = (width*img.getHeight())/img.getWidth();
        } 
        if(width == null || width<=0) {
            width = (height*img.getWidth())/img.getHeight();
        }
        
        // Si no se indica el formato, usamos el mismo de la imagen original.
        if( targetFormat == null ) {
        	ImageInputStream iis = ImageIO.createImageInputStream(source);
        	Iterator<ImageReader> iter = ImageIO.getImageReaders(iis); 
        	if (iter.hasNext()) {
        		ImageReader reader = (ImageReader)iter.next();
        		targetFormat = reader.getFormatName();
        	}
        	iis.close();
        }
        
        // Imagen temporal con el tamaño nuevo.
        BufferedImage scaledImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gScaledImg = scaledImg.createGraphics();
        // Note the use of BILNEAR filtering to enable smooth scaling
        gScaledImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // copiamos la imagen en el destino con el nuevo tamaño, por lo que se escala.
        gScaledImg.drawImage(img, 0, 0, width, height, null);

        // Guardamos en el formato indicado.
        ImageIO.write(scaledImg, targetFormat, target);
        
        if(Logger.getLogger(LOGNAME).isLoggable(Level.FINE)) {
            long endTime = System.currentTimeMillis();
            Logger.getLogger(LOGNAME).fine("Milisegundos : " + (endTime - initTime)+ " Proporciones: " + width + "x" + height );
        }
    }
    
}
