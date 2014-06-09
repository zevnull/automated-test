package es.s2o.automated.test.core.filedownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckFileHash {

	private static final Logger LOG = LoggerFactory.getLogger(CheckFileHash.class);

    private TypeOfHash typeOfOfHash = null;
    private String expectedFileHash = null;
    private File fileToCheck = null;

    /**
     * The File to perform a Hash check upon
     *
     * @param fileToCheck
     * @throws FileNotFoundException
     */
    public void fileToCheck(File fileToCheck) throws FileNotFoundException {
        if (!fileToCheck.exists()) throw new FileNotFoundException(fileToCheck + " does not exist!");

        this.fileToCheck = fileToCheck;
    }

    /**
     * Hash details used to perform the Hash check
     *
     * @param hash
     * @param typeOfHash
     */
    public void hashDetails(String hash, TypeOfHash typeOfHash) {
        this.expectedFileHash = hash;
        this.typeOfOfHash = typeOfHash;
    }

    /**
     * Performs a expectedFileHash check on a File.
     *
     * @return
     * @throws IOException
     */
    public boolean hasAValidHash() throws IOException {
        if (this.fileToCheck == null) throw new FileNotFoundException("File to check has not been set!");
        if (this.expectedFileHash == null || this.typeOfOfHash == null) throw new NullPointerException("Hash details have not been set!");

        String actualFileHash = "";
        boolean isHashValid = false;

        switch (this.typeOfOfHash) {
            case MD5:
                actualFileHash = DigestUtils.md5Hex(new FileInputStream(this.fileToCheck));
                if (this.expectedFileHash.equals(actualFileHash)) isHashValid = true;
                break;
            case SHA1:
                actualFileHash = DigestUtils.shaHex(new FileInputStream(this.fileToCheck));
                if (this.expectedFileHash.equals(actualFileHash)) isHashValid = true;
                break;
        }

        LOG.info("Filename = '" + this.fileToCheck.getName() + "'");
        LOG.info("Expected Hash = '" + this.expectedFileHash + "'");
        LOG.info("Actual Hash = '" + actualFileHash + "'");

        return isHashValid;
    }

}
