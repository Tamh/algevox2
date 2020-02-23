/**
 * @file DibujanteLaTeX.java
 * @brief Archivo de clase para la reproducción de las señales acústicas
 * usadas en el proyecto.
 */
package org.hitcombo.algevox2.utils;

import org.hitcombo.algevox2.Algevox;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Juan Pablo García
 * @brief Clase que permite la reproducción de un sonido, normalmente asociado a un
 * evento.
 */
public class ReproductorEventos implements Runnable {
	private Clip clip;
	/**
	 * < Objeto Clip que permite reproducir un archivo de sonido.
	 */
	private Thread reproductor;
	/**
	 * < Objeto Thread con el hilo usado para reproducir el sonido.
	 */

	public static final int SONIDO_OK = 1;
	/**
	 * < Constante para un sonido de afirmación.
	 */
	public static final int SONIDO_ADVERTENCIA = 2;
	/**
	 * < Constante para un sonido de advertencia.
	 */
	public static final int SONIDO_ERROR = 3;
	/**
	 * < Constante para un sonido de error.
	 */
	public static final int SONIDO_ERRORCRITICO = 4;
	/**
	 * < Constante para un sonido de error crítico.
	 */

	public static final int SONIDO_GRABAR = 11;
	/**
	 * < Constante para un sonido que indica el inicio de la grabación.
	 */
	public static final int SONIDO_PAUSA = 10;
	/**
	 * < Constante para un sonido que indica una pausa en la grabación.
	 */

	public static final int SONIDO_NUEVAEXPRESION = 20;
	/**
	 * < Constante para un sonido que indica la creación de una nueva expresión.
	 */
	public static final int SONIDO_DESHACER = 21;
	/**
	 * < Constante para un sonido que indica la ejecución de la acción de deshacer.
	 */
	public static final int SONIDO_CAMBIOMODO = 29;
	/**
	 * < Constante para un sonido que indica el inicio de la grabación.
	 */

	public static final int SONIDO_INICIO = 99;     /**< Constante para el sonido de inicio de AlgeVox. */

	/**
	 * @param tipo Una constante de esta clase que indica que sonido reproducir
	 * @brief Constructor de la clase que permite pasar por parámetro el tipo de sonido
	 * que será reproducido.
	 */
	public ReproductorEventos(int tipo) {
		if (tipo == SONIDO_OK) {
			reproducirArchivoEvento("/ok.wav");
		} else if (tipo == SONIDO_ADVERTENCIA) {
			reproducirArchivoEvento("/warning.wav");
		} else if (tipo == SONIDO_ERROR) {
			reproducirArchivoEvento("/error.wav");
		} else if (tipo == SONIDO_ERRORCRITICO) {
			reproducirArchivoEvento("/criticalerror.wav");
		} else if (tipo == SONIDO_GRABAR) {
			reproducirArchivoEvento("/record.wav");
		} else if (tipo == SONIDO_PAUSA) {
			reproducirArchivoEvento("/pause.wav");
		} else if (tipo == SONIDO_NUEVAEXPRESION) {
			reproducirArchivoEvento("/nuevaexpresion.wav");
		} else if (tipo == SONIDO_DESHACER) {
			reproducirArchivoEvento("/undo.wav");
		} else if (tipo == SONIDO_CAMBIOMODO) {
			reproducirArchivoEvento("/cambiomodo.wav");
		} else if (tipo == SONIDO_INICIO) {
			reproducirArchivoEvento("/startup.wav");
		} else {
			reproducirArchivoEvento("/generic.wav");
		}
	}

	/**
	 * @param nombre_evento Una cadena de texto que indica el nombre de archivo
	 *                      sin extensión del archivo que se desea reproducir
	 * @brief Constructor de la clase que permite pasar por parámetro el nombre de
	 * archivo del sonido en formato WAV que será reproducido.
	 */
	public ReproductorEventos(String nombre_evento) {
		reproducirArchivoEvento(nombre_evento + ".wav");
	}

	/**
	 * @param nombre_evento Una cadena de texto que indica el nombre de archivo
	 *                      con extensión del archivo que se desea reproducir
	 * @brief Función que configura e inicia el hilo de reproducción del
	 * archivo de sonido solicitado.
	 */
	private void reproducirArchivoEvento(String nombre_evento) {
		try {
			// Abre un stream del archivo de audio.
			URL url = Algevox.class.getResource("" + nombre_evento);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			// Obtiene un recurso de clip de audio.
			clip = AudioSystem.getClip();
			// Abre el clip de audio.
			clip.open(audioIn);

			reproductor = new Thread(this);
			reproductor.start();
		} catch (UnsupportedAudioFileException e) {
			System.err.println("[ReproductorEventos] El formato del archivo a reproducir no es compatible.");
		} catch (IOException e) {
			System.err.println("[ReproductorEventos] El archivo a reproducir no existe en el sistema.");
		} catch (LineUnavailableException e) {
			//System.err.println("[ReproductorEventos] No existe un dispositivo de salida para el sonido especificado.");
		}
	}

	/**
	 * @brief Implementa el método run() de este hilo.
	 */
	public void run() {
		clip.start();
	}
}