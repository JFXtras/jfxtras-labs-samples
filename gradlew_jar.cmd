@echo off
IF EXIST "gradle_localOverride.cmd" call "gradle_localOverride.cmd"
call gradlew clean jar -x test -x javadoc --stacktrace --info
pause