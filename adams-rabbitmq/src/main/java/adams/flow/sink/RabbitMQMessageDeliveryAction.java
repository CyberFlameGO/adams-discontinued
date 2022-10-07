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
 * RabbitMQMessageDeliveryAction.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package adams.flow.sink;

import adams.core.ClassCrossReference;
import adams.core.QuickInfoHelper;
import adams.core.net.rabbitmq.deliveryaction.AbstractDeliveryAction;
import adams.core.net.rabbitmq.deliveryaction.NoAction;
import adams.data.conversion.RabbitMQEnvelopeToMap;
import adams.flow.control.StorageName;
import adams.flow.control.StorageUser;
import com.rabbitmq.client.Channel;

/**
 <!-- globalinfo-start -->
 * Executes the specified message delivery action using the incoming delivery tag.<br>
 * The specified storage item represents the channel used by the action.<br>
 * <br>
 * See also:<br>
 * adams.data.conversion.RabbitMQEnvelopeToMap
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;java.lang.Long<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: RabbitMQMessageDeliveryAction
 * </pre>
 *
 * <pre>-annotation &lt;adams.core.base.BaseAnnotation&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-skip &lt;boolean&gt; (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded
 * &nbsp;&nbsp;&nbsp;as it is.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-stop-flow-on-error &lt;boolean&gt; (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow execution at this level gets stopped in case this
 * &nbsp;&nbsp;&nbsp;actor encounters an error; the error gets propagated; useful for critical
 * &nbsp;&nbsp;&nbsp;actors.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-silent &lt;boolean&gt; (property: silent)
 * &nbsp;&nbsp;&nbsp;If enabled, then no errors are output in the console; Note: the enclosing
 * &nbsp;&nbsp;&nbsp;actor handler must have this enabled as well.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 *
 * <pre>-storage-name &lt;adams.flow.control.StorageName&gt; (property: storageName)
 * &nbsp;&nbsp;&nbsp;The name of the stored channel to retrieve.
 * &nbsp;&nbsp;&nbsp;default: storage
 * </pre>
 *
 * <pre>-action &lt;adams.core.net.rabbitmq.deliveryaction.AbstractDeliveryAction&gt; (property: action)
 * &nbsp;&nbsp;&nbsp;The message delivery action to execute.
 * &nbsp;&nbsp;&nbsp;default: adams.core.net.rabbitmq.deliveryaction.NoAction
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class RabbitMQMessageDeliveryAction
  extends AbstractSink
  implements ClassCrossReference, StorageUser {

  private static final long serialVersionUID = -7073183797972945731L;

  /** the name of the stored channel. */
  protected StorageName m_StorageName;

  /** the action to execute. */
  protected AbstractDeliveryAction m_Action;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Executes the specified message delivery action using the incoming delivery tag.\n"
      + "The specified storage item represents the channel used by the action.";
  }

  /**
   * Returns the cross-referenced classes.
   *
   * @return		the classes
   */
  public Class[] getClassCrossReferences() {
    return new Class[]{RabbitMQEnvelopeToMap.class};
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "storage-name", "storageName",
      new StorageName());

    m_OptionManager.add(
      "action", "action",
      new NoAction());
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    String	result;

    result = QuickInfoHelper.toString(this, "storageName", m_StorageName, "channel: ");
    result += QuickInfoHelper.toString(this, "action", m_Action, ", action: ");

    return result;
  }

  /**
   * Sets the name of the stored channel.
   *
   * @param value	the name
   */
  public void setStorageName(StorageName value) {
    m_StorageName = value;
    reset();
  }

  /**
   * Returns the name of the stored channel.
   *
   * @return		the name
   */
  public StorageName getStorageName() {
    return m_StorageName;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String storageNameTipText() {
    return "The name of the stored channel to retrieve.";
  }

  /**
   * Sets the action to run.
   *
   * @param value	the action
   */
  public void setAction(AbstractDeliveryAction value) {
    m_Action = value;
    reset();
  }

  /**
   * Returns the action to run.
   *
   * @return 		the action
   */
  public AbstractDeliveryAction getAction() {
    return m_Action;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   *             	displaying in the GUI or for listing the options.
   */
  public String actionTipText() {
    return "The message delivery action to execute.";
  }

  /**
   * Returns whether storage items are being used.
   *
   * @return		true if storage items are used
   */
  public boolean isUsingStorage() {
    return !getSkip();
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the Class of objects that can be processed
   */
  @Override
  public Class[] accepts() {
    return new Class[]{Long.class};
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String	result;
    Long	tag;
    Channel	channel;

    result = null;

    channel = null;
    if (getStorageHandler().getStorage().has(m_StorageName))
      channel = (Channel) getStorageHandler().getStorage().get(m_StorageName);
    else
      result = "Failed to retrieve channel: '" + m_StorageName.getValue() + "'";

    if (result == null) {
      tag = m_InputToken.getPayload(Long.class);
      m_Action.performAction(channel, tag);
    }

    return result;
  }
}
