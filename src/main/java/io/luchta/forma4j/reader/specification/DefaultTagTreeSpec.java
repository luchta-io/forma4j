package io.luchta.forma4j.reader.specification;

import io.luchta.forma4j.reader.model.tag.*;
import io.luchta.forma4j.reader.model.tag.property.Index;
import io.luchta.forma4j.reader.model.tag.property.Name;

import java.util.ArrayList;
import java.util.List;

public class DefaultTagTreeSpec {
    public TagTree create() {
        List<TagTree> listTagTreeList = new ArrayList<>();
        TagTree listTagTree = new TagTree(new Tags(), new ListTag(new Index(0), new Index(0), new Index(1), new Index(0), new Name("list")), new TagTrees());
        listTagTreeList.add(listTagTree);

        SheetTag.SheetTagBuilder builder = new SheetTag.SheetTagBuilder();
        builder.name(new Name("data"));
        SheetTag sheetTag = builder.build();

        List<TagTree> sheetTagTreeList = new ArrayList<>();
        TagTree sheetTagTree = new TagTree(new Tags(), sheetTag, new TagTrees(listTagTreeList));
        sheetTagTreeList.add(sheetTagTree);

        return new TagTree(new Tags(), new FormaReaderTag(), new TagTrees(sheetTagTreeList));
    }
}
