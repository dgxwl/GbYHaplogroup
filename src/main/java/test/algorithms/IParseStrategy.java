package test.algorithms;

import java.io.File;
import java.util.List;

import test.entity.SNP;

/**
 * 解析策略: 每次拿到的原始数据文件格式/文本排版都不同,使用策略模式方便扩展
 * @author Administrator
 *
 */
public interface IParseStrategy {

	/**
	 * 解析原始数据文件, 返回所有匹配的SNP
	 * @param rawFile
	 * @return
	 */
	List<SNP> parse(File rawFile);
}
