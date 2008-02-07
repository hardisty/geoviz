package geovista.marshal;



import geovista.toolkitcore.GeoVizToolkit;
import geovista.toolkitcore.ToolkitBeanSet;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class GeoVizToolkitConverter implements Converter {

	
	public boolean canConvert(Class clazz) {
		return clazz.equals(GeoVizToolkit.class);
	}
	
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		GeoVizToolkit gvz = (GeoVizToolkit)value;
		String  fileName = gvz.getFileName();

		writer.startNode("fileName");
		context.convertAnother(fileName);
		writer.endNode();
		
		writer.startNode("useProj");
		context.convertAnother(gvz.isUseProj());
		writer.endNode();
		
		writer.startNode("tBeanSet");
		context.convertAnother(gvz.getTBeanSet());
		writer.endNode();
		
		

	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		GeoVizToolkit gvz = new GeoVizToolkit();
		String fileName = null;
		Boolean useProj = false;
		
		while(reader.hasMoreChildren()){
			reader.moveDown();
			if ("fileName".equals(reader.getNodeName())){
				fileName = (String) context.convertAnother(gvz, String.class);
			}
			else if ("useProj".equals(reader.getNodeName())){
				useProj = (Boolean) context.convertAnother(gvz, boolean.class);
			}			
			else if ("tBeanSet".equals(reader.getNodeName())){
				ToolkitBeanSet tBeans = (ToolkitBeanSet) context.convertAnother(gvz, ToolkitBeanSet.class);
				
				
				
				
				gvz.setTBeanSet(tBeans);
			}			
			
			
			
			reader.moveUp();
			
			
			
		}
		
		gvz.init(fileName, useProj);
		return gvz;
	}



}
