package ouc.cs.course.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ouc.cs.course.java.model.MusicSheet;
import ouc.cs.course.java.other.Constant;
import ouc.cs.course.java.sqlite.OpSqliteDB;

public class NewSheetDialog extends JDialog {
	JTextField jSheetName;
	JTextField jCreator, jCreateorId, jPicture;
	JLabel jLabel1, jLabel2, jLabel3, jLabel4;
	JPanel jp1, jp2, jp3, jp4, jp5;
	JButton jb1, jb2, jbselpic;
	JFileChooser jFileChooser;
	
	private OnAddSheetListener onAddSheetListener;

	public NewSheetDialog(String title) throws HeadlessException {
		this.setTitle(title);
		this.setSize(300, 300);
		this.setLocation(400, 400);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout(4, 4));
		
		JpgFileFilter filter = new JpgFileFilter();
		jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(filter);

		jSheetName = new JTextField(16);
		jCreateorId = new JTextField(16);
		jCreateorId.setText(Constant.MY_ID);
		jCreator = new JTextField(16);
		jCreator.setText(Constant.MY_NAME);
		jPicture = new JTextField(12);
		jPicture.setEditable(false);
		
		jLabel1 = new JLabel("歌单名称");
		jLabel2 = new JLabel("创建者ID");
		jLabel3 = new JLabel("创建者");
		jLabel4 = new JLabel("歌单封面");

		jb1 = new JButton("确认");
		jb2 = new JButton("取消");
		jbselpic = new JButton("...");
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		jp5 = new JPanel();

		// 设置布局
		this.setLayout(new GridLayout(5, 1));

		jp1.add(jLabel1);
		jp1.add(jSheetName);

		jp2.add(jLabel2);
		jp2.add(jCreateorId); 
		
		jp3.add(jLabel3);
		jp3.add(jCreator);
		
		jp4.add(jLabel4);
		jp4.add(jPicture);
		jp4.add(jbselpic);
		
		jp5.add(jb1);
		jp5.add(jb2);		

		this.add(jp1);
		this.add(jp2);
		this.add(jp3); 
		this.add(jp4);
		this.add(jp5);
		
		jb2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				NewSheetDialog.this.setVisible(false);
			}
		});
		
		jb1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(jSheetName.getText().equals("")){
					JOptionPane.showMessageDialog(null, "歌单名称不能为空！", "提示", JOptionPane.ERROR_MESSAGE); 
					return;
				}
				
				if(jPicture.getText().equals("")){
					JOptionPane.showMessageDialog(null, "请选择一个封面图片！", "提示", JOptionPane.ERROR_MESSAGE); 
					return;
				}
				
				MusicSheet sheet = new MusicSheet();
				sheet.setName(jSheetName.getText());
				sheet.setCreator(jCreator.getText());
				sheet.setCreatorId(jCreateorId.getText());
				sheet.setPicture(jPicture.getText());
				OpSqliteDB.addMusicSheet(sheet);
				
				if(onAddSheetListener != null){
					onAddSheetListener.onAddSheet();
				}
				
				NewSheetDialog.this.setVisible(false);
			}
		});
		
		jbselpic.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int result = jFileChooser.showOpenDialog(NewSheetDialog.this);
				if(result == JFileChooser.APPROVE_OPTION){
					jPicture.setText(jFileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
	}
	
	class JpgFileFilter extends javax.swing.filechooser.FileFilter{

		@Override
		public boolean accept(File f) {
			if(f.getAbsolutePath().endsWith(".jpg") || f.isDirectory()){
				return true;
			}
			
			return false;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "Jpg文件";
		}
	}
	
	public OnAddSheetListener getOnAddSheetListener() {
		return onAddSheetListener;
	}

	public void setOnAddSheetListener(OnAddSheetListener onAddSheetListener) {
		this.onAddSheetListener = onAddSheetListener;
	}
	
	public interface OnAddSheetListener{
		public void onAddSheet();
	}
}
