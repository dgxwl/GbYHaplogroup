package test;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

	public void init() {
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

	private void myEvent() {
		// 打开菜单项监听
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				pickFileDialog.setVisible(true);

				String dirpath = pickFileDialog.getDirectory();
				String fileName = pickFileDialog.getFile();

				if (dirpath == null || fileName == null)
					return ;
				
				rawDataFile = new File(dirpath, fileName);
				try {
					//处理原始数据文件, 单倍群分型
					String suffix = rawDataFile.getName().substring(rawDataFile.getName().lastIndexOf('.') + 1);
					ParseXlsxRawDataStrategy parseXlsx = new ParseXlsxRawDataStrategy();
					ParseTxtRawDataStrategy parseTxt = new ParseTxtRawDataStrategy();
					
					String result;
					switch (suffix) {
					case "xlsx":
						result = new ParseContext(parseXlsx).parse(rawDataFile);
						break;
					case "txt":
						result = new ParseContext(parseTxt).parse(rawDataFile);
						break;
					default:
						result = "无法解析 ." + suffix + "文件";
					}
					textArea.setText(result);
				} catch (Exception e1) {
					textArea.setText("错误: " + e1.getMessage());
				}
			}
		});
	}

	public static void main(String[] args) {
		ParseRawData prd = new ParseRawData();
		prd.init();
		prd.myEvent();
	}
}
