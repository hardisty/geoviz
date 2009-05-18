/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.coordination;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This subclass of JPanel is responsible for communicating between the user and
 * an instance of CoordinationManager concerning the status of one FiringBean ->
 * ListeningBean connection for one type of coordinated event. It has a
 * JCheckBox.
 * 
 * @see CoordinationManager
 * @author Frank Hardisty
 */
public class BeanConnectionGUI extends JPanel implements ItemListener {
	private transient final ListeningBean lBean;
	private transient final FiringMethod meth; // contains reference to parent
												// FiringBean
	private transient final CoordinationManager cm;
	private transient final JCheckBox cBox;

	/**
   *
  */
	public BeanConnectionGUI(ListeningBean lBean, FiringMethod meth,
			CoordinationManager cm) {
		FiringBean fBean = meth.getFiringBean();
		cBox = new JCheckBox();
		// cBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		cBox.setSelected(true);

		JLabel lab = new JLabel(fBean.getBeanName(), fBean.getSmallIcon(),
				JLabel.LEFT);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(cBox);
		this.add(lab);
		this.lBean = lBean;
		this.meth = meth;
		this.cm = cm;
		// this.setAlignmentX(Component.LEFT_ALIGNMENT);
		cBox.addItemListener(this);
	}

	/** We listen to our box */
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			cm.disconnectBeans(meth, lBean);
		} else if (e.getStateChange() == ItemEvent.SELECTED) {
			cm.reconnectBeans(meth, lBean);
		}
	}
}