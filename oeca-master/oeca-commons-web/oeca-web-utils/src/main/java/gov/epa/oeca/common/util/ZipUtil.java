package gov.epa.oeca.common.util;

import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author dfladung
 */
public class ZipUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    public static File zip(File src, String name) {
        File zip = null;
        ZipOutputStream zipOut = null;
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(src);
            zip = File.createTempFile("ZipUtil", ".zip");
            fileOut = new FileOutputStream(zip);
            zipOut = new ZipOutputStream(fileOut);
            ZipEntry entry = new ZipEntry(name);
            zipOut.putNextEntry(entry);
            IOUtils.copy(fileIn, zipOut);
            zipOut.closeEntry();
            zipOut.finish();

            return zip;
        } catch (Exception e) {
            FileUtils.deleteQuietly(zip);
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        } finally {
            IOUtils.closeQuietly(fileIn);
            IOUtils.closeQuietly(zipOut);
            IOUtils.closeQuietly(fileOut);
        }
    }

    public static File zip(List<File> files){
        File zip = null;
        ZipOutputStream zipOut = null;
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        try {
            zip = File.createTempFile("ZipUtil", ".zip");
            fileOut = new FileOutputStream(zip);
            zipOut = new ZipOutputStream(fileOut);
            for (File src : files){
                ZipEntry entry = new ZipEntry(src.getName());
                zipOut.putNextEntry(entry);
                fileIn = new FileInputStream(src);
                IOUtils.copy(fileIn, zipOut);
                zipOut.closeEntry();
                IOUtils.closeQuietly(fileIn);
            }
            zipOut.finish();
            return zip;
        } catch (Exception e) {
            FileUtils.deleteQuietly(zip);
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        } finally {
            IOUtils.closeQuietly(fileIn);
            IOUtils.closeQuietly(zipOut);
            IOUtils.closeQuietly(fileOut);
        }
    }
}
