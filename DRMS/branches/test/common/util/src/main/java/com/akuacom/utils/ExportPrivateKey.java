/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.ExportPrivateKey.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;

import sun.misc.BASE64Encoder;

/**
 * The Class ExportPrivateKey.
 */
public class ExportPrivateKey {
    
    /** The keystore file. */
    private File keystoreFile;
    
    /** The key store type. */
    private String keyStoreType;
    
    /** The password. */
    private char[] password;
    
    /** The alias. */
    private String alias;
    
    /** The exported file. */
    private File exportedFile;

    /**
     * Gets the private key.
     * 
     * @param keystore the keystore
     * @param alias the alias
     * @param password the password
     * 
     * @return the private key
     */
    public static KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) {
        try {
            Key key = keystore.getKey(alias, password);
            if (key instanceof PrivateKey) {
                Certificate cert = keystore.getCertificate(alias);
                PublicKey publicKey = cert.getPublicKey();
                return new KeyPair(publicKey, (PrivateKey) key);
            }
        } catch (UnrecoverableKeyException e) {//
        } catch (NoSuchAlgorithmException e) {//
        } catch (KeyStoreException e) {//
        }
        return null;
    }

    /**
     * Export.
     * 
     * @throws Exception the exception
     */
    public void export() throws Exception {
        KeyStore keystore = KeyStore.getInstance(keyStoreType);
        BASE64Encoder encoder = new BASE64Encoder();
        keystore.load(new FileInputStream(keystoreFile), password);
        KeyPair keyPair = getPrivateKey(keystore, alias, password);
        PrivateKey privateKey = keyPair.getPrivate();
        String encoded = encoder.encode(privateKey.getEncoded());
        FileWriter fw = new FileWriter(exportedFile);
        fw.write("-----BEGIN PRIVATE KEY-----\n");
        fw.write(encoded);
        fw.write("\n");
        fw.write("-----END PRIVATE KEY-----");
        fw.close();
    }

    /**
     * command line parameters: {keystore_path} JKS {keystore_password} {alias} {target_file}.
     * 
     * @param args the args
     * 
     * @throws Exception the exception
     */
    public static void main(String args[]) throws Exception {
        ExportPrivateKey export = new ExportPrivateKey();
        export.keystoreFile = new File(args[0]);
        export.keyStoreType = args[1];
        export.password = args[2].toCharArray();
        export.alias = args[3];
        export.exportedFile = new File(args[4]);
        export.export();
    }
}