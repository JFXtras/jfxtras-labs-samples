package jfxtras.samples;

import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import fxsampler.FXSamplerProject;
import fxsampler.model.WelcomePage;

/**
 */
public class JFXtrasSamplesProject implements FXSamplerProject {
	@Override
	public String getProjectName() {
		return "JFxtras";
	}

	@Override
	public String getSampleBasePackage() {
		return "jfxtras.samples";
	}

	@Override
	public WelcomePage getWelcomePage() {
		WebView webView = new WebView();
		webView.getEngine().load("http://jfxtras.org");
		return new WelcomePage("JFXtras", webView);
	}
}
