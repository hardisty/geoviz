package geovista.marshal;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import geovista.common.ui.VariablePicker;

public class VariablePickerConverter implements Converter {

	public boolean canConvert(Class clazz) {
		return clazz.equals(VariablePicker.class);
	}

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		// int[] selection = map.getSelectedObservations();
		int[] selection = null;
		if (selection != null) {
			writer.startNode("selection");
			context.convertAnother(selection);

			writer.endNode();

		}

	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		VariablePicker sPlot = new VariablePicker();

		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("selection".equals(reader.getNodeName())) {
				int[] selection = (int[]) context.convertAnother(sPlot,
						int[].class);
				// map.setSelectedObservations(selection);
			}
			reader.moveUp();
		}

		return sPlot;
	}

}
