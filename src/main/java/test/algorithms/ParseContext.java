package test.algorithms;

import java.io.File;
import java.util.List;

import test.entity.SNP;

/**
 * 策略模式上下文类
 * @author Administrator
 *
 */
public class ParseContext {
	private IParseStrategy parseStrategy;

	public ParseContext(IParseStrategy parseStrategy) {
		this.parseStrategy = parseStrategy;
	}
	
	public List<SNP> parse(File rawFile) {
		return parseStrategy.parse(rawFile);
	}
}
