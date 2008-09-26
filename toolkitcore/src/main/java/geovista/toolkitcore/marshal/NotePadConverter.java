package geovista.toolkitcore.marshal;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import geovista.common.ui.NotePad;

public class NotePadConverter implements Converter {

	public boolean canConvert(Class clazz) {
		return clazz.equals(NotePad.class);
	}

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		NotePad frame = (NotePad) value;

		writer.startNode("fieldText");
		String text = frame.textField.getText();
		context.convertAnother(Marshaler.newLinesToXML(text));
		writer.endNode();

		writer.startNode("areaText");
		context.convertAnother(Marshaler.newLinesToXML(frame.textArea
				.getText()));
		writer.endNode();

	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		NotePad frame = new NotePad();

		reader.moveDown();

		String fieldText = (String) context.convertAnother(frame, String.class);
		frame.textField.setText(Marshaler.newLinesToJava(fieldText));
		reader.moveUp();
		reader.moveDown();
		String areaText = (String) context.convertAnother(frame, String.class);
		frame.textArea.setText(Marshaler.newLinesToJava(areaText));
		reader.moveUp();

		return frame;
	}

}
