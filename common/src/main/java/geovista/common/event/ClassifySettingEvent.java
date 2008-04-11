/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen 
 *
 * The event fired to change the classification setting( e.g.: variable name, number of categories,....)
 * Not complete yet, it simply meet current requirement, may need redesign later

 */
package geovista.common.event;

import java.util.EventObject;

import geovista.common.classification.setting.ClassifySetting;

public class ClassifySettingEvent extends EventObject {
	ClassifySetting setting;

	/**
	 * Constructs a prototypical Event.
	 * 
	 * @param source
	 *            The object on which the Event initially occurred.
	 */
	public ClassifySettingEvent(Object source, ClassifySetting setting) {
		super(source);
		this.setting = setting;
	}

	public ClassifySetting getSetting() {
		return setting;
	}
}
