/**
 * 
 */
package geovista.toolkitcore.marshal;

import com.thoughtworks.xstream.XStream;

import geovista.animation.IndicationAnimator;
import geovista.common.ui.VariablePicker;
import geovista.geoviz.map.GeoMap;
import geovista.geoviz.parvis.ParallelPlot;
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
	public final static Marshaller INSTANCE = new Marshaller();

	private Marshaller() {

		streamer = new XStream();
		// streamer.setMode(XStream.NO_REFERENCES);
		streamer.alias("GeoMap", GeoMap.class);
		streamer.registerConverter(new GeoMapConverter());

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

		streamer.alias("ParallelPlot", ParallelPlot.class);
		streamer.registerConverter(new ParallelPlotConverter());

		streamer.alias("GeoVizToolkit", GeoVizToolkit.class);
		streamer.registerConverter(new GeoVizToolkitConverter());

		streamer.alias("ToolkitBean", ToolkitBean.class);
		streamer.registerConverter(new ToolkitBeanConverter());

		streamer.registerConverter(new JInternalFrameConverter());

		// streamer.registerConverter(new JPanelConverter(streamer.getMapper(),
		// streamer.getReflectionProvider()));

	}

	public String vizToXML(GeoVizToolkit gvt) {

		return null;
	}

	public String toXML(Object obj) {
		String xml = streamer.toXML(obj);
		return xml;
	}

	public Object fromXML(String xml) {
		Object gvz2 = streamer.fromXML(xml);
		return gvz2;
	}

}
