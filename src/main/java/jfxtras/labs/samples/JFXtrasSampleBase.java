package jfxtras.labs.samples;

import fxsampler.SampleBase;

/**
 */
abstract public class JFXtrasSampleBase extends SampleBase {

	/** {@inheritDoc} */
	@Override public String getProjectName() {
		return "JFXtras";
	}

	/** {@inheritDoc} */
	@Override public String getProjectVersion() {
		return "8.0"; // TBEE read from gradle meta-inf?
	}
}
