@echo off

"%~dp0\launcher.bat" -main adams.gui.Main -memory 2g -title ADAMS-Deeplearning-Spectral -env-modifier adams.core.management.WekaHomeEnvironmentModifier
