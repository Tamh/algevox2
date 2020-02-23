/**
 * @file ControlResultListener.java
 * @brief Archivo de clase para la escucha de resultados en el reconocedor de
 * voz de control.
 */
package org.hitcombo.algevox2.sphinx;

import org.hitcombo.algevox2.Algevox;
import org.hitcombo.algevox2.VentanaInicial;
import edu.cmu.sphinx.decoder.ResultListener;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.PropertySheet;

/**
 * @author Juan Pablo García
 * @class ControlResultListener
 * @brief Clase que permite el control del sistema a través de comandos de voz.
 * <p>
 * Implementa un ResultListener y ejecuta acciones dependiendo del resultado
 * recibido.
 */
public class ControlResultListener implements ResultListener {
	private Algevox parent; /**< Objeto AlgeVox padre para este ResultListener. */

	/**
	 * @param parent el objeto AlgeVox padre
	 * @brief Constructor de la clase.
	 * <p>
	 * Permite enlazar un objeto AlgeVox para intercambiar información.
	 */
	public ControlResultListener(Algevox parent) {
		this.parent = parent;
	}

	/**
	 * @param result el objeto Result con el resultado del reconocimiento
	 * @brief Función que se dispara cuando un nuevo resultado del reconocimiento de
	 * voz del modo control ha sido obtenido.
	 */
	public void newResult(Result result) {
		if (!result.isFinal() && parent.isSetShowPartialResults()) {
			parent.showPartialResult(result);
		}
		if (result.isFinal()) {
			//System.out.println(result.getBestToken().getWordUnitPath());
			String temporal = result.getBestFinalResultNoFiller();
			//System.out.println(temporal);
			parent.getVentanaInicial().cambiarTextoBarraReconocimiento(temporal);

			if (temporal.indexOf("pausar") >= 0) {
				parent.getVentanaInicial().cambiarEstadoMicrofono();
			} else if (temporal.indexOf("nueva") >= 0) {
				// Proceso para la nueva expresión
			} else if (temporal.indexOf("dictado") >= 0) {
				parent.cambiarModoReconocedorVoz(Algevox.MODO_DICTADO);
			} else if (temporal.indexOf("salir") >= 0) {
				parent.exitApplication();
			} else if (temporal.equals("copiar") || temporal.indexOf("copiar imagen") >= 0) {
				parent.getVentanaInicial().copiarDatosPortapapelesTipo(VentanaInicial.MODO_EXPRESION);
			} else if (temporal.indexOf("copiar codigo") >= 0) {
				parent.getVentanaInicial().copiarDatosPortapapelesTipo(VentanaInicial.MODO_CODIGO);
			}
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
