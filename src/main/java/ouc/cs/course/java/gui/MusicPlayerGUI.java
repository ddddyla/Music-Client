package ouc.cs.course.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections.functors.OnePredicate;

import ouc.cs.course.java.gui.LocalMusicSheetBlock.OnRowSelectListener;
import ouc.cs.course.java.gui.MusicSheetManagementBlock.OnDeleteClickListener;
import ouc.cs.course.java.model.MusicSheet;

public class MusicPlayerGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private LocalMusicSheetBlock localMusicSheetBlock;
	private MusicSheetManagementBlock musicSheetManagementBlock;
	private MusicSheetDisplayBlock musicSheetDisplayBlock;
	private SharedMusicSheetBlock sharedMusicSheetBlock;

	public MusicPlayerGUI(String title) {
		this.setTitle(title);
		this.setSize(900, 600);
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout(4, 4));
		container.setBackground(Color.WHITE);

		/* WEST ************************************/
		JPanel westPanel = new JPanel();
		westPanel.setPreferredSize(new Dimension(250, 600));
		BoxLayout westPanelLayout = new BoxLayout(westPanel, BoxLayout.Y_AXIS);
		westPanel.setLayout(westPanelLayout);
		container.add("West", westPanel);
		
		localMusicSheetBlock = new LocalMusicSheetBlock(); 
		localMusicSheetBlock.setOnRowSelectListener(new OnRowSelectListener() {
			
			public void onRowSelect(MusicSheet sheet) {
				musicSheetDisplayBlock.showSheet(sheet);
			}
		});
		
		
		musicSheetManagementBlock = new MusicSheetManagementBlock();
		musicSheetManagementBlock.setOnDeleteClickListener(new OnDeleteClickListener() {
			
			public void onDeleteBtnClick() {
				localMusicSheetBlock.deleteSheet();
			}
		});
		
		musicSheetManagementBlock.setOnAddSheetListener(new MusicSheetManagementBlock.OnAddSheetListener() {
			
			public void onAddSheet() {
				localMusicSheetBlock.showMusicSheets();
			}
		});

		sharedMusicSheetBlock = new SharedMusicSheetBlock();
		sharedMusicSheetBlock.setOnRowSelectListener(new SharedMusicSheetBlock.OnRowSelectListener() {
			
			public void onRowSelect(MusicSheet sheet) {
				musicSheetDisplayBlock.showSheet(sheet);
			}
		});
		westPanel.add(sharedMusicSheetBlock);
		westPanel.add(localMusicSheetBlock);
//		westPanel.add(musicSheetManagementBlock);

		/* CENTER ************************************/
		JPanel centerPanel = new JPanel();
		BoxLayout centerPanelLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
		centerPanel.setLayout(centerPanelLayout);
		container.add("Center", centerPanel);

		musicSheetDisplayBlock = new MusicSheetDisplayBlock();
		centerPanel.add(musicSheetDisplayBlock);
		
//		centerPanel.add(new MusicPlayerBlock());
	}

	public static void main(String[] args) {
		new MusicPlayerGUI("MUSIC PLAYER").setVisible(true);
	}
}
