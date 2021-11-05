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
 * ImageMagickHelper.java
 * Copyright (C) 2011-2015 University of Waikato, Hamilton, New Zealand
 */
package adams.data.imagemagick;

import adams.core.io.FileUtils;
import adams.env.Environment;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.Pipe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Helper class for ImageMagick (http://www.imagemagick.org/).
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class ImageMagickHelper {
  
  /** the environment variable to check for imagemagick. */
  public final static String ENV_PATH = "IM_TOOLPATH";

  /** whether "convert" is present. */
  protected static Boolean m_ConvertPresent;

  /**
   * Checks whether the "convert" utility is available.
   *
   * @return		true if "convert" is available (on PATH)
   */
  public static boolean isConvertAvailable() {
    Process	proc;
    String	path;
    String	exec;

    if (m_ConvertPresent == null) {
      exec = FileUtils.fixExecutable("convert");
      path = System.getenv(ENV_PATH);
      if (path != null)
        exec = path + File.separator + exec;
      try {
	proc = Runtime.getRuntime().exec(new String[]{exec, "-version"});
	m_ConvertPresent = (proc.waitFor() == 0);
      }
      catch (Exception e) {
        System.err.println("Failed to execute '" + exec + "':");
        e.printStackTrace();
	m_ConvertPresent = false;
      }
    }

    return m_ConvertPresent;
  }

  /**
   * Returns a standard error message if specifiec command is not available.
   *
   * @param cmd		the command to use in error message, e.g., "convert"
   * @return		the error message
   */
  protected static String getMissingCommandErrorMessage(String cmd) {
    return "ImageMagick (i.e., '" + cmd + "' command) not installed or " 
           + ENV_PATH + " environment variable not pointing to installation!";
  }

  /**
   * Returns a standard error message if "convert" is not available.
   *
   * @return		the error message
   */
  public static String getMissingConvertErrorMessage() {
    return getMissingCommandErrorMessage("convert");
  }

  /**
   * Reads a BufferedImage from the file.
   *
   * @param file	the file to read the image from
   * @return		the image, null in case of an error
   */
  public static BufferedImage read(File file) {
    BufferedImage	result;
    FileInputStream	stream;

    stream = null;
    try {
      stream = new FileInputStream(file.getAbsoluteFile());
      result = read(stream);
    }
    catch (Exception e) {
      result = null;
      System.err.println("Failed to read image from '" + file + "':");
      e.printStackTrace();
    }
    finally {
      FileUtils.closeQuietly(stream);
    }

    return result;
  }

  /**
   * Reads a BufferedImage from a URL.
   *
   * @param url	the URL to read the image from
   * @return		the image, null in case of an error
   */
  public static BufferedImage read(URL url) {
    BufferedImage	result;
    InputStream		stream;

    stream = null;
    try {
      stream = url.openStream();
      result = read(stream);
    }
    catch (Exception e) {
      result = null;
      System.err.println("Failed to read image from '" + url + "':");
      e.printStackTrace();
    }
    finally {
      if (stream != null) {
	try {
	  stream.close();
	}
	catch (Exception e) {
	  // ignored
	}
      }
    }

    return result;
  }

  /**
   * Reads a BufferedImage from the stream.
   * Caller must close stream explicitly.
   *
   * @param stream	the stream to read the image from
   * @return		the image, null in case of an error
   */
  public static BufferedImage read(InputStream stream) {
    BufferedImage		result;
    IMOperation			op;
    ConvertCmd			cmd;
    Stream2BufferedImage 	s2b;

    op = new IMOperation();
    op.addImage("-");  // stdin
    op.addImage("-");  // stdout (no conversion)

    s2b = new Stream2BufferedImage();
    cmd = new ConvertCmd();
    cmd.setInputProvider(new Pipe(stream, null));
    cmd.setOutputConsumer(s2b);
    try {
      cmd.run(op);
      result = s2b.getImage();
    }
    catch (Exception e) {
      result = null;
      System.err.println("Failed to read image from stream:");
      e.printStackTrace();
    }

    return result;
  }
  
  /**
   * Just outputs some information on availability of tools.
   * 
   * @param args	ignored
   */
  public static void main(String[] args) {
    Environment.setEnvironmentClass(Environment.class);
    System.out.println("Tool availability:");
    System.out.println("- convert? " + isConvertAvailable());
  }
}
