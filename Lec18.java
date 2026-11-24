import java.util.List;
import java.util.ArrayList;

record Info(
    String name,
    int createdAt,
    String type,
    int modifiedAt,
    String owner) {
}
interface FSItem {
    Info info();
    int totalSize();
    String tree(String indent);
}
record Folder(Info info, List<FSItem> items) implements FSItem {
    public int totalSize() {
        int total = 0;
        for(FSItem item : items) {
            total += item.totalSize();
        }
        return total;
    }
    public String tree(String indent) {
        String result = indent + info.name() + "/\n";
        for (FSItem item : items) {
            result += item.tree(indent + "  ");
        }
        return result;
    }
}
record File(Info info, char[] content) implements FSItem {
    public int totalSize() {
        return content.length;
    }
    public String tree(String indent) {
        return indent + info.name() + "\n";
    }
}
/* Operations:
    - Given a folder, return a list of the names of the contents
    - Get the TOTAL size of a FSItem (sum of file sizes)
    - "Enter" one of the folders
    - How many UNIQUE authors in the folder/filesystem
    - Edit the contents of a specific file
    - How deep is the deepest file
    - adding and deleting folders and files
    - What is the largest file
    - Duplicate a file or folder
    - Return a string of the "tree view" of the filesystem like Claude did
    - Search for a file (by name, etc)
    - Add an id for a file, and look up a file by id
    - Move a file

*/

Folder f(String name, List<FSItem> items) {
    return new Folder(new Info(
        name, 0, "folder", 0, "jpolitz@ucsd.edu"
    ), items);
}
File fi(String name, String type, String contents) {
    return new File(new Info(
        name, 0, type, 0, "jpolitz@ucsd.edu"
    ), contents.toCharArray());
}

void main() {
    // Make examples! Put *one* example of your own design in Answer Box #2
    Folder simple = f("PA2", List.of(
        fi("grades.csv", "csv", "fake"),
        fi("feedback.pdf", "pdf", "feedback")
    ));

    Folder deep = f("CSE11", List.of(
        fi("syllabus.pdf", "pdf", "syllabus"),
        f("PA1", List.of(
            fi("handout.pdf", "pdf", "handout"),
            f("starter", List.of(
                fi("Main.java", "java", "class Main {}"),
                fi("README.md", "md", "# PA1")
            )),
            fi("grades.csv", "csv", "a,b")
        )),
        f("PA2", List.of(
            f("tests", List.of(
                fi("Test1.java", "java", "test1"),
                fi("Test2.java", "java", "test2"),
                fi("expected.txt", "txt", "42")
            )),
            fi("feedback.pdf", "pdf", "feedback")
        )),
        fi("notes.txt", "txt", "notes")
    ));

    System.out.println(simple.tree(""));
    System.out.println(deep.tree(""));

}