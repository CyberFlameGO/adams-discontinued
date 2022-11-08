import adams.core.Utils as Utils
import adams.flow.core.Token as Token
import adams.flow.core.Unknown as Unknown
import adams.flow.transformer.AbstractScript as AbstractScript

import java.lang.Class as Class

class TemplateTransformer(AbstractScript):
    """
    Template of a Jython transformer.

    @author FracPete (fracpete at waikato dot ac dot nz)
    """

    def __init__(self):
        """
        Initializes the transformer.
        """

        AbstractScript.__init__(self)

    def globalInfo(self):
        """
        Returns a string describing the object.

        @return: a description suitable for displaying in the gui
        """

        return "FIXME."

    def accepts(self):
        """
        Returns the class of objects that it accepts.

        @return: the classes
        @rtype: list
        """

        # very in-elegant, but works
        # http://www.prasannatech.net/2009/02/class-object-name-java-interface-jython.html
        return [Class.forName("java.lang.Object")]  # FIXME

    def generates(self):
        """
        Returns the class of objects that it generates.

        @return: the classes
        @rtype: list
        """

        # very in-elegant, but works
        # http://www.prasannatech.net/2009/02/class-object-name-java-interface-jython.html
        return [Class.forName("java.lang.Object")]  # FIXME

    def doExecute(self):
        """
        Executes the flow item.

        @return: None if everything is fine, otherwise error message
        @rtype: str
        """
        
        # FIXME
        self.m_OutputToken = Token(self.m_InputToken.getPayload())
        return None

