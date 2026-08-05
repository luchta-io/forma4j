package io.luchta.forma4j.writer.engine.strategy.ooxml;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

final class SheetState {
    final String name;
    final CellCommandStore commands;
    final Map<Integer, Integer> columnWidths = new TreeMap<>();
    private AutoFilterRange autoFilter;
    private long cellCount;

    SheetState(String name, Path directory, int number) throws IOException {
        this.name = name;
        commands = new CellCommandStore(directory, number);
    }

    void markAutoFilter(int row) {
        autoFilter = new AutoFilterRange(row);
    }

    void appendedCell() {
        cellCount++;
    }

    boolean hasChanges() {
        return cellCount > 0 || !columnWidths.isEmpty() || autoFilter != null;
    }

    void includeInAutoFilter(int row, int column) {
        if (autoFilter != null && autoFilter.row == row) {
            autoFilter.include(column);
        }
    }

    String autoFilterReference() {
        if (autoFilter == null || autoFilter.firstColumn == null) {
            return null;
        }
        return CellReferences.cell(autoFilter.row, autoFilter.firstColumn)
                + ":"
                + CellReferences.cell(autoFilter.row, autoFilter.lastColumn);
    }

    private static final class AutoFilterRange {
        private final int row;
        private Integer firstColumn;
        private Integer lastColumn;

        private AutoFilterRange(int row) {
            this.row = row;
        }

        private void include(int column) {
            if (firstColumn == null || column < firstColumn) {
                firstColumn = column;
            }
            if (lastColumn == null || column > lastColumn) {
                lastColumn = column;
            }
        }
    }
}
