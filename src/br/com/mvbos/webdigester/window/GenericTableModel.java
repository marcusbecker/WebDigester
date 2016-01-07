/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.webdigester.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MarcusS
 * @param <T>
 */
public class GenericTableModel<T> extends AbstractTableModel {

    private final List<ColType> colTypes;
    private List<T> data = Collections.EMPTY_LIST;
    private final java.lang.reflect.Field[] fields;

    private Set<String> ignore = Collections.EMPTY_SET;

    private class ColType {

        String name;
        Class type;
        boolean edit;

        public ColType(String name, Class type, boolean edit) {
            this.name = name;
            this.type = type;
            this.edit = edit;
        }

    }

    public GenericTableModel(Class cType) {
        this(cType, Collections.EMPTY_SET);
    }

    public GenericTableModel(Class cType, Set<String> ignoreFields) {
        if(ignoreFields == null)
            throw new IllegalArgumentException("Ignore fields can not be null...");
        
        ignore = ignoreFields;
        
        fields = cType.getDeclaredFields();

        colTypes = new ArrayList<>(fields.length - ignore.size() + 1);
        colTypes.add(new ColType("#", Integer.class, false));

        for (java.lang.reflect.Field f : fields) {
            if (!ignore.contains(f.getName())) {
                colTypes.add(new ColType(f.getName(), f.getType(), true));
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return colTypes.get(columnIndex).edit;
    }

    @Override
    public Class getColumnClass(int c) {
        return colTypes.get(c).type;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {

        int classCol = col - 1;

        if (classCol < 0) {
            return;
        }

        T f = data.get(row);

        try {
            java.lang.reflect.Field ff = fields[classCol];
            ff.setAccessible(true);
            ff.set(f, value);

            fireTableCellUpdated(row, col);

        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(GenericTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String getColumnName(int column) {
        return colTypes.get(column).name;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return colTypes.size();
    }

    @Override
    public Object getValueAt(int row, int col) {

        int classCol = col - 1;

        if (classCol < 0) {
            return row + 1;
        }

        T f = data.get(row);

        try {
            java.lang.reflect.Field ff = fields[classCol];
            ff.setAccessible(true);
            return ff.get(f);

        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(GenericTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    private void addIgnoreField(String fields) {
        if (ignore == Collections.EMPTY_SET) {
            ignore = new HashSet<>(fields.length() - 1);
        }

        ignore.add(fields);
    }

}
