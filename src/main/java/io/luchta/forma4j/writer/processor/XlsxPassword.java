package io.luchta.forma4j.writer.processor;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class XlsxPassword {
    public void set(String outputXlsxPath, String password) {
        try (POIFSFileSystem poifsFileSystem = new POIFSFileSystem()) {
            EncryptionInfo encryptionInfo = new EncryptionInfo(EncryptionMode.standard);
            Encryptor encryptor = encryptionInfo.getEncryptor();
            encryptor.confirmPassword(password);
            try (OPCPackage opcPackage = OPCPackage.open(outputXlsxPath);
                 OutputStream outputStream = encryptor.getDataStream(poifsFileSystem)) {
                opcPackage.save(outputStream);
            }

            try (FileOutputStream outputStream = new FileOutputStream(outputXlsxPath)) {
                poifsFileSystem.writeFilesystem(outputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
