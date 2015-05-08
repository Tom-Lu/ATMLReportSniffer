package com.tomlu.ATMLReportSniffer.GUI;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		
		// Display all folder
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals("xml")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
	}

	@Override
	public String getDescription() {
		return "XML Test Report";
	}

}
