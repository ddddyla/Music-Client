package ouc.cs.course.java.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ouc.cs.course.java.gui.NewSheetDialog.OnAddSheetListener;

public class MusicSheetManagementBlock extends JPanel {

	private static final long serialVersionUID = 1L;
	private OnDeleteClickListener onDeleteClickListener;
	private OnAddSheetListener onAddSheetListener;

	public MusicSheetManagementBlock() {
		this.setPreferredSize(new Dimension(250, 50));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		JButton createMusicSheetButton = new JButton("创建新歌单");
		createMusicSheetButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				NewSheetDialog dialog = new NewSheetDialog("添加歌单");
				dialog.setOnAddSheetListener(new NewSheetDialog.OnAddSheetListener() {
					
					public void onAddSheet() {
						if(onAddSheetListener != null){
							onAddSheetListener.onAddSheet();
						}
					}
				});
				dialog.setModal(true);
				dialog.setVisible(true);
			}
		});
		
		JButton deleteMusicSheetButton = new JButton("删除歌单");
		deleteMusicSheetButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(onDeleteClickListener != null){
					onDeleteClickListener.onDeleteBtnClick();
				}
			}
		});
		
		

		this.add(createMusicSheetButton);
		this.add(deleteMusicSheetButton);
	}
	
	public OnDeleteClickListener getOnDeleteClickListener() {
		return onDeleteClickListener;
	}

	public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
		this.onDeleteClickListener = onDeleteClickListener;
	}
	
	interface OnDeleteClickListener{
		public void onDeleteBtnClick();
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
