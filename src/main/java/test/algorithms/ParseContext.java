package test.algorithms;

import java.io.File;

public class ParseContext {
	private IParseStrategy parseStrategy;

	public ParseContext(IParseStrategy parseStrategy) {
		this.parseStrategy = parseStrategy;
	}
	
	public String parse(File rawFile) {
		return parseStrategy.parse(rawFile);
	}
}
