package io.luchta.forma4j.writer.engine.strategy.ooxml;

import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

final class CellCommand implements Comparable<CellCommand> {
    enum ValueType {
        TEXT,
        BOOLEAN,
        NUMERIC,
        DATE,
        DATETIME,
        FORMULA
    }

    final int row;
    final int column;
    final long sequence;
    final ValueType type;
    final int styleToken;
    final boolean emptyStyle;
    final String value;

    CellCommand(
            int row,
            int column,
            long sequence,
            ValueType type,
            int styleToken,
            boolean emptyStyle,
            String value
    ) {
        this.row = row;
        this.column = column;
        this.sequence = sequence;
        this.type = type;
        this.styleToken = styleToken;
        this.emptyStyle = emptyStyle;
        this.value = value;
    }

    static CellCommand from(
            int row,
            int column,
            long sequence,
            XlsxCell cell,
            int styleToken
    ) {
        boolean emptyStyle = cell.style() == null || cell.style().isEmpty();
        try {
            if (cell.isFormula()) {
                return new CellCommand(
                        row, column, sequence, ValueType.FORMULA, styleToken, emptyStyle,
                        cell.toFormula().substring(1)
                );
            }
            if (cell.isBoolean()) {
                return new CellCommand(
                        row, column, sequence, ValueType.BOOLEAN, styleToken, emptyStyle,
                        cell.toBoolean() ? "1" : "0"
                );
            }
            if (cell.isDate()) {
                return new CellCommand(
                        row, column, sequence, ValueType.DATE, styleToken, emptyStyle,
                        cell.toDate().toString()
                );
            }
            if (cell.isDateTime()) {
                return new CellCommand(
                        row, column, sequence, ValueType.DATETIME, styleToken, emptyStyle,
                        cell.toDateTime().toString()
                );
            }
            if (cell.isNumeric()) {
                return new CellCommand(
                        row, column, sequence, ValueType.NUMERIC, styleToken, emptyStyle,
                        Double.toString(cell.toNumeric())
                );
            }
            return new CellCommand(
                    row, column, sequence, ValueType.TEXT, styleToken, emptyStyle,
                    cell.isEmpty() ? "" : cell.toText()
            );
        } catch (Exception ignored) {
            return new CellCommand(
                    row, column, sequence, ValueType.TEXT, styleToken, emptyStyle,
                    cell.isEmpty() ? "" : cell.toText()
            );
        }
    }

    void write(DataOutput output) throws IOException {
        output.writeInt(row);
        output.writeInt(column);
        output.writeLong(sequence);
        output.writeByte(type.ordinal());
        output.writeInt(styleToken);
        output.writeBoolean(emptyStyle);
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        output.writeInt(bytes.length);
        output.write(bytes);
    }

    static CellCommand read(DataInput input) throws IOException {
        final int row;
        try {
            row = input.readInt();
        } catch (EOFException e) {
            return null;
        }
        int column = input.readInt();
        long sequence = input.readLong();
        int type = input.readUnsignedByte();
        int styleToken = input.readInt();
        boolean emptyStyle = input.readBoolean();
        int length = input.readInt();
        if (length < 0) {
            throw new IOException("セル命令の文字列長が不正です: " + length);
        }
        byte[] bytes = new byte[length];
        input.readFully(bytes);
        return new CellCommand(
                row,
                column,
                sequence,
                ValueType.values()[type],
                styleToken,
                emptyStyle,
                new String(bytes, StandardCharsets.UTF_8)
        );
    }

    boolean sameAddress(CellCommand other) {
        return row == other.row && column == other.column;
    }

    @Override
    public int compareTo(CellCommand other) {
        int compared = Integer.compare(row, other.row);
        if (compared != 0) {
            return compared;
        }
        compared = Integer.compare(column, other.column);
        if (compared != 0) {
            return compared;
        }
        return Long.compare(sequence, other.sequence);
    }
}
