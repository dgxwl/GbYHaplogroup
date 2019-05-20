package test.prepare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

import test.entity.SNP;

import java.util.LinkedList;
import java.util.List;

/**
 * parse genebox txt raw data
 * @author Administrator
 *
 */
public class ParseTextRawData {
	public static void main(String[] args) {
		try (FileReader fr = new FileReader("F:/genebox.txt");
				BufferedReader br = new BufferedReader(fr);
				FileWriter fw = new FileWriter("F:/运行结果.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw, true)) {
			Map<String, SNP> snpMap = ParseSNPsXlsx.getSnpMap();
			
			String allData = br.readLine();
			String[] rowStr = allData.split("<br/>");
			String[] rows;
			SNP snp;
			List<String> list = new LinkedList<>();
			for (String s : rowStr) {
				rows = s.split("\\s");
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
			
			for (String line : list) {
				pw.println(line);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}
