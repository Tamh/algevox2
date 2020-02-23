/**
 * @file DragMoverListener.java
 * @brief Archivo de clase para agregar la funcionalidad de Drap and Scroll
 * a componentes de Swing.
 * @package algevox.utils
 * @brief Paquete con las clases utilitarias de AlgeVox
 * <p>
 * Encapsula las clases accesorias, que cumplen funciones adicionales al núcleo
 * de funcionamiento de AlgeVox.
 */
package org.hitcombo.algevox2.utils;

import org.hitcombo.algevox2.Algevox;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Versión original: terai @ http://terai.xrea.jp/Swing/AutoScroll.html
 * @author Modificado: Juan Pablo García
 * @brief Clase que permite agregar la funcionalidad de Drag and Scroll a
 * JScrollPane.
 * <p>
 * Permite hacer scroll sobre un componente Swing inmerso en un JScrollPane
 * usando el gesto de arrastrar y desplazar. Funciona de forma similar a la
 * herramienta Mano de programas como Adobe Acrobat o Inkscape.
 */
public class DragMoverListener extends MouseInputAdapter {

	private static final int SPEED = 2;
	/**
	 * < Constante que configura la velocidad de desplazamiento al arrastrar.
	 */
	private final Cursor dc;
	/**
	 * < Objeto Cursor para el cursor normal del JScrollPane.
	 */
	private final Cursor hd;
	/**
	 * < Objeto Cursor para el cursor del JScrollPane cuando se arrastra el cursor.
	 */
	private final Rectangle rect = new Rectangle();
	/**
	 * < Objeto Rectangle que indica las dimensiones de la posición a mostrar.
	 */
	private final JComponent label;
	/**
	 * < Objeto JComponent que indica el objeto que se va a desplazar.
	 */
	private final JViewport vport;
	/**
	 * < Objeto JViewport que almacena que JScrollPane obtendrá la funcionalidad.
	 */
	private Point startPt = new Point();
	/**
	 * < Objeto Point que contiene el punto inicial sobre el cual se hizo clic antes de arrastrar.
	 */
	private Point move = new Point();               /**< Objeto Point que contiene el punto final de la acción de desplazar. */

	/**
	 * @param vport El objeto Viewport que contiene el objeto que será
	 *              desplazado
	 * @param comp  El objeto del formulario que será desplazado mediante el
	 *              gesto
	 * @brief Constuctor de la clase.
	 * <p>
	 * Asigna el Viewport y el componente que se
	 * requiere adquieran la habilidad de arrastrar y desplazar.
	 */
	public DragMoverListener(JViewport vport, JComponent comp) {
		hd = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(Algevox.class.getResource("/cursor-hand-closed.png")), new Point(10, 3), "GrabbingHand");
		this.vport = vport;
		this.label = comp;
		this.dc = label.getCursor();
		vport.addMouseMotionListener(this);
		vport.addMouseListener(this);
	}

	/**
	 * @param e información del evento que ha ocurrido
	 * @brief Funcion Evento que se activa cuando el botón del mouse es dejado
	 * presionado y el mouse es arrastrado.
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		Point pt = e.getPoint();
		move.setLocation(SPEED * (pt.x - startPt.x), SPEED * (pt.y - startPt.y));
		startPt.setLocation(pt);

		Rectangle vr = vport.getViewRect();
		int w = vr.width;
		int h = vr.height;
		Point ptZero = SwingUtilities.convertPoint(vport, 0, 0, label);
		rect.setRect(ptZero.x - move.x, ptZero.y - move.y, w, h);
		label.scrollRectToVisible(rect);
	}

	/**
	 * @param e información del evento que ha ocurrido
	 * @brief Funcion Evento que se activa cuando el botón del mouse es
	 * presionado.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		label.setCursor(hd);
		startPt.setLocation(e.getPoint());
	}

	/**
	 * @param e información del evento que ha ocurrido
	 * @brief Funcion Evento que se activa cuando el botón del mouse es
	 * soltado.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		label.setCursor(dc);
	}

	/**
	 * @param e información del evento que ha ocurrido
	 * @brief Funcion Evento que se activa cuando el cursor del mouse sale
	 * del componente sobre el que se aplica esta funcionalidad.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		label.setCursor(dc);
		move.setLocation(0, 0);
	}
}
