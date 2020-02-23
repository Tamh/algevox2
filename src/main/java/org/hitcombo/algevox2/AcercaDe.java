/**
 * @file AcercaDe.java
 * @brief Archivo de clase JDialog que muestra la pantalla de Acerca de AlgeVox.
 */
package org.hitcombo.algevox2;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

/**
 * @author Juan Pablo García
 * @class AcercaDe
 * @brief Clase que muestra la pantalla de "Acerca de".
 */
public class AcercaDe extends javax.swing.JDialog {

	/**
	 * @param parent el JFrame del cual será modal este formulario
	 * @brief Crea nuevo formulario modal AcercaDe.
	 */
	public AcercaDe(JFrame parent) {
		super(parent, "", ModalityType.APPLICATION_MODAL);
		initComponents();
		edpDescripcion.setCaretPosition(0);
		edpOtrosProyectos.setCaretPosition(0);
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

		lblIcon = new javax.swing.JLabel();
		lblNombre = new javax.swing.JLabel();
		sclpDescripcion = new javax.swing.JScrollPane();
		edpDescripcion = new javax.swing.JEditorPane();
		lblTOtrosProyectos = new javax.swing.JLabel();
		sclpOtrosProyectos = new javax.swing.JScrollPane();
		edpOtrosProyectos = new javax.swing.JEditorPane();
		btnCerrar = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Acerca de AlgeVox");
		setName("fmeAcercaDe"); // NOI18N
		setResizable(false);

		lblIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogoAlgeVoxB.png"))); // NOI18N
		lblIcon.setText("jLabel1");
		lblIcon.setMaximumSize(new java.awt.Dimension(128, 128));
		lblIcon.setMinimumSize(new java.awt.Dimension(128, 128));
		lblIcon.setPreferredSize(new java.awt.Dimension(128, 128));

		lblNombre.setFont(lblNombre.getFont().deriveFont(lblNombre.getFont().getStyle() | java.awt.Font.BOLD, 30));
		lblNombre.setText("AlgeVox beta 1.0");

		sclpDescripcion.setBorder(null);
		sclpDescripcion.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sclpDescripcion.setAutoscrolls(true);
		sclpDescripcion.setFocusable(false);
		sclpDescripcion.setHorizontalScrollBar(null);

		edpDescripcion.setBorder(null);
		edpDescripcion.setContentType("text/html");
		edpDescripcion.setEditable(false);
		edpDescripcion.setFont(edpDescripcion.getFont().deriveFont((float) 14));
		edpDescripcion.setText("<html>\r\n" +
				"  <head>\r\n\r\n" +
				"  </head>\r\n" +
				"  <body>\r\n" +
				"    <p style=\"margin-top: 0; font-size: 11pt; font-family: sans-serif;\">\r\n" +
				"      <span style=\"font-size: 13pt;\"><b>Proyecto AlgeVox</b> - Prototipo de Sistema de Reconocimiento Autom&aacute;tico de Voz para la Escritura de Aritm&eacute;tica y &Aacute;lgebra</span> <br />\n" +
				"      <span style=\"font-size: 12pt;\">Por <b>Luis Felipe Abad G.</b> <span style=\"font-family: monospaced\">\n" +
				"&lt;<a href=\"mailto:labad@utp.edu.co\">labadf@gmail.com</a>&gt;</span> y <b>Juan Pablo Garc&iacute;a H.</b> \n" +
				"<span style=\"font-family: monospaced\">&lt;<a href=\"mailto:jpgarcia@utp.edu.co\">jpgarcia@utp.edu.co</a>&gt;</span></span><br />\n" +
				"      Programa de Ingenier&iacute;a de Sistemas y Computaci&oacute;n<br/>\n" +
				"      Facultad de Ingenier&iacute;as El&eacute;ctrica, Electr&oacute;nica, F&iacute;sica y Ciencias de la Computaci&oacute;n<br/>\n" +
				"      <a href=\"http://www.utp.edu.co/\">Universidad Tecnol&oacute;gica de Pereira</a> - Pereira, Colombia<br/>\n" +
				"      Septiembre de 2011\n" +
				"    </p>\r\n" +
				"  </body>\r\n" +
				"</html>\r\n");
		edpDescripcion.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				edpDescripcionHyperlinkUpdate(evt);
			}
		});
		sclpDescripcion.setViewportView(edpDescripcion);

		lblTOtrosProyectos.setText("Este proyecto utiliza las siguientes librerías, proyectos y recursos:");

		sclpOtrosProyectos.setBorder(null);
		sclpOtrosProyectos.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sclpOtrosProyectos.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

		edpOtrosProyectos.setBorder(null);
		edpOtrosProyectos.setContentType("text/html");
		edpOtrosProyectos.setEditable(false);
		edpOtrosProyectos.setFont(edpOtrosProyectos.getFont().deriveFont((float) 14));
		edpOtrosProyectos.setText("<html>\r\n" +
				"  <head>\r\n" +
				"\r\n" +
				"  </head>\r\n" +
				"  <body style=\"margin-top: 0; font-size: 11pt; font-family: sans-serif;\">\r\r\n" +
				"     - El motor de reconocimiento de voz es provisto por el proyecto CMU Sphinx-4 versi&oacute;n 1.0 beta 3 \n" +
				"&lt;<a href=\"http://cmusphinx.sourceforge.net/\">http://cmusphinx.sourceforge.net/</a>&gt;. <br />\n" +
				"      <div style=\"padding-left: 20px;\">Copyright 1999-2008 Carnegie Mellon University.<br />\n" +
				"      Portions Copyright 2002-2008 Sun Microsystems, Inc.<br />\n" +
				"      Portions Copyright 2002-2008 Mitsubishi Electric Research Laboratories.<br />\n" +
				"      All Rights Reserved.  Use is subject to license terms.<br /></div>\n" +
				"      Para mayor informaci&oacute;n de la licencia <i>open source</i> de CMU Sphinx-4, leer el archivo <span style=\"font-family: monospaced\">license.terms</span> \n" +
				"en la distribuci&oacute;n de c&oacute;digo fuente de AlgeVox.<br />\n" +
				"      - Algunos componentes de CMU Sphinx-4 hacen uso extenso de la librer&iacute;a JSAPI - Java Speech Application Programming Interface \n" +
				"&lt;<a href=\"http://java.sun.com/products/java-media/speech/\">http://java.sun.com/products/java-media/speech/</a>&gt;. <br />\n" +
				"      - El modelo ac&uacute;stico en espa&ntilde;ol creado para esta aplicaci&oacute;n fue creado con base en el corpus ac&uacute;stico del proyecto VoxForge \n" +
				"&lt;<a href=\"http://www.voxforge.org/\">http://www.voxforge.org/</a>&gt;. Este corpus se encuentra cobijado bajo la licencia FSF GPL.<br />\n" +
				"      - El dibujo de las expresiones matem&aacute;ticas en LaTeX es provista por la librer&iacute;a JLatexMath \n" +
				"&lt;<a href=\"http://forge.scilab.org/index.php/p/jlatexmath/\">http://forge.scilab.org/index.php/p/jlatexmath/</a>&gt;. <br />\n" +
				"      <div style=\"padding-left: 20px;\">Copyright (C) 2009 DENIZET Calixte<br />\n" +
				"      Copyright (C) Kris Coolsaet<br />\n" +
				"      Copyright (C) Nico Van Cleemput<br />\n" +
				"      Copyright (C) Kurt Vermeulen<br />\n" +
				"      Copyright 2004-2007 Universiteit Gent</div>\n" +
				"      La librer&iacute;a JLatexMath est&aacute; licenciada con FSF GPL 2.0. Algunos componentes internos de esta podr&iacute;an estar cobijados por otros esquemas de licenciamiento.<br />\n" +
				"      - Algunos iconos e im&aacute;genes tienen copyright © Yusuke Kamiyamane &lt;<a href=\"http://p.yusukekamiyamane.com/\">http://p.yusukekamiyamane.com/</a>&gt;.\n" +
				"Todos los derechos reservados. Licenciados bajo <a href=\"http://creativecommons.org/licenses/by/3.0/\">Creative Commons Attribution 3.0</a>. <br />\n" +
				"      - Algunos iconos e im&aacute;genes provienen de la Open Icon Library &lt;<a href=\"http://openiconlibrary.sourceforge.net/\">http://openiconlibrary.sourceforge.net/</a>&gt;.\n" +
				"Estos &iacute;conos est&aacute;n disponibles bajo diferentes licencias <i>open source</i> o disponibles gratuitamente.<br />\n" +
				"      - Algunos sonidos provienen del proyecto KDE. &lt;<a href=\"http://www.kde.org/\">http://www.kde.org/</a>&gt;. Licenciados bajo el FSF GPL 2.0.<br />\n" +
				"      - Algunos sonidos son provistos por el paquete Fresh and Clean &lt;<a href=\"http://kde-look.org/content/show.php/Fresh+and+Clean?content=123207\">\n" +
				"http://kde-look.org/content/show.php/Fresh+and+Clean?content=123207</a>&gt; creado por \"Mrh Mouse\". Licenciados bajo el FSF GPL.<br />\n" +
				"      - Algunos sonidos son provistos por el paquete Dream &lt;<a href=\"http://pulicoti.deviantart.com/art/Dream-SystemSounds-103772795\">\n" +
				"http://pulicoti.deviantart.com/art/Dream-SystemSounds-103772795</a>&gt; creado por Andrea V. Licenciados bajo\n" +
				"<a href=\"http://creativecommons.org/licenses/by-nc-nd/3.0/\">Creative Commons Attribution Non-Commercial No-Derivative-Works 3.0</a>.<br />\n" +
				"      - Algunos sonidos son provistos por el paquete Borealis &lt;<a href=\"http://kde-look.org/content/show.php?content=12584\">\n" +
				"http://kde-look.org/content/show.php?content=12584</a>&gt; creado por  Ivica Ico Bukvic. Licenciados bajo\n" +
				"<a href=\"http://creativecommons.org/licenses/by-sa/3.0/\">Creative Commons Attribution Share-Alike 3.0</a>.<br />\n" +
				"  </body>\r\n" +
				"</html>\r\n");
		edpOtrosProyectos.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				edpOtrosProyectosHyperlinkUpdate(evt);
			}
		});
		sclpOtrosProyectos.setViewportView(edpOtrosProyectos);

		btnCerrar.setMnemonic('C');
		btnCerrar.setText("Cerrar");
		btnCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCerrarActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(sclpOtrosProyectos, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
										.addGroup(layout.createSequentialGroup()
												.addComponent(lblIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(sclpDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
														.addComponent(lblNombre)))
										.addComponent(lblTOtrosProyectos)
										.addComponent(btnCerrar, javax.swing.GroupLayout.Alignment.TRAILING))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(lblNombre)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(sclpDescripcion, 0, 0, Short.MAX_VALUE))
										.addComponent(lblIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(lblTOtrosProyectos)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(sclpOtrosProyectos, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(btnCerrar)
								.addContainerGap())
		);

		pack();
	}// </editor-fold>

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar un hiperenlace en el campo
	 * de Descripción del cuadro Acerca de.
	 */
	private void edpDescripcionHyperlinkUpdate(HyperlinkEvent evt) {
		if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				Desktop.getDesktop().browse(evt.getURL().toURI());
			} catch (Exception e) {
				System.err.println("[Hyperlink] No se pudo abrir el documento especificado en el hiperenlace.");
			}
		}
	}

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar un hiperenlace en el campo
	 * de Otros Proyectos del cuadro Acerca de.
	 */
	private void edpOtrosProyectosHyperlinkUpdate(HyperlinkEvent evt) {
		if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				Desktop.getDesktop().browse(evt.getURL().toURI());
			} catch (Exception e) {
				System.err.println("[Hyperlink] No se pudo abrir el documento especificado en el hiperenlace.");
			}
		}
	}

	/**
	 * @param evt información del evento que se ha disparado
	 * @brief Función Evento que se activa al presionar el botón Cerrar en el cuadro
	 * Acerca de.
	 */
	private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}

	// Variables declaration - do not modify
	private javax.swing.JButton btnCerrar;
	private javax.swing.JEditorPane edpDescripcion;
	private javax.swing.JEditorPane edpOtrosProyectos;
	private javax.swing.JLabel lblIcon;
	private javax.swing.JLabel lblNombre;
	private javax.swing.JLabel lblTOtrosProyectos;
	private javax.swing.JScrollPane sclpDescripcion;
	private javax.swing.JScrollPane sclpOtrosProyectos;
	// End of variables declaration
}
