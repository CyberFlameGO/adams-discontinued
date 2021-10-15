/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * CNTKPreferencesPanel.java
 * Copyright (C) 2017 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.application;

import adams.core.io.FileUtils;
import adams.env.Environment;
import adams.gui.core.PropertiesParameterPanel.PropertyType;

import java.io.File;
import java.util.List;

/**
 * Preferences for CNTK.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class CNTKPreferencesPanel
  extends AbstractPropertiesPreferencesPanel {

  /** for serialization. */
  private static final long serialVersionUID = 3895159356677639564L;

  /**
   * For initializing the GUI.
   */
  @Override
  protected void initGUI() {
    List<String> 	order;

    super.initGUI();

    addPropertyType("Binary", PropertyType.FILE_ABSOLUTE);

    setPreferences(adams.ml.cntk.CNTK.getProperties());
  }

  /**
   * The title of the preferences.
   *
   * @return		the title
   */
  @Override
  public String getTitle() {
    return "CNTK";
  }

  /**
   * Returns whether the panel requires a wrapper scrollpane/panel for display.
   *
   * @return		true if wrapper required
   */
  @Override
  public boolean requiresWrapper() {
    return false;
  }

  /**
   * Activates the settings.
   *
   * @return		null if successfully activated, otherwise error message
   */
  @Override
  public String activate() {
    if (getPreferences().save(Environment.getInstance().createPropertiesFilename(new File(adams.ml.cntk.CNTK.FILENAME).getName())))
      return null;
    else
      return "Failed to save CNTK setup!";
  }

  /**
   * Returns whether the panel supports resetting the options.
   *
   * @return		true if supported
   */
  public boolean canReset() {
    String	props;

    props = Environment.getInstance().createPropertiesFilename(new File(adams.ml.cntk.CNTK.FILENAME).getName());
    return FileUtils.fileExists(props);
  }

  /**
   * Resets the settings to their default.
   *
   * @return		null if successfully reset, otherwise error message
   */
  public String reset() {
    String	props;

    props = Environment.getInstance().createPropertiesFilename(new File(adams.ml.cntk.CNTK.FILENAME).getName());
    if (FileUtils.fileExists(props)) {
      if (!FileUtils.delete(props))
        return "Failed to remove custom CNTK properties: " + props;
    }

    return null;
  }
}
