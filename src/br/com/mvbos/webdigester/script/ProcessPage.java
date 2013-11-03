/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.script;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author mbecker
 */
public class ProcessPage implements IProcessor<StringBuilder, String> {

    @Override
    public StringBuilder process(String adrs) {
        String address = adrs.toLowerCase();

        if (!address.startsWith("http")) {
            address = "http://" + address;
        }

        try {
            URL url = new URL(address);

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder(500);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            
            return sb;
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
