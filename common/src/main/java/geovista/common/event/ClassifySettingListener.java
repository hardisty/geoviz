/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Jin Chen */
package geovista.common.event;

import java.util.EventListener;

public interface ClassifySettingListener extends EventListener {
	public void classifySettingChanged(ClassifySettingEvent setting);
}
