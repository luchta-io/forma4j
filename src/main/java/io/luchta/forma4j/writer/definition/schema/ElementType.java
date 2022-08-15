package io.luchta.forma4j.writer.definition.schema;

import java.util.EnumSet;
import java.util.Set;

public enum ElementType {
    SHEET,
    ROW,
    COLUMN,
    CELL,
    VERTICAL_FOR,
    HORIZONTAL_FOR,
    LIST;

    public static Set<ElementType> allowRootChildren = EnumSet.of(SHEET);
    public static Set<ElementType> allowSheetChildren = EnumSet.of(ROW, COLUMN, CELL, VERTICAL_FOR, HORIZONTAL_FOR, LIST);
    public static Set<ElementType> allowRowChildren = EnumSet.of(CELL, VERTICAL_FOR);
    public static Set<ElementType> allowColumnChildren = EnumSet.of(CELL, HORIZONTAL_FOR);
    public static Set<ElementType> allowVerticalForChildren = EnumSet.of(ROW, COLUMN, CELL, VERTICAL_FOR, HORIZONTAL_FOR);
    public static Set<ElementType> allowHorizontalForChildren = EnumSet.of(ROW, COLUMN, CELL, VERTICAL_FOR, HORIZONTAL_FOR);
}
