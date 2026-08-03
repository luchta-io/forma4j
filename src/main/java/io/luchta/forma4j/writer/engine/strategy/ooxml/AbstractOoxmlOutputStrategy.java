package io.luchta.forma4j.writer.engine.strategy.ooxml;

import io.luchta.forma4j.writer.engine.model.cell.XlsxCell;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxCellAddress;
import io.luchta.forma4j.writer.engine.model.cell.address.XlsxSheetName;
import io.luchta.forma4j.writer.engine.model.column.XlsxColumnAddress;
import io.luchta.forma4j.writer.engine.model.column.property.WidthProperty;
import io.luchta.forma4j.writer.engine.model.column.property.XlsxColumnProperty;
import io.luchta.forma4j.writer.engine.model.row.address.XlsxRowAddress;
import io.luchta.forma4j.writer.engine.model.row.property.AutoFilterProperty;
import io.luchta.forma4j.writer.engine.model.row.property.XlsxRowProperty;
import io.luchta.forma4j.writer.engine.strategy.XlsxOutputStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractOoxmlOutputStrategy implements XlsxOutputStrategy, AutoCloseable {
    protected final Map<String, SheetState> sheets = new LinkedHashMap<>();
    protected final OoxmlStyleRegistry styles = new OoxmlStyleRegistry();
    protected final Path temporaryDirectory;
    private long sequence;
    private boolean formulas;
    private boolean closed;

    protected AbstractOoxmlOutputStrategy() {
        try {
            temporaryDirectory = Files.createTempDirectory("forma4j-ooxml-");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public final void startSheet(XlsxSheetName sheetName, Boolean autoSizeColumnEnabled) {
        sheet(sheetName.toString());
        // OOXML の逐次出力では全セルを保持しないため autoSizeColumn は意図的に無効です。
    }

    @Override
    public final void finishSheet(XlsxSheetName sheetName) {
        // 最終パッケージ生成時にシートを確定します。
    }

    @Override
    public final void writeCell(XlsxCellAddress address, XlsxCell cell) {
        SheetState sheet = sheet(address.sheetName().toString());
        int row = address.rowNumber().toInt();
        int column = cell.columnNumber().toInt();
        int styleToken = styles.register(cell.style());
        CellCommand command = CellCommand.from(
                row,
                column,
                sequence++,
                cell,
                styleToken
        );
        if (command.type == CellCommand.ValueType.FORMULA) {
            formulas = true;
        }
        try {
            sheet.commands.append(command);
            sheet.appendedCell();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        sheet.includeInAutoFilter(row, column);
    }

    @Override
    public final void writeRowProperty(XlsxRowAddress address, XlsxRowProperty property) {
        if (property instanceof AutoFilterProperty
                && ((AutoFilterProperty) property).booleanValue()) {
            sheet(address.sheetName().toString()).markAutoFilter(address.rowNumber().toInt());
        }
    }

    @Override
    public final void writeColumnProperty(
            XlsxColumnAddress address,
            XlsxColumnProperty property
    ) {
        if (property instanceof WidthProperty) {
            sheet(address.sheetName().toString()).columnWidths.put(
                    address.columnNumber().toInt(),
                    ((WidthProperty) property).intValue()
            );
        }
    }

    @Override
    public final void write(OutputStream outputStream) throws IOException {
        try {
            writePackage(outputStream);
        } catch (CellCommandStore.CellCommandReadException e) {
            throw e.ioCause();
        } finally {
            close();
        }
    }

    protected abstract void writePackage(OutputStream outputStream) throws IOException;

    protected final boolean hasFormulas() {
        return formulas;
    }

    protected final void writeBlankOoxmlPackage(OutputStream outputStream) throws IOException {
        OoxmlStyleRegistry.PreparedStyles preparedStyles = styles.prepareBlank();
        new BlankOoxmlPackageWriter().write(
                outputStream,
                sheets,
                preparedStyles,
                formulas
        );
    }

    protected final void writeTemplateOoxmlPackage(
            Path template,
            OutputStream outputStream
    ) throws IOException {
        new TemplateOoxmlPackageWriter(template).write(
                outputStream,
                sheets,
                styles,
                formulas
        );
    }

    protected final SheetState sheet(String name) {
        SheetState existing = sheets.get(name);
        if (existing != null) {
            return existing;
        }
        try {
            SheetState created = new SheetState(name, temporaryDirectory, sheets.size());
            sheets.put(name, created);
            return created;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        IOException failure = null;
        for (SheetState sheet : sheets.values()) {
            try {
                sheet.commands.close();
            } catch (IOException e) {
                failure = append(failure, e);
            }
        }
        try {
            styles.close();
        } catch (IOException e) {
            failure = append(failure, e);
        }
        try (Stream<Path> paths = Files.walk(temporaryDirectory)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            failure = append(failure, e.getCause());
        } catch (IOException e) {
            failure = append(failure, e);
        }
        if (failure != null) {
            throw failure;
        }
    }

    private static IOException append(IOException current, IOException addition) {
        if (current == null) {
            return addition;
        }
        current.addSuppressed(addition);
        return current;
    }
}
