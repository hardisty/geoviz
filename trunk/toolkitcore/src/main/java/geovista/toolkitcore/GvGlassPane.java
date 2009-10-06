/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 */

package geovista.toolkitcore;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A glass pane for doing custom painting on top of the other components.
 * 
 * @author Frank Hardisty
 */

public class GvGlassPane extends JPanel implements AWTEventListener {
	private final JFrame frame;

	public GvGlassPane(JFrame frame) {
		super(null);
		this.frame = frame;
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GREEN.darker());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.7f));
		int d = 22;
		// g2.fillRect(getWidth() - d, 0, d, d);
		// if (point != null) {
		// g2.fillOval(point.x + d, point.y + d, d, d);
		// }
		g2.fillOval(0, 0, d, d);
		g2.dispose();
	}

	public void eventDispatched(AWTEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent me = (MouseEvent) event;
			if (!SwingUtilities.isDescendingFrom(me.getComponent(), frame)) {
				return;
			}

			repaint();
		}
	}

	/**
	 * If someone adds a mouseListener to the GlassPane or set a new cursor we
	 * expect that he knows what he is doing and return the super.contains(x, y)
	 * otherwise we return false to respect the cursors for the underneath
	 * components
	 */
	@Override
	public boolean contains(int x, int y) {
		if (getMouseListeners().length == 0
				&& getMouseMotionListeners().length == 0
				&& getMouseWheelListeners().length == 0
				&& getCursor() == Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) {
			return false;
		}
		return super.contains(x, y);
	}
}