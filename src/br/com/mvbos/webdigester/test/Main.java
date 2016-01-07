/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.test;

import br.com.mvbos.webdigester.core.Core;
import br.com.mvbos.webdigester.core.Element;
import br.com.mvbos.webdigester.core.FileUtil;
import java.util.List;

/**
 *
 * @author mbecker
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuilder c = FileUtil.load("teste.html");

        Core.config(null);

        List<Element> lst = Core.digester(c);
        System.out.println(" total: " + lst.size());
        for (Element e : lst) {
            System.out.println(e.getName());
        }
    }
}
