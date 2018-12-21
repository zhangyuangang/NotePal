
package com.notepal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//让其继承窗口类
public class NotePal extends JFrame implements ActionListener {

	// 定义序列号
	private static final long serialVersionUID = 1L;
	// 定义一个flag记录文件内容是否发生改变（false：已保存）
	Boolean notSave = false;
	// 定义是否按下crtl键
	Boolean isCtrl = false;
	// 定义一个用户打开的文件路径(新建文件地址则为空)
	String address = "";
	// 定义一个文本框
	JTextArea jTextArea = null;
	// 定义一个菜单栏
	JMenuBar jMenuBar = null;
	// 定义一个菜单
	JMenu jMenu1 = null;
	// 定义一个“新建”子选项
	JMenuItem jMenuItem1 = null;
	// 定义一个“打开”子选项
	JMenuItem jMenuItem2 = null;
	// 定义一个“保存”子选项
	JMenuItem jMenuItem3 = null;
	// 定义一个“另存为”子选项
	JMenuItem jMenuItem4 = null;
	// 定义一个“退出”子选项
	JMenuItem jMenuItem5 = null;
	// 定义一个文件选择
	JFileChooser jFileChooser = null;
	// 定义一个InputStreamReader输入流
	InputStreamReader inputStreamReader = null;
	// 定义一个FileWriter输出流
	FileWriter fileWriter = null;
	// 定义一个缓冲字符输入流
	BufferedReader bufferedReader = null;
	// 定义一个缓冲字符输出流
	BufferedWriter bufferedWriter = null;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// 实例化
		NotePal notePal = new NotePal();
	}

	// 构造函数
	public NotePal() {
		// 实例化jTextArea
		jTextArea = new JTextArea();
		JScrollPane js = new JScrollPane(jTextArea);
		// 分别设置水平和垂直滚动条自动出现
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// 分别设置水平和垂直滚动条总是出现
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// 实例化jMenuBar
		jMenuBar = new JMenuBar();
		// 实例化就jMenu1，并设置其名字为“文件”
		jMenu1 = new JMenu("文件");
		jMenuItem1 = new JMenuItem("新建            Ctrl+N");
		jMenuItem1.addActionListener(this);
		jMenuItem1.setActionCommand("新建");
		jMenuItem2 = new JMenuItem("打开            Ctrl+O");
		jMenuItem2.addActionListener(this);
		jMenuItem2.setActionCommand("打开");
		jMenuItem3 = new JMenuItem("保存            Ctrl+S");
		jMenuItem3.addActionListener(this);
		jMenuItem3.setActionCommand("保存");
		jMenuItem4 = new JMenuItem("另存为        Ctrl+D");
		jMenuItem4.addActionListener(this);
		jMenuItem4.setActionCommand("另存为");
		jMenuItem5 = new JMenuItem("退出            Ctrl+X");
		jMenuItem5.addActionListener(this);
		jMenuItem5.setActionCommand("退出");

		// 设置jTextArea的背景颜色为
		jTextArea.setBackground(Color.CYAN);

		// 将组件添加上各自的位置
		this.setJMenuBar(jMenuBar);
		jMenuBar.add(jMenu1);
		jMenu1.add(jMenuItem1);
		jMenu1.add(jMenuItem2);
		jMenu1.add(jMenuItem3);
		jMenu1.add(jMenuItem4);
		jMenu1.add(jMenuItem5);
		this.add(js);

		// 设置窗体的标题
		this.setTitle("记事本（简易版）");
		// 设置窗体的大小
		this.setSize(800, 600);
		// 设置窗口居中
		this.setLocationRelativeTo(null);
		// 显示窗口
		this.setVisible(true);


		jTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				notSave = true;
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				notSave = true;
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				notSave = true;
			}

		});
		// 注册按键事件
		jTextArea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (isCtrl) {
					switch (keyCode) {
					case KeyEvent.VK_N:
						event_new();
						break;
					case KeyEvent.VK_O:
						event_open();
						break;
					case KeyEvent.VK_S:
						event_save();
						break;
					case KeyEvent.VK_D:
						event_save_as();
						break;
					case KeyEvent.VK_X:
						event_exit();
						break;
					}
				} else {
					switch (keyCode) {
					// 记录是否按下Ctrl键
					case KeyEvent.VK_CONTROL:
						isCtrl = true;
						break;
					case KeyEvent.VK_SHIFT:
						break;
					case KeyEvent.VK_ALT:
						break;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// 记录是否抬起Ctrl键
				if (isCtrl && e.getKeyCode() == KeyEvent.VK_CONTROL) {
					isCtrl = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					event_exit();
				}
			}

		});
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				event_exit();
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("新建")) {
			event_new();
		}
		if (e.getActionCommand().equals("打开")) {
			event_open();
		}
		if (e.getActionCommand().equals("保存")) {
			event_save();
		}
		if (e.getActionCommand().equals("另存为")) {
			event_save_as();
		}
		if (e.getActionCommand().equals("退出")) {
			event_exit();
		}
	}

	private void event_new() {
		// 如果没有保存，提示用户
		if (notSave) {
			int result = JOptionPane.showConfirmDialog(null, "文件未保存，确认创建新文件吗?", "确认对话框", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}
		address = "";
		jTextArea.setText("");
		// 状态改成已保存
		notSave = false;
	}

	private void event_open() {
		// 如果没有保存，提示用户
		if (notSave) {
			int result = JOptionPane.showConfirmDialog(null, "文件未保存，确认打开新文件吗?", "确认对话框", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}
		jFileChooser = new JFileChooser();
		jFileChooser.setDialogTitle("选择自己的文件... ...");
		jFileChooser.showOpenDialog(null);
		jFileChooser.setVisible(true);
		try {
			// 用address保存用户编辑文件的绝对路径，用户没有选择打开的文件，返回时会报错
			address = jFileChooser.getSelectedFile().getAbsolutePath();
		} catch (Exception e) {
			return;
		}
		// 获取文件的字符编码
		String charset = getFileCharset(address);
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(address), charset);
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = "";
			StringBuilder strAll = new StringBuilder();
			// 读取文件信息，并将其保存到strAll中
			while ((str = bufferedReader.readLine()) != null) {
				strAll.append(str).append("\r\n");
			}
			// 将strAll中的全部信息显示到JTextArea上
			jTextArea.setText(strAll.toString());
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			// 状态改成已保存
			notSave = false;
		}
	}

	private void event_save() {
		// 如果打开的文件地址为空，则说明是新建的文件，则无法保存，导航到另存为
		if (address.equals("")) {
			event_save_as();
			return;
		}
		try {
			fileWriter = new FileWriter(address);
			bufferedWriter = new BufferedWriter(fileWriter);
			// 内容写入文件
			bufferedWriter.write(this.jTextArea.getText());

		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				fileWriter.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			// 状态改成已保存
			notSave = false;
		}
		JOptionPane.showMessageDialog(null, "文件保存成功");
	}

	private void event_save_as() {
		// 创建一个保存窗口
		JFileChooser jFileChooser1 = new JFileChooser();
		jFileChooser1.setDialogTitle("另存为... ...");
		jFileChooser1.showSaveDialog(null);
		jFileChooser1.setVisible(true);
		try {
			String newAddress = "";
			try {
				// 用address保存用户编辑文件的绝对路径，用户没有选择打开的文件，返回时会报错
				newAddress = jFileChooser1.getSelectedFile().getAbsolutePath();
			} catch (Exception e) {
				return;
			}
			// 如果用户输入的文件名不包含后缀，则默认后缀为.txt
			if (newAddress.indexOf(".") <= 0) {
				newAddress += ".txt";
			} else if (newAddress.indexOf(".") == newAddress.length() - 1) {
				newAddress += "txt";
			}
			// 如果文件名重复
			File file = new File(newAddress);
			if (file.exists()) {
				int result = JOptionPane.showConfirmDialog(null, "文件已存在，要替换它吗?", "确认对话框", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.NO_OPTION) {
					return;
				}
			}
			fileWriter = new FileWriter(newAddress);
			bufferedWriter = new BufferedWriter(fileWriter);
			// 内容写入文件
			bufferedWriter.write(this.jTextArea.getText());
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				fileWriter.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			// 状态改成已保存
			notSave = false;
		}
		JOptionPane.showMessageDialog(null, "文件保存成功");
	}

	private void event_exit() {
		// 如果没有保存，提示用户
		if (notSave) {
			int result = JOptionPane.showConfirmDialog(null, "文件未保存，确认退出吗？", "确认对话框", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}
		System.exit(0);
	}

	private String getFileCharset(String path) {
		String charset = "GBK";
		try {
			byte[] first3Bytes = new byte[3];
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				bis.close();
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bis.reset();
			if (!checked) {
				while ((read = bis.read()) != -1) {
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80 - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--文件-> [" + path + "] 采用的字符集为: [" + charset + "]");
		return charset;
	}

}
