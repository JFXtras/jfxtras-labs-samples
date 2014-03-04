package jfxtras.samples;

import jfxtras.labs.samples.SampleBase;

/**
 */
abstract public class JFXtrasSampleBase extends SampleBase {

	/** {@inheritDoc} */
	@Override public String getProjectName() {
		return "JFXtras";
	}

	/** {@inheritDoc} */
	@Override public String getProjectVersion() {
		return "8.0"; // TODO read from gradle meta-inf?
	}
}
