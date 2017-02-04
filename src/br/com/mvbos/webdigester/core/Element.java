/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.core;

import java.util.Collections;
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
            params = Core.parseStringAttributesToMap(param);
        }

        if (params == null) {
            params = Collections.emptyMap();
        }

        return params;
    }

    @Override
    public String toString() {
        return "Element{" + "name=" + name + ", param='" + param + "', body=" + body + '}';
    }

    public void showParams() {
        for (String s : getParams().keySet()) {
            System.out.println(s + " = " + getParams().get(s));
        }
    }
}
