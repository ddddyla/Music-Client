package ouc.cs.course.java.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import javazoom.jl.player.Player;

import javax.sound.sampled.AudioFormat;  
import javax.sound.sampled.AudioInputStream;  
import javax.sound.sampled.AudioSystem;  
import javax.sound.sampled.DataLine;  
import javax.sound.sampled.SourceDataLine;

import ouc.cs.course.java.httpclient.FileDownloader;
import ouc.cs.course.java.model.Music;
import ouc.cs.course.java.model.MusicSheet;
import ouc.cs.course.java.other.AudioFilePlayer;
import ouc.cs.course.java.other.AudioFilePlayer.OnPlayFinishListener;
import ouc.cs.course.java.other.AudioFilePlayer.OnProgressChangeListener;
import ouc.cs.course.java.other.FileMd5;
import ouc.cs.course.java.other.Mp3Info;
import ouc.cs.course.java.sqlite.OpSqliteDB;

public class MusicSheetDisplayBlock extends JPanel {

	private static final long serialVersionUID = 1L;
	private String picPath = "E:\\wangchao\\Documents\\asecondyear\\Java\\shiyan\\wangchao\\pictures\\小巷.jpg";
	private String picPath1 = "E:\\wangchao\\Documents\\asecondyear\\Java\\shiyan\\wangchao\\pictures\\小巷.jpg";
	
	private JLabel musicSheetTitleLabel;
	private JLabel musicSheetCreatorLabel;
	private ImageIcon musicSheetPicture;
	private JLabel musicSheetPictureLabel;
	
	private JTable musicTable;
	
	private JFileChooser jFileChooser;
	
	private JComboBox<String> playMode ;
	
	private MusicSheet selsheet;
	
	private AudioFilePlayer player ;
	
	JSlider slider;
	
	private int playingMusicIndex = 0;
	private Music playingMusic = null;
	
	private Object[][] musicData = { { "Yesterday.mp3", "Guns and Roses", "10 min", "", "" },
			{ "Night train.mp3", "Guns and Roses", "10 min", "", "" },
			{ "November rain.mp3", "Guns and Roses", "10 min", "", "" } };
	private String[] musicColumnNames = { "曲名", "歌手", "时长", "播放", "下载" };

	private MusicModel musicModel = new MusicModel();
	private ArrayList<Music> musicList = new ArrayList();
	

	public MusicSheetDisplayBlock() {
		this.setPreferredSize(new Dimension(550, 200));
		GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gb);
		
		player = new AudioFilePlayer();
		player.setOnProgressChangeListener(onProgressChangeListener);
		player.setOnPlayFinishListener(new OnPlayFinishListener() {
			
			public void onPlayFinish() {
				playNextMusic(1);
			}
		});
		
		JPanel musicTopPanel = new JPanel();
		musicTopPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		musicTopPanel.setPreferredSize(new Dimension(300, 300));

		
		jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(new Mp3FileFilter());
		
		musicSheetPicture = new ImageIcon(picPath);
		int musicSheetPictureWidth = 250;
		int musicSheetPictureHeight = 250 * musicSheetPicture.getIconHeight() / musicSheetPicture.getIconWidth();
		musicSheetPicture.setImage(musicSheetPicture.getImage().getScaledInstance(musicSheetPictureWidth,
				musicSheetPictureHeight, Image.SCALE_DEFAULT));

		musicSheetPictureLabel = new JLabel(musicSheetPicture);
		musicSheetPictureLabel.setPreferredSize(new Dimension(musicSheetPictureWidth, musicSheetPictureHeight));

		JPanel musicSheetInfoPanel = new JPanel();
		musicSheetInfoPanel.setPreferredSize(new Dimension(200, 300));
		musicSheetInfoPanel.setLayout(new BoxLayout(musicSheetInfoPanel, BoxLayout.Y_AXIS));

		musicSheetTitleLabel = new JLabel("杂七杂八的摇滚");
		musicSheetCreatorLabel = new JLabel("2011022 于 2017年11月24日 创建");

		JPanel musicSheetButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton playAllMusicButton = new JButton("播放全部");
		playAllMusicButton.addActionListener(playAllListener);
		JButton downloadAllMusicButton = new JButton("下载全部");
		downloadAllMusicButton.addActionListener(downloadMp3Listener);
		JButton addMusic = new JButton("添加音乐");
		addMusic.addActionListener(addMusicListener);
		JButton deleteMusic = new JButton("删除音乐");
		deleteMusic.addActionListener(deleteMusicListener);
		
