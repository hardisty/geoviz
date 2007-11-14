/**
 * 
 */
package geovista.toolkitcore;

import geovista.coordination.CoordinatingBean;
import junit.framework.TestCase;

/**
 * @author localadmin
 *
 */
public class ToolkitBeanSetTest extends TestCase {
	
	
	ToolkitBeanSet tBeanSet = new ToolkitBeanSet();
	
	/**
	 * @param name
	 */
	public ToolkitBeanSetTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}


	/**
	 * Test method for {@link geovista.toolkitcore.ToolkitBeanSet#add(geovista.toolkitcore.ToolkitBean)}.
	 */
	public void testAdd() {
		ToolkitBean bean = new ToolkitBean();
        CoordinatingBean coorBean = new CoordinatingBean();
		bean.setOriginalBean(coorBean);
		tBeanSet.add(bean);
		assert(tBeanSet.getBeanSet().size() == 1);
		tBeanSet.remove(bean);
	}

	/**
	 * Test method for {@link geovista.toolkitcore.ToolkitBeanSet#contains(geovista.toolkitcore.ToolkitBean)}.
	 */
	public void testContainsToolkitBean() {
		ToolkitBean bean = new ToolkitBean();
        CoordinatingBean coorBean = new CoordinatingBean();
		bean.setOriginalBean(coorBean);
		tBeanSet.add(bean);
		assert(tBeanSet.contains(bean) == true);
		
	}

	/**
	 * Test method for {@link geovista.toolkitcore.ToolkitBeanSet#contains(javax.swing.JMenuItem)}.
	 */
	public void testContainsJMenuItem() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link geovista.toolkitcore.ToolkitBeanSet#contains(javax.swing.JInternalFrame)}.
	 */
	public void testContainsJInternalFrame() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link geovista.toolkitcore.ToolkitBeanSet#getToolkitBean(javax.swing.JMenuItem)}.
	 */
	public void testGetToolkitBeanJMenuItem() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link geovista.toolkitcore.ToolkitBeanSet#getToolkitBean(javax.swing.JInternalFrame)}.
	 */
	public void testGetToolkitBeanJInternalFrame() {
		//fail("Not yet implemented"); // TODO
	}



	/**
	 * Test method for {@link geovista.toolkitcore.ToolkitBeanSet#remove(geovista.toolkitcore.ToolkitBean)}.
	 */
	public void testRemove() {
		ToolkitBean bean = new ToolkitBean();
        CoordinatingBean coorBean = new CoordinatingBean();
		bean.setOriginalBean(coorBean);
		tBeanSet.add(bean);
		assert(tBeanSet.contains(bean) == true);
		tBeanSet.remove(bean);
		assert(tBeanSet.contains(bean) == false);
	}

}
