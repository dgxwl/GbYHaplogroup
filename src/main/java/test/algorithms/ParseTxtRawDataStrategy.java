package test.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import test.entity.SNP;
import test.prepare.ParseSNPsXlsx;

/**
 * 解析txt文本原始数据文件
 * @author Administrator
 *
 */
public class ParseTxtRawDataStrategy implements IParseStrategy {

	@Override
	public List<SNP> parse(File rawFile) {
		try (FileReader fr = new FileReader(rawFile);
				BufferedReader br = new BufferedReader(fr)) {
			Map<String, SNP> snpMap = ParseSNPsXlsx.getSnpMap();
			
			String line;
			
			String[] rows;
			SNP snp;
			List<SNP> list = new LinkedList<>();
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				rows = line.split("\\s");
				if (!rows[1].equals("Y")) {  //只看Y染色体的位点
					continue;
				}

				snp = snpMap.get(rows[2]);
				if (snp == null) {
					continue;
				}

				if (rows[3].startsWith(snp.getMutant())) {  //匹配突变型
					list.add(snp);
				}
			}
			
			list.sort((o1, o2) -> o1.getHaplogroup().compareTo(o2.getHaplogroup()));  //根据单倍群名称进行排序

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
