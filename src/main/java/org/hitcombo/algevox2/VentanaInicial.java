/**
 * @file VentanaInicial.java
 * @brief Archivo de clase para la interfaz gráfica de usuario del sistema
 * AlgeVox.
 */
package org.hitcombo.algevox2;

import org.hitcombo.algevox2.utils.*;
import edu.cmu.sphinx.recognizer.RecognizerState;
import org.scilab.forge.jlatexmath.TeXConstants;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * @author Juan Pablo García
 * @brief Clase del modelo gráfico de AlgeVox.
 */
public class VentanaInicial extends javax.swing.JFrame {

	private Algevox algevox;
	/**
	 * < Objeto AlgeVox que actúa como el padre de este objeto.
	 */
	private Cursor handcursor;
	/**
	 * < Objeto Cursor para el ScrollPane de renderización.
	 */
	private String ultimoTextoRenderizado;
	/**
	 * < Cadena de texto que contiene el último texto de TeX generado.
	 */
	private ManejadorBarraEstado manejador;
	/**
	 * < Objeto ManejadorBarraEstado que administra la barra de estado de esta ventana.
	 */
	private boolean estadoMic;
	/**
	 * < Variable que permite saber en que estado se encuentra el micrófono.
	 */
	private int modoPantalla;
	/**< Variable que permite saber que representación se muestra en esta ventana. */
	/**
	 * Constante que indica que el modo de operación visual del sistema es modo
	 * expresión visual.
	 */
	public static final int MODO_EXPRESION = 1;
	/**
	 * Constante que indica que el modo de operación visual del sistema es modo
	 * código de TeX
	 */
	public static final int MODO_CODIGO = 2;
	/**
	 * Constante que indica que el modo de operación del portapapeles del
	 * sistema es modo código MathML
	 */
	public static final int MODO_MATHML = 3;

	/**
	 * @param algevox el objeto AlgeVox que será padre de esta ventana.
	 * @brief Crea un nuevo formulario VentanaInicial referido a la clase principal
	 * AlgeVox.
	 */
	public VentanaInicial(Algevox algevox) {
		this.algevox = algevox;
		estadoMic = false;
		modoPantalla = MODO_EXPRESION;

		ultimoTextoRenderizado = "";

		initComponents();

		handcursor = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(Algevox.class.getResource("/cursor-hand.png")), new Point(10, 3), "GrabbingHand");
		lblResultadoTeX.setCursor(handcursor);

		DragMoverListener dml = new DragMoverListener(spneResultadoTeX.getViewport(), lblResultadoTeX);

		manejador = new ManejadorBarraEstado(lblTextoEstado);

		asignarAceleradores();

