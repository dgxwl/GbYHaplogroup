package test;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import test.algorithms.ParseContext;
import test.algorithms.ParseTxtRawDataStrategy;
import test.algorithms.ParseXlsxRawDataStrategy;
import test.entity.SNP;

/**
 * 解析原始文件
 * 
 * @author Administrator
 *
 */
public class ParseRawData extends JPanel {
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private JMenuBar bar;
	private JTextArea textArea;
	private JMenu fileMenu;
	private JMenuItem openItem;
	private FileDialog pickFileDialog;
	private File rawDataFile;

	private void init() {  //初始化窗口界面
		frame = new JFrame("geneboxY");
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		fileMenu = new JMenu("操作");  //操作菜单
		openItem = new JMenuItem("选择原始数据");  //菜单项
		fileMenu.add(openItem);
		
		bar = new JMenuBar();  //菜单栏
		bar.add(fileMenu);
		frame.setJMenuBar(bar);
		
		textArea = new JTextArea(25, 50);  //文本域
		textArea.setText("提示: 操作->选择原始数据文件, 稍等几秒钟即可看到结果");
		textArea.setEditable(false);
		this.add(new JScrollPane(textArea));
		
		pickFileDialog = new FileDialog(frame, "选择原始数据", FileDialog.LOAD);  //文件选择窗口

		frame.add(this);
		frame.setVisible(true);
	}
	
	/**
	 * 处理原始数据文件, 得到SNP匹配结果
	 * @param rawDataFile
	 * @return
	 */
	public static List<SNP> parseFile(File rawDataFile) {
		String suffix = rawDataFile.getName().substring(rawDataFile.getName().lastIndexOf('.') + 1);
		ParseXlsxRawDataStrategy parseXlsx = new ParseXlsxRawDataStrategy();
		ParseTxtRawDataStrategy parseTxt = new ParseTxtRawDataStrategy();
		
		List<SNP> matchedSNPs;
		switch (suffix) {
		case "xlsx":
			matchedSNPs = new ParseContext(parseXlsx).parse(rawDataFile);
			break;
		case "txt":
			matchedSNPs = new ParseContext(parseTxt).parse(rawDataFile);
			break;
		default:
			throw new RuntimeException("无法解析 ." + suffix + "文件");
		}
		
		return matchedSNPs;
	}
	
	/**
	 * 根据已匹配的SNP获取单倍群分型;最终确定的单倍群应每一个上游节点都有匹配,
	 * 且名称长度最长(即下游最深入)
	 * @param snps 匹配的SNP list
	 * @return 单倍群名称
	 */
	public static SNP decideHaplogroup(List<SNP> snps) {
		int[] counts = new int[snps.size()];
		
		for (int i = 0; i < snps.size(); i++) {
			String str = snps.get(i).getHaplogroup();
			int count = 0;
			for (int j = 1; j < str.length(); j++) {
				String sub = str.substring(0, j);
				
				for (int k = 0; k < snps.size(); k++) {
					if (snps.get(k).getHaplogroup().equals(sub)) {
						count++;
						break;
					}
				}
			}
			if (count == str.length() - 1) {
				counts[i] = count;
			}
		}
		
		int max = 0;
		int maxIndex = 0;
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > max) {
				max = counts[i];
				maxIndex = i;
			}
		}
		
		return snps.get(maxIndex);
	}

	private void handleEvent() {
		//监听打开选择文件菜单项
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				pickFileDialog.setVisible(true);

				String dirpath = pickFileDialog.getDirectory();
				String fileName = pickFileDialog.getFile();

				if (dirpath == null || fileName == null) {
					return ;
				}
				
				rawDataFile = new File(dirpath, fileName);

				List<SNP> l = parseFile(rawDataFile);
				StringBuilder builder = new StringBuilder();
				for (SNP s : l) {
					builder.append(s.getHaplogroup()).append("-").append(s.getName()).append("\n");
				}
				SNP finalSNP = decideHaplogroup(l);
				builder.append('\n').append("单倍群为: ").append(finalSNP.getHaplogroup()).append('-').append(finalSNP.getName());
				//显示解析结果, 从中可得到SNP匹配情况和单倍群分型
				textArea.setText(builder.toString());
			}
		});
	}

	public static void main(String[] args) {
		ParseRawData prd = new ParseRawData();
		prd.init();
		prd.handleEvent();
	}
}
