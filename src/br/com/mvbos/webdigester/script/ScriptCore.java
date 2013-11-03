/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.script;

import br.com.mvbos.webdigester.core.Core;
import br.com.mvbos.webdigester.core.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author mbecker
 */
public class ScriptCore {

    private static IProcessor pPage = new ProcessPage();
    private static IProcessor pSelect = new ProcessSelect();

    public static List<Element> process(final String script) {
        new Thread() {
            public void run() {
                Scanner sc = new Scanner(script);

                List<Element> elements = Collections.EMPTY_LIST;
                Map<String, List<Element>> cache = null;

                while (sc.hasNext()) {
                    String ln = sc.nextLine();

                    if (ln.startsWith("page")) {
                        String cmd = getCommand("page", ln);
                        StringBuilder c = ((ProcessPage) pPage).process(cmd);
                        elements = Core.digester(c.toString());
                        cache = index(elements, cache);

                    } else if (ln.startsWith("select")) {
                        //select a where href = '%.zip'
                        ProcessSelect ps = (ProcessSelect) pSelect;
                        ps.prepare(ln, elements, cache);
                        List<Element> rs = ps.process(null);

                        Set<String> rsDistinct = new HashSet<String>(rs.size());
                        for (Element e : rs) {
                            rsDistinct.add(e.getParams().get("href"));
                        }

                        for (String s : rsDistinct) {
                            System.out.println(s);
                        }
                    }
                }

                
            }
        }.start();
        
        return null;

    }

    private static String getCommand(String action, String ln) {
        return ln.substring(action.length()).trim();
    }

    private static Map<String, List<Element>> index(List<Element> elements, Map<String, List<Element>> cache) {
        if (cache == null) {
            cache = new HashMap<String, List<Element>>(20);
        }

        for (Element el : elements) {
            String k = el.getName();

            if (!cache.containsKey(k)) {
                cache.put(k, new ArrayList<Element>(10));
            }

            cache.get(k).add(el);
        }

        return cache;
    }
}
