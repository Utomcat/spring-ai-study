package com.ranyk.spring.ai.demo.common.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CLASS_NAME: FileTypeEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件类型常量枚举类
 * @date: 2026-06-25
 */
@Getter
@SuppressWarnings("unused")
public enum FileTypeEnum {
    /**
     * 文本文件类型
     */
    TEXT("文本文件", List.of("txt", "text")),
    /**
     * Word文件类型
     */
    WORD("Word文件", List.of("doc", "docx")),
    /**
     * Excel文件类型
     */
    EXCEL("Excel文件", List.of("xls", "xlsx")),
    /**
     * PowerPoint文件类型
     */
    PPT("PowerPoint文件", List.of("ppt", "pptx")),
    /**
     * PDF文件类型
     */
    PDF("PDF文件", List.of("pdf", "PDF")),
    /**
     * Markdown文件类型
     */
    MARKDOWN("Markdown文件", List.of("md", "markdown")),
    /**
     * HTML文件类型
     */
    HTML("HTML文件", List.of("html", "htm")),
    /**
     * JSON文件类型
     */
    JSON("JSON文件", List.of("json", "JSON")),
    /**
     * YAML文件类型
     */
    YAML("YAML文件", List.of("yaml", "yml")),
    /**
     * 未知文件类型
     */
    UNKNOWN("未知文件类型", List.of("UNKNOWN"));

    /**
     * 文件类型描述
     */
    private final String desc;
    /**
     * 文件类型后缀
     */
    private final List<String> suffix;

    /**
     * 构造方法 - 文件类型枚举对象
     *
     * @param desc   文件类型描述
     * @param suffix 文件类型后缀
     */
    FileTypeEnum(String desc, List<String> suffix) {
        this.desc = desc;
        this.suffix = suffix;
    }

    /**
     * 文件类型序数与枚举对象的映射关系 - 如果存在文件类型新增,则需要对此 Map 进行维护
     */
    private static final Map<Integer, FileTypeEnum> ORDINAL_MAP = Map.of(
            0, TEXT,
            1, WORD,
            2, EXCEL,
            3, PPT,
            4, PDF,
            5, MARKDOWN,
            6, HTML,
            7, JSON,
            8, YAML,
            9, UNKNOWN
    );

    /**
     * 通过传入的 ordinal (文件类型序数) 获取所有的枚举对象的 suffix  List 集合, 当不传时, 直接返回 {@link FileTypeEnum#UNKNOWN} 值
     *
     * @param ordinals 文件类型序数
     * @return 文件类型后缀 List 集合, 当不传时, 直接返回 {@link FileTypeEnum#UNKNOWN} 值
     */
    public static List<String> getSuffixListByOrdinal(Integer... ordinals) {
        // 如果 ordinals 为 null 或者长度为 0,则返回所有文件类型的后缀 List 集合
        if (Objects.isNull(ordinals) || ordinals.length == 0) {
            return UNKNOWN.getSuffix();
        }
        // 通过 ordinals 获取对应的文件类型枚举对象的后缀 List 集合
        return Arrays.stream(ordinals)
                .map(ORDINAL_MAP::get)
                .filter(Objects::nonNull)
                .flatMap(type -> type.getSuffix().stream())
                .distinct()
                .toList();
    }

    /**
     * 通过 文件类型序数 列表获取后缀列表（支持 SequencedCollection，JDK 21）
     *
     * @param ordinals 文件类型序数 List 列表
     * @return 去重后的后缀列表
     */
    public static List<String> getSuffixListById(List<Integer> ordinals) {
        if (Objects.isNull(ordinals) || ordinals.isEmpty()) {
            return UNKNOWN.getSuffix();
        }
        return ordinals.stream()
                .map(ORDINAL_MAP::get)
                .filter(Objects::nonNull)
                .flatMap(type -> type.getSuffix().stream())
                .distinct()
                .toList();
    }

    /**
     * 获取指定文件类型的后缀列表
     *
     * @param fileTypes 文件类型枚举
     * @return 去重后的后缀列表
     */
    public static List<String> getSuffixList(FileTypeEnum... fileTypes) {
        if (fileTypes == null || fileTypes.length == 0) {
            return UNKNOWN.getSuffix();
        }

        return Arrays.stream(fileTypes)
                .flatMap(type -> type.getSuffix().stream())
                .distinct()
                .toList();
    }

    /**
     * 获取所有文件类型的后缀列表
     *
     * @return 所有后缀（去重）
     */
    public static List<String> getAllSuffixes() {
        return Arrays.stream(values())
                .flatMap(type -> type.getSuffix().stream())
                .distinct()
                .toList();
    }

    /**
     * 通过后缀查找对应的文件类型（JDK 21 增强版）
     *
     * @param suffix 文件后缀（不区分大小写）
     * @return 匹配的文件类型, 未找到返回 {@link FileTypeEnum#UNKNOWN}
     */
    public static FileTypeEnum findBySuffix(String suffix) {
        if (Objects.isNull(suffix) || suffix.isBlank()) {
            return UNKNOWN;
        }

        return Arrays.stream(values())
                .filter(type -> type.getSuffix().stream()
                        .anyMatch(s -> s.equalsIgnoreCase(suffix)))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
