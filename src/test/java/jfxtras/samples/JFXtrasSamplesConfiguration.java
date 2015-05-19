package jfxtras.samples;

import fxsampler.FXSamplerConfiguration;

public class JFXtrasSamplesConfiguration implements FXSamplerConfiguration {

	@Override
	public String getSceneStylesheet() {
		String stylesheet = JFXtrasSamplesConfiguration.class.getResource("/jfxtras/samples/JFXtrasSamples.css").toExternalForm();
		return stylesheet;
	}
}
