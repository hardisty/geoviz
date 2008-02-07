/**
 * 
 */
package geovista.marshal;

import com.thoughtworks.xstream.XStream;

import geovista.animation.IndicationAnimator;
import geovista.common.ui.VariablePicker;
import geovista.geoviz.map.GeoMap;
import geovista.geoviz.scatterplot.SingleHistogram;
import geovista.geoviz.star.StarPlot;
import geovista.geoviz.star.StarPlotMap;
import geovista.toolkitcore.GeoVizToolkit;
import geovista.toolkitcore.ToolkitBean;

/**
 * @author localadmin
 * 
 */
public class Marshaller {

	XStream streamer;

	public Marshaller() {
		streamer = new XStream();
		streamer.alias("GeoMap", GeoMap.class);
		streamer.registerConverter(new GeoMapConverter());

		streamer.registerConverter(new JPanelConverter(streamer.getMapper(),
				streamer.getReflectionProvider()));
		streamer.registerConverter(new JInternalFrameConverter());

		streamer.alias("StarPlot", StarPlot.class);
		streamer.registerConverter(new StarPlotConverter());

		streamer.alias("StarPlotMap", StarPlotMap.class);
		streamer.registerConverter(new StarPlotMapConverter());

		streamer.alias("IndicationAnimator", IndicationAnimator.class);
		streamer.registerConverter(new IndicationAnimatorConverter());

		streamer.alias("SingleHistogram", SingleHistogram.class);
		streamer.registerConverter(new SingleHistogramConverter());

		streamer.alias("VariablePicker", VariablePicker.class);
		streamer.registerConverter(new VariablePickerConverter());

		streamer.alias("GeoVizToolkit", GeoVizToolkit.class);
		streamer.registerConverter(new GeoVizToolkitConverter());

		streamer.alias("ToolkitBean", ToolkitBean.class);
		streamer.registerConverter(new ToolkitBeanConverter());

	}

	public String vizToXML(GeoVizToolkit gvt) {

		return null;
	}

	public String toXML(Object obj) {
		String xml = streamer.toXML(obj);
		return xml;
	}

	public GeoVizToolkit fromXML(String xml) {
		GeoVizToolkit gvz2 = (GeoVizToolkit) streamer.fromXML(xml);
		return gvz2;
	}

}
