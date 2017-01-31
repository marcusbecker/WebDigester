/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private static Set<String> ignore = new HashSet<>(0);
    private static final Logger log = Logger.getLogger(Core.class.getName());

    public static void config(String path) {
        Scanner sc = null;

        if (path == null) {
            try {
                File f = new File("config.txt");

                if (f.exists()) {
                    sc = new Scanner(f);
                }
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }

        if (sc == null) {
            return;
        }

        ignore = new HashSet<>(5);

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
    public static List<Element> digester(StringBuilder content) {
        // Map<String, List<Element>> values = new HashMap<String,
        // List<Element>>();
        Element el;
        List<Element> lst = new ArrayList<>(300);

        try {
            int start = content.indexOf(TAG_OPEN);
            int end;
            int close;

            String body;
            String paramBody;
            boolean isClosed;

            String paramName;

            while (start > -1) {
                if (content.substring(start, start + TAG_OPEN_CLOSE.length()).equals(TAG_OPEN_CLOSE)) {
                    start = content.indexOf(TAG_OPEN, start + 1);
                    continue;
                }

                end = 0;
                close = content.indexOf(TAG_CLOSE, start + 1);

                body = null;
                paramBody = content.substring(start + TAG_OPEN.length(), close).trim();
                isClosed = paramBody.endsWith("/");

                if (paramBody.indexOf(SPACE) > 0) {
                    paramName = paramBody.substring(0, paramBody.indexOf(SPACE));

                } else if (paramBody.indexOf(System.lineSeparator()) > 0) {
                    paramName = paramBody.substring(0, paramBody.indexOf(System.lineSeparator()));

                } else {
                    paramName = paramBody;
                }

                paramBody = paramBody.substring(paramName.length());

                if (isClosed) {
                    end = 0;// sb.indexOf(TAG_END_CLOSE, start);
                } else {
                    if (content.indexOf(TAG_OPEN_CLOSE + paramName) > 0) {
                        // System.out.println("end : " + TAG_OPEN_CLOSE +
                        // paramName);
                        end = content.indexOf(TAG_OPEN_CLOSE + paramName);
                    }
                }

                if (end > start) {
                    body = content.substring(close + TAG_CLOSE.length(), end);
                }

                start = content.indexOf(TAG_OPEN, start + 1);

                if (ignore.contains(paramName)) {
                    // System.out.println("ignored: " + paramName);
                    // System.out.println();
                    continue;
                }

                // System.out.println("paramName " + paramName);
                // System.out.println("paramBody " + paramBody);
                // System.out.println("selfClosed " + isClosed);
                // System.out.println("body:\n" + (body != null ? body :
                // "No body"));
                // System.out.println();
                el = new Element();
                el.setBody(body);
                el.setName(paramName);
                el.setParam(paramBody);

                // el.showParams();
                // System.out.println(el);
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
    public static void smartDigester(StringBuilder content) {
        // Map<String, List<Element>> values = new HashMap<String,
        // List<Element>>();
        //StringBuilder temp = new StringBuilder(100);

        int start = content.indexOf(TAG_OPEN);
        while (start > -1) {
            int end = 0;
            int close = content.indexOf(TAG_CLOSE, start + 1);// + ">".length();

            boolean isClosed = content.substring(close - 1, close).equals(TAG_END_CLOSE);
            String param = content.substring(start + TAG_OPEN.length(), close);
            param = param.trim();
            String paramName = param.indexOf(SPACE) > 0 ? param.substring(0, param.indexOf(SPACE)) : param;

            System.out.println("param " + param);
            System.out.println("isClosed " + isClosed);
            System.out.println("paramName " + paramName);

            if (isClosed) {
                end = content.indexOf(TAG_END_CLOSE);
            } else {
                if (content.indexOf(TAG_OPEN_CLOSE + paramName) > 0) {
                    System.out.println("end : " + TAG_OPEN_CLOSE + paramName);
                    end = content.indexOf(TAG_OPEN_CLOSE + paramName);
                }
            }

            if (end > start) {
                System.out.println("body:\n" + content.substring(close + TAG_CLOSE.length(), end));
            } else {
                System.out.println("no body");
            }
            start = content.indexOf(TAG_OPEN, start + 1);
        }
    }

    public synchronized static Map<String, String> parseStringAttributesToMap(final String param) {
        String tag = null;
        String content = null;
        char delimiterIni = '\0';
        char[] arr = param.toCharArray();
        final StringBuilder temp = new StringBuilder(20);

        Map<String, String> params = null;

        for (int i = 0; i < arr.length; i++) {
            if (tag == null) {
                if ('=' == arr[i]) {
                    tag = temp.toString();
                    temp.delete(0, temp.length());

                } else {
                    temp.append(arr[i]);
                }

            } else if ('\0' == delimiterIni) {
                if (arr[i] != ' ') {
                    // expected ' or "
                    delimiterIni = arr[i];
                }

            } else if (content == null) {
                // ignore escape characters \' or \"
                if (delimiterIni == arr[i] && arr[i - 1] != '\\') {
                    content = temp.toString();
                    temp.delete(0, temp.length());

                } else {
                    temp.append(arr[i]);
                }

            } else {

                if (params == null) {
                    params = new HashMap<>(5);
                }

                params.put(tag.trim().toLowerCase(), content.trim());

                tag = null;
                content = null;
                delimiterIni = '\0';
                temp.delete(0, temp.length());
            }
        }

        //try to add invalid tag
        if (tag != null && temp.length() > 0) {
            if (params == null) {
                params = new HashMap<>(1);
            }

            params.put(tag.trim().toLowerCase(), content.trim());
        }

        return params;
    }
}
