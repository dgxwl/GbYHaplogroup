package test.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import test.entity.SNP;
import test.prepare.ParseSNPsXlsx;

public class ParseTxtRawDataStrategy implements IParseStrategy {

	@Override
	public String parse(File rawFile) {
		try (FileReader fr = new FileReader(rawFile);
				BufferedReader br = new BufferedReader(fr)) {
			Map<String, SNP> snpMap = ParseSNPsXlsx.getSnpMap();
			
			String line;
			
			String[] rows;
			SNP snp;
			List<String> list = new LinkedList<>();
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				
				rows = line.split("\\s");
				if (!rows[1].equals("Y")) {
					continue;
				}

				snp = snpMap.get(rows[2]);
				if (snp == null) {
					continue;
				}

				if (rows[3].startsWith(snp.getMutant())) {
					list.add(snp.getHaplogroup() + "-" + snp.getName());
				}
			}
			list.sort(null);
			
			StringBuilder builder = new StringBuilder();
			for (String l : list) {
				builder.append(l).append('\n');
			}
			
			return builder.toString();
		} catch (Exception e) {
			return "错误: " + e.getMessage();
		}
	}

}
