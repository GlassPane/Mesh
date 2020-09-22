package io.github.glasspane.mesh.util.serialization.csv;

import com.google.common.base.MoreObjects;
import io.github.glasspane.mesh.util.serialization.StringUtil;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CSVBuilder {

    private final String[] columns;
    private final List<String[]> rows = new LinkedList<>();

    private CSVBuilder(String[] columns) {
        if (columns.length == 0) {
            throw new IllegalArgumentException("no columns provided");
        }
        this.columns = Arrays.stream(columns).map(StringUtil::quoteCSV).toArray(String[]::new);
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
        if (content.length != columns.length) {
            throw new IllegalArgumentException(String.format("expected %s columns but got %s", columns.length, content.length));
        }
        rows.add(Arrays.stream(content).map(StringUtil::quoteCSV).toArray(String[]::new));
        return this;
    }

    public CSVBuilder put(Object... content) {
        return put(Arrays.stream(content).map(String::valueOf).toArray(String[]::new));
    }

    public CSVBuilder.Row beginRow() {
        return new Row(this, this.columns.length);
    }

    public void build(Writer output) {
        PrintWriter writer = output instanceof PrintWriter ? (PrintWriter) output : new PrintWriter(output);
        writer.println(String.join(",", columns));
        rows.stream().map(it -> String.join(",", it)).forEachOrdered(writer::println);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("columns", columns.length)
                .add("rows", rows.size())
                .toString();
    }

    public static class Row {

        private final CSVBuilder parent;
        private final int maxColumns;
        private final List<String> rowData = new LinkedList<>();

        private Row(CSVBuilder parent, int maxColumns) {
            this.parent = parent;
            this.maxColumns = maxColumns;
        }

        public Row put(Object content) {
            return put(String.valueOf(content));
        }

        public Row put(String content) {
            if (rowData.size() + 1 > maxColumns) {
                throw new IllegalArgumentException("row exceeded expected length: " + maxColumns);
            }
            rowData.add(content);
            return this;
        }

        public Row put(String[] content) {
            if (content.length == 0) {
                throw new IllegalArgumentException("empty column array provided");
            }
            if (rowData.size() + content.length > maxColumns) {
                throw new IllegalArgumentException("row exceeded expected length: " + maxColumns);
            }
            return this;
        }

        public Row put(String first, String... rest) {
            return put(first).put(rest);
        }

        public CSVBuilder end() {
            return this.parent.put(this.rowData.toArray(new String[0]));
        }
    }
}
