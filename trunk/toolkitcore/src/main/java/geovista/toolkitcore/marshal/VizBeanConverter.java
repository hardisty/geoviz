package geovista.toolkitcore.marshal;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;

public abstract class VizBeanConverter implements Converter {

	private static String SELECTION_STRING = "selection";
	private static String BACKGROUND_COLOR_STRING = "backgroundColor";

	/* subclassses need to override, always returns false */
	public boolean canConvert(Class clazz) {
		return false;
	}

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {

		if (value instanceof SelectionListener) {
			SelectionListener selList = (SelectionListener) value;
			int[] selection = selList.getSelectionEvent().getSelection();
			if (selection != null) {
				writer.startNode(VizBeanConverter.SELECTION_STRING);
				context.convertAnother(selection);
				writer.endNode();

			}
		}
		if (value instanceof JComponent) {
			JComponent jcomp = (JComponent) value;
			writer.startNode(VizBeanConverter.BACKGROUND_COLOR_STRING);
			context.convertAnother(jcomp.getBackground());
			writer.endNode();
		}

	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return new Object();

	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context, Object instantiatedObject) {

		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if (VizBeanConverter.SELECTION_STRING.equals(reader.getNodeName())) {
				SelectionListener selList = (SelectionListener) instantiatedObject;
				int[] selection = (int[]) context.convertAnother(
						instantiatedObject, int[].class);
				SelectionEvent e = new SelectionEvent(this, selection);
				selList.selectionChanged(e);
			} else if (VizBeanConverter.BACKGROUND_COLOR_STRING.equals(reader
					.getNodeName())) {
				JComponent comp = (JComponent) instantiatedObject;
				Color bColor = (ColorUIResource) context.convertAnother(
						instantiatedObject, ColorUIResource.class);

				comp.setBackground(bColor);
			}
			reader.moveUp();
		}

		return instantiatedObject;
	}

}
