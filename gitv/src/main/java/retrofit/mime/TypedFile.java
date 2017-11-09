package retrofit.mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TypedFile implements TypedInput, TypedOutput {
    private static final int BUFFER_SIZE = 4096;
    private final File file;
    private final String mimeType;

    public TypedFile(String mimeType, File file) {
        if (mimeType == null) {
            throw new NullPointerException("mimeType");
        } else if (file == null) {
            throw new NullPointerException("file");
        } else {
            this.mimeType = mimeType;
            this.file = file;
        }
    }

    public File file() {
        return this.file;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public long length() {
        return this.file.length();
    }

    public String fileName() {
        return this.file.getName();
    }

    public InputStream in() throws IOException {
        return new FileInputStream(this.file);
    }

    public void writeTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        FileInputStream in = new FileInputStream(this.file);
        while (true) {
            try {
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                out.write(buffer, 0, read);
            } finally {
                in.close();
            }
        }
    }

    public void moveTo(TypedFile destination) throws IOException {
        if (!mimeType().equals(destination.mimeType())) {
            throw new IOException("Type mismatch.");
        } else if (!this.file.renameTo(destination.file())) {
            throw new IOException("Rename failed!");
        }
    }

    public String toString() {
        return this.file.getAbsolutePath() + " (" + mimeType() + ")";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypedFile)) {
            return false;
        }
        return this.file.equals(((TypedFile) o).file);
    }

    public int hashCode() {
        return this.file.hashCode();
    }
}
