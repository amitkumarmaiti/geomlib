package de.yvert.model;

import java.io.IOException;
import java.io.InputStream;

public interface ModelReader
{

Model read(String name, InputStream in, int size) throws IOException;

}
