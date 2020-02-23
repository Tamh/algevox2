/**
 * @mainpage Documentación de AlgeVox Beta 1.0
 * <p>
 * Bienvenido a la documentación de AlgeVox Beta 1.0.
 * @author Luis Felipe Abad G.
 * @author Juan Pablo García H.
 * @file AlgeVox.java
 * @brief Archivo de clase principal para el proyecto AlgeVox
 * <p>
 * Contiene la clase AlgeVox y la función de entrada para la aplicación, junto
 * con la clase local DecodingThread.
 * @package algevox
 * @brief Paquete principal de la aplicación.
 * <p>
 * Encapsula todas las clases desarrolladas para el proyecto AlgeVox.
 * @file AlgeVox.java
 * @brief Archivo de clase principal para el proyecto AlgeVox
 * <p>
 * Contiene la clase AlgeVox y la función de entrada para la aplicación, junto
 * con la clase local DecodingThread.
 * @package algevox
 * @brief Paquete principal de la aplicación.
 * <p>
 * Encapsula todas las clases desarrolladas para el proyecto AlgeVox.
 */
/**
 * @file AlgeVox.java
 *
 * @brief Archivo de clase principal para el proyecto AlgeVox
 *
 * Contiene la clase AlgeVox y la función de entrada para la aplicación, junto
 * con la clase local DecodingThread.
 */
/**
 * @package algevox
 * @brief Paquete principal de la aplicación.
 *
 * Encapsula todas las clases desarrolladas para el proyecto AlgeVox.
 */
package org.hitcombo.algevox2;

import org.hitcombo.algevox2.sphinx.ControlResultListener;
import org.hitcombo.algevox2.sphinx.DictadoResultListener;
import org.hitcombo.algevox2.sphinx.LiveRecognizer;
import org.hitcombo.algevox2.utils.ManejadorBarraEstado;
import org.hitcombo.algevox2.utils.ReproductorEventos;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.recognizer.RecognizerState;
import edu.cmu.sphinx.result.Result;

/**
 * @class AlgeVox
 *
 * @brief Clase principal del programa AlgeVox.
 *
 * Exhibe toda la funcionalidad "debajo del capó" del programa.
 *
 * @author Juan Pablo García
 */
public class Algevox {

	private LiveRecognizer currentRecognizer;
	/**< Reconocedor de voz usado en un punto dado de la ejecución del programa. */
	private VentanaInicial vi;
	/**< Objeto VentanaInicial que permite el control de la interfaz desde su clase padre. */
	private Result lastResult;
	private boolean showPartialResults = true;
	/**< Variable que permite controlar los mensajes de depuración de los resultados del reconocimiento. */
	private boolean epMode = true;
	private int modoReconocedorVoz = MODO_CONTROL;
	/**< Variable que permite controlar y determinar el modo del reconocedor de voz. */
	private boolean deallocateCurrentRecognizer = false;
	/**< Variable que permite terminar el hilo de reconocimiento actual. */
	private LiveRecognizer lrControl;
	/**< Objeto LiveRecognizer que contiene el reconocedor de voz para el control. */
	private LiveRecognizer lrDictado;
	/**< Objeto LiveRecognizer que contiene el reconocedor de voz para el dictado. */
	private DictadoResultListener rlDictado;
	/**< Objeto DictadoResultListener que se enlazará al reconocedor de voz del dictado. */
	public static final int MODO_APAGADO = -1;
	/**< Constante del modo del reconocedor de voz  desactivado o no inicializado. */
	public static final int MODO_CONTROL = 0;
	/**< Constante del modo del reconocedor de voz para control. */
	public static final int MODO_DICTADO = 1;

	/**< Constante del modo del reconocedor de voz para dictado. */
	/**
	 * @brief Función de ejecución inicial del programa.
	 *
	 * Ningún argumento es usado.
	 * @param args Los argumentos por línea de comando.
	 */
	public static void main(String[] args) {
		System.out.println("AlgeVox beta 1.0");
		new Algevox();
	}

	/**
	 * @brief Constructor de la clase AlgeVox.
	 *
	 * Crea el objeto vista VentanaInicial y lo
	 * muestra. Seguido, crea el reconocedor del voz del modo de control
	 * de antemano.
	 */
	public Algevox() {
		vi = new VentanaInicial(this);
		vi.setVisible(true);

		this.lrControl = new LiveRecognizer("control", Algevox.class.getResource("sphinx/algevoxcontrol.config.xml"), this);
		this.lrDictado = new LiveRecognizer("dictado", Algevox.class.getResource("sphinx/algevoxdictado.config.xml"), this);
		currentRecognizer = lrControl;
		new ReproductorEventos(ReproductorEventos.SONIDO_INICIO);
	}

