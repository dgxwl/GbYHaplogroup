package test.algorithms;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import test.entity.SNP;
import test.prepare.ParseSNPsXlsx;
import test.util.XLSXCovertCSVReader;

/**
 * 解析xlsx表格式原始数据文件
 * @author Administrator
 *
 */
public class ParseXlsxRawDataStrategy implements IParseStrategy {

	@Override
	public List<SNP> parse(File rawFile) {
		Map<String, SNP> snpMap = ParseSNPsXlsx.getSnpMap();
		String hg19;
		String genotype;
		SNP snp;
		List<SNP> matchedSNPs = new LinkedList<>();
		try {
			List<String[]> list = XLSXCovertCSVReader.readerExcel(rawFile.getPath(), "Sheet1", 4);  //直接解析20多万行的xlsx会内存溢出, 需要先转为csv
			for (String[] record : list) {
				if (!record[1].equals("Y")) {  //只看Y染色体的位点
					continue;
				}
				hg19 = record[2];
				genotype = record[3];
				snp = snpMap.get(hg19);
				if (snp == null) {
					continue;
				}
				if (genotype.startsWith(snp.getMutant())) {  //匹配突变型
					matchedSNPs.add(snp);
				}
			}
			
			matchedSNPs.sort((o1, o2) -> o1.getHaplogroup().compareTo(o2.getHaplogroup()));  //根据单倍群名称进行排序
			
			return matchedSNPs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
