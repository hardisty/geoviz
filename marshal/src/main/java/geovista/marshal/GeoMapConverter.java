package geovista.marshal;

import java.awt.Color;

import geovista.geoviz.map.GeoMap;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class GeoMapConverter implements Converter {

	
	public boolean canConvert(Class clazz) {
		return clazz.equals(GeoMap.class);
	}
	
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		GeoMap map = (GeoMap)value;
		int[] selection = map.getSelectedObservations();

		if (selection != null){
		writer.startNode("selection");
		context.convertAnother(selection);

		//ArrayConverter.marshal(selection, writer, context);
		writer.endNode();
		
		}
		
		writer.startNode("background");
		context.convertAnother(map.getBackgroundColor());
		writer.endNode();

	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		GeoMap map = new GeoMap();
		
		while(reader.hasMoreChildren()){
			reader.moveDown();
			if ("selection".equals(reader.getNodeName())){
				int[] selection = (int[])context.convertAnother(map, int[].class);
				map.setSelectedObservations(selection);
			} else if ("background".equals(reader.getNodeName())){
				map.setBackgroundColor((Color) context.convertAnother(map, Color.class));
			}
			reader.moveUp();
		}
		
		
		return map;
	}



}
