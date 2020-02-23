/**
 * @file ManejadorPortapapeles.java
 * @brief Archivo de clase para administrar las diferentes funciones que se
 * usan para copiar al portapapeles del sistema distintos contenidos.
 */
package org.hitcombo.algevox2.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Juan Pablo Garcia (método general)
 * @author Kulvir Singh Bhogal (clase estática ImageSelection)
 * @brief Clase que administra las funciones de copia de imágenes y texto al
 * portapapeles.
 */
public class ManejadorPortapapeles {

	/**
	 * @author Kulvir Singh Bhogal
	 * @brief Clase interna que implementa un selector de un objeto Image para ser
	 * copiada al portapapeles.
	 * <p>
	 * Código original disponible en http://www.devx.com/Java/Article/22326/1954
	 */
	public static class ImageSelection implements Transferable {
		private Image image; /**< El objeto Image que sera envuelto por la selección de imagen. */

		/**
		 * @param image Imagen que se requiere seleccionar.
		 * @brief Constructor de la clase.
		 * <p>
		 * Inicializa el atributo image de la clase
		 * a la imagen que se requiere seleccionar.
		 */
		public ImageSelection(Image image) {
			this.image = image;
		}

		/**
		 * @return Arreglo de DataFlavor
		 * @brief Retorna los "sabores" soportados por la implementación.
		 */
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[]{DataFlavor.imageFlavor};
		}

		/**
		 * @param flavor DataFlavor a buscar.
		 * @return true si ha sido encontrado, false en el caso contrario.
		 * @brief Permite determinar un "sabor" determinado se encuentra soportado
		 * por la implementación.
		 */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.imageFlavor.equals(flavor);
		}

		/**
		 * @param flavor El DataFlavor del objeto.
		 * @return En caso de no existir excepción, retorna el objeto Image original.
		 * @throws UnsupportedFlavorException causada si el DataFlavor no está soportado.
		 * @throws IOException                causada si la información no es correcta.
		 * @brief Retorna el objeto Image encapsulado por el objeto Transferable.
		 */
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (!DataFlavor.imageFlavor.equals(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			// de lo contrario retorne el objeto encapsulado
			return image;
		}
	}

	/**
	 * @param icono El objeto Icon que va a ser copiado al portapapeles.
	 * @brief Función que copia al portapapeles del sistema un objeto Icon.
	 * <p>
	 * Prepara un
	 * objeto Image a partir de la información del objeto Icon pasado por
	 * referencia.
	 */
	public static void copiarAlPortapapeles(Icon icono) {
		System.out.println("Creando imagen buffereada...");
		BufferedImage image = new BufferedImage(icono.getIconWidth(), icono.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, icono.getIconWidth(), icono.getIconHeight());

		System.out.println("Pintando el ícono en la imagen buffereada...");
		JLabel jl = new JLabel();
		jl.setForeground(new Color(0, 0, 0));
		icono.paintIcon(jl, g2, 0, 0);

		copiarAlPortapapeles(image);
	}

	/**
	 * @param imagen El objeto Image que será copiado al portapapeles.
	 * @brief Función que copia al portapapeles del sistema una imagen almacenada en un
	 * objeto Image.
	 */
	public static void copiarAlPortapapeles(Image imagen) {
		System.out.println("Seleccionando la imagen y copiando al Portapapeles...");
		ImageSelection imageSelection = new ImageSelection(imagen);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imageSelection, null);
	}

	/**
	 * @param cadena La cadena que será copiada al portapapeles del sistema.
	 * @brief Función que copia al portapapeles del sistema una cadena de texto
	 * arbitraria.
	 */
	public static void copiarAlPortapapeles(String cadena) {
		System.out.println("Copiando el código al Portapapeles...");
		StringSelection stringSelection = new StringSelection(cadena);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
}
