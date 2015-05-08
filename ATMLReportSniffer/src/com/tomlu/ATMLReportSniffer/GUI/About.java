package com.tomlu.ATMLReportSniffer.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class About extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public About() {
		this(null);
	}
	
	public About(Frame owner) {
		super(owner);
		setType(Type.POPUP);
		setResizable(false);
		setModal(true);
		setTitle(Messages.getString("About.lblAppName.text")); //$NON-NLS-1$
		setBounds(100, 100, 296, 166);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 146, 85, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{20, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblAppName = new JLabel(Messages.getString("About.lblAppName.text")); //$NON-NLS-1$
			lblAppName.setFont(new Font("Tahoma", Font.PLAIN, 16));
			GridBagConstraints gbc_lblAppName = new GridBagConstraints();
			gbc_lblAppName.anchor = GridBagConstraints.NORTHEAST;
			gbc_lblAppName.insets = new Insets(0, 0, 5, 5);
			gbc_lblAppName.gridx = 1;
			gbc_lblAppName.gridy = 0;
			contentPanel.add(lblAppName, gbc_lblAppName);
		}
		{
			JLabel lblVersion = new JLabel(Messages.getString("About.lblNewLabel.text")); //$NON-NLS-1$
			lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 16));
			GridBagConstraints gbc_lblVersion = new GridBagConstraints();
			gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
			gbc_lblVersion.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblVersion.gridx = 2;
			gbc_lblVersion.gridy = 0;
			contentPanel.add(lblVersion, gbc_lblVersion);
		}
		{
			JLabel lblByTomLu = new JLabel(Messages.getString("About.lblByTomLu.text")); //$NON-NLS-1$
			GridBagConstraints gbc_lblByTomLu = new GridBagConstraints();
			gbc_lblByTomLu.gridwidth = 2;
			gbc_lblByTomLu.insets = new Insets(0, 0, 0, 5);
			gbc_lblByTomLu.gridx = 1;
			gbc_lblByTomLu.gridy = 1;
			contentPanel.add(lblByTomLu, gbc_lblByTomLu);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						About.this.dispose();
					}
				});
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setLocationRelativeTo(owner);
	}

}
