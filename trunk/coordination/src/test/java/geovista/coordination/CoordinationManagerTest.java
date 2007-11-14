/**
 * 
 */
package geovista.coordination;

import geovista.common.event.IndicationListener;

import javax.swing.JPanel;

import junit.framework.TestCase;

/**
 * @author localadmin
 *
 */
public class CoordinationManagerTest extends TestCase {
	protected CoordinationManager coord;
	/**
	 * @param name
	 */
	public CoordinationManagerTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		coord = new CoordinationManager();
	}
	/**
	*Test method for {@link geovista.coordination.CoordinationManager#addBean(java.lang.Object)}.
	*Test method for {@link geovista.coordination.CoordinationManager#removeBean(java.lang.Object)}.
	*/
	public void testCoordinateObject() {

        System.out.println("stuff.....");
        JPanel pan = new JPanel();

        CoordinatingBean coorBean = new CoordinatingBean();
        CoordinatingBean coorBean2 = new CoordinatingBean();
        CoordinatingBean coorBean3 = new CoordinatingBean();
        CoordinatingBean coorBean4 = new CoordinatingBean();


        Class[] paramTypes = new Class[1];
        paramTypes[0] = IndicationListener.class;

        coord.addBean(coorBean);
        coord.addBean(coorBean2);
        coord.addBean(coorBean3);
        coord.addBean(coorBean4);

        assertTrue(coorBean2.getIndication() == 0);
        coorBean.setArgTwo(1);
        assertTrue(coorBean2.getIndication() == 1);
        coord.removeBean(coorBean2);
        coorBean.setArgTwo(2);
        assertTrue(coorBean2.getIndication() == 1);

        assertTrue(coorBean3.getIndication() == 2);

        coord.removeBean(coorBean);
        coorBean.setArgTwo(5);
        
        //trigger events
        coorBean.setArgOne(10);
        coorBean.setArgTwo(10);

        //let's remove, add, and remove beans and see if that goes ok
        coord.removeBean(coorBean4);
        coord.addBean(coorBean4);
        coord.addBean(coorBean4);
        coord.removeBean(coorBean4);
        coord.removeBean(coorBean3);
        coord.removeBean(coorBean3);
        assertTrue(coorBean3.getIndication() == 2);

        //OK, let's try some other beans.




}



	/**
	 * Test method for {@link geovista.coordination.CoordinationManager#getFiringMethods(geovista.coordination.ListeningBean)}.
	 */
	public void testGetFiringMethods() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link geovista.coordination.CoordinationManager#getFiringBeans()}.
	 */
	public void testGetFiringBeans() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link geovista.coordination.CoordinationManager#disconnectBeans(geovista.coordination.FiringMethod, geovista.coordination.ListeningBean)}.
	 */
	public void testDisconnectBeans() {
		//fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link geovista.coordination.CoordinationManager#reconnectBeans(geovista.coordination.FiringMethod, geovista.coordination.ListeningBean)}.
	 */
	public void testReconnectBeans() {
		//fail("Not yet implemented"); // TODO
	}

}
