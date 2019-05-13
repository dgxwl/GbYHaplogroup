package test;

/**
 * 解析原始文件
 * @author Administrator
 *
 */
public class ParseRawData {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("map size: "+ParseSNPsXlsx.getSnpMap().size());
		System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
	}
}
