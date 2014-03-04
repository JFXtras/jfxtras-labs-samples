@echo off
IF EXIST "gradle_localOverride.cmd" call "gradle_localOverride.cmd"
call gradlew clean shadow -x test --stacktrace --info
javafxpackager -deploy -native -outdir build\dist -outfile JFXtrasSamples -srcdir build\libs -srcfiles jfxtras-labs-samples-8.0-r1-SNAPSHOT-shadow.jar -appclass fxsampler.FXSampler -name "JFXtrasSamples" -title "JFXtras samples"
pause