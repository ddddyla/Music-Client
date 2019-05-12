package ouc.cs.course.java.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import ouc.cs.course.java.model.MusicSheet;
import ouc.cs.course.java.sqlite.OpSqliteDB;

public class LocalMusicSheetBlock extends JPanel {

	private static final long serialVersionUID = 1L;
	
	SheetsModel dataModel ;
	private ArrayList<MusicSheet> sheets = new ArrayList<MusicSheet>();
	
	private JTable localMusicSheetTable;
	
	private OnRowSelectListener onRowSelectListener;
	

	String[] localMusicColumnNames = { "歌 单" };
	

	public LocalMusicSheetBlock() {
		this.setPreferredSize(new Dimension(250, 400));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.setBackground(Color.LIGHT_GRAY);
		JLabel localMusicSheetLabel = new JLabel("本地歌单");
		
		dataModel = new SheetsModel();
		localMusicSheetTable = new JTable(dataModel);
		localMusicSheetTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		localMusicSheetTable.getColumnModel().getColumn(0).setHeaderValue("歌单");

		JScrollPane localMusicSheetTablePanel = new JScrollPane(localMusicSheetTable);
		
		localMusicSheetTable.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent arg0) {				
			}
			
			public void mousePressed(MouseEvent arg0) {				
			}
			
			public void mouseExited(MouseEvent arg0) {
			}
			
			public void mouseEntered(MouseEvent arg0) {
			}
			
			public void mouseClicked(MouseEvent e) {
                int sr;
                if ((sr = localMusicSheetTable.getSelectedRow()) == -1) {
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                	if(onRowSelectListener != null){
                		onRowSelectListener.onRowSelect(sheets.get(sr));
                	}
                }
				
			}
		});
		
		JButton createMusicSheetButton = new JButton("创建新歌单");
		createMusicSheetButton.addActionListener(addSheetListener);
		
		JButton deleteMusicSheetButton = new JButton("删除歌单");
		deleteMusicSheetButton.addActionListener(deleteSheetListener);
		
		JButton uploadSheetButton = new JButton("上传歌单");
		uploadSheetButton.addActionListener(uploadSheetListener);
		
		JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
		buttonPanel.add(createMusicSheetButton);
		buttonPanel.add(deleteMusicSheetButton);
		buttonPanel.add(uploadSheetButton);
		
		this.add(Box.createVerticalStrut(5));
		this.add(localMusicSheetLabel);
		this.add(Box.createVerticalStrut(5));
		this.add(localMusicSheetTablePanel);
		this.add(buttonPanel);
		
		showMusicSheets();
	}

	/*
	 * 刷新显示歌单列表
	 */
	public void showMusicSheets(){
		sheets = OpSqliteDB.getMusicSheets();
		for(int i = 0; i < sheets.size(); i++){
			dataModel.setValueAt(sheets.get(i).getName(), i, 0);
			//localMusicData[i][0] = sheets.get(i).getName();
		}
		
		localMusicSheetTable.invalidate();
		localMusicSheetTable.updateUI();
	}
	
	/*
	 * 删除选中的歌单
	 */
	public void deleteSheet(){
		if(localMusicSheetTable.getRowCount() == 0){
			return;
		}
		
		if(localMusicSheetTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(null, "请先选择一个歌单！", "提示", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		
		OpSqliteDB.deleteSheet(sheets.get(localMusicSheetTable.getSelectedRow()).getUuid());
		
		showMusicSheets();
	}
	
	/*
	 * 歌单表格数据管理
	 */
	public class SheetsModel extends AbstractTableModel{

		public int getColumnCount() {
			return 1;
		}

		public int getRowCount() {
			return sheets.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return sheets.get(rowIndex).getName();
		}
		
	}
	
	private ActionListener deleteSheetListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			deleteSheet();
		}
	};
	
	private ActionListener addSheetListener =new ActionListener() {
		
		public void actionPerformed(ActionEvent arg0) {
			NewSheetDialog dialog = new NewSheetDialog("添加歌单");
			dialog.setOnAddSheetListener(new NewSheetDialog.OnAddSheetListener() {
				
				public void onAddSheet() {
					showMusicSheets();
				}
			});
			dialog.setModal(true);
			dialog.setVisible(true);
		}
	};	
	
	private ActionListener uploadSheetListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			if(localMusicSheetTable.getRowCount() == 0){
				return;
			}
			
			if(localMusicSheetTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(null, "请先选择一个歌单！", "提示", JOptionPane.ERROR_MESSAGE); 
				return;
			}
			
			UploadSheetDialog dialog = new UploadSheetDialog("上传歌单");
			dialog.setUploadSheet(sheets.get(localMusicSheetTable.getSelectedRow()));
			dialog.setModal(true);
			dialog.setVisible(true);
		}
	};
	
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
