package top.aoyudi.rag.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import top.aoyudi.rag.properties.Document;
import top.aoyudi.rag.DocumentLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 文件系统文档加载器，从本地文件系统加载文档
 */
@Log4j2
public class FileSystemDocumentLoader implements DocumentLoader {

    private static final Set<String> SUPPORTED_EXTENSIONS = new HashSet<>(Arrays.asList(".txt", ".md", ".docx", ".pdf",".xlsx"));

    @Override
    public Document loadDocument(String source) {
        Path filePath = Paths.get(source);
        validateFile(filePath);

        try {
            String content = readFileContent(filePath);
            Map<String, Object> metadata = extractMetadata(filePath);
            return Document.of(content, metadata);
        } catch (IOException e) {
            log.error("Failed to load document from {}", source, e);
            throw new RuntimeException("Error loading document: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Document> loadDocuments(List<String> sources) {
        return sources.stream()
                .map(this::loadDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> loadDocumentsFromPath(String path) throws IOException {
        // 判断是否为目录
        boolean directory = Files.isDirectory(Paths.get(path));
        if (!directory) {
            throw new IllegalArgumentException("Path is not a directory: " + path);
        }
        List<String> files = this.findFiles(path);
        return this.loadDocuments(files);
    }

    /**
     * 查找指定目录下所有符合条件的文件（包括子目录）
     * @param rootPath 根目录路径
     * @return 符合条件的文件列表
     * @throws IOException 如果发生I/O错误
     */
    private List<String> findFiles(String rootPath) throws IOException {
        List<String> resultPaths = new ArrayList<>();

        Path rootDir = Paths.get(rootPath);

        // 使用Files.walkFileTree遍历目录
        Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                // 检查文件扩展名是否符合条件
                String fileName = file.getFileName().toString().toLowerCase();
                for (String ext : SUPPORTED_EXTENSIONS) {
                    if (fileName.endsWith(ext)) {
                        // 获取文件的绝对路径并添加到列表
                        resultPaths.add(file.toAbsolutePath().toString());
                        break;
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println("无法访问文件: " + file + "，错误: " + exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });

        return resultPaths;
    }

    /**
     * 验证文件是否存在且支持
     */
    private void validateFile(Path filePath) {
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }

        if (!Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException("Not a regular file: " + filePath);
        }

        String fileName = filePath.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("Unsupported file type: " + fileName);
        }

        String extension = fileName.substring(lastDotIndex).toLowerCase();
        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file extension: " + extension + ", supported types: " + SUPPORTED_EXTENSIONS);
        }
    }

    /**
     * 读取文件内容
     */
    private String readFileContent(Path filePath) throws IOException {
        String extension = getFileExtension(filePath);

        switch (extension) {
            case ".txt":
                return readTextFile(filePath);
            case ".md":
                return readMarkdownFile(filePath);
            case ".docx":
                return readDocxContent(filePath);
            case ".pdf":
                return readPdfContent(filePath);
            case ".xlsx":
                return readExcelContent(filePath);
            default:
                throw new IllegalArgumentException("Unsupported file extension: " + extension);
        }
    }

    /**
     * 提取文件元数据
     */
    private Map<String, Object> extractMetadata(Path filePath) throws IOException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", filePath.toString());
        metadata.put("filename", filePath.getFileName().toString());
        metadata.put("size", Files.size(filePath));
        metadata.put("lastModified", new Date(Files.getLastModifiedTime(filePath).toMillis()));
        metadata.put("extension", getFileExtension(filePath));
        return metadata;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex).toLowerCase();
    }

    /**
     * 读取文本文件内容，支持指定编码
     */
    private String readTextFile(Path filePath) throws IOException {
        try {
            return Files.readString(filePath, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("Failed to read file with UTF-8 encoding, trying default system encoding: {}", filePath);
            // 尝试使用系统默认编码
            return Files.readString(filePath);
        }
    }

    /**
     * 读取Markdown文件内容，进行基本处理
     */
    private String readMarkdownFile(Path filePath) throws IOException {
        Parser parser = Parser.builder().build();
        Node document = parser.parseReader(
                new InputStreamReader(Files.newInputStream(Paths.get(filePath.toAbsolutePath().toString())), StandardCharsets.UTF_8)
        );
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 使用Apache POI读取DOCX文件内容
     */
    private String readDocxContent(Path filePath) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(Files.newInputStream(filePath))) {
            StringBuilder content = new StringBuilder();

            // 提取段落内容
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                content.append(paragraph.getText()).append("\n");
            }

            // 提取表格内容
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        content.append("| ").append(cell.getText()).append(" ");
                    }
                    content.append("|\n");
                }
                content.append("\n");
            }

            log.info("Successfully read DOCX file: {}", filePath);
            return content.toString().trim();
        } catch (IOException e) {
            log.error("Error reading DOCX file: {}", filePath, e);
            throw e;
        }
    }
    public String readPdfContent(Path pdfPath) {
        try (PDDocument document = Loader.loadPDF(pdfPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract PDF text", e);
        }
    }
    public String readExcelContent(Path excelPath) {
        StringBuilder content = new StringBuilder();
        try (Workbook workbook = new XSSFWorkbook(excelPath.toFile())) {
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        content.append(getCellValue(cell)).append("\t");
                    }
                    content.append("\n");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel", e);
        }
        return content.toString();
    }

    // 获取单元格值（兼容不同类型）
    private String getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

}