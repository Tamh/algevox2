/**
 * @file RenderizadorLaTeX.java
 * @brief Archivo de clase para renderizar el código TeX en una imagen.
 */
package org.hitcombo.algevox2.utils;

import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import java.awt.*;

/**
 * @author Juan Pablo García
 * @class RenderizadorLaTeX
 * @brief Clase que permite la generación de un objeto Icono de TeX a partir de una
 * cadena dada.
 * <p>
 * Implementa esto en un hilo separado.
 */
public class RenderizadorLaTeX implements Runnable {

	private String textoLaTeX;
	/**
	 * < Variable que contiene el texto de TeX a dibujar.
	 */
	private int tamanoFuente;
	/**
	 * < Variable para el tamaño de la fuente en puntos para el dibujo.
	 */
	private int estiloExpresion;
	/**
	 * < Variable que indica el estilo de expresión que se va a dibujar.
	 */
	private Thread hiloRender;
	/**
	 * < Objeto Thread que contiene el hilo usado para dibujar.
	 */
	private TeXIcon icono;
	/**
	 * < Objeto TeXIcon que contiene el espacio de imagen donde se dibuja la expresión.
	 */
	private ParseException ultimaExcepcion; /**< Objeto ParseException que contiene la información de la última excepción ocurrida. */

	/**
	 * @param textoLaTeX   la cadena de texto de comandos de TeX que se quiere
	 *                     dibujar
	 * @param tamanoFuente tamaño en puntos visuales de pantalla del dibujo
	 *                     requerido
	 * @brief Constructor básico de la clase.
	 * <p>
	 * Llama al constructor avanzado para
	 * generar el dibujo de la expresión dada con el tamaño determinado en modo
	 * de presentación.
	 */
	public RenderizadorLaTeX(String textoLaTeX, int tamanoFuente) {
		this(textoLaTeX, tamanoFuente, TeXConstants.STYLE_DISPLAY);
	}

	/**
	 * @param textoLaTeX      la cadena de texto de comandos de TeX que se quiere
	 *                        dibujar
	 * @param tamanoFuente    tamaño en puntos visuales de pantalla del dibujo
	 *                        requerido
	 * @param estiloExpresion estilo como se quiere dibujar la expresión de
	 *                        TeX
	 * @brief Constructor avanzado de la clase.
	 * <p>
	 * Permite generar el dibujo de una
	 * expresion en TeX con un tamaño determinado en un modo de presentación
	 * dado
	 */
	public RenderizadorLaTeX(String textoLaTeX, int tamanoFuente, int estiloExpresion) {
		this.estiloExpresion = estiloExpresion;
		this.textoLaTeX = textoLaTeX;
		this.tamanoFuente = tamanoFuente;
		hiloRender = new Thread(this);
		hiloRender.start();
	}

	/**
	 * @brief Implementa el método run() de este hilo.
	 */
	public void run() {
		try {
			// Formula de LaTeX
			System.out.println("[RenderizadorLaTeX] Generando la fórmula de JLaTeXMath...");
			TeXFormula formula = new TeXFormula(getTextoLaTeX());

			// Crear el ícono (es decir la imagen) (Tipo de renderización, tamaño de la fuente)
			System.out.println("[RenderizadorLaTeX] Renderizando el ícono (imagen)...");
			icono = formula.createTeXIcon(estiloExpresion, tamanoFuente);
			icono.setInsets(new Insets(5, 5, 5, 5));
		} catch (ParseException ex) {
			icono = null;
			ultimaExcepcion = ex;
		}
	}

	/**
	 * @return el estado del hilo de dibujo
	 * @brief Devuelve el estado actual del hilo de dibujo
	 */
	public boolean estaActivo() {
		return hiloRender.isAlive();
	}

	/**
	 * @return el texto pasado por argumento al constructor para generar
	 * @brief Devuelve la cadena de comandos de TeX que se requirió dibujar.
	 */
	public String getTextoLaTeX() {
		return textoLaTeX;
	}

	/**
	 * @return el tamaño de fuente pasado por argumento al constructor para generar.
	 * @brief Entrega el tamaño de la fuente en puntos de pantalla con la que fue
	 * configurado este objeto.
	 */
	public int getTamanoFuente() {
		return tamanoFuente;
	}

	/**
	 * @return el ícono generado por el hilo. Podría ser null.
	 * @brief Entrega el objeto TeXIcon que contiene la representación visual de
	 * la cadena de texto de lenguaje TeX que se requería dibujar.
	 */
	public TeXIcon getIcono() {
		return icono;
	}

	/**
	 * @return la última excepción lanzada por el parser de JLaTeXMath. Podría ser null.
	 * @brief Retorna el contenido del mensaje de excepción que se haya
	 * generado en el proceso.
	 * <p>
	 * Este corresponde a lo que la librería JLaTeXMath generó en alguna de sus
	 * operaciones.
	 */
	public ParseException getUltimaExcepcion() {
		return ultimaExcepcion;
	}

}
