package ouc.cs.course.java.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ouc.cs.course.java.httpclient.HttpPostWithJSON;
import ouc.cs.course.java.httpclient.MusicSheetAndFilesUploader;
import ouc.cs.course.java.model.Music;
import ouc.cs.course.java.model.MusicSheet;
import ouc.cs.course.java.sqlite.OpSqliteDB;

public class UploadSheetDialog extends JDialog {
	private JLabel titleLabel ;
	private JLabel progressLabel;
	private JButton startButton;
	private JButton cancelButton;
	
	private MusicSheet uploadSheet;

	private Thread uploadThread;

	public UploadSheetDialog(String title) {
		this.setTitle(title);
		this.setSize(300, 200);
		this.setLocation(400, 200);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container container = this.getContentPane();
		container.setLayout(new GridLayout(3, 1));
		
		titleLabel = new JLabel("上传歌单……");
		progressLabel = new JLabel("Waiting...");
		
		startButton = new JButton("开始上传");
		startButton.addActionListener(startUploadListener);
		cancelButton = new JButton("取消上传");
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(startButton);
		buttonPanel.add(cancelButton);
		
		this.add(titleLabel);
		this.add(progressLabel);
		this.add(buttonPanel);
	}

	private ActionListener startUploadListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			progressLabel.setText("正在上传...");
			if (uploadThread == null){
				uploadThread = new Thread(new Runnable() {
					
					public void run() {
						List<String> musicFilePaths = new ArrayList<String>();
						
						ArrayList<Music> mlist = OpSqliteDB.getMusicList(uploadSheet.getUuid());
						for(int i = 0; i < mlist.size(); i++){
							musicFilePaths.add(mlist.get(i).getMusicpath());
						}
						
						MusicSheetAndFilesUploader.createMusicSheetAndUploadFiles(
								"http://service.uspacex.com/music.server/upload", 
								uploadSheet, musicFilePaths);
						
						SwingUtilities.invokeLater(new Runnable() {
							
							public void run() {
								progressLabel.setText("上传完成！");
							}
						});
					}
				});
			}
			
			uploadThread.start();
		}
	};
	
	public MusicSheet getUploadSheet() {
		return uploadSheet;
	}

	public void setUploadSheet(MusicSheet uploadSheet) {
		this.uploadSheet = uploadSheet;
		titleLabel.setText("上传歌单： " + uploadSheet.getName());
	}
}
