package jfxtras.labs.samples;

import javafx.scene.web.WebView;
import fxsampler.FXSamplerProject;
import fxsampler.model.WelcomePage;

/**
 */
public class JFXtrasLabsSamplesProject implements FXSamplerProject {
	@Override
	public String getProjectName() {
		return "JFxtrasLabs";
	}

	@Override
	public String getSampleBasePackage() {
		return "jfxtras.labs.samples";
	}

	@Override
	public WelcomePage getWelcomePage() {
		WebView webView = new WebView();
		webView.getEngine().load("http://jfxtras.org");
		return new WelcomePage("JFXtras labs", webView);
	}
}
