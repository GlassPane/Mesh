package io.github.glasspane.mesh.util.data.csv;

import java.io.Writer;
import java.util.List;

public class CSVBuilder {

    private String[] columns;
    private List<String> rows;

    private CSVBuilder(String[] columns) {
        //NO-OP
    }

    public static CSVBuilder create(String firstColumn, String... otherColumns) {
        String[] columns = new String[otherColumns.length + 1];
        System.arraycopy(otherColumns, 0, columns, 1, columns.length);
        columns[0] = firstColumn;
        return new CSVBuilder(columns);
    }

    public static CSVBuilder create(String[] columns) {
        return new CSVBuilder(columns);
    }

    /**
     * add a full row to the builder
     */
    public CSVBuilder put(String... content) {

    }

    public CSVBuilder.Row beginRow() {
        return new Row(this);
    }

    public void build(Writer output) {

    }

    public static class Row {

        private final CSVBuilder parent;

        private Row(CSVBuilder parent) {
            this.parent = parent;
        }

        public CSVBuilder endRow() {
            return this.parent;
        }
    }
}
