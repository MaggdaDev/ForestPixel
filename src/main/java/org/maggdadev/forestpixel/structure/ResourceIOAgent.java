package org.maggdadev.forestpixel.structure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ResourceIOAgent {
    void saveTo(OutputStream out) throws IOException;
    void loadFrom(InputStream in) throws IOException;
}
