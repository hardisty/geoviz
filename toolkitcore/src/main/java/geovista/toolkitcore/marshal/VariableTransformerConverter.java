package geovista.toolkitcore.marshal;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import geovista.geoviz.spreadsheet.VariableTransformer;

public class VariableTransformerConverter extends VizBeanConverter {

	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(VariableTransformer.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		super.marshal(value, writer, context);

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		VariableTransformer plot = new VariableTransformer();
		plot = (VariableTransformer) super.unmarshal(reader, context, plot);
		return plot;

	}

}
