/**
 * @file ManejadorBarraEstado.java
 * @brief Archivo de clase para la modificación del contenido de la barra de
 * estado.
 */
package org.hitcombo.algevox2.utils;

import org.hitcombo.algevox2.Algevox;

import javax.swing.*;

/**
 * @author Juan Pablo García
 * @class ManejadorBarraEstado
 * @brief Clase para la modificación del contenido de la barra de estado.
 * <p>
 * La barra de estado está ubicada en el objeto vista VentanaInicial.
 */
public class ManejadorBarraEstado implements Runnable {

	private JLabel barraEstado;
	/**
	 * < Objeto JLabel que contiene la barra de estado.
	 */
	private Thread reinicializador;
	/**
	 * < Objeto Thread que contiene el hilo que reinicializa la barra de estado a un mensaje genérico.
	 */
	private int tiempoReinicio;     /**< Variable que indica cuanto tiempo se toma para reinicializar la barra de estado. */
	/**
	 * Constante que define que el mensaje a mostrar no debe tener un ícono
	 * (caso no usual)
	 */
	public static final int ICONO_VACIO = -1;
	/**
	 * Constante que define que el mensaje a mostrar debe tener un ícono de una
	 * bandera verde (usualmente significa que el sistema está listo para
	 * trabajar).
	 */
	public static final int ICONO_LISTO = 0;
	/**
	 * Constante que define que el mensaje a mostrar debe tener un icono de
	 * circulos rotando (usualmente significa que el sistema está ocupado o
	 * realizando un proceso largo).
	 */
	public static final int ICONO_CARGANDO = 1;
	/**
	 * Constante que define que el mensaje a mostrar debe tener un icono de
	 * símbolo de verificación (usualmente indica que el sistema ha terminado
	 * correctamente una operación).
	 */
	public static final int ICONO_OK = 2;
	/**
	 * Constante que define que el mensaje a mostrar debe tener un icono de
	 * equis roja (usualmente indica que el sistema ha tenido un error durante
	 * la ejecución de un proceso).
	 */
	public static final int ICONO_ERROR = 3;
	/**
	 * Constante que define que el mensaje a mostrar debe tener un icono de
	 * símbolo de exclamación (usualmente indica que el sistema ha tenido un
	 * problema no crítico durante la ejecución de un proceso).
	 */
	public static final int ICONO_ADVERTENCIA = 4;
	/**
	 * Constante que define que el mensaje a mostrar debe tener un icono de
	 * símbolo de pausa (usualmente indica que el sistema se encuentra en
	 * estado de pausa).
	 */
	public static final int ICONO_PAUSA = 10;
	/**
	 * Constante que define que el mensaje a mostrar debe tener un icono de
	 * símbolo de grabación (usualmente indica que el sistema se encuentra
	 * grabando información de micrófono).
	 */
	public static final int ICONO_GRABACION = 11;

	/**
	 * @param barraEstado el objeto JLabel que será modificado con esta clase
	 * @brief Constructor de la clase.
	 * <p>
	 * Obtiene la referencia del JLabel que será
	 * manipulado durante las llamadas a la clase.
	 */
	public ManejadorBarraEstado(JLabel barraEstado) {
		this.barraEstado = barraEstado;
	}

	/**
	 * @param mensaje        la cadena de texto que contiene el mensaje que será
	 *                       mostrado en la barra de estado
	 * @param icono          el tipo de icono que será usado en la barra de estado
	 * @param tiempoReinicio indica el tiempo en milisegundos que el objeto
	 *                       esperará para reinicializar la barra de estado a un mensaje genérico que
	 *                       indica que el sistema está listo. Si el valor es 0, la barra de estado
	 *                       nunca se reinicializa automáticamente.
	 * @param otroSonido     cadena de texto con el nombre del archivo que se
	 *                       desea reproducir sin la extensión.
	 * @brief Imprime un mensaje en la barra de estado asignada.
	 */
	public void imprimirBarraEstado(String mensaje, int icono, int tiempoReinicio, String otroSonido) {
		this.tiempoReinicio = tiempoReinicio;
		if (icono == ICONO_LISTO) {
			barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/flag-green.png")));
		} else if (icono == ICONO_CARGANDO) {
			barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/ajax-loader.gif")));
		} else if (icono == ICONO_OK) {
			barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/ok.png")));
		} else if (icono == ICONO_ERROR) {
			barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/error.png")));
		} else if (icono == ICONO_ADVERTENCIA) {
			barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/warning.png")));
		} else if (icono == ICONO_PAUSA) {
			barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/pause.png")));
		} else if (icono == ICONO_GRABACION) {
			barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/record.png")));
		} else {
			barraEstado.setIcon(null);
		}
		barraEstado.setText(mensaje);

		if (otroSonido != null) {
			if (otroSonido.equals("")) {
				if (icono == ICONO_LISTO) {
					new ReproductorEventos(ReproductorEventos.SONIDO_OK);
				} else if (icono == ICONO_OK) {
					new ReproductorEventos(ReproductorEventos.SONIDO_OK);
				} else if (icono == ICONO_ERROR) {
					new ReproductorEventos(ReproductorEventos.SONIDO_ERROR);
				} else if (icono == ICONO_ADVERTENCIA) {
					new ReproductorEventos(ReproductorEventos.SONIDO_ADVERTENCIA);
				} else if (icono == ICONO_PAUSA) {
					new ReproductorEventos(ReproductorEventos.SONIDO_PAUSA);
				} else if (icono == ICONO_GRABACION) {
					new ReproductorEventos(ReproductorEventos.SONIDO_GRABAR);
				}
			} else if (otroSonido != null) {
				new ReproductorEventos(otroSonido);
			}
		}

		if (tiempoReinicio != 0) {
			reinicializador = new Thread(this);
			reinicializador.start();
		}
	}

	/**
	 * @param mensaje la cadena de texto que contiene el mensaje que será
	 *                mostrado en la barra de estado
	 * @param icono   el tipo de icono que será usado en la barra de estado
	 * @brief Imprime un mensaje en la barra de estado asignada sin permitir que se
	 * reinicialice automáticamente.
	 */
	public void imprimirBarraEstado(String mensaje, int icono) {
		imprimirBarraEstado(mensaje, icono, 0, "");
	}

	/**
	 * @param mensaje    la cadena de texto que contiene el mensaje que será
	 *                   mostrado en la barra de estado
	 * @param icono      el tipo de icono que será usado en la barra de estado
	 * @param otroSonido la cadena de texto que contiene el nombre de archivo
	 *                   del sonido que se desea reproducir
	 * @brief Imprime un mensaje en la barra de estado asignada sin permitir que se
	 * reinicialice automáticamente, usando otro sonido.
	 */
	public void imprimirBarraEstado(String mensaje, int icono, String otroSonido) {
		imprimirBarraEstado(mensaje, icono, 0, otroSonido);
	}

	/**
	 * @brief Implementa el método run() de este hilo.
	 */
	public void run() {
		try {
			Thread.sleep(tiempoReinicio);
		} catch (InterruptedException ex) {
			//
		}
		barraEstado.setIcon(new ImageIcon(Algevox.class.getResource("/flag-green.png")));
		barraEstado.setText("Listo.");
	}
}
