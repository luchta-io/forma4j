package io.luchta.forma4j.writer.engine.strategy.ooxml;

final class CellReferences {
    private CellReferences() {
    }

    static String cell(int row, int column) {
        return column(column) + (row + 1);
    }

    static String column(int column) {
        if (column < 0) {
            throw new IllegalArgumentException("列番号は0以上である必要があります: " + column);
        }
        StringBuilder result = new StringBuilder();
        int value = column + 1;
        while (value > 0) {
            int remainder = (value - 1) % 26;
            result.append((char) ('A' + remainder));
            value = (value - 1) / 26;
        }
        return result.reverse().toString();
    }

    static int columnIndex(String reference) {
        int value = 0;
        int index = 0;
        while (index < reference.length()) {
            char current = reference.charAt(index);
            if (current >= 'a' && current <= 'z') {
                current = (char) (current - ('a' - 'A'));
            }
            if (current < 'A' || current > 'Z') {
                break;
            }
            value = value * 26 + (current - 'A' + 1);
            index++;
        }
        return value - 1;
    }
}
