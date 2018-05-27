package com.water.ce.utils;

import com.google.common.base.Joiner;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mrwater on 2018/5/27.
 */
public class Markdown2Html {

    public static String markdown2html(String markdownContent) {
        InputStream stream = new ByteArrayInputStream(markdownContent.getBytes());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<String> list = reader.lines().collect(Collectors.toList());
        String content = Joiner.on("\n").join(list);

        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[] { TablesExtension.create()}));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }
}
