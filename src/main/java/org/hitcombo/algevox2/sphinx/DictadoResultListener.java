/**
 * @file ControlResultListener.java
 * @brief Archivo de clase para la escucha de resultados en el reconocedor de
 * voz de dictado.
 */
package org.hitcombo.algevox2.sphinx;

import org.hitcombo.algevox2.Algevox;
import org.hitcombo.algevox2.grams.Sintactico;
import org.hitcombo.algevox2.utils.ReproductorEventos;
import edu.cmu.sphinx.decoder.ResultListener;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.PropertySheet;

import java.util.ArrayList;

/**
 * @author Juan Pablo García
 * @class DictadoResultListener
 * @brief Clase que permite el dictado de matemáticas al sistema.
 * <p>
 * Implementa un ResultListener para enlazar con la gramática y obtener
 * resultados en TeX.
 */
public class DictadoResultListener implements ResultListener {
	private Algevox parent;
	/**
	 * < Objeto AlgeVox padre para este ResultListener.
	 */
	private ArrayList<String> arregloReconocer;
	/**
	 * < ArrayList donde se almacena incrementalmente el texto reconocido.
	 */
	private String cadenaReconocer;
	/**
	 * < Cadena de texto donde se acumula el texto reconocido.
	 */
	private Sintactico analizador; /**< Objeto Sintactico que se encarga del análisis de la cadena reconocida */

	/**
	 * @param parent el objeto AlgeVox padre
	 * @brief Constructor de la clase.
	 * <p>
	 * Permite enlazar un objeto AlgeVox para
	 * intercambiar información.
	 */
	public DictadoResultListener(Algevox parent) {
		this.parent = parent;
		this.cadenaReconocer = "";
		this.arregloReconocer = new ArrayList<String>();
		this.analizador = new Sintactico();
	}

	/**
	 * @param result el objeto Result con el resultado del reconocimiento
	 * @brief Función que se dispara cuando un nuevo resultado del reconocimiento de
	 * voz del modo dictado ha sido obtenido.
	 */
	public void newResult(Result result) {
		if (!result.isFinal() && parent.isSetShowPartialResults()) {
			parent.showPartialResult(result);
		}
		if (result.isFinal()) {
			String temporal = result.getBestFinalResultNoFiller().toLowerCase();
			System.out.println("[DictadoResultListener] Reconocido \"" + temporal + "\".");

			if (temporal.indexOf("control") >= 0) {
				parent.cambiarModoReconocedorVoz(Algevox.MODO_CONTROL);
			} else if (temporal.indexOf("pausar") >= 0) {
				parent.getVentanaInicial().cambiarEstadoMicrofono();
			} else if (temporal.indexOf("deshacer") >= 0) {
				if (arregloReconocer.size() > 0) {
					arregloReconocer.remove(arregloReconocer.size() - 1);
					new ReproductorEventos(ReproductorEventos.SONIDO_DESHACER);
					cadenaReconocer = arregloReconocer.size() > 0 ? arregloReconocer.get(arregloReconocer.size() - 1) : "";
					System.out.println("[DictadoResultListener] Cadena va \"" + cadenaReconocer + "\".");
					analizador.analizarCadena(cadenaReconocer);
					if (analizador.getLatex() != null) {
						parent.getVentanaInicial().setUltimoTextoRenderizado(analizador.getLatex());
					} else {
						new ReproductorEventos(ReproductorEventos.SONIDO_ERROR);
					}
					parent.getVentanaInicial().cambiarTextoBarraReconocimiento(cadenaReconocer);
				}
			} else if (temporal.trim().length() != 0) {
				cadenaReconocer += " " + temporal;
				arregloReconocer.add(cadenaReconocer);
				System.out.println("[DictadoResultListener] Cadena va \"" + cadenaReconocer + "\".");
				analizador.analizarCadena(cadenaReconocer);
				System.out.println("[LaTeX] " + analizador.getLatex());
				if (analizador.getLatex() != null) {
					parent.getVentanaInicial().setUltimoTextoRenderizado(analizador.getLatex());
				} else {
					new ReproductorEventos(ReproductorEventos.SONIDO_ERROR);
				}
				parent.getVentanaInicial().cambiarTextoBarraReconocimiento(cadenaReconocer);
			}

			// Poner el código para enlazarlo con la gramática y retornar la
			// información al padre para su renderización
		}
	}

	/**
	 * @param ps la nueva hoja de propiedades del objeto Recognizer
	 * @throws PropertyException
	 * @brief Función que se dispara cuando el objeto Recognizer al que está enlazado
	 * este objeto cambia sus propiedades.
	 * <p>
	 * Es obligatorio hacer Override a esta función, pero no hace nada.
	 */
	public void newProperties(PropertySheet ps) throws PropertyException {
		return;
	}
}
