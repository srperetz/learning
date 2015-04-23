import java.nio.file.*;
import java.nio.file.attribute.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import org.w3c.dom.*;

public class Pix {

    static final String GLOB_DEFAULT = "0*.JPG";

    private Path _pixDir;
    private String _glob;
    private boolean _print;
    private String _attrNamePattern;

    public Pix(Path pixDir, String glob, boolean print, String attrNamePattern) {
        _pixDir = pixDir;
        _glob = glob;
        _print = print;
        _attrNamePattern = attrNamePattern;
    }

    void process() {
        if (_print) printAttributes();
        else renameFiles();
    }

    private void renameFiles() {

    }

    private void printAttributes() {
        try (DirectoryStream<Path> pixDirStr = Files.newDirectoryStream(_pixDir, _glob)) {

            for (Path picFile : pixDirStr) {
                System.out.format("%n--------------------%n%s:%n", picFile);
                readAndDisplayMetadata(picFile);
                /*
                UserDefinedFileAttributeView attrView = Files.getFileAttributeView(picFile, UserDefinedFileAttributeView.class);
                List<String> attrNames = attrView.list();
                for (String attrName : attrNames) {
                    if (attrName.matches(_attrNamePattern)) {
                        int size = attrView.size(attrName);
                        ByteBuffer attrValBuf = ByteBuffer.allocateDirect(size);
                        attrView.read(attrName, attrValBuf);
                        attrValBuf.flip();
                        System.out.format(
                     " >>> %s=%s%n",
                     attrName,
                     Charset.defaultCharset().decode(attrValBuf).toString());
                    }
                }
                */
            }
        } catch (Throwable t) {
            System.err.format("Encountered exception: %s", t);
            System.exit(-1);
        }
    }
    private void readAndDisplayMetadata(Path picFile) throws Throwable {
        ImageReader reader = null;
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(picFile.toFile());
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {
                reader = readers.next();
                reader.setInput(iis, true);
                IIOMetadata metadata = reader.getImageMetadata(0);
                String[] names = metadata.getMetadataFormatNames();
                int length = names.length;
                for (int i = 0; i < length; i++) {
                    System.out.format("Format name: %s%n", names[i]);
                    displayMetadata(metadata.getAsTree(names[i]));
                }
            }
        } finally {
            Throwable caughtEx = null;
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    caughtEx = t;
                }
            }
            if (iis != null) {
                try {
                    iis.close();
                } catch (Throwable t) {
                    if (caughtEx == null) caughtEx = t;
                }
            }
            if (caughtEx != null) throw caughtEx;
        }
    }

    private void displayMetadata(Node root) {
        displayMetadata(root, 0);
    }

    private void indent(int level) {
        for (int i = 0; i < level; i++) System.out.print("    ");
    }

    private void displayMetadata(Node node, int level) {
        String nodeName = node.getNodeName();
        boolean isMatch = nodeName.matches(_attrNamePattern);
        Node child = node.getFirstChild();

        if (isMatch) {
            // print open tag of element
            indent(level);
            System.out.format("<%s", node.getNodeName());
            NamedNodeMap map = node.getAttributes();
            if (map != null) {

                // print attribute values
                int length = map.getLength();
                for (int i = 0; i < length; i++) {
                    Node attr = map.item(i);
                    System.out.format(" %s=\"%s\"", attr.getNodeName(), attr.getNodeValue());
                }
            }

            if (child == null) {
                // no children, so close element and return
                System.out.format("/>%n");
                return;
            }

            // children, so close current tag
            System.out.format(">%n");
        }

        while (child != null) {
            // print children recursively
            displayMetadata(child, level + 1);
            child = child.getNextSibling();
        }

        if (isMatch) {
            // print close tag of element
            indent(level);
            System.out.format("</%s>%n", node.getNodeName());
        }
    }

    static void usage() {
        System.out.println("Usage: java Pix [-h] <path> [-g <glob>] [-print <namepattern> | -rename <attrname>]");
        System.out.println("   where:");
        System.out.println("   -h indicates to prepend the user.home directory in front of <path>");
        System.out.println("   -g <glob> indicates an alternative glob, where the default is '" + GLOB_DEFAULT + "'");
        System.out.println("   -print <namepattern> indicates to print attributes of the files matching the specified name pattern. This can be a regexp.");
        System.out.println("   -rename <attrname> indicates to rename the files based on the specified attribute name.");
        System.exit(-1);
    }
    public static void main(String[] args) {
        if (args.length < 1) {
            usage();
        }

        Path pixDir =
            args[0].equalsIgnoreCase("-h")
                ? Paths.get(System.getProperty("user.home"), args[1])
                : Paths.get(args[0]);

        String glob = GLOB_DEFAULT;
        boolean print = true;
        String attrName = ".*";

        for (int i = (args[0].equalsIgnoreCase("-h") ? 2 : 1); i < args.length; i+=2) {
            if (args.length <= i+1) usage();

            switch (args[i].toLowerCase()) {
                case "-g": {
                    glob = args[i+1];
                    break;
                }
                case "-print": {
                    print = true;
                    attrName = args[i+1];
                    break;
                }
                case "-rename": {
                    print = false;
                    attrName = args[i+1];
                    break;
                }
            }
        }

        Pix pix = new Pix(pixDir, glob, print, attrName);
        pix.process();
    }
}