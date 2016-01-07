/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mbecker
 */
public class Element {

    private String name;
    private String param;
    private String body;
    private Map<String, String> params;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Map<String, String> getParams() {
        if (params == null && param != null) {
            String[] arr = param.trim().split(" ");
            params = new HashMap<String, String>(arr.length);

            for (String a : arr) {
                String[] keyValue = a.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().toLowerCase();
                    String value = keyValue[1].substring(1, keyValue[1].length() - 1);

                    params.put(key, value);
                }
            }
        }

        return params;
    }

    @Override
    public String toString() {
        return "Element{" + "name=" + name + ", param=" + param + ", body=" + body + '}';
    }

    public void showParams() {
        for (String s : getParams().keySet()) {
            System.out.println(s + " = " + getParams().get(s));
        }
    }
}
