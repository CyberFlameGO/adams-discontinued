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

/**
 * TwitterSetupPanel.java
 * Copyright (C) 2013-2016 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.application;

import adams.core.Constants;
import adams.core.Properties;
import adams.core.io.FileUtils;
import adams.core.net.TwitterHelper;
import adams.env.Environment;
import adams.env.TwitterDefinition;
import adams.gui.core.BaseCheckBox;
import adams.gui.core.BaseTextField;
import adams.gui.core.ParameterPanel;

import javax.swing.JPasswordField;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

/**
 * Panel for configuring the system-wide twitter settings.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class TwitterSetupPanel
  extends AbstractPreferencesPanel {

  /** for serialization. */
  private static final long serialVersionUID = -7937644706618374284L;

  /** the parameters. */
  protected ParameterPanel m_PanelParameters;

  /** the consumer key. */
  protected BaseTextField m_TextConsumerKey;

  /** the consumer secret. */
  protected JPasswordField m_TextConsumerSecret;

  /** Whether to show the consumer secret. */
  protected BaseCheckBox m_CheckBoxShowConsumerSecret;

  /** the access token. */
  protected BaseTextField m_TextAccessToken;

  /** the access token secret. */
  protected JPasswordField m_TextAccessTokenSecret;

  /** Whether to show the access token secret. */
  protected BaseCheckBox m_CheckBoxShowAccessTokenSecret;

  /**
   * Initializes the members.
   */
  @Override
  protected void initGUI() {
    super.initGUI();

    setLayout(new BorderLayout());

    m_PanelParameters = new ParameterPanel();
    add(m_PanelParameters, BorderLayout.CENTER);

    m_TextConsumerKey = new BaseTextField(20);
    m_TextConsumerKey.setText(TwitterHelper.getConsumerKey());
    m_PanelParameters.addParameter("Consumer _key", m_TextConsumerKey);

    m_TextConsumerSecret = new JPasswordField(20);
    m_TextConsumerSecret.setText(TwitterHelper.getConsumerSecret().getValue());
    m_TextConsumerSecret.setEchoChar(Constants.PASSWORD_CHAR);
    m_PanelParameters.addParameter("Consumer _secret", m_TextConsumerSecret);

    m_CheckBoxShowConsumerSecret = new BaseCheckBox();
    m_CheckBoxShowConsumerSecret.setSelected(false);
    m_CheckBoxShowConsumerSecret.addActionListener((ActionEvent e) -> {
      if (m_CheckBoxShowConsumerSecret.isSelected())
        m_TextConsumerSecret.setEchoChar((char) 0);
      else
        m_TextConsumerSecret.setEchoChar(Constants.PASSWORD_CHAR);
    });
    m_PanelParameters.addParameter("Show consumer secret", m_CheckBoxShowConsumerSecret);

    m_TextAccessToken = new BaseTextField(20);
    m_TextAccessToken.setText(TwitterHelper.getAccessToken());
    m_PanelParameters.addParameter("_Access token", m_TextAccessToken);

    m_TextAccessTokenSecret = new JPasswordField(20);
    m_TextAccessTokenSecret.setText(TwitterHelper.getAccessTokenSecret().getValue());
    m_TextAccessTokenSecret.setEchoChar(Constants.PASSWORD_CHAR);
    m_PanelParameters.addParameter("Access _token secret", m_TextAccessTokenSecret);

    m_CheckBoxShowAccessTokenSecret = new BaseCheckBox();
    m_CheckBoxShowAccessTokenSecret.setSelected(false);
    m_CheckBoxShowAccessTokenSecret.addActionListener((ActionEvent e) -> {
      if (m_CheckBoxShowAccessTokenSecret.isSelected())
        m_TextAccessTokenSecret.setEchoChar((char) 0);
      else
        m_TextAccessTokenSecret.setEchoChar(Constants.PASSWORD_CHAR);
    });
    m_PanelParameters.addParameter("Show access token secret", m_CheckBoxShowAccessTokenSecret);
  }

  /**
   * Turns the parameters in the GUI into a properties object.
   *
   * @return		the properties
   */
  protected Properties toProperties() {
    Properties	result;

    result = new Properties();

    result.setProperty(TwitterHelper.CONSUMER_KEY, m_TextConsumerKey.getText());
    result.setProperty(TwitterHelper.CONSUMER_SECRET, m_TextConsumerSecret.getText());
    result.setProperty(TwitterHelper.ACCESS_TOKEN, m_TextAccessToken.getText());
    result.setProperty(TwitterHelper.ACCESS_TOKEN_SECRET, m_TextAccessTokenSecret.getText());

    return result;
  }

  /**
   * The title of the preference panel.
   * 
   * @return		the title
   */
  @Override
  public String getTitle() {
    return "Twitter";
  }

  /**
   * Returns whether the panel requires a wrapper scrollpane/panel for display.
   * 
   * @return		true if wrapper required
   */
  @Override
  public boolean requiresWrapper() {
    return true;
  }
  
  /**
   * Activates the twitter setup.
   * 
   * @return		null if successfully activated, otherwise error message
   */
  @Override
  public String activate() {
    boolean	result;

    result = TwitterHelper.writeProperties(toProperties());
    if (result)
      return null;
    else
      return "Failed to save twitter setup to " + TwitterHelper.FILENAME + "!";
  }

  /**
   * Returns whether the panel supports resetting the options.
   *
   * @return		true if supported
   */
  public boolean canReset() {
    String	props;

    props = Environment.getInstance().getCustomPropertiesFilename(TwitterDefinition.KEY);
    return (props != null) && FileUtils.fileExists(props);
  }

  /**
   * Resets the settings to their default.
   *
   * @return		null if successfully reset, otherwise error message
   */
  public String reset() {
    String	props;

    props = Environment.getInstance().getCustomPropertiesFilename(TwitterDefinition.KEY);
    if ((props != null) && FileUtils.fileExists(props)) {
      if (!FileUtils.delete(props))
	return "Failed to remove custom Twitter properties: " + props;
    }

    return null;
  }
}
