package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 测试: 打印输出看看基因宝可检测位点对应的单倍群
 * @author Administrator
 *
 */
public class ParseGbYXlsx {

	public static void main(String[] args) {
		Map<String, SNP> snpMap = ParseSNPsXlsx.getSnpMap();
		writeHaplogroup(snpMap);
		
		System.out.println("Finished.");
	}
	
	private static void writeHaplogroup(Map<String, SNP> snpMap) {
		//解析GbY表格
		File gbyFile = new File("./data/GbY.xlsx");
		if (!gbyFile.exists()) {
			System.exit(0);
		}
		
		try (XSSFWorkbook workbook = new XSSFWorkbook(gbyFile);
				FileOutputStream fos = new FileOutputStream("haplogroup.txt");
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				PrintWriter pw = new PrintWriter(fos, true)) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取行数
			int lastRowNum = sheet.getLastRowNum();
			
			Row row;
			String hg19;
			SNP s;
			Hg19AndName han;
			List<Hg19AndName> l = new LinkedList<>();
			
			for (int i = 1; i < lastRowNum; i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				
				hg19 = ParseSNPsXlsx.getCellValue(row.getCell(2));
				s = snpMap.get(hg19);
				if (s != null) {
					StringBuilder b = new StringBuilder();
					han = new Hg19AndName();
					han.setHg19(hg19);
					han.setFullName(b.append(s.getHaplogroup()).append('-').append(s.getName()).toString());
					l.add(han);
				}
			}
			
			l.sort((o1, o2) -> o1.getFullName().compareTo(o2.getFullName()));
			
			StringBuilder builder;
			for (Hg19AndName h : l) {
				builder = new StringBuilder();
				builder.append(h.getHg19()).append("\t").append(h.getFullName());
				pw.println(builder.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
