package test.algorithms;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import test.entity.SNP;
import test.prepare.ParseSNPsXlsx;
import test.util.XLSXCovertCSVReader;

/**
 * 解析xlsx表格式原始数据
 * @author Administrator
 *
 */
public class ParseXlsxRawDataStrategy implements IParseStrategy {

	@Override
	public String parse(File rawFile) {
		Map<String, SNP> snpMap = ParseSNPsXlsx.getSnpMap();
		String hg19;
		String genotype;
		SNP snp;
		StringBuilder builder = new StringBuilder();
		List<SNP> sortList = new LinkedList<>();
		try {
			List<String[]> list = XLSXCovertCSVReader.readerExcel(rawFile.getPath(), "Sheet1", 4);
			for (String[] record : list) {
				if (!record[1].equals("Y")) {
					continue;
				}
				hg19 = record[2];
				genotype = record[3];
				snp = snpMap.get(hg19);
				if (snp == null) {
					continue;
				}
				if (genotype.startsWith(snp.getMutant())) {
					sortList.add(snp);
				}
			}
			
			sortList.sort((o1, o2) -> o1.getHaplogroup().compareTo(o2.getHaplogroup()));
			for (SNP s : sortList) {
				builder.append(s.getName()).append("\t").append(s.getHaplogroup()).append("\n");
			}
			return builder.toString();
		} catch (Exception e) {
			return "错误: " + e.getMessage();
		}
	}

}
