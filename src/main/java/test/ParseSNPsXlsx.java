package test;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 解析isogg提供的SNP信息
 * @author Administrator
 *
 */
public class ParseSNPsXlsx {
	
	//key: hg19Pos, value: SNP entry
	private static Map<String, SNP> snpMap = new TreeMap<>();

	static {
		File snpFile = new File("./data/SNPs.xlsx");
		if (!snpFile.exists()) {
			System.exit(0);
		}
		
		try (XSSFWorkbook workbook = new XSSFWorkbook(snpFile)) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取行数
			int lastRowNum = sheet.getLastRowNum();

			Row row;
			SNP snp;
			String name;
			String haplogroup;
			String hg19;
			String hg38;
			String[] mutation;
			
			for (int i = 0; i < lastRowNum; i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				
				snp = new SNP();
				
				try {
					name = getCellValue(row.getCell(0)).trim();
					if (!StringUtils.isNullOrEmpty(name)) {
						snp.setName(name);
					}
					haplogroup = getCellValue(row.getCell(1)).trim();
					if (!StringUtils.isNullOrEmpty(haplogroup)) {
						snp.setHaplogroup(haplogroup);
					}
					hg19 = getCellValue(row.getCell(4)).trim();
					if (!StringUtils.isNullOrEmpty(hg19)) {
						snp.setHg19Pos(hg19);
					}
					hg38 = getCellValue(row.getCell(5)).trim();
					if (!StringUtils.isNullOrEmpty(hg38)) {
						snp.setHg38Pos(hg38);
					}
					mutation = getCellValue(row.getCell(6)).trim().split("->");
					if (mutation.length == 2) {
						if (!StringUtils.isNullOrEmpty(mutation[0])) {
							snp.setOriginal(mutation[0].trim());
						}
						if (!StringUtils.isNullOrEmpty(mutation[1])) {
							snp.setMutant(mutation[1].trim());
						}
					} else {
						snp.setOriginal(mutation[0]);
						snp.setMutant(mutation[0]);
					}
					
					snpMap.put(snp.getHg19Pos(), snp);
				} catch (RuntimeException e) {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell) {
		Object result = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case STRING:
				result = cell.getStringCellValue();
				break;
			case NUMERIC:
				cell.setCellType(CellType.STRING);  //不需要获取浮点型,应获取文本
				result = cell.getStringCellValue();
				break;
			case BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case FORMULA:
				result = cell.getCellFormula();
				break;
			case ERROR:
				result = cell.getErrorCellValue();
				break;
			case BLANK:
				break;
			default:
				break;
			}
		}
		return result.toString();
	}
	
	public static Map<String, SNP> getSnpMap() {
		return snpMap;
	}

	public static void setSnpMap(Map<String, SNP> snpMap) {
		ParseSNPsXlsx.snpMap = snpMap;
	}
}
