package ouc.cs.course.java.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.httpclient.HttpException;

import ouc.cs.course.java.gui.LocalMusicSheetBlock.OnRowSelectListener;
import ouc.cs.course.java.httpclient.FileDownloader;
import ouc.cs.course.java.httpclient.MusicSheetTaker;
import ouc.cs.course.java.model.MusicSheet;

public class SharedMusicSheetBlock extends JPanel {

	private static final long serialVersionUID = 1L;

	private Object[][] shareMusicData = { { "101", "music sheet 01" }, { "102", "music sheet 02" },
			{ "103", "music sheet 03" } };
	private String[] shareMusicColumnNames = { "分享者", "歌 单" };
	
	private List<MusicSheet> slist = new ArrayList();
	private SheetsModel sheetModel = new SheetsModel();
	
	private JTable sharedMusicSheetTable;

	public SharedMusicSheetBlock() {
		this.setPreferredSize(new Dimension(250, 400));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
		this.setBackground(Color.ORANGE);
		JLabel sharedMusicSheetLabel = new JLabel("共享歌单");
				 		
		sharedMusicSheetTable = new JTable(sheetModel);
		sharedMusicSheetTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		for(int i = 0; i < shareMusicColumnNames.length; i++){
			sharedMusicSheetTable.getColumnModel().getColumn(i).setHeaderValue(shareMusicColumnNames[i]);
		}
		
		sharedMusicSheetTable.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
                int sr;
                if ((sr = sharedMusicSheetTable.getSelectedRow()) == -1) {
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                	if(onRowSelectListener != null){
                		File directory = new File("");
                		File picdir = new File(directory.getAbsolutePath() + "\\picture\\");
                		if(!picdir.exists()){
                			picdir.mkdirs();
                		}
                		slist.get(sr).setIslocal(false);
                		slist.get(sr).setPicture(picdir.getAbsolutePath() + "\\" + slist.get(sr).getPicture());
                		FileDownloader.downloadMusicSheetPicture(
                				"http://service.uspacex.com/music.server/downloadPicture", 
                				slist.get(sr).getUuid(), picdir.getAbsolutePath());
                		onRowSelectListener.onRowSelect(slist.get(sr));
                	}
                }
			}
		});
		
		JScrollPane sharedMusicSheetTablePanel = new JScrollPane(sharedMusicSheetTable);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		JButton btnShowSharedSheets = new JButton("下载歌单");
		btnShowSharedSheets.addActionListener(getSharedSheetAction);
		
		buttonPanel.add(btnShowSharedSheets);
		
		this.add(Box.createVerticalStrut(5));		
		this.add(sharedMusicSheetLabel);
		this.add(Box.createVerticalStrut(5));		
		this.add(sharedMusicSheetTablePanel);
		this.add(Box.createVerticalStrut(5));
		this.add(buttonPanel);
	}
	
	ActionListener getSharedSheetAction = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			try {
				slist = MusicSheetTaker.queryMusicSheets("http://service.uspacex.com/music.server/queryMusicSheets?type=all");
				sharedMusicSheetTable.invalidate();
				sharedMusicSheetTable.updateUI();
			} catch (HttpException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	};

	private OnRowSelectListener onRowSelectListener;
	
	class SheetsModel extends AbstractTableModel{

		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		public int getRowCount() {
			// TODO Auto-generated method stub
			return slist.size();
		}

		public Object getValueAt(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
			switch(arg1){
			case 1:
				return slist.get(arg0).getName();
			default:
				return slist.get(arg0).getCreator();
			}
		}
		
	}
	
	public OnRowSelectListener getOnRowSelectListener() {
		return onRowSelectListener;
	}

	public void setOnRowSelectListener(OnRowSelectListener onRowSelectListener) {
		this.onRowSelectListener = onRowSelectListener;
	}
	
	public interface OnRowSelectListener{
		public void onRowSelect(MusicSheet sheet);
	}
}