		musicSheetButtonPanel.add(playAllMusicButton);
		musicSheetButtonPanel.add(downloadAllMusicButton);
		musicSheetButtonPanel.add(addMusic);
		musicSheetButtonPanel.add(deleteMusic);
		
		//
		musicTable = new JTable(musicModel);
		musicTable.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                	if(musicTable.getSelectedRow() != -1){
                		Music music = musicList.get(musicTable.getSelectedRow());
                		if(music == playingMusic && music.isPlaying()){
                			stopMusic();
                		}else{
                			playMusic(music);
                		}
                	}
                }
			}
		});;

//		TableColumnModel columnModel = new DefaultTableColumnModel();
//		columnModel.addColumn(new TableColumn(0).set);
//		musicTable.setColumnModel(columnModel );
		musicTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		for(int i = 0; i < musicColumnNames.length; i++){
			musicTable.getColumnModel().getColumn(i).setHeaderValue(musicColumnNames[i]);
		}
		JScrollPane musicTablePanel = new JScrollPane(musicTable);

		slider = new JSlider();
		slider.setValue(0);

		JPanel musicPlayerPanel = new JPanel();
		musicPlayerPanel.setLayout(new FlowLayout());
		musicPlayerPanel.setBackground(Color.GRAY);
		JButton previousMusicButton = new JButton("Previous");
		previousMusicButton.addActionListener(playLastListener);
		JButton playMusicButton = new JButton("Play");
		playMusicButton.addActionListener(playListener);
		JButton stopMusicButton = new JButton("Stop");
		stopMusicButton.addActionListener(stopListener);
		JButton nextMusicButtonButton = new JButton("Next");
		nextMusicButtonButton.addActionListener(playNextListener);
		JLabel playModeLabel = new JLabel("播放模式");
		playMode = new JComboBox<String>();
		playMode.addItem("顺序");
		playMode.addItem("随机");
		playMode.addItem("单曲");

		musicPlayerPanel.add(previousMusicButton);
		musicPlayerPanel.add(playMusicButton);
		musicPlayerPanel.add(stopMusicButton);
		musicPlayerPanel.add(nextMusicButtonButton);
		musicPlayerPanel.add(Box.createHorizontalStrut(50));
		musicPlayerPanel.add(playModeLabel);
		musicPlayerPanel.add(playMode);
		//

		musicSheetInfoPanel.add(Box.createVerticalStrut(10));
		musicSheetInfoPanel.add(musicSheetTitleLabel);
		musicSheetInfoPanel.add(Box.createVerticalStrut(10));
		musicSheetInfoPanel.add(musicSheetCreatorLabel);
		musicSheetInfoPanel.add(Box.createVerticalStrut(10));
		musicSheetInfoPanel.add(musicSheetButtonPanel);
		
		musicSheetButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		musicTopPanel.add(musicSheetPictureLabel);
		musicTopPanel.add(musicSheetInfoPanel);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gb.setConstraints(musicTopPanel, gbc);
		this.add(musicTopPanel);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weighty = 2;
        gb.setConstraints(musicTablePanel, gbc);
		this.add(musicTablePanel);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weighty = 0;
		gb.setConstraints(slider, gbc);
		this.add(slider);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weighty = 0;
		gb.setConstraints(musicPlayerPanel, gbc);
		this.add(musicPlayerPanel);
		
		loadMusicList(null);
	}
	
	public void loadMusicList(String sheetid){
		musicList.clear();
		musicList = OpSqliteDB.getMusicList(sheetid);
		
		musicTable.invalidate();
		musicTable.updateUI();
	}
	
	public void showSheet(MusicSheet sheet){
		player.stop();
		
		selsheet = sheet;
		musicSheetTitleLabel.setText(sheet.getName());
		musicSheetCreatorLabel.setText(sheet.getCreator() + " 于 " + sheet.getDateCreated() + " 创建");

		musicSheetPicture = new ImageIcon(sheet.getPicture());
		
		int musicSheetPictureWidth = 250;
		int musicSheetPictureHeight = 250 * musicSheetPicture.getIconHeight() / musicSheetPicture.getIconWidth();
		musicSheetPicture.setImage(musicSheetPicture.getImage().getScaledInstance(musicSheetPictureWidth,
				musicSheetPictureHeight, Image.SCALE_DEFAULT));

		musicSheetPictureLabel.setIcon(musicSheetPicture);
		musicSheetPictureLabel.invalidate();
		musicSheetPictureLabel.updateUI();
		
		musicList.clear();
		
		if(sheet.isIslocal()){
			loadMusicList(sheet.getUuid());
		}else{
			if(sheet.getMusicItems() != null){
				for(String s: sheet.getMusicItems().keySet()){
					Music music = new Music();
					music.setMd5value(s);
					music.setName(sheet.getMusicItems().get(s));
					musicList.add(music);
				}
				
				musicTable.invalidate();
				musicTable.updateUI();
			}
		}
//		musicSheetPictureLabel.setPreferredSize(new Dimension(musicSheetPictureWidth, musicSheetPictureHeight));
	}
	
	private ActionListener addMusicListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(selsheet == null){
				JOptionPane.showMessageDialog(null, "请选择歌单后再添加！", "提示", JOptionPane.ERROR_MESSAGE); 
				return;
			}
			
			int result = jFileChooser.showOpenDialog(MusicSheetDisplayBlock.this);
			if(result == JFileChooser.APPROVE_OPTION){
				Music music = new Music();
				music.setName(jFileChooser.getSelectedFile().getName());
				music.setMusicpath(jFileChooser.getSelectedFile().getAbsolutePath());
				
				String md5 = FileMd5.getFileMD5(jFileChooser.getSelectedFile());
				music.setMd5value(md5);
				
				try {
					Mp3Info mp3info = new Mp3Info(jFileChooser.getSelectedFile());
					music.setSinger(mp3info.getArtist());
					music.setLength(mp3info.getDuration());
//					System.out.println(music.getSinger());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
//				AudioFilePlayer player = new AudioFilePlayer();
//				player.play(jFileChooser.getSelectedFile().getAbsolutePath());
				music.setSheetid(selsheet.getUuid());
				music.setId(UUID.randomUUID().toString());
				
				OpSqliteDB.addMusic(music);
				
				loadMusicList(selsheet.getUuid());
			}
		}
	};
	
	private ActionListener deleteMusicListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(musicTable.getSelectedRow() == -1){
				JOptionPane.showMessageDialog(null, "没有选择音乐！", "提示", JOptionPane.ERROR_MESSAGE); 
				return;
			}
			
			player.stop();
			
			System.out.println("Delete music " + musicList.get(musicTable.getSelectedRow()).getId());
			OpSqliteDB.deleteMusic(musicList.get(musicTable.getSelectedRow()).getId());
			String sheetid = selsheet == null ? null : selsheet.getUuid();
			loadMusicList(sheetid);
		}
	};
	
	private ActionListener playNextListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			playNextMusic(0);
		}
	};
	
	private void playNextMusic(int from){
		if(musicList.size() == 0){
			return;
		}
		
		if(playMode.getSelectedIndex() == 0){
			playingMusicIndex++;
		}else if(playMode.getSelectedIndex() == 1){
			Random rnd = new Random();
			playingMusicIndex = rnd.nextInt(musicList.size());
		}else if(playMode.getSelectedIndex() == 1){
			if(from == 0){
				playingMusicIndex++;
			}
		}
		
		if(playingMusicIndex >= musicList.size()){
			playingMusicIndex = 0;
		}
		
		System.out.println("playNext: " +musicList.get(playingMusicIndex).getMusicpath());
		playMusic(musicList.get(playingMusicIndex));
	}
	
	private void playMusic(Music music){
		if(musicList.size() == 0){
			JOptionPane.showMessageDialog(null, "歌曲列表为空！", "提示", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		
		if(music == null){
			JOptionPane.showMessageDialog(null, "请双击音乐进行播放！", "提示", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		
		if(music.getMusicpath() == null){
			JOptionPane.showMessageDialog(null, "歌曲文件不存在，请先尝试下载！", "提示", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		
		File file = new File(music.getMusicpath());
		if(!file.exists()){
			JOptionPane.showMessageDialog(null, "歌曲文件不存在，请先尝试下载！", "提示", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		
		player.play(music.getMusicpath());
		
		for(int i = 0; i < musicList.size(); i++){
			musicList.get(i).setPlaying(false);
		}
		
		music.setPlaying(true);
		playingMusic = music;
		
		
		musicTable.setRowSelectionInterval(playingMusicIndex, playingMusicIndex);
		musicTable.invalidate();
		musicTable.updateUI();
	}
	
	private void stopMusic(){
		player.stop();
		
		for(int i = 0; i < musicList.size(); i++){
			musicList.get(i).setPlaying(false);
		}
		
		musicTable.invalidate();
		musicTable.updateUI();
	}
	
	private ActionListener playLastListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent arg0) {
			if(musicList.size() == 0){
				return;
			}
			
			playingMusicIndex--;
			
			if(playingMusicIndex < 0){
				playingMusicIndex = musicList.size() - 1;
			}
			
			System.out.println("playList: " + musicList.get(playingMusicIndex).getMusicpath());
			playMusic(musicList.get(playingMusicIndex));
		}
	};
	
	private ActionListener playListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(musicList.size() == 0){
				return;
			}
			
			if(playingMusicIndex >= musicList.size() || playingMusicIndex < 0){
				playingMusicIndex = 0;
			}
			
			System.out.println("play: " + musicList.get(playingMusicIndex).getMusicpath());
			playMusic(musicList.get(playingMusicIndex));
		}
	};
	
	private ActionListener playAllListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(musicList.size() == 0){
				return;
			}
			
			playingMusicIndex = 0;
			System.out.println("playAll: " +musicList.get(playingMusicIndex).getMusicpath());
			playMusic(musicList.get(playingMusicIndex));
		}
	};
	
	private ActionListener stopListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			stopMusic();
		}
	};
	
	private ActionListener downloadMp3Listener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < musicList.size(); i++){
				File file = new File("");
				File mp3dir = new File(file.getAbsolutePath() + "\\mp3\\");
				if(!mp3dir.exists()){
					mp3dir.mkdirs();
				}
				
				musicList.get(i).setDownload("下载中...");
				musicTable.invalidate();
				musicTable.updateUI();
				
				File oldmp3file = new File(mp3dir.getAbsolutePath() + "\\" +  musicList.get(i).getName());
				if(!oldmp3file.exists() || FileMd5.getFileMD5(oldmp3file) !=  musicList.get(i).getMd5value()){
					FileDownloader.downloadMusicFile("http://service.uspacex.com/music.server/downloadMusic", 
						musicList.get(i).getMd5value(), mp3dir.getAbsolutePath());
				}
				
				musicList.get(i).setMusicpath(mp3dir.getAbsolutePath() + "\\" + musicList.get(i).getName());
				musicList.get(i).setDownload("下载完成");
				
				File mp3file = new File(musicList.get(i).getMusicpath());
				if(mp3file.exists()){
					Mp3Info mp3info;
					try {
						mp3info = new Mp3Info(mp3file);
						musicList.get(i).setSinger(mp3info.getArtist());
						musicList.get(i).setLength(mp3info.getDuration());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					musicTable.invalidate();
					musicTable.updateUI();
				}
				
			}
		}
	};
	
	private OnProgressChangeListener onProgressChangeListener = new OnProgressChangeListener() {
		
		public void onProgress(int pos, long max) {
			slider.setMaximum(musicList.get(playingMusicIndex).getLength());
			slider.setValue(pos);
//			System.out.println("pos: " + pos + ", max: " + max);
		}
	};
	
	public class MusicModel extends AbstractTableModel{

		public int getColumnCount() {
			// TODO Auto-generated method stub
			return musicColumnNames.length;
		}

		public int getRowCount() {
			// TODO Auto-generated method stub
			return musicList.size();
		}

		public Object getValueAt(int arg0, int arg1) {
			// TODO Auto-generated method stub
			switch(arg1){
			case 1:
				return musicList.get(arg0).getSinger();
			case 2:
				return musicList.get(arg0).getLength() + "秒";
			case 3:
				return musicList.get(arg0).isPlaying() ? "是" : "否";
			case 4: 
				return musicList.get(arg0).getDownload();
			default:
				return musicList.get(arg0).getName();
			}
		}
	}
	

	
	public class Mp3FileFilter extends FileFilter{

		@Override
		public boolean accept(File f) {
			if(f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".mp3")){
				return true;
			}
			return false;
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "Mp3文件";
		}
		
	}
}
