/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbecker
 */
public class Core {

    private static final String SPACE = " ";
    private static final String TAG_CLOSE = ">";
    private static final String TAG_END_CLOSE = "/>";
    private static final String TAG_OPEN = "<";
    private static final String TAG_OPEN_CLOSE = "</";
    public static boolean ignoreComments = true;
    private static Set<String> ignore = new HashSet<String>(0);
    private static final Logger log = Logger.getLogger(Core.class.getName());

    public static void config(String path) {
        Scanner sc = null;

        if (path == null) {
            try {
                sc = new Scanner(new File("config.txt"));
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }

        if (sc == null) {
            return;
        }

        ignore = new HashSet<String>(5);

        while (sc.hasNext()) {
            String ln = sc.nextLine();
            String[] arr = ln.split("=");

            if (arr.length == 2) {
                if (arr[0].equalsIgnoreCase("ignore")) {
                    ignore.add(arr[1]);
                }
            }
        }
    }

    /**
     * Parse all html and separate tags
     *
     * @param content html to parser
     */
    public static List<Element> digester(String content) {
        //Map<String, List<Element>> values = new HashMap<String, List<Element>>();
        Element el;
        List<Element> lst = new ArrayList<Element>(100);
        StringBuilder sb = new StringBuilder(content);

        try {
            int start = sb.indexOf(TAG_OPEN);
            while (start > -1) {
                if (sb.substring(start, start + TAG_OPEN_CLOSE.length()).equals(TAG_OPEN_CLOSE)) {
                    start = sb.indexOf(TAG_OPEN, start + 1);
                    continue;
                }

                int end = 0;
                int close = sb.indexOf(TAG_CLOSE, start + 1);

                String body = null;
                String paramBody = sb.substring(start + TAG_OPEN.length(), close);
                paramBody = paramBody.trim();
                boolean isClosed = paramBody.endsWith("/");

                String paramName;

                if (paramBody.indexOf(SPACE) > 0) {
                    paramName = paramBody.substring(0, paramBody.indexOf(SPACE));

                } else if (paramBody.indexOf(System.lineSeparator()) > 0) {
                    paramName = paramBody.substring(0, paramBody.indexOf(System.lineSeparator()));

                } else {
                    paramName = paramBody;
                }

                paramBody = paramBody.substring(paramName.length());

                if (isClosed) {
                    end = 0;//sb.indexOf(TAG_END_CLOSE, start);
                } else {
                    if (sb.indexOf(TAG_OPEN_CLOSE + paramName) > 0) {
                        //System.out.println("end : " + TAG_OPEN_CLOSE + paramName);
                        end = sb.indexOf(TAG_OPEN_CLOSE + paramName);
                    }
                }

                if (end > start) {
                    body = sb.substring(close + TAG_CLOSE.length(), end);
                }

                start = sb.indexOf(TAG_OPEN, start + 1);

                if (ignore.contains(paramName)) {
                    System.out.println("ignored: " + paramName);
                    System.out.println();
                    continue;
                }

                //System.out.println("paramName " + paramName);
                //System.out.println("paramBody " + paramBody);
                //System.out.println("selfClosed " + isClosed);
                //System.out.println("body:\n" + (body != null ? body : "No body"));
                //System.out.println();

                el = new Element();
                el.setBody(body);
                el.setName(paramName);
                el.setParam(paramBody);

                //el.showParams();
                //System.out.println(el);

                lst.add(el);

            }

        } catch (Exception e) {
        }

        return lst;
    }

    /**
     * Parse html separated tags in blocks
     *
     * @param content html to parser
     */
    public static void smartDigester(String content) {
        //Map<String, List<Element>> values = new HashMap<String, List<Element>>();
        StringBuilder sb = new StringBuilder(content);
        StringBuilder temp = new StringBuilder(100);

        int start = sb.indexOf(TAG_OPEN);
        while (start > -1) {
            int end = 0;
            int close = sb.indexOf(TAG_CLOSE, start + 1);// + ">".length();

            boolean isClosed = sb.substring(close - 1, close).equals(TAG_END_CLOSE);
            String param = sb.substring(start + TAG_OPEN.length(), close);
            param = param.trim();
            String paramName = param.indexOf(SPACE) > 0 ? param.substring(0, param.indexOf(SPACE)) : param;

            System.out.println("param " + param);
            System.out.println("isClosed " + isClosed);
            System.out.println("paramName " + paramName);

            if (isClosed) {
                end = sb.indexOf(TAG_END_CLOSE);
            } else {
                if (sb.indexOf(TAG_OPEN_CLOSE + paramName) > 0) {
                    System.out.println("end : " + TAG_OPEN_CLOSE + paramName);
                    end = sb.indexOf(TAG_OPEN_CLOSE + paramName);
                }
            }

            if (end > start) {
                System.out.println("body:\n" + sb.substring(close + TAG_CLOSE.length(), end));
            } else {
                System.out.println("no body");
            }
            start = sb.indexOf(TAG_OPEN, start + 1);
        }
    }
}
