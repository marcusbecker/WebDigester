/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.core;

import br.com.mvbos.IOWorkUtil.IOFile;

/**
 *
 * @author mbecker
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void _main(String[] args) {
        IOFile f = new IOFile();
        String c = f.load("teste.html");
        
        Core.config(null);
        
        Core.digester(c);
    }
}
