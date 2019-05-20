package test.algorithms;

import java.io.File;

/**
 * 解析策略: 每次拿到的原始数据文件排版都不同...
 * 使用策略模式方便扩展
 * @author Administrator
 *
 */
public interface IParseStrategy {

	String parse(File rawFile);
}
