/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcus Becker
 */
public class FileUtil {

    public static StringBuilder load(String fileName) {
        StringBuilder sb = new StringBuilder(500);
        File f = new File(fileName);
        
        if (f.exists() && f.isFile()) {
            InputStreamReader is;
            BufferedReader reader = null;

            try {
                is = new InputStreamReader(new FileInputStream(f), Charset.forName("iso-8859-1"));
                reader = new BufferedReader(is);

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(System.getProperty("line.separator"));
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);

            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ex) {
                }
            }

        }

        return sb;
    }

}