		// Renderizando el logo inicial (y así mismo disminuyendo el tiempo de renderizado para las otras tareas)
		inicializarSistemaLaTeX();

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				VentanaInicial.this.algevox.exitApplication();
				//System.exit(0);
			}
		});
	}

	/**
	 * @param tipo un entero indicando el tipo de expresión a copiar
	 * @brief Método que permite el copiado de la información generada por el programa
	 * al portapapeles del sistema.
	 */
	public void copiarDatosPortapapelesTipo(int tipo) {
		if (tipo == MODO_EXPRESION) {
			if (lblResultadoTeX.getIcon() != null) {
				ManejadorPortapapeles.copiarAlPortapapeles(lblResultadoTeX.getIcon());
				manejador.imprimirBarraEstado("Copiada imagen al Portapapeles.", ManejadorBarraEstado.ICONO_OK, 10000, "");
			} else {
				JOptionPane.showMessageDialog(rootPane, "No hay nada para copiar aún.", "Error de copiado", JOptionPane.ERROR_MESSAGE);
			}
		} else if (tipo == MODO_CODIGO) {
			ManejadorPortapapeles.copiarAlPortapapeles(txtaResultadoTexto.getText());
			manejador.imprimirBarraEstado("Copiado texto al Portapapeles.", ManejadorBarraEstado.ICONO_OK, 10000, "");
		} else if (tipo == MODO_MATHML) {
			SnuggleSession session = new SnuggleEngine().createSession();
			try {
				session.parseInput(new SnuggleInput("$$" + txtaResultadoTexto.getText() + "$$"));
			} catch (IOException ie) {
				manejador.imprimirBarraEstado("Error copiando MathML al portapapeles.", ManejadorBarraEstado.ICONO_ERROR, 10000, "");
				return;
			}
			ManejadorPortapapeles.copiarAlPortapapeles(session.buildXMLString());
			manejador.imprimirBarraEstado("Copiado MathML al Portapapeles.", ManejadorBarraEstado.ICONO_OK, 10000, "");
		}
	}

	/**
	 * @brief Método que permite el copiado de la información generada por el programa
	 * al portapapeles del sistema.
	 */
	public void copiarDatosPortapapeles() {
		copiarDatosPortapapelesTipo(modoPantalla);
	}

	/**
	 * @brief Método que asigna accesos de teclado a varias funciones del
	 * programa.
	 */
	private void asignarAceleradores() {
		// Configuración para el botón del Portapapeles
		InputMap keyMap = new ComponentInputMap(pnlBarraEstado);
		keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, false), "actionCtrlC");
		keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK | Event.SHIFT_MASK, false), "actionCtrlShC");
		keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK | Event.SHIFT_MASK | Event.ALT_MASK, false), "actionCtrlShAlC");
		keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK, false), "actionCtrlN");

		ActionMap actionMap = new ActionMapUIResource();
		actionMap.put("actionCtrlC", AccionControlC());
		actionMap.put("actionCtrlShC", AccionControlShiftC());
		actionMap.put("actionCtrlShAlC", AccionControlShiftAltC());
		actionMap.put("actionCtrlN", AccionControlN());

		SwingUtilities.replaceUIActionMap(pnlBarraEstado, actionMap);
		SwingUtilities.replaceUIInputMap(pnlBarraEstado, JComponent.WHEN_IN_FOCUSED_WINDOW, keyMap);
	}

	/**
	 * @brief Método que inicializa el subsistema de dibujo de expresiones
	 * matemáticas con un dibujo inicial.
	 */
	private void inicializarSistemaLaTeX() {
		new DibujanteLaTeX(lblResultadoTeX, manejador, "\\forall \\ell \\mathcal{G} \\varepsilon \\mathbb{VOX}", 60, TeXConstants.STYLE_DISPLAY, true);
	}

	/**
	 * @param cadena cadena de comandos de TeX con el texto que se desea dibujar
	 * @brief Método que permite configurar cual es el último texto que será
	 * dibujado y llama al dibujante.
	 */
	public void setUltimoTextoRenderizado(String cadena) {
		ultimoTextoRenderizado = cadena;
		txtaResultadoTexto.setText(cadena);
		llamarDibujanteLaTeX();
	}

	/**
	 * @brief Llama a la clase DibujanteLaTeX para su inicialización.
	 */
	public void llamarDibujanteLaTeX() {
		new DibujanteLaTeX(lblResultadoTeX, manejador, ultimoTextoRenderizado, 50);
	}

	/**
	 * @param mensaje una cadena de texto con el mensaje que se desea mostrar.
	 * @param icono   el icono que se desea mostrar.
	 * @brief Llama a la clase ManejadorBarraEstado para mostrar un nuevo mensaje con
	 * un icono determinado.
	 */
	public void cambiarTextoBarraEstado(String mensaje, int icono) {
		manejador.imprimirBarraEstado(mensaje, icono);
	}

	/**
	 * @param mensaje    una cadena de texto con el mensaje que se desea mostrar.
	 * @param icono      el icono que se desea mostrar.
	 * @param otroSonido la cadena de texto con el nombre de archivo del sonido
	 *                   que se desea reproducir
	 * @brief Llama a la clase ManejadorBarraEstado para mostrar un nuevo mensaje con
	 * un icono determinado, reproduciendo otro sonido.
	 */
	public void cambiarTextoBarraEstado(String mensaje, int icono, String otroSonido) {
		manejador.imprimirBarraEstado(mensaje, icono, otroSonido);
	}

	/**
	 * @param mensaje la cadena de texto con el mensaje que se desea mostrar
	 * @brief Permite cambiar el texto del cuadro de modo de funcionamiento.
	 */
	public void cambiarTextoBarraModo(String mensaje) {
		lblModo.setText(mensaje);
	}

	/**
	 * @param mensaje la cadena de texto con el mensaje que se desea mostrar
	 * @brief Permite cambiar el texto del cuadro del texto de reconocimiento.
	 */
	public void cambiarTextoBarraReconocimiento(String mensaje) {
		lblTextoReconocido.setText(mensaje);
		lblTextoReconocido.setCaretPosition(lblTextoReconocido.getText().length());
	}

	/**
	 * @param estado estado deseado del botón
	 * @brief Permite conmutar entre el estado presionado o por defecto del botón
	 * del micrófono de la ventana.
	 */
	public void cambiarEstadoBotonMicrofono(boolean estado) {
		if (estado) {
			tbtnPausarMicrofono.setSelected(true);
			tbtnPausarMicrofono.setText("Pausar Micrófono");
		} else {
			tbtnPausarMicrofono.setSelected(false);
			tbtnPausarMicrofono.setText("Iniciar Micrófono");
		}
		estadoMic = estado;
	}

	/**
	 * @brief Permite conmutar entre los estados encendido y apagado del microfono del
	 * reconocedor de voz.
	 */
	public void cambiarEstadoMicrofono() {
		if (!estadoMic) {
			if (!algevox.isCurrentRecognizerAllocated()) {
				algevox.allocateCurrentRecognizer();
			}
			if (algevox.startRecording()) {
				if (algevox.getCurrentRecognizerStatus() == RecognizerState.READY) {
					algevox.decode();
				}
				manejador.imprimirBarraEstado("Micrófono inicializado correctamente.", ManejadorBarraEstado.ICONO_GRABACION, 10000, "");
				if (algevox.getModoReconocedorVoz() == Algevox.MODO_CONTROL) {
					lblModo.setText("MODO DE CONTROL");
				} else {
					lblModo.setText("MODO DE DICTADO");
				}
				cambiarEstadoBotonMicrofono(true);
				estadoMic = true;
			} else {
			}
		} else {
			algevox.stopRecording();
			manejador.imprimirBarraEstado("Micrófono detenido correctamente.", ManejadorBarraEstado.ICONO_PAUSA, 10000, "");
			lblModo.setText("MICRÓFONO SILENCIADO");
			cambiarEstadoBotonMicrofono(false);
			estadoMic = false;
		}
	}

	/**
	 * @return la AbstractAction que permite usar el acceso para copiar.
	 * @brief Define la acción de teclado que permite usar el acceso Control + C para
	 * copiar los datos al portapapeles del sistema
	 */
	public AbstractAction AccionControlC() {
		return new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				copiarDatosPortapapeles();
			}
		};
	}

	/**
	 * @return la AbstractAction que permite usar el acceso para copiar.
	 * @brief Define la acción de teclado que permite usar el acceso Control + C para
	 * copiar los datos al portapapeles del sistema
	 */
	public AbstractAction AccionControlShiftC() {
		return new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				copiarDatosPortapapelesTipo(MODO_CODIGO);
			}
		};
	}

	/**
	 * @return la AbstractAction que permite usar el acceso para copiar.
	 * @brief Define la acción de teclado que permite usar el acceso Control + C para
	 * copiar los datos al portapapeles del sistema
	 */
	public AbstractAction AccionControlShiftAltC() {
		return new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				copiarDatosPortapapelesTipo(MODO_MATHML);
			}
		};
	}

	/**
	 * @return la AbstractAction que permite usar el acceso para copiar.
	 * @brief Define la acción de teclado que permite usar el acceso Control + N para
	 * crear una nueva expresion
	 */
	public AbstractAction AccionControlN() {
		return new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				algevox.iniciarNuevaExpresion();
			}
		};
	}

	/**
	 * @brief Permite intercambiar entre los distintos estados de presentación de los
	 * datos generados.
	 * <p>
	 * Intercambia entre una versión gráfica y el código fuente de TeX.
	 */
	public void cambiarModoCodigo() {
		CardLayout cl = (CardLayout) (pneResultado.getLayout());
		if (tbtnCambiarModoCodigo.isSelected()) {
			txtaResultadoTexto.setText(ultimoTextoRenderizado);
			//pneResultadoTeX.getVerticalScrollBar().setValue(0);
			cl.show(pneResultado, "ResultadoTexto");

			txtaResultadoTexto.requestFocusInWindow();
			tbtnCambiarModoCodigo.setText("Cambiar a Modo Gráfica");
			modoPantalla = MODO_CODIGO;
		} else {
			ultimoTextoRenderizado = txtaResultadoTexto.getText();
			llamarDibujanteLaTeX();
			cl.show(pneResultado, "ResultadoTeX");

			tbtnCambiarModoCodigo.setText("Cambiar a Modo Código");
			modoPantalla = MODO_EXPRESION;
		}
		new ReproductorEventos(ReproductorEventos.SONIDO_CAMBIOMODO);
	}

	/**
	 * @brief Inicializa el formulario.
	 * <p>
	 * Este método es llamado desde el constructor para inicializar el
	 * formulario. No se debe modificar este código pues será siempre regenerado
	 * por el Editor de Formularios de NetBeans.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		popmCopiarPortapapeles = new javax.swing.JPopupMenu();
		mnuiCopiarImagen = new javax.swing.JMenuItem();
		mnuiCopiarLaTeX = new javax.swing.JMenuItem();
		mnuiCopiarMathML = new javax.swing.JMenuItem();
		pneResultado = new javax.swing.JPanel();
		spneResultadoTeX = new javax.swing.JScrollPane();
		fmeResultadoTeX = new javax.swing.JPanel();
		lblResultadoTeX = new javax.swing.JLabel();
		spneResultadoTexto = new javax.swing.JScrollPane();
		txtaResultadoTexto = new javax.swing.JTextArea();
		lblModo = new javax.swing.JLabel();
		tbtnPausarMicrofono = new javax.swing.JToggleButton();
		tbtnCambiarModoCodigo = new javax.swing.JToggleButton();
		btnNuevaExpresion = new javax.swing.JButton();
		btnCopiarPortapapeles = new javax.swing.JButton();
		btnAcercaDe = new javax.swing.JButton();
		btnCerrarAplicacion = new javax.swing.JButton();
		pnlBarraEstado = new javax.swing.JPanel();
		lblTextoEstado = new javax.swing.JLabel();
		pnlBarraReconocimiento = new javax.swing.JPanel();
		lblTextoReconocido = new javax.swing.JTextField();
		lblEstadoReconocimiento = new javax.swing.JLabel();

		mnuiCopiarImagen.setMnemonic('i');
		mnuiCopiarImagen.setText("Copiar Imagen");
		mnuiCopiarImagen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mnuiCopiarImagenActionPerformed(evt);
			}
		});
		popmCopiarPortapapeles.add(mnuiCopiarImagen);

		mnuiCopiarLaTeX.setMnemonic('l');
		mnuiCopiarLaTeX.setText("Copiar Código de LaTeX");
		mnuiCopiarLaTeX.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mnuiCopiarLaTeXActionPerformed(evt);
			}
		});
		popmCopiarPortapapeles.add(mnuiCopiarLaTeX);

		mnuiCopiarMathML.setMnemonic('m');
		mnuiCopiarMathML.setText("Copiar Código de MathML");
		mnuiCopiarMathML.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				mnuiCopiarMathMLActionPerformed(evt);
			}
		});
		popmCopiarPortapapeles.add(mnuiCopiarMathML);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("AlgeVox beta 1.0");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Algevox.class.getResource("/IconoAlgeVox.png")));
		setMinimumSize(new java.awt.Dimension(550, 350));
		setName("fmeMain"); // NOI18N
		getContentPane().setLayout(new java.awt.GridBagLayout());

		pneResultado.setBackground(new java.awt.Color(255, 255, 255));
		pneResultado.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		pneResultado.setLayout(new CardLayout());

		spneResultadoTeX.setBackground(new java.awt.Color(255, 255, 255));
		spneResultadoTeX.setBorder(null);
		spneResultadoTeX.setDoubleBuffered(true);

		fmeResultadoTeX.setBackground(new java.awt.Color(255, 255, 255));
		fmeResultadoTeX.setLayout(new java.awt.BorderLayout());

		lblResultadoTeX.setBackground(new java.awt.Color(255, 255, 255));
		lblResultadoTeX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblResultadoTeX.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		fmeResultadoTeX.add(lblResultadoTeX, java.awt.BorderLayout.CENTER);

		spneResultadoTeX.setViewportView(fmeResultadoTeX);

		pneResultado.add(spneResultadoTeX, "ResultadoTeX");

		spneResultadoTexto.setBorder(null);
		spneResultadoTexto.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		txtaResultadoTexto.setColumns(20);
		txtaResultadoTexto.setFont(new java.awt.Font("Monospaced", 0, 12));
		txtaResultadoTexto.setLineWrap(true);
		txtaResultadoTexto.setRows(5);
		spneResultadoTexto.setViewportView(txtaResultadoTexto);

		pneResultado.add(spneResultadoTexto, "ResultadoTexto");

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
		getContentPane().add(pneResultado, gridBagConstraints);

		lblModo.setFont(lblModo.getFont().deriveFont(lblModo.getFont().getStyle() | java.awt.Font.BOLD, lblModo.getFont().getSize() + 2));
		lblModo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblModo.setText("MICRÓFONO SILENCIADO");
		lblModo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
		getContentPane().add(lblModo, gridBagConstraints);

		tbtnPausarMicrofono.setText("Iniciar Micrófono");
		tbtnPausarMicrofono.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tbtnPausarMicrofonoActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
		getContentPane().add(tbtnPausarMicrofono, gridBagConstraints);

		tbtnCambiarModoCodigo.setText("Cambiar a Modo Código");
		tbtnCambiarModoCodigo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tbtnCambiarModoCodigoActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 4);
		getContentPane().add(tbtnCambiarModoCodigo, gridBagConstraints);

		btnNuevaExpresion.setText("Nueva Expresión");
		btnNuevaExpresion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnNuevaExpresionActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
		getContentPane().add(btnNuevaExpresion, gridBagConstraints);

		btnCopiarPortapapeles.setText("Copiar al Portapapeles");
		btnCopiarPortapapeles.setComponentPopupMenu(popmCopiarPortapapeles);
		btnCopiarPortapapeles.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnCopiarPortapapelesActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 4);
		getContentPane().add(btnCopiarPortapapeles, gridBagConstraints);

		btnAcercaDe.setText("Acerca de AlgeVox");
		btnAcercaDe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnAcercaDeActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
		getContentPane().add(btnAcercaDe, gridBagConstraints);

		btnCerrarAplicacion.setText("Cerrar Aplicación");
		btnCerrarAplicacion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnCerrarAplicacionActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 4);
		getContentPane().add(btnCerrarAplicacion, gridBagConstraints);

		pnlBarraEstado.setMinimumSize(new java.awt.Dimension(100, 25));
		pnlBarraEstado.setLayout(new java.awt.BorderLayout());

		lblTextoEstado.setText("Estado");
		lblTextoEstado.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		pnlBarraEstado.add(lblTextoEstado, java.awt.BorderLayout.CENTER);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
		getContentPane().add(pnlBarraEstado, gridBagConstraints);

		pnlBarraReconocimiento.setLayout(new java.awt.BorderLayout());

		lblTextoReconocido.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
		lblTextoReconocido.setEditable(false);
		lblTextoReconocido.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
		lblTextoReconocido.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		lblTextoReconocido.setFocusable(false);
		pnlBarraReconocimiento.add(lblTextoReconocido, java.awt.BorderLayout.CENTER);
		pnlBarraReconocimiento.add(lblEstadoReconocimiento, java.awt.BorderLayout.LINE_END);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
		getContentPane().add(pnlBarraReconocimiento, gridBagConstraints);

		getAccessibleContext().setAccessibleDescription("Ventana Principal de Algebox Beta 1");

		pack();
	}// </editor-fold>

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar el botón Copiar al Portapapeles.
	 */
	private void btnCopiarPortapapelesActionPerformed(ActionEvent evt) {
		copiarDatosPortapapeles();
	}

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar el botón Cambiar Modo Gráfica/Código.
	 */
	private void tbtnCambiarModoCodigoActionPerformed(ActionEvent evt) {
		cambiarModoCodigo();
	}

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar el botón Iniciar/Pausar Micrófono.
	 */
	private void tbtnPausarMicrofonoActionPerformed(ActionEvent evt) {
		cambiarEstadoMicrofono();
	}

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar el botón Nueva Expresión.
	 */
	private void btnNuevaExpresionActionPerformed(ActionEvent evt) {
		algevox.iniciarNuevaExpresion();
	}

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar el botón Acerca De.
	 */
	private void btnAcercaDeActionPerformed(ActionEvent evt) {
		AcercaDe ad = new AcercaDe(this);
		ad.setVisible(true);
		ad.setModal(true);
	}

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar el botón Cerrar Aplicacion.
	 */
	private void btnCerrarAplicacionActionPerformed(ActionEvent evt) {
		algevox.exitApplication();
	}

	private void mnuiCopiarImagenActionPerformed(ActionEvent evt) {
		copiarDatosPortapapeles();
	}

	private void mnuiCopiarLaTeXActionPerformed(ActionEvent evt) {
		copiarDatosPortapapelesTipo(MODO_CODIGO);
	}

	private void mnuiCopiarMathMLActionPerformed(ActionEvent evt) {
		copiarDatosPortapapelesTipo(MODO_MATHML);
	}

	// Variables declaration - do not modify
	private javax.swing.JButton btnAcercaDe;
	private javax.swing.JButton btnCerrarAplicacion;
	private javax.swing.JButton btnCopiarPortapapeles;
	private javax.swing.JButton btnNuevaExpresion;
	private javax.swing.JPanel fmeResultadoTeX;
	private javax.swing.JLabel lblEstadoReconocimiento;
	private javax.swing.JLabel lblModo;
	private javax.swing.JLabel lblResultadoTeX;
	private javax.swing.JLabel lblTextoEstado;
	private javax.swing.JTextField lblTextoReconocido;
	private javax.swing.JMenuItem mnuiCopiarImagen;
	private javax.swing.JMenuItem mnuiCopiarLaTeX;
	private javax.swing.JMenuItem mnuiCopiarMathML;
	private javax.swing.JPanel pneResultado;
	private javax.swing.JPanel pnlBarraEstado;
	private javax.swing.JPanel pnlBarraReconocimiento;
	private javax.swing.JPopupMenu popmCopiarPortapapeles;
	private javax.swing.JScrollPane spneResultadoTeX;
	private javax.swing.JScrollPane spneResultadoTexto;
	private javax.swing.JToggleButton tbtnCambiarModoCodigo;
	private javax.swing.JToggleButton tbtnPausarMicrofono;
	private javax.swing.JTextArea txtaResultadoTexto;
	// End of variables declaration
}