	/**
	 * @brief Revisa si el objeto del reconocedor de voz tiene sus recursos de memoria
	 * y procesador asignados.
	 * @return true si el objeto del reconocedor de voz tiene asignados
	 * recursos requeridos, false de lo contrario
	 */
	public boolean isCurrentRecognizerAllocated() {
		return currentRecognizer.isAllocated();
	}

	/**
	 * @brief Asigna recursos de memoria y procesador al objeto actual de
	 * reconocimiento de voz.
	 * @return true si el proceso se ejecutó correctamente, false de lo
	 * contrario
	 */
	public boolean allocateCurrentRecognizer() {
		if (currentRecognizer.allocate()) {
			//System.err.println(currentRecognizer.getName() + "::" + currentRecognizer.getRecognizer().getState());
			if (currentRecognizer.equals(lrControl)) {
				System.out.println("[allocateCurrentRecognizer] Asignando el hook para el modo de control.");
				currentRecognizer.hookResultListener(new ControlResultListener(this));
			} else if (currentRecognizer.equals(lrDictado)) {
				System.out.println("[allocateCurrentRecognizer] Asignando el hook para el modo de dictado.");
				rlDictado = new DictadoResultListener(this);
				currentRecognizer.hookResultListener(rlDictado);
			}
			return true;
		}
		return false;
	}

	/**
	 * @brief Permite obtener el modo de reconocedor de voz actual.
	 * @return el modo de reconocedor de voz actual
	 */
	public int getModoReconocedorVoz() {
		return this.modoReconocedorVoz;
	}

	/**
	 * @brief Permite intercambiar entre los modos de reconocedor de voz.
	 * @param modo el modo de reconocimiento al que se desea cambiar
	 */
	public void cambiarModoReconocedorVoz(int modo) {
		if (modo == MODO_CONTROL) {
			if (!currentRecognizer.equals(lrControl)) {
				stopRecording();
				deallocateCurrentRecognizer = true;
				currentRecognizer.deallocate();
				// Cambia al reconocedor de voz para control
				currentRecognizer = lrControl;
				allocateCurrentRecognizer();
				modoReconocedorVoz = modo;
				vi.cambiarTextoBarraModo("MODO DE CONTROL");
				// currentRecognizer.hookResultListener(new ControlResultListener(this));
				//vi.cambiarEstadoMicrofono();
				startRecording();
				vi.cambiarEstadoBotonMicrofono(true);
				decode();
				vi.cambiarTextoBarraEstado("Cambiado correctamente al modo de control.", ManejadorBarraEstado.ICONO_OK);
			}
		} else if (modo == MODO_DICTADO) {
			if (!currentRecognizer.equals(lrDictado)) {
				if (lrControl.isAllocated()) {
					stopRecording();
					deallocateCurrentRecognizer = true;
					currentRecognizer.deallocate();
				}
				// Cambia al reconocedor de voz para dictado
				currentRecognizer = lrDictado;
				deallocateCurrentRecognizer = false;
				allocateCurrentRecognizer();
				modoReconocedorVoz = modo;
				vi.cambiarTextoBarraModo("MODO DE DICTADO");
				// currentRecognizer.hookResultListener(new DictadoResultListener(this));
				//vi.cambiarEstadoMicrofono();
				startRecording();
				vi.cambiarEstadoBotonMicrofono(true);
				decode();
				vi.cambiarTextoBarraEstado("Cambiado correctamente al modo de dictado.", ManejadorBarraEstado.ICONO_OK);
				vi.setUltimoTextoRenderizado("");
			}
		} else {
			//this.stopRecording();
		}
		vi.cambiarTextoBarraReconocimiento("");
	}

	/**
	 * @brief Retorna el valor de la variable showPartialResults.
	 *
	 * Esta controla la impresión de un resultado de reconocimiento parcial en la vista de la
	 * aplicación.
	 * @return el valor de la variable showPartialResults
	 */
	public boolean isSetShowPartialResults() {
		return showPartialResults;
	}

