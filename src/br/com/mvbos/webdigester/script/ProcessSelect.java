/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.script;

import br.com.mvbos.webdigester.core.Element;
import br.com.mvbos.webdigester.script.ProcessSelect.Select;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mbecker
 */
public class ProcessSelect implements IProcessor<List<Element>, Void> {

    private List<Element> elements;
    private Map<String, List<Element>> cache;
    private Select select;

    public void prepare(String cmd, List<Element> elements, Map<String, List<Element>> cache) {
        this.elements = elements;
        this.cache = cache;

        String[] arr = cmd.split(" ");
        //String s = arr.length > 0 ? arr[0] : null;//Select
        String f = arr.length > 1 ? arr[1] : null;//*
        String w = arr.length > 2 ? arr[2] : null;//where
        String fw = arr.length > 3 ? arr[3] : null;//foo
        String eq = arr.length > 4 ? arr[4] : null;//=
        String fv = arr.length > 5 ? arr[5] : null;//bar

        this.select = new Select(f, fw, fv);
    }

    @Override
    public List<Element> process(Void in) {
        //select a where href = '%.zip'
        List<Element> temp;
        if (select.getFields().equals("*")) {
            temp = elements;
        } else {
            temp = cache.get(select.getFields());
        }

        if (select.getRestrictionValue().isEmpty()) {
            return temp;
        } else {
            List<Element> filter = new ArrayList<Element>(temp.size());

            String restriction = select.getRestrictionValue().replaceAll("\'", "");
            int type = -1;

            if (restriction.startsWith("%")) {
                type = 0;

            } else if (restriction.endsWith("%")) {
                type = 1;
            }

            restriction = restriction.replaceAll("%", "");

            for (Element el : temp) {
                Map<String, String> params = el.getParams();

                if (params.containsKey(select.getRestrictionTag())) {
                    String value = params.get(select.getRestrictionTag());
                    if (type == 0 && value.endsWith(restriction)) {
                        filter.add(el);
                    } else if (type == 1 && value.startsWith(restriction)) {
                        filter.add(el);
                    } else if (value.contains(restriction)) {
                        filter.add(el);
                    }
                }
            }

            return filter;
        }

    }

    protected class Select {

        private String fields;
        private String restrictionTag;
        private String restrictionValue;

        public Select(String fields, String restrictionTag, String restrictionValue) {
            this.fields = fields;
            this.restrictionTag = restrictionTag;
            this.restrictionValue = restrictionValue;
        }

        public String getFields() {
            return fields;
        }

        public void setFields(String fields) {
            this.fields = fields;
        }

        public String getRestrictionTag() {
            return restrictionTag;
        }

        public void setRestrictionTag(String restrictionTag) {
            this.restrictionTag = restrictionTag;
        }

        public String getRestrictionValue() {
            return restrictionValue;
        }

        public void setRestrictionValue(String restrictionValue) {
            this.restrictionValue = restrictionValue;
        }
    }
}
