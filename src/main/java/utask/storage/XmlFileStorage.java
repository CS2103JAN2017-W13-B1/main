package utask.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import utask.commons.exceptions.DataConversionException;
import utask.commons.util.XmlUtil;

/**
 * Stores addressbook data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given addressbook data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableUTask addressBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, addressBook);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns address book in the file or an empty address book
     */
    public static XmlSerializableUTask loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableUTask.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