	/**
	 * @brief Reinicia las variables y otras para la creación de una nueva expresión.
	 *
	 * Si se encuentra activo el modo dictado, eliminar el ResultListener y lo
	 * vuelve a crear. Si se encuentra activo el modo control, cambia al modo
	 * dictado.
	 */
	public void iniciarNuevaExpresion() {
		if (modoReconocedorVoz == MODO_DICTADO) {
			lrDictado.unhookResultListener(rlDictado);
			rlDictado = new DictadoResultListener(this);
			lrDictado.hookResultListener(rlDictado);
			vi.cambiarTextoBarraEstado("Creada nueva expresión.", ManejadorBarraEstado.ICONO_OK, null);
			new ReproductorEventos(ReproductorEventos.SONIDO_NUEVAEXPRESION);
		} else if (modoReconocedorVoz == MODO_CONTROL) {
			cambiarModoReconocedorVoz(MODO_DICTADO);
		}
	}

	/**
	 * @brief Comienza la grabación del micrófono del reconocedor de voz actual.
	 *
	 * @return true si la grabación inició satisfactoriamente, false de lo
	 * contrario
	 */
	public boolean startRecording() {
		currentRecognizer.getMicrophone().clear();
		return currentRecognizer.getMicrophone().startRecording();
	}

	/**
	 * @brief Detiene la grabación desde el micrófono del reconocedor de voz actual.
	 */
	public void stopRecording() {
		currentRecognizer.getMicrophone().stopRecording();
		currentRecognizer.getMicrophone().clear();
	}

	/**
	 * @brief Permite obtener el estado del reconocedor de voz actual.
	 * @return un objeto RecognizerState con el estado actual del reconocedor
	 * de voz
	 */
	public RecognizerState getCurrentRecognizerStatus() {
		return currentRecognizer.getRecognizer().getState();
	}

	/**
	 * @brief Permite obtener el nombre del reconocedor de voz actual.
	 * @return una cadena de texto con el nombre del reconocedor de voz actual
	 */
	public String getCurrentRecognizerName() {
		return currentRecognizer.getName();
	}

	/**
	 * @brief Inicia el proceso de decodificación en un hilo separado.
	 */
	public void decode() {
		(new DecodingThread()).start();
	}

	/**
	 * @brief Devuelve un apuntador al objeto vista VentanaInicial del programa.
	 *
	 * @return el objeto VentanaInicial
	 */
	public VentanaInicial getVentanaInicial() {
		return vi;
	}

	/**
	 * @brief Muestra el resultado parcial encapsulado en el objeto Result.
	 *
	 * @param result el objeto Result parcial a mostrar
	 */
	public void showPartialResult(Result result) {
		getVentanaInicial().cambiarTextoBarraEstado(result.toString(), 0);
	}

	/**
	 * @brief Cierra la aplicación.
	 */
	public void exitApplication() {
		System.exit(0);
	}

	/**
	 * @brief Encapsula un hilo de decodificación separado para el
	 * reconocimiento de voz.
	 *
	 * Hace decodificación en un hilo separado de tal forma que no bloquee al
	 * hilo que lo llama. Automáticamente acumulará datos, llamará a la
	 * gramática y actualizará los componentes de la Interfaz Gráfica de
	 * Usuario, cuando termina una decodificación. Funciona de forma análoga a
	 * componente de "Control" en un modelo MVC.
	 *
	 * @author Equipo de CMU Sphinx-4 (código original)
	 * @author Juan Pablo García (modificaciones para AlgeVox)
	 */
	class DecodingThread extends Thread {

		/**
		 * @brief Construye un DecodingThread.
		 *
		 * Llama a su clase padre con un mensaje, util en caso que se tenga
		 * anidamiento de hilos.
		 */
		public DecodingThread() {
			super("Decoding");
		}

		/**
		 * @brief Implementa el método run() de este hilo.
		 */
		@Override
		public void run() {
			Microphone microphone = currentRecognizer.getMicrophone();
			Recognizer recognizer = currentRecognizer.getRecognizer();

			//System.err.println("[DecodingThread::" + currentRecognizer.getName() + "] Inició.");

			while (microphone.hasMoreData() && !deallocateCurrentRecognizer) {
				// duerma por 500 ms para que no estrese el computador con
				// estas llamadas
				microphone = currentRecognizer.getMicrophone();
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					System.err.println("[DecodingThread] El hilo de decodificación fue interrumpido inesperadamente.");
				}
				if (microphone.isRecording() && recognizer.getState() == RecognizerState.READY) {
					recognizer.recognize();
				}
				//System.err.println("[DecodingThread] Está enredado.");
			}
			deallocateCurrentRecognizer = false;
			//System.err.println("[DecodingThread::" + currentRecognizer.getName() + "] Salió.");
		}
	}
}
