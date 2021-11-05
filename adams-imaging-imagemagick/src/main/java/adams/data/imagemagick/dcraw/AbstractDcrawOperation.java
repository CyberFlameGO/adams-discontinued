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
 * AbstractDcrawOperation.java
 * Copyright (C) 2014-2015 University of Waikato, Hamilton, New Zealand
 */
package adams.data.imagemagick.dcraw;

import adams.core.AtomicMoveSupporter;
import adams.core.io.FileUtils;
import adams.core.io.PlaceholderFile;
import adams.core.logging.LoggingHelper;
import adams.data.imagemagick.AbstractImageOperation;
import adams.data.imagemagick.DCRawHelper;

/**
 * Ancestor for DCRaw operations.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractDcrawOperation
  extends AbstractImageOperation
  implements AtomicMoveSupporter {

  /** for serialization. */
  private static final long serialVersionUID = 4447009209054143230L;

  /** whether to perform an atomic move. */
  protected boolean m_AtomicMove;

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "atomic-move", "atomicMove",
      false);
  }

  /**
   * Sets whether to attempt atomic move operation.
   *
   * @param value	if true then attempt atomic move operation
   */
  public void setAtomicMove(boolean value) {
    m_AtomicMove = value;
    reset();
  }

  /**
   * Returns whether to attempt atomic move operation.
   *
   * @return 		true if to attempt atomic move operation
   */
  public boolean getAtomicMove() {
    return m_AtomicMove;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String atomicMoveTipText() {
    return
        "If true, then an atomic move operation will be attempted "
	  + "(NB: not supported by all operating systems).";
  }

  /**
   * Hook method for performing checks before applying the operation.
   * 
   * @param input	the input file
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  @Override
  protected String check(PlaceholderFile input, PlaceholderFile output) {
    if (!DCRawHelper.isDcrawAvailable())
      return DCRawHelper.getMissingDcrawErrorMessage();
    else
      return super.check(input, output);
  }
  
  /**
   * Moves the temporary file to its final location.
   * 
   * @param input	the input file (used to determine tmp file name)
   * @param output	the output file
   * @return		null if successful, otherwise error message
   */
  protected String move(PlaceholderFile input, PlaceholderFile output) {
    String		result;
    PlaceholderFile	tmp;
    
    result = null;
    
    tmp = FileUtils.replaceExtension(input, ".ppm");
    if (isLoggingEnabled())
      getLogger().info("Moving tmp file '" + tmp + "' to '" + output + "'...");
    
    try {
      if (!FileUtils.move(tmp, output, m_AtomicMove))
	result = "Failed to move file '" + tmp + "' to '" + output + "'!";
    }
    catch (Exception e) {
      result = LoggingHelper.handleException(
	  this, "Failed to move file '" + tmp + "' to '" + output + "'!", e);
    }
    
    if ((result == null) && isLoggingEnabled())
      getLogger().info("Moved tmp file '" + tmp + "' successfully to '" + output + "'.");
    
    return result;
  }
}
