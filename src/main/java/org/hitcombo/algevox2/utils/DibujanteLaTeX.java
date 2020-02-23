/**
 * @file DibujanteLaTeX.java
 * @brief Archivo de clase que permite incluir imagenes de LaTeX en componentes
 * de GUI.
 */
package org.hitcombo.algevox2.utils;

import org.scilab.forge.jlatexmath.TeXConstants;

import javax.swing.*;

/**
 * @author Juan Pablo García
 * @class DibujanteLaTeX
 * @brief Clase de inclusión gráfica de LaTeX en Swing.
 * <p>
 * Clase que permite la inclusión de representaciones gráficas de un código en
 * lenguaje TeX en componentes de Swing, nominalmente para este caso, JLabel.
 */
public class DibujanteLaTeX implements Runnable {

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
	private JLabel componente;
	/**
	 * < Objeto JLabel donde será dibujada la imagen generada.
	 */
	private Thread hiloDibujo;
	/**
	 * < Objeto Thread que contiene el hilo usado para dibujar.
	 */
	private boolean primeraEjecucion;
	/**
	 * < Bandera que indica si esta es la primera ejecución del motor de dibujo.
	 */
	private ManejadorBarraEstado manejador; /**< Objeto ManejadorBarraEstado que le permite a este objeto cambiar la información mostrada. */

	/**
	 * @param componente componente JLabel donde se dibujará el icono generado
	 *                   por el RenderizadorLaTeX
	 * @param manejador  objeto ManejadorBarraEstado que mostrará las
	 *                   actualizaciones de las actividades
	 * @param textoLaTeX cadena de texto que contiene el conjunto de comandos
	 *                   de TeX que se requieren dibujar
	 * @brief Constructor básico de la clase.
	 * <p>
	 * Hace un llamado al constructor avanzado de la
	 * clase predefiniendo el tamaño de la fuente, el estilo de presentación y
	 * la bandera de primera ejecución.
	 */
	public DibujanteLaTeX(JLabel componente, ManejadorBarraEstado manejador, String textoLaTeX) {
		this(componente, manejador, textoLaTeX, 50, TeXConstants.STYLE_DISPLAY, false);
	}

	/**
	 * @param componente   componente JLabel donde se dibujará el icono generado
	 *                     por el RenderizadorLaTeX
	 * @param manejador    objeto ManejadorBarraEstado que mostrará las
	 *                     actualizaciones de las actividades
	 * @param textoLaTeX   cadena de texto que contiene el conjunto de comandos
	 *                     de TeX que se requieren dibujar
	 * @param tamanoFuente entero que indica cual es el tamaño de la fuente en
	 *                     puntos de pantalla que se usará para el dibujo de la expresión
	 * @brief Constructor básico de la clase.
	 * <p>
	 * Hace un llamado al constructor avanzado de la
	 * clase predefiniendo el estilo de presentación y la bandera de primera
	 * ejecución.
	 */
	public DibujanteLaTeX(JLabel componente, ManejadorBarraEstado manejador, String textoLaTeX, int tamanoFuente) {
		this(componente, manejador, textoLaTeX, tamanoFuente, TeXConstants.STYLE_DISPLAY, false);
	}

	/**
	 * @param componente      componente JLabel donde se dibujará el icono generado
	 *                        por el RenderizadorLaTeX
	 * @param manejador       objeto ManejadorBarraEstado que mostrará las
	 *                        actualizaciones de las actividades
	 * @param textoLaTeX      cadena de texto que contiene el conjunto de comandos
	 *                        de TeX que se requieren dibujar
	 * @param tamanoFuente    entero que indica cual es el tamaño de la fuente en
	 *                        puntos de pantalla que se usará para el dibujo de la expresión
	 * @param estiloExpresion valor TeXConstants que indica el tipo de
	 *                        presentación que se le dará a la imagen generada
	 * @brief Constructor básico de la clase.
	 * <p>
	 * Hace un llamado al constructor avanzado de la
	 * clase predefiniendo la bandera de primera ejecución.
	 */
	public DibujanteLaTeX(JLabel componente, ManejadorBarraEstado manejador, String textoLaTeX, int tamanoFuente, int estiloExpresion) {
		this(componente, manejador, textoLaTeX, tamanoFuente, TeXConstants.STYLE_DISPLAY, false);
	}

	/**
	 * @param componente       componente JLabel donde se dibujará el icono generado
	 *                         por el RenderizadorLaTeX
	 * @param manejador        objeto ManejadorBarraEstado que mostrará las
	 *                         actualizaciones de las actividades
	 * @param textoLaTeX       cadena de texto que contiene el conjunto de comandos
	 *                         de TeX que se requieren dibujar
	 * @param tamanoFuente     entero que indica cual es el tamaño de la fuente en
	 *                         puntos de pantalla que se usará para el dibujo de la expresión
	 * @param estiloExpresion  valor TeXConstants que indica el tipo de
	 *                         presentación que se le dará a la imagen generada
	 * @param primeraEjecucion configura si se debe mostrar un mensaje de
	 *                         estdo específico para la primera ejecución
	 * @brief Constructor avanzado de la clase.
	 * <p>
	 * Configura todos los parámetros y da
	 * inicio al hilo de ejecución para el dibujo de la expresión que genera
	 * los comandos de TeX pasados por parámetro
	 */
	public DibujanteLaTeX(JLabel componente, ManejadorBarraEstado manejador, String textoLaTeX, int tamanoFuente, int estiloExpresion, boolean primeraEjecucion) {
		this.componente = componente;
		this.manejador = manejador;
		this.estiloExpresion = estiloExpresion;
		this.textoLaTeX = textoLaTeX;
		this.tamanoFuente = tamanoFuente;
		this.primeraEjecucion = primeraEjecucion;
		hiloDibujo = new Thread(this);
		hiloDibujo.start();
	}

	/**
	 * @brief Implementa el método run() de este hilo.
	 */
	public void run() {
		manejador.imprimirBarraEstado((primeraEjecucion ? "Inicializando sistema TeX..." : "Renderizando..."), ManejadorBarraEstado.ICONO_CARGANDO);
		RenderizadorLaTeX rl = new RenderizadorLaTeX(textoLaTeX, tamanoFuente, estiloExpresion);
		while (rl.estaActivo()) {
			try {
				Thread.sleep(250);
				System.out.println("[DibujanteLaTeX] Esperando renderizado...");
			} catch (InterruptedException ex) {
				//
			}
		}
		if (rl.getIcono() != null) {
			System.out.println("[DibujanteLaTeX] Colocando el ícono en el label de display...");
			componente.setIcon(rl.getIcono());
		} else {
			JOptionPane.showMessageDialog(componente, "Ha ocurrido un error en el intérprete de LaTeX: \n"
					+ rl.getUltimaExcepcion().getMessage(), "Error del intérprete de LaTeX", JOptionPane.ERROR_MESSAGE);
		}
		manejador.imprimirBarraEstado("Listo.", ManejadorBarraEstado.ICONO_LISTO);
	}

	/**
	 * @return el estado del hilo de dibujo
	 * @brief Devuelve el estado actual del hilo de dibujo
	 */
	public boolean estaActivo() {
		return hiloDibujo.isAlive();
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
}
