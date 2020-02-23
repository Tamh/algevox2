/**
 * @file LiveRecognizer.java
 * @brief Archivo de clase para el reconocimiento de voz en hilos usando Sphinx.
 * @package algevox.sphinx
 * @brief Paquete con las clases de reconocimiento de voz.
 * <p>
 * Encapsula todas las clases que trabajan sobre la librería de reconocimiento
 * de voz CMU Sphinx-4.
 */
package org.hitcombo.algevox2.sphinx;

import org.hitcombo.algevox2.Algevox;
import edu.cmu.sphinx.decoder.ResultListener;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.instrumentation.AccuracyTracker;
import edu.cmu.sphinx.instrumentation.SpeedTracker;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.recognizer.RecognizerState;
import edu.cmu.sphinx.recognizer.StateListener;
import edu.cmu.sphinx.util.NISTAlign;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.PropertySheet;

import javax.swing.*;
import java.net.URL;

/**
 * @author Equipo de CMU Sphinx-4 (original)
 * @author Juan Pablo García (modificado para AlgeVox)
 * @class LiveRecognizer
 * @brief Clase contenedora de Recognizer con atributos adicionales.
 * <p>
 * Asocia un nombre de reconocedor, archivo de configuración, junto a las
 * estadísticas y control del micrófono.
 */
public class LiveRecognizer {

	private String name;
	private URL configName;
	private ConfigurationManager cm;
	private Recognizer recognizer;
	private Microphone microphone;
	private SpeedTracker speedTracker;
	private NISTAlign aligner;
	private boolean allocated;
	private Algevox parent;

	/**
	 * @param name       el nombre del reconocedor
	 * @param configName el archivo de configuración del reconocedor
	 * @param parent     la clase padre de este reconocedor
	 * @brief Crea un nuevo reconocedor en vivo.
	 */
	public LiveRecognizer(String name, URL configName, Algevox parent) {
		this.name = name;
		this.configName = configName;
		this.parent = parent;
		allocated = false;
	}

	/**
	 * @return el nombre de este reconocedor
	 * @brief Devuelve el nombre de este reconocedor
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return true si el proceso fue exitoso, false de lo contrario.
	 * @brief Asigna recursos a este reconocedor.
	 */
	public boolean allocate() {
		try {
			if (!allocated) {
				URL url = configName;
				cm = new ConfigurationManager(url);
				recognizer = (Recognizer) cm.lookup("recognizer");

				// Utilidad para depuración
                /*recognizer.addStateListener(new StateListener(){
                    public void statusChanged(RecognizerState rs){
                        System.out.println(System.currentTimeMillis() + " [" + name + "] " + rs);
                    }
                    public void newProperties(PropertySheet ps) throws PropertyException {
                        return;
                    }
                });/**/

				microphone = (Microphone) cm.lookup("microphone");
				speedTracker = (SpeedTracker) cm.lookup("speedTracker");
				aligner = ((AccuracyTracker) cm.lookup("accuracyTracker")).getAligner();
				recognizer.allocate();

				allocated = true;
			}
		} catch (PropertyException pe) {
			JOptionPane.showMessageDialog(parent.getVentanaInicial(), "No pude configurar reconocedor " + pe, "Error de reconocedor", JOptionPane.ERROR_MESSAGE);
		}
		return allocated;
	}

	/**
	 * @brief Quita la asignación de recursos de este reconocedor.
	 */
	public void deallocate() {
		if (allocated) {
			//recognizer.deallocate();
			recognizer.addStateListener(new StateListener() {
				public void statusChanged(RecognizerState rs) {
					//System.out.println(System.currentTimeMillis() + " [Deallocate::" + name + "] " + rs);
					if (recognizer.getState() == RecognizerState.READY) {
						recognizer.deallocate();
						allocated = false;
					}
				}

				public void newProperties(PropertySheet ps) throws PropertyException {
					return;
				}
			});
		}
	}

	/**
	 * @return el objeto Microphone
	 * @brief Devuelve el objeto Microphone de este reconocedor.
	 */
	public Microphone getMicrophone() {
		return microphone;
	}

	/**
	 * @return el objeto Recognizer
	 * @brief Retorna el objeto reconocedor.
	 */
	public Recognizer getRecognizer() {
		return recognizer;
	}

	/**
	 * @return true si el reconocedor está asignado
	 * @brief Determina si este reconocedor tiene asignación de recursos.
	 */
	public boolean isAllocated() {
		return allocated;
	}

	/**
	 * @return el alineador usado por este reconocedor
	 * @brief Retorna el alineador (que entrega estadísticas del reconocimiento).
	 */
	public NISTAlign getAligner() {
		return aligner;
	}

	/**
	 * @return la velocidad acumulada del reconocedor
	 * @brief Retorna la velocidad acumulada de este reconocedor.
	 */
	public float getCumulativeSpeed() {
		return speedTracker.getCumulativeSpeed();
	}

	/**
	 * @return la velocidad actual del reconocedor
	 * @brief Retorna la velocidad actual de este reconocedor como una fracción del
	 * tiempo real.
	 */
	public float getSpeed() {
		return speedTracker.getSpeed();
	}

	/**
	 * @brief Reinicializa las estadísticas de velocidad.
	 */
	public void resetSpeed() {
		speedTracker.reset();
	}

	/**
	 * @param rl el ResultListener a enganchar
	 * @brief Engancha el ResultListener provisto al reconocedor.
	 */
	public void hookResultListener(ResultListener rl) {
		if (isAllocated()) {
			recognizer.addResultListener(rl);
		} else {
			System.err.println("[LiveRecognizer] Imposible enganchar un ResultListener a un Recognizer sin asignar.");
		}
	}

	/**
	 * @param rl el ResultListener a desenganchar
	 * @brief Desengancha el ResultListener del reconocedor.
	 */
	public void unhookResultListener(ResultListener rl) {
		if (isAllocated()) {
			recognizer.removeResultListener(rl);
		} else {
			System.err.println("[LiveRecognizer] Imposible desenganchar un ResultListener de un Recognizer sin asignar.");
		}
	}
}
