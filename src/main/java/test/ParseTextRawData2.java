package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

/**
 * 解析基因宝或魔方的原始数据文件 
 * @author Administrator
 *
 */
public class ParseTextRawData2 {
	public static void main(String[] args) {
		try (FileReader fr = new FileReader("F:/raw.txt");
				BufferedReader br = new BufferedReader(fr);
				FileWriter fw = new FileWriter("F:/运行结果.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw, true)) {
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
			
			for (String l : list) {
				pw.println(l);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		System.out.println("finished.");
	}
}
